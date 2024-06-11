package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_ReturnsFalse_WhenFunctionIsConstructor() {
        // Test case for shouldTraverse method when the function is a constructor
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsFalse_WhenFunctionHasThisAnnotation() {
        // Test case for shouldTraverse method when the function has @this annotation
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsFalse_WhenFunctionIsOverride() {
        // Test case for shouldTraverse method when the function is an override
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsFalse_WhenFunctionIsInterface() {
        // Test case for shouldTraverse method when the function is an interface
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setInterface(true);
        node.setJSDocInfo(jsDocInfo);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
    } 

    @Test
    public void testShouldTraverse_ReturnsFalse_WhenParentTypeIsNotABlockOrScriptOrNameOrAssign() {
        // Test case for shouldTraverse method when the parent type is not BLOCK or SCRIPT or NAME or ASSIGN
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.DO);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(false, result);
    } 

    @Test
    public void testShouldTraverse_ReturnsTrue_WhenParentIsNull() {
        // Test case for shouldTraverse method when the parent is null
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsTrue_WhenParentIsAssign() {
        // Test case for shouldTraverse method when the parent is ASSIGN
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsTrue_WhenParentIsName() {
        // Test case for shouldTraverse method when the parent is NAME
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_ReturnsTrue_WhenParentIsBlock() {
        // Test case for shouldTraverse method when the parent is BLOCK
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
    } 


    @Test
    public void testVisit_ReportsError_WhenNodeIsGlobalThis() {
        // Test case for visit method when the node is global this
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Compiler compiler = new Compiler();
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node n = new Node(Token.THIS);
        n.setType(Token.THIS);

        // Act
        checkGlobalThis.visit(nodeTraversal, n, new Node(Token.GETPROP));

        // Assert
        assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"), compiler.getErrors().get(0).getType());
    }

    @Test
    public void testVisit_ReportsNoError_WhenNodeIsNotGlobalThis() {
        // Test case for visit method when the node is not global this
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Compiler compiler = new Compiler();
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node n = new Node(Token.NAME);
        n.setType(Token.NAME);

        // Act
        checkGlobalThis.visit(nodeTraversal, n, new Node(Token.GETPROP));

        // Assert
        assertEquals(0, compiler.getErrors().size());
    }
}