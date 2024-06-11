package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse1() {
        // Test shouldTraverse when the given node is a function that is not a constructor,
        // does not have @this or @override annotation, and the parent is not a block, script, name,
        // assign, string or number.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();

        assertEquals(false, checkGlobalThis.shouldTraverse(t, n, createParentNode()));
    }

    @Test
    public void testShouldTraverse2() {
        // Test shouldTraverse when the given node is a function that is a constructor.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);

        assertEquals(false, checkGlobalThis.shouldTraverse(t, n, createParentNode()));
    }

    @Test
    public void testShouldTraverse3() {
        // Test shouldTraverse when the given node is a function that has @this annotation.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);

        assertEquals(false, checkGlobalThis.shouldTraverse(t, n, createParentNode()));
    }

    @Test
    public void testShouldTraverse4() {
        // Test shouldTraverse when the given node is a function that has @override annotation.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        n.setJSDocInfo(jsDocInfo);

        assertEquals(false, checkGlobalThis.shouldTraverse(t, n, createParentNode()));
    }

    @Test
    public void testShouldTraverse5() {
        // Test shouldTraverse when the given node is a function and the parent is a block.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        Node parent = new Node(Token.BLOCK);

        assertEquals(true, checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse6() {
        // Test shouldTraverse when the given node is a function and the parent is a script.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        Node parent = new Node(Token.SCRIPT);

        assertEquals(true, checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse7() {
        // Test shouldTraverse when the given node is a function and the parent is a name.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        Node parent = new Node(Token.NAME);

        assertEquals(true, checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse8() {
        // Test shouldTraverse when the given node is a function and the parent is an assign.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        Node parent = new Node(Token.ASSIGN);

        assertEquals(true, checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse9() {
        // Test shouldTraverse when the given node is a function and the parent is a string.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        Node parent = new Node(Token.STRING);

        assertEquals(true, checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse10() {
        // Test shouldTraverse when the given node is a function and the parent is a number.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        Node n = createFunctionNode();
        Node parent = new Node(Token.NUMBER);

        assertEquals(true, checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit1() {
        // Test visit when the given node is not "this".
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.NAME);
        Node parent = null;
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);

        assertNull(n.getJSDocInfo());
    }

    @Test
    public void testVisit2() {
        // Test visit when the given node is "this" and assignLhsChild is null.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = null;
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);

        assertNull(n.getJSDocInfo());
    }

    @Test
    public void testVisit3() {
        // Test visit when the given node is "this" and assignLhsChild is not null.
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = null;
        NodeTraversal t = new NodeTraversal(checkGlobalThis);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);

        assertNull(n.getJSDocInfo());
    }

    private Node createFunctionNode() {
        Node n = new Node(Token.FUNCTION);
        return n;
    }

    private Node createParentNode() {
        Node parent = new Node();
        return parent;
    }
}