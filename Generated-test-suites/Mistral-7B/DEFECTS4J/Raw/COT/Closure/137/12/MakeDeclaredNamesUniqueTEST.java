import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

    private TestCompiler compiler;
    private Node originalCode;

    @Before
    public void setUp() {
        compiler = new TestCompiler();
        originalCode = Node.parse("function foo() { var x = 1; var x = 2; }");
    }

    @Test
    public void testMakeDeclaredNamesUnique_BasicFunction() {
        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), originalCode);
        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.traverse(renamer);

        Node transformedCode = traverser.getTree();
        String transformedCodeString = traverser.getCompiler().getSourceCode();
        String expectedTransformedCodeString =
                "function foo() {" +
                        "var x_0 = 1;" +
                        "var x_1 = 2;" +
                        "}";

        assertEquals(expectedTransformedCodeString, transformedCodeString);
        Node xNode1 = findNodeByName(transformedCode, "x_0");
        Node xNode2 = findNodeByName(transformedCode, "x_1");
        assertSame(xNode1, transformedCode.getFirstChild().getLastChild());
        assertSame(xNode2, transformedCode.getLastChild().getFirstChild());
    }

    @Test
    public void testMakeDeclaredNamesUnique_GlobalNames() {
        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), originalCode);
        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.traverse(renamer);

        Node transformedCode = traverser.getTree();
        String transformedCodeString = traverser.getCompiler().getSourceCode();
        String expectedTransformedCodeString =
                "function foo() {" +
                        "var x_0 = 1;" +
                        "var x_1 = 2;" +
                        "}";

        assertEquals(expectedTransformedCodeString, transformedCodeString);
        Node xNode1Global = findNodeByName(transformedCode, "x_0");
        Node xNode2Global = findNodeByName(transformedCode, "x_1");
        assertSame(xNode1Global, transformedCode.getFirstChild().getLastChild());
        assertSame(xNode2Global, transformedCode.getLastChild().getFirstChild());
    }

    @Test
    public void testMakeDeclaredNamesUnique_NestedFunction() {
        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), originalCode);
        Node functionNode = traverser.getCurrentNode();
        Node functionBody = functionNode.getLastChild();

        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.pushCallback(renamer);

        Node renamedFunctionBody = NodeUtil.deepClone(functionBody);
        NodeUtil.traverse(renamer, renamedFunctionBody);

        Node transformedCode = Node.concat(Node.makeNode(Token.FUNCTION),
                functionNode.getFirstChild(),
                Node.makeNode(Token.LEFT_BRACE),
                renamedFunctionBody,
                Node.makeNode(Token.RIGHT_BRACE));

        String transformedCodeString = traverser.getCompiler().getSourceCode();
        String expectedTransformedCodeString =
                "function foo() {" +
                        "function foo1() {" +
                        "var x_0 = 1;" +
                        "var x_1 = 2;" +
                        "}" +
                        "}" +
                        "}";

        assertEquals(expectedTransformedCodeString, transformedCodeString);
        Node xNode1 = findNodeByName(transformedCode, "x_0");
        Node xNode2 = findNodeByName(transformedCode, "x_1");

        Node xNode1Function = findNodeByName(transformedCode, "x_0").getParent();
        Node xNode2Function = findNodeByName(transformedCode, "x_1").getParent();

        assertSame(xNode1Function, functionNode);
        assertSame(xNode2Function, functionNode);
    }

    @Test
    public void testMakeDeclaredNamesUnique_FunctionProperty() {
        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), originalCode);
        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.traverse(renamer);

        Node transformedCode = traverser.getTree();
        String transformedCodeString = traverser.getCompiler().getSourceCode();
        String expectedTransformedCodeString =
                "function foo() {" +
                        "var x = 1;" +
                        "this.x = 2;" +
                        "}";

        assertEquals(expectedTransformedCodeString, transformedCodeString);

        Node xNodeTransformed = findNodeByName(transformedCode, "x_0");
        Node thisNodeTransformed = findNodeByName(transformedCode, "this");
        Node xNodeProperty = findNodeByName(transformedCode, "x_1");

        assertSame(xNodeTransformed, transformedCode.getFirstChild().getLastChild());
        assertSame(thisNodeTransformed, transformedCode.getLastChild().getFirstChild());
        assertSame(xNodeProperty, thisNodeTransformed.getLastChild());
    }

    @Test(expected = AssertionError.class)
    public void testMakeDeclaredNamesUnique_DuplicateGlobalNames() {
        Node javaScriptCode = Node.parse("function foo() {" +
                        "var x = 1;" +
                        "var x = 2;" +
                        "}");

        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), javaScriptCode);
        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.traverse(renamer);

        Node transformedCode = traverser.getTree();
        String transformedCodeString = traverser.getCompiler().getSourceCode();
    }

    @Test
    public void testMakeDeclaredNamesUnique_FunctionWithParameters() {
        String javaScriptCode = "function foo(x, y) { var x = 1; }";
        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), Node.parse(javaScriptCode));
        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.traverse(renamer);

        Node transformedCode = traverser.getTree();
        String transformedCodeString = traverser.getCompiler().getSourceCode();

        String expectedTransformedCodeString =
                "function foo(x_0, y_0) {" +
                        "var x_1 = 1;" +
                        "}";

        assertEquals(expectedTransformedCodeString, transformedCodeString);

        Node xNodeTransformed = findNodeByName(transformedCode, "x_1");
        assertSame(xNodeTransformed, transformedCode.getFirstChild().getLastChild());
    }

    @Test
    public void testMakeDeclaredNamesUnique_FunctionWithLocalAndGlobalVariables() {
        String javaScriptCode = "function foo() {" +
                "var x = 1;" +
                "var y = 1;" +
                "this.x = 2;" +
                "this.y = 2;" +
                "}";

        NodeTraversal traverser = new NodeTraversal(compiler.getFactory(), Node.parse(javaScriptCode));
        ScopedCallback renamer = new MakeDeclaredNamesUnique();
        traverser.traverse(renamer);

        Node transformedCode = traverser.getTree();
        String transformedCodeString = traverser.getCompiler().getSourceCode();

        String expectedTransformedCodeString =
                "function foo() {" +
                        "var x_0 = 1;" +
                        "var y_0 = 1;" +
                        "this.x_1 = 2;" +
                        "this.y_1 = 2;" +
                        "}";

        assertEquals(expectedTransformedCodeString, transformedCodeString);

        Node xNodeTransformed = findNodeByName(transformedCode, "x_0");
        Node yNodeTransformed = findNodeByName(transformedCode, "y_0");
        Node xNodeGlobal = findNodeByName(transformedCode, "x_1");
        Node yNodeGlobal = findNodeByName(transformedCode, "y_1");

        assertSame(xNodeTransformed, transformedCode.getFirstChild().getLastChild());
        assertSame(yNodeTransformed, transformedCode.getLastChild().getFirstChild());
        assertSame(xNodeGlobal, findNodeByName(transformedCode, "x_1").getParent());
        assertSame(yNodeGlobal, findNodeByName(transformedCode, "y_1").getParent());
    }

    private Node findNodeByName(Node node, String name) {
        if (node == null) {
            return null;
        }

        if (node.getType() == Token.NAME && node.getString().equals(name)) {
            return node;
        }

        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNext()) {
            Node result = findNodeByName(childNode, name);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private static class NodeUtil {
        public static Node deepClone(Node node) {
            Node clonedNode = compiler.getFactory().makeNode(node.getType(), null, null);
            if (node.hasProps()) {
                for (Map.Entry<String, Node> entry : node.getProps().entrySet()) {
                    clonedNode.setProp(entry.getKey(), deepClone(entry.getValue()));
                }
            }
            return clonedNode;
        }

        public static void traverse(ScopedCallback renamer, Node node) {
            renamer.visit(new NodeTraversal(compiler.getFactory(), node, null), node, null);
        }
    }
}