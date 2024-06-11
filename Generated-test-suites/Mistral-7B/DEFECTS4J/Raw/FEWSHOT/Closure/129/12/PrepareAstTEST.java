package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PrepareAstTest {
    private TestCompiler compiler;
    private NodeFactory nodeFactory;

    @Before
    public void setUp() {
        compiler = new TestCompiler();
        nodeFactory = compiler.getFactory();
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node root = getNode(
            "function foo() {}",
            "if (true) {} else {}",
            "var x = 1;"
        );

        PrepareAst prepareAst = new PrepareAst(compiler);
        Node result = prepareAst.process(null, root).getRootNode();

        assertFalse(result.getFirstChild().isBlock());
        assertFalse(result.getLastChild().isBlock());

        for (Node ifStmt : Iterables.filter(result.traverse(), node -> node.isIf())) {
            Node ifBlock = ifStmt.getNext();
            assertTrue(ifBlock.isBlock());
        }

        for (Node whileStmt : Iterables.filter(result.traverse(), node -> node.isWhile())) {
            Node whileBlock = whileStmt.getNext();
            assertTrue(whileBlock.isBlock());
        }
    }

    @Test
    public void testNormalizeBlocks() {
        Node root = getNode(
            "function foo() { if (true) {} else {} }",
            "function bar() { if (true) if (true) {} else {} else {} }"
        );

        PrepareAst prepareAst = new PrepareAst(compiler);
        Node result = prepareAst.process(null, root).getRootNode();

        for (Node ifStmt : Iterables.filter(result.traverse(), node -> node.isIf())) {
            Node ifBlock = ifStmt.getNext();
            assertTrue(ifBlock.isBlock());

            Node ifStmtBody = ifBlock.getFirstChild();
            if (ifStmtBody.isIf()) {
                Node ifBlock2 = ifStmtBody.getNext();
                assertTrue(ifBlock2.isBlock());
            }
        }

        Node barFn = Iterables.getFirst(result.traverse(), node -> node.isFunction() && node.getString().equals("bar"));
        Preconditions.checkState(barFn != null);

        for (Node ifStmt : Iterables.filter(barFn.getChildren(), node -> node.isIf())) {
            Node ifBlock = ifStmt.getNext();
            assertTrue(ifBlock.isBlock());

            Node ifStmtBody = ifBlock.getFirstChild();
            if (ifStmtBody.isIf()) {
                Node ifBlock2 = ifStmtBody.getNext();
                assertTrue(ifBlock2.isBlock());
            }

            Node elseStmt = Iterables.getFirst(ifBlock.getChildren(), node -> node.isElse());
            assertFalse(elseStmt.getNext().isBlock());
        }
    }

    @Test
    public void testPrepareAnnotations() {
        Node root = getNode(
            "/** @constructor */\nfunction Foo() {}\n",
            "function foo() {}\n",
            "function Bar() {\n  this.x = 1;\n  this.y = 2;\n}"
        );

        PrepareAst prepareAst = new PrepareAst(compiler);
        Node result = prepareAst.process(null, root).getRootNode();

        for (Node fn : Iterables.filter(result.traverse(), node -> node.isFunction())) {
            if (fn.getString().equals("Foo")) {
                assertTrue(fn.getJSDocInfo().isConstructor());
            }

            if (fn.getString().equals("Bar")) {
                assertTrue(fn.getJSDocInfo().isConstructor());
                for (Node prop : fn.getChildren()) {
                    if (prop.isName()) {
                        String name = prop.getString();
                        if ("x".equals(name)) {
                            assertTrue(prop.getJSDocInfo().isThis());
                        }
                        if ("y".equals(name)) {
                            assertTrue(prop.getJSDocInfo().isThis());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testPrepareAnnotationsWithObjectLiteral() {
        Node root = getNode(
            "/** @constructor */\nfunction Foo() {}\n",
            "var foo = {\n  /** @type {number} */\n  x: 1,\n  y: 2\n};\n"
        );

        PrepareAst prepareAst = new PrepareAst(compiler);
        Node result = prepareAst.process(null, root).getRootNode();

        for (Node objLit : Iterables.filter(result.traverse(), node -> node.isObjectLit())) {
            Node obj = nodeFactory.createObject();
            nodeFactory.setJSDocInfo(obj, node.getJSDocInfo());

            for (Node keyValue : objLit.getChildren()) {
                Node key = keyValue.getFirstChild();
                Node value = keyValue.getLastChild();

                if (key.isName()) {
                    Node keyNode = nodeFactory.createName(key.getString(), obj);
                    obj.addChildToEnd(keyNode);
                    obj.addChildToEnd(value);
                }
            }

            result.replaceChild(objLit, obj);
        }

        for (Node fn : Iterables.filter(result.traverse(), node -> node.isFunction())) {
            if (fn.getString().equals("Foo")) {
                assertTrue(fn.getJSDocInfo().isConstructor());
            }
        }

        Node foo = Iterables.getFirst(result.traverse(), node -> node.isObjectLit());
        Preconditions.checkState(foo != null);

        for (Node keyValue : foo.getChildren()) {
            Node key = keyValue.getFirstChild();
            Node value = keyValue.getLastChild();

            if (key.isName()) {
                String name = key.getString();
                if ("x".equals(name)) {
                    Node x = nodeFactory.createName("x", foo);
                    foo.addChildToEnd(x);
                    nodeFactory.setJSDocInfo(x, key.getJSDocInfo());
                    x.addChildToEnd(value);
                } else if ("y".equals(name)) {
                    Node y = nodeFactory.createName("y", foo);
                    foo.addChildToEnd(y);
                    nodeFactory.setJSDocInfo(y, key.getJSDocInfo());
                    y.addChildToEnd(value);
                }
            }
        }

        TestSourceFile sourceFile = compiler.getSourceFile("test.js");
        sourceFile.replaceTree(0, result);
        List<String> errors = compiler.check("-W:type_inference_check", "--warning_level=ERROR");
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testPrepareAnnotationsWithFunctionLiteral() {
        Node root = getNode(
            "function foo() {}\n",
            "var bar = function() {}\n"
        );

        PrepareAst prepareAst = new PrepareAst(compiler, true);
        Node result = prepareAst.process(null, root).getRootNode();

        for (Node fn : Iterables.filter(result.traverse(), node -> node.isFunction())) {
            if (fn.getString().equals("bar")) {
                assertNull(fn.getJSDocInfo());
            }
        }
    }

    private Node getNode(String... nodes) {
        TestSourceFile sourceFile = TestSourceFile.create("test.js");
        for (String node : nodes) {
            sourceFile.append(node);
        }
        return compiler.parse(sourceFile, null);
    }
}

class NodeUtil {
    static boolean isControlStructure(Node node) {
        return node.isIf() || node.isWhile() || node.isDo() || node.isSwitch();
    }

    static boolean isControlStructureCodeBlock(Node parent, Node node) {
        if (parent.isIf()) {
            return node.isElse() || node.isElseIf() || node.isStatement();
        } else if (parent.isWhile() || parent.isDo()) {
            return node.isStatement();
        } else if (parent.isSwitch()) {
            return node.isCase() || node.isDefault();
        }
        return false;
    }
}