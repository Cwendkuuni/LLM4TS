package com.google.javascript.jscomp;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionsWithNoAnnotationsOrConstructorsOrThisTypesOrOverrides() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.NAME);
        assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithConstructorAnnotation() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo = jsDocInfo.recordConstructor();
        Node node = new Node(Token.NAME);
        node.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithThisTypeAnnotation() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo = jsDocInfo.recordThisType();
        Node node = new Node(Token.NAME);
        node.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithOverrideAnnotation() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo = jsDocInfo.recordOverride();
        Node node = new Node(Token.NAME);
        node.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithInterfaceAnnotation() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo = jsDocInfo.recordInterface();
        Node node = new Node(Token.NAME);
        node.setJSDocInfo(jsDocInfo);
        assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForFunctionsWithNonGlobalContexts() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.FUNCTION);
        Node block = new Node(Token.BLOCK);
        parent.addChildToFront(block);
        Node node = new Node(Token.NAME);
        assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForAssignmentNodes() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.ASSIGN);
        Node node = new Node(Token.NAME);
        assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForParentNode() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.NAME);
        assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    }

    @Test
    public void testVisit_shouldReportThisIfOnTheLeftSideOfAssignment() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.ASSIGN);
        Node node = new Node(Token.THIS);
        checkGlobalThis.visit(traversal, node, parent);
        // To Do - Assertion Statement
    }

    @Test
    public void testVisit_shouldNotReportThisIfNotOnTheLeftSideOfAssignment() {
        NodeTraversal traversal = new NodeTraversal(new Compiler(), checkGlobalThis);
        Node parent = new Node(Token.NAME);
        Node node = new Node(Token.THIS);
        checkGlobalThis.visit(traversal, node, parent);
        // To Do - Assertion Statement
    }
}