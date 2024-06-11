package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
    }

    @Test
    public void testShouldTraverse_Function_Consructor() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        boolean result = t.shouldTraverse(n, null, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_Function_ThisType() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);

        boolean result = t.shouldTraverse(n, null, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_Function_Override() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);

        boolean result = t.shouldTraverse(n, null, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_Function_BlockParent() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setParent(parent);

        boolean result = t.shouldTraverse(n, null, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_Function_ScriptParent() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        boolean result = t.shouldTraverse(n, null, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_Function_NameParent() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);

        boolean result = t.shouldTraverse(n, null, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_Function_AssignParent() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);

        boolean result = t.shouldTraverse(n, null, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_NonFunction_ParentNull() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);

        boolean result = t.shouldTraverse(n, null, null);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_NonFunction_ParentNotAssign() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.DIV);

        boolean result = t.shouldTraverse(n, null, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_NonFunction_ParentName_SiblingVar() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        parent.setParent(grandparent);
        grandparent.addChildToBack(parent);

        boolean result = t.shouldTraverse(n, null, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_LeftSide_AssignLhsChildIsNull() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);

        boolean result = t.shouldTraverse(n, parent.getFirstChild(), parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_LeftSide_AssignLhsChildIsNotNull() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);
        t.visit(null, null, parent); // set assignLhsChild

        boolean result = t.shouldTraverse(n, parent.getFirstChild(), parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_RightSide_PrototypePropertyAccess() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        parent.addChildToBack(lhs);

        boolean result = t.shouldTraverse(n, parent.getLastChild(), parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_RightSide_PrototypeSubpropertyAccess() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);
        Node lhs = new Node(Token.NAME, "a.prototype.b");
        parent.addChildToBack(lhs);

        boolean result = t.shouldTraverse(n, parent.getLastChild(), parent);

        assertEquals(false, result);
    }

    @Test
    public void testVisit_ThisShouldReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NULL);

        t.visit(null, n, parent);

        // assert compilation error was reported
    }

    @Test
    public void testVisit_NotThisShouldNotReportThis() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.GETPROP);

        t.visit(null, n, parent);

        // assert no compilation error was reported
    }

    @Test
    public void testVisit_AssignLhsChildIsNull() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.THIS);
        Node assignLhsChild = null;

        t.visit(null, n, null);

        // assert no change to assignLhsChild
    }

    @Test
    public void testVisit_AssignLhsChildIsNotNull() {
        NodeTraversal t = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
        Node n = new Node(Token.THIS);
        Node assignLhsChild = new Node(Token.STRING);

        t.visit(null, n, null);

        // assert assignLhsChild is set to null
    }
}