package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_functionWithConstructorAnnotation() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionWithThisAnnotation() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_nestedAssignments() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_validFunction() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testVisit_globalThisLeftSideOfAssignment() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // Make assertion about the reported error
    }

    @Test
    public void testVisit_globalThisWithPropertyAccess() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // Make assertion about the reported error
    }

    @Test
    public void testVisit_globalThisNotTraversingLeftSideOfAssignment() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t;
        Node n;
        Node parent;

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // Ensure that the assignLhsChild is set to null
    }
}