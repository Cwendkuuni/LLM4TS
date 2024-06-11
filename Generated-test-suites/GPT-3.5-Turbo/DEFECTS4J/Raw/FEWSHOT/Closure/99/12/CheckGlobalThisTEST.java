
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverseFunctionWithConstructorAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNodeWithConstructorAnnotation(), null));
    }

    @Test
    public void testShouldTraverseFunctionWithThisAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNodeWithThisAnnotation(), null));
    }

    @Test
    public void testShouldTraverseFunctionWithOverrideAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNodeWithOverrideAnnotation(), null));
    }

    @Test
    public void testShouldTraverseFunctionNotInGlobalContext() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNodeWithoutGlobalContext(), null));
    }

    @Test
    public void testShouldTraverseFunctionInBlock() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNodeInBlockContext(), null));
    }

    @Test
    public void testShouldTraverseFunctionInScript() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNodeInScriptContext(), null));
    }

    @Test
    public void testShouldTraverseFunctionInName() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNodeInNameContext(), null));
    }

    @Test
    public void testShouldTraverseFunctionInAssign() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNodeInAssignContext(), null));
    }

    @Test
    public void testVisitThisNodeOnLeftSideOfAssignment() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldReportThis(createThisNode(), createAssignmentNodeWithThisOnLeftSide()));
    }

    @Test
    public void testVisitThisNodeWithPropertyAccess() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldReportThis(createThisNode(), createPropertyAccessNode()));
    }

    @Test
    public void testVisitThisNodeWithoutAssignmentOrPropertyAccess() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldReportThis(createThisNode(), createNonAssignmentOrPropertyAccessNode()));
    }

    private Node createFunctionNodeWithConstructorAnnotation() {
        Node functionNode = createFunctionNode();
        functionNode.putProp(Node.JSDOC_INFO_PROP, 1);
        return functionNode;
    }

    private Node createFunctionNodeWithThisAnnotation() {
        Node functionNode = createFunctionNode();
        functionNode.putProp(Node.JSDOC_INFO_PROP, 2);
        return functionNode;
    }

    private Node createFunctionNodeWithOverrideAnnotation() {
        Node functionNode = createFunctionNode();
        functionNode.putProp(Node.JSDOC_INFO_PROP, 3);
        return functionNode;
    }

    private Node createFunctionNodeWithoutGlobalContext() {
        Node functionNode = createFunctionNode();
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createFunctionNodeInBlockContext() {
        Node functionNode = createFunctionNode();
        Node parent = new Node(Token.BLOCK);
        Node scriptNode = new Node(Token.SCRIPT);
        parent.addChildToFront(scriptNode);
        scriptNode.addChildToFront(functionNode);

        return functionNode;
    }

    private Node createFunctionNodeInScriptContext() {
        Node functionNode = createFunctionNode();
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createFunctionNodeInNameContext() {
        Node functionNode = createFunctionNode();
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createFunctionNodeInAssignContext() {
        Node functionNode = createFunctionNode();
        Node assignNode = createAssignmentNodeWithFunctionAssign();
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(assignNode);
        assignNode.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createFunctionNode() {
        return new Node(Token.FUNCTION);
    }

    private Node createAssignmentNodeWithThisOnLeftSide() {
        Node assignmentNode = new Node(Token.ASSIGN);
        Node leftSideNode = new Node(Token.THIS);
        assignmentNode.addChildToFront(leftSideNode);
        return assignmentNode;
    }

    private Node createPropertyAccessNode() {
        Node propertyAccessNode = new Node(Token.GETPROP);
        Node leftSideNode = new Node(Token.THIS);
        Node rightSideNode = new Node(Token.STRING, "property");
        propertyAccessNode.addChildToFront(leftSideNode);
        propertyAccessNode.addChildToBack(rightSideNode);
        return propertyAccessNode;
    }

    private Node createNonAssignmentOrPropertyAccessNode() {
        return new Node(Token.THIS);
    }

    private Node createThisNode() {
        return new Node(Token.THIS);
    }

    private Node createAssignmentNodeWithFunctionAssign() {
        Node assignmentNode = new Node(Token.ASSIGN);
        Node leftSideNode = new Node(Token.NAME, "a");
        Node rightSideNode = createFunctionNode();
        assignmentNode.addChildToFront(leftSideNode);
        assignmentNode.addChildToBack(rightSideNode);
        return assignmentNode;
    }
}
