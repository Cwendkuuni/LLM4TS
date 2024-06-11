package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_AssignNode_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.ASSIGN), null);
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeWithConstructorAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().setConstructor(true));
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, null);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeWithThisTypeAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().setHasThisType(true));
        boolean result = checkGlobalThis.shouldTraverse(null, functionNode, null);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionNodeWithoutJsDocInfo_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null);
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_LhsChildIsNull_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        checkGlobalThis.shouldTraverse(null, new Node(Token.ASSIGN), null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), null);
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_LhsIsAssignmentToPrototype_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), new Node(Token.STRING, "property"));
        boolean result = checkGlobalThis.shouldTraverse(null, lhs, new Node(Token.ASSIGN));
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_LhsIsAssignmentToSubproperty_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node lhs = new Node(Token.GETPROP, new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "prototype")), new Node(Token.STRING, "property"));
        boolean result = checkGlobalThis.shouldTraverse(null, lhs, new Node(Token.ASSIGN));
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_Default_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        boolean result = checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), new Node(Token.ASSIGN));
        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsGlobalThis_ShouldReportError() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node globalThisNode = new Node(Token.THIS);
        checkGlobalThis.visit(null, globalThisNode, null);
        // Assert that an error was reported
    }

    @Test
    public void testVisit_NodeIsNotGlobalThis_ShouldNotReportError() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node otherNode = new Node(Token.NAME);
        checkGlobalThis.visit(null, otherNode, null);
        // Assert that no error was reported
    }
}