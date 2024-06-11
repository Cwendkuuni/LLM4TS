package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setup() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }
    
    @Test
    public void testShouldTraverse_NotFunctionNode_ShouldReturnTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createNode(Token.EXPR_RESULT);
        Node parent = createNode(Token.SCRIPT);

        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeWithConstructor_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createFunctionNode();
        n.putProp(Node.JSDOC_INFO_PROP, createJSDocInfo(true, false, false, false));
        Node parent = createNode(Token.FUNCTION);

        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeWithThisType_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createFunctionNode();
        n.putProp(Node.JSDOC_INFO_PROP, createJSDocInfo(false, false, true, false));
        Node parent = createNode(Token.FUNCTION);

        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeWithoutLogicalPlaceForThisAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createFunctionNode();
        n.putProp(Node.JSDOC_INFO_PROP, createJSDocInfo(false, false, false, false));
        Node parent = createNode(Token.FUNCTION);

        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeNotBlockScriptNameOrAssign_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createFunctionNode();
        Node parent = createNode(Token.NOT);

        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ParentAssign_NodeEqualsAssignLhsChild_ShouldReturnTrue() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createNode(Token.NAME);
        Node parent = createNode(Token.ASSIGN);
        Node lhsChild = n;

        checkGlobalThis.assignLhsChild = lhsChild;

        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ParentAssign_NodeNotEqualsAssignLhsChild_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createNode(Token.NAME);
        Node parent = createNode(Token.ASSIGN);
        Node lhsChild = createNode(Token.GETPROP);

        checkGlobalThis.assignLhsChild = lhsChild;

        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testVisit_NodeIsThisNodeAndShouldReportThis_ShouldReportError() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createNode(Token.THIS);
        Node parent = createNode(Token.GETPROP);

        checkGlobalThis.compiler = new AbstractCompiler();
        checkGlobalThis.level = CheckLevel.WARNING;

        checkGlobalThis.visit(t, n, parent);

        assertNotNull(checkGlobalThis.compiler.getErrors().get(0));
    }
    
    @Test
    public void testVisit_NodeIsThisNodeAndShouldNotReportThis_ShouldNotReportError() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createNode(Token.THIS);
        Node parent = createNode(Token.ASSIGN);

        checkGlobalThis.compiler = new AbstractCompiler();
        checkGlobalThis.level = CheckLevel.WARNING;

        checkGlobalThis.visit(t, n, parent);

        assertTrue(checkGlobalThis.compiler.getErrors().isEmpty());
    }
    
    @Test
    public void testVisit_NodeIsAssignLhsChild_ShouldResetAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = createNode(Token.NAME);
        Node parent = createNode(Token.ASSIGN);

        checkGlobalThis.assignLhsChild = n;
        checkGlobalThis.compiler = new AbstractCompiler();
        checkGlobalThis.level = CheckLevel.WARNING;

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }
    
    private Node createNode(int type) {
        return new Node(type);
    }
    
    private Node createFunctionNode() {
        Node function = createNode(Token.FUNCTION);
        function.putProp(Node.JSDOC_INFO_PROP, createJSDocInfo(false, false, false, false));
        return function;
    }
    
    private JSDocInfo createJSDocInfo(boolean isConstructor, boolean isInterface, boolean hasThisType, boolean isOverride) {
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(isConstructor);
        jsDoc.setInterface(isInterface);
        jsDoc.setHasThisType(hasThisType);
        jsDoc.setOverride(isOverride);
        return jsDoc;
    }
}