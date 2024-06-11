
package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_ShouldReturnFalse_WhenFunctionIsConstructorOrHasThisOrOverrideAnnotation() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_WhenParentTypeIsNotBlockOrScriptOrNameOrAssignOrStringOrNumber() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = new Node(Token.VAR);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_WhenParentTypeIsGetAndLastChildIsPrototype() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_WhenParentTypeIsGetAndFirstChildTypeIsGetAndLastChildIsPrototype() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node llhs = new Node(Token.GETPROP);
        llhs.addChildToBack(new Node(Token.NAME, "prototype"));
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(llhs);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnTrue_WhenAssignLhsChildIsNotNull() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node lhs = new Node(Token.NAME, "a");
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(lhs);
        checkGlobalThis.shouldTraverse(t, lhs, parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnTrue_WhenParentIsGet() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = new Node(Token.GETPROP);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ShouldReturnFalse_WhenParentIsNull() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testVisit_ShouldReportError_WhenNodeIsThisAndParentIsGet() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // Verify that an error is reported
    }

    @Test
    public void testVisit_ShouldNotReportError_WhenNodeIsThisAndParentIsNotGet() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.VAR);

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        // Verify that no error is reported
    }

    @Test
    public void testVisit_ShouldResetAssignLhsChild_WhenNodeIsAssignLhsChild() {
        // Arrange
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.VAR);
        checkGlobalThis.shouldTraverse(t, n, parent);

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        assertEquals(null, checkGlobalThis.assignLhsChild);
    }
}
