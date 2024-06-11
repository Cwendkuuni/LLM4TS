package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.addChildToFront(newNodeWithJsDoc(true));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithInterfaceJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.addChildToFront(newNodeWithJsDoc(false, true));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisTypeJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.addChildToFront(newNodeWithJsDoc(false, false, true));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideJsDoc_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.addChildToFront(newNodeWithJsDoc(false, false, false, true));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionInBlock_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInScript_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInName_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInAssign_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInStringKey_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING_KEY);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInNestedAssignmentToPrototypeProperty_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);
        Node grandparent = new Node(Token.GETPROP);
        grandparent.addChildToFront(newNodeWithJsDoc(true));
        parent.addChildToFront(grandparent);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithNoJsDocAndAssignmentToPrototypeProperty_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);
        Node grandparent = new Node(Token.GETPROP);
        grandparent.setJSDocInfo(null);
        parent.addChildToFront(grandparent);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ReturnsError() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.shouldReportThis = true;
        // Dummy compiler needed for report() method
        AbstractCompiler compiler = new AbstractCompiler();
        checkGlobalThis.compiler = compiler;
        DiagnosticType expectedType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS",
                "dangerous use of the global 'this' object");
        DiagnosticType actualType = null;
        try {
            checkGlobalThis.visit(t, n, parent);
        } catch (DiagnosticType type) {
            actualType = type;
        }
        assertEquals(expectedType, actualType);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_ThisAndAssignLhsChildNotNull_ResetAssignLhsChild() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.shouldReportThis = false;
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_NodeWithJsDoc_ReturnsJsDoc() {
        Node n = newNodeWithJsDoc(false);
        JSDocInfo expected = getNodeJsDocInfo(n);
        JSDocInfo actual = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentNodeWithJsDoc_ReturnsParentJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = newNodeWithJsDoc(true);
        n.addChildToFront(parent);
        JSDocInfo expected = getNodeJsDocInfo(parent);
        JSDocInfo actual = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_GrandparentVarWithJsDoc_ReturnsVarJsDoc() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node grandparent = newNodeWithJsDoc(false);
        grandparent.addChildToFront(new Node(Token.VAR));
        parent.addChildrenToFront(grandparent);
        n.addChildToFront(parent);
        JSDocInfo expected = getNodeJsDocInfo(grandparent);
        JSDocInfo actual = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(expected, actual);
    }

    private Node newNodeWithJsDoc(boolean isConstructor, boolean isInterface, boolean hasThisType,
                                  boolean isOverride) {
        Node jsDocNode = new Node(Token.JSDOC);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setParameterCount(0);
        jsDoc.setConstructor(isConstructor);
        jsDoc.setInterface(isInterface);
        jsDoc.setThisType(hasThisType);
        jsDoc.setOverride(isOverride);
        jsDocNode.setJSDocInfo(jsDoc);
        return jsDocNode;
    }

    private Node newNodeWithJsDoc(boolean isConstructor, boolean isInterface, boolean hasThisType) {
        return newNodeWithJsDoc(isConstructor, isInterface, hasThisType, false);
    }

    private Node newNodeWithJsDoc(boolean isConstructor, boolean isInterface) {
        return newNodeWithJsDoc(isConstructor, isInterface, false, false);
    }

    private Node newNodeWithJsDoc(boolean isConstructor) {
        return newNodeWithJsDoc(isConstructor, false, false, false);
    }

    private JSDocInfo getNodeJsDocInfo(Node n) {
        return n.getJSDocInfo();
    }
}