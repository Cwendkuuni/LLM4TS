package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.TreeSet;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeTypeCheckTest {

    private AbstractCompiler compiler;
    private RuntimeTypeCheck runtimeTypeCheck;
    private Comparator<Node> nodeComparator;

    @Before
    public void setUp() {
        compiler = Mockito.mock(AbstractCompiler.class);
        nodeComparator = new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return NodeUtil.toString(n1).compareTo(NodeUtil.toString(n2));
            }
        };
        runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    }

    @Test
    public void testProcess_EmptyExternsAndRoot() {
        Node externs = Node.NULL;
        Node root = Node.NULL;
        runtimeTypeCheck.process(externs, root);
        // Empty test since no code change is expected
    }

    @Test
    public void testProcess_FunctionWithSingleParameter() {
        String classSource = "function Foo() {}";
        CompilerInput input = new CompilerInput("Foo.js", new ByteArrayInputStream(classSource.getBytes()));
        Node externs = Node.newObject(Node.newQualifiedNameNode("Foo", -1, -1));
        Node root = NodeUtil.parseTree(NodeUtil.toString(
                "function bar(x) { return x; }",
                nodeComparator),
                new InputStreamReader(new ByteArrayInputStream(
                        "function bar(x) { return x; }".getBytes()))).getFirstChild();

        Node actual = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
        Node expected = NodeUtil.newQualifiedNameNode("Foo", -1, -1);

        Node enclosingConstructorDeclaration = NodeUtil.getFunctionNameNode(root);
        Node marker = Node.newString("instance_of__Foo");
        Node addCheckerNode = new Node(Token.EXPR_RESULT,
                NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo"))),
                marker);
        Node insertionPoint = enclosingConstructorDeclaration.getParent();

        Node checkNode = Node.newCall(
                NodeUtil.jsCode("checkType"),
                actual,
                NodeUtil.newArray(addCheckerNode));

        insertionPoint.addChildAfter(checkNode, marker);

        runtimeTypeCheck.process(externs, root);
        assertTrue(NodeUtil.toString(root).contains(NodeUtil.toString(checkNode)));
    }

    @Test
    public void testProcess_FunctionWithMultipleParameters() {
        String classSource1 = "function Foo() {}";
        String classSource2 = "function Foo2() {}";
        CompilerInput input1 = new CompilerInput("Foo.js", new ByteArrayInputStream(classSource1.getBytes()));
        CompilerInput input2 = new CompilerInput("Foo2.js", new ByteArrayInputStream(classSource2.getBytes()));
        Node externs = Node.newObject(
                Node.newQualifiedNameNode("Foo", -1, -1),
                Node.newQualifiedNameNode("Foo2", -1, -1));
        Node root = NodeUtil.parseTree(NodeUtil.toString(
                "function bar(x, y) { return x + y; }",
                nodeComparator),
                new InputStreamReader(new ByteArrayInputStream(
                        "function bar(x, y) { return x + y; }".getBytes()))).getFirstChild();

        Node actualX = NodeUtil.getFunctionNameNode(root, 0);
        Node actualY = NodeUtil.getFunctionNameNode(root, 1);

        Node class1CheckerNode = NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo")));
        Node class2CheckerNode = NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo2")));

        Node class1Marker = Node.newString("instance_of__Foo");
        Node class2Marker = Node.newString("instance_of__Foo2");

        Node addCheckerNodes = new Node(Token.ARRAYLIT);
        addCheckerNodes.addChildToBack(class1CheckerNode);
        addCheckerNodes.addChildToBack(class2CheckerNode);

        Node enclosingConstructorDeclaration = NodeUtil.getFunctionNameNode(root);

        Node checkNodeX = new Node(Token.EXPR_RESULT,
                NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo"))),
                class1Marker);
        Node checkNodeY = new Node(Token.EXPR_RESULT,
                NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo2"))),
                class2Marker);

        Node insertionPointX = enclosingConstructorDeclaration.getParent();
        Node insertionPointY = enclosingConstructorDeclaration.getParent();

        Node checkNodes = new Node(Token.EXPR_RESULT,
                NodeUtil.jsCode("checkType"),
                actualX,
                addCheckerNodes);

        insertionPointX.addChildAfter(checkNodes, class1Marker);

        Node checkNodes2 = new Node(Token.EXPR_RESULT,
                NodeUtil.jsCode("checkType"),
                actualY,
                addCheckerNodes);

        insertionPointY.addChildAfter(checkNodes2, class2Marker);

        runtimeTypeCheck.process(externs, root);

        assertTrue(NodeUtil.toString(root).contains(NodeUtil.toString(checkNodeX)));
        assertTrue(NodeUtil.toString(root).contains(NodeUtil.toString(checkNodeY)));
    }

    @Test
    public void testProcess_FunctionWithReturnType() {
        String classSource = "function Foo() { return 1; }";
        CompilerInput input = new CompilerInput("Foo.js", new ByteArrayInputStream(classSource.getBytes()));
        Node externs = Node.NULL;
        Node root = NodeUtil.parseTree(NodeUtil.toString(
                "function bar() { return new Foo(); }",
                nodeComparator),
                new InputStreamReader(new ByteArrayInputStream(
                        "function bar() { return new Foo(); }".getBytes()))).getFirstChild();

        Node actual = NodeUtil.getFunctionNameNode(root);
        Node expected = NodeUtil.newQualifiedNameNode("Foo", -1, -1);

        Node enclosingConstructorDeclaration = NodeUtil.getFunctionNameNode(root);
        Node marker = Node.newString("instance_of__Foo");
        Node addCheckerNode = new Node(Token.EXPR_RESULT,
                NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo"))),
                marker);
        Node insertionPoint = enclosingConstructorDeclaration.getParent();

        Node checkNode = Node.newCall(
                NodeUtil.jsCode("checkType"),
                actual,
                NodeUtil.newArray(addCheckerNode));

        insertionPoint.addChildAfter(checkNode, marker);

        runtimeTypeCheck.process(externs, root);
        assertTrue(NodeUtil.toString(root).contains(NodeUtil.toString(checkNode)));
    }

    @Test(expected = NullPointerException.class)
    public void testProcess_FunctionWithInvalidType() {
        String classSource = "function Foo() { return 1; }";
        CompilerInput input = new CompilerInput("Foo.js", new ByteArrayInputStream(classSource.getBytes()));
        Node externs = Node.NULL;
        Node root = NodeUtil.parseTree(NodeUtil.toString(
                "function bar() { return new Unknown(); }",
                nodeComparator),
                new InputStreamReader(new ByteArrayInputStream(
                        "function bar() { return new Unknown(); }".getBytes()))).getFirstChild();

        Node actual = NodeUtil.getFunctionNameNode(root);
        Node expected = NodeUtil.newQualifiedNameNode("Foo", -1, -1);

        Node enclosingConstructorDeclaration = NodeUtil.getFunctionNameNode(root);
        Node marker = Node.newString("instance_of__Foo");
        Node addCheckerNode = NodeUtil.createCheckerNode(new ObjectType(new ObjectType("Foo")));
        Node insertionPoint = enclosingConstructorDeclaration.getParent();

        Node checkNode = Node.newCall(
                NodeUtil.jsCode("checkType"),
                actual,
                NodeUtil.newArray(addCheckerNode));

        try {
            insertionPoint.addChildAfter(checkNode, marker);
            runtimeTypeCheck.process(externs, root);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAddBoilerplateCode() throws IOException {
        String boilerplateCode = NodeUtil.toString(RuntimeTypeCheckTest.class.getResourceAsStream("js/runtime_type_check_test.js"));
        Node expected = NodeUtil.parseTree(boilerplateCode, new InputStreamReader(new ByteArrayInputStream(boilerplateCode.getBytes()))).getFirstChild();

        Node actual = runtimeTypeCheck.addBoilerplateCode(compiler, null);
        Node expectedRoot = NodeUtil.getFunctionNameNode(expected);
        Node actualRoot = NodeUtil.getFunctionNameNode(actual);

        assertEquals(NodeUtil.toString(expectedRoot), NodeUtil.toString(actualRoot));
    }

    @Test
    public void testAddBoilerplateCodeWithLogFunction() throws IOException {
        String logFunctionSource = "function log(warning, expr) { console.log('warning: ' + warning + ', expression: ' + expr); }";
        String boilerplateCode = NodeUtil.toString(RuntimeTypeCheckTest.class.getResourceAsStream("js/runtime_type_check_log_test.js"));
        Node expected = NodeUtil.parseTree(boilerplateCode, new InputStreamReader(new ByteArrayInputStream(boilerplateCode.getBytes()))).getFirstChild();

        Node actual = runtimeTypeCheck.addBoilerplateCode(compiler, logFunctionSource);
        Node expectedRoot = NodeUtil.getFunctionNameNode(expected);
        Node actualRoot = NodeUtil.getFunctionNameNode(actual);

        assertEquals(NodeUtil.toString(expectedRoot), NodeUtil.toString(actualRoot));
    }
}