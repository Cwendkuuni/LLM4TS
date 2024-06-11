
package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void testContainsAnnotation_shouldReturnTrue_whenAnnotationIsPresent() {
        // Arrange
        DetailAST annotation = createAnnotation("MyAnnotation");
        ast.addAnnotation(annotation);

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "MyAnnotation");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_shouldReturnFalse_whenAnnotationIsNotPresent() {
        // Arrange
        DetailAST annotation = createAnnotation("SomeAnnotation");
        ast.addAnnotation(annotation);

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "MyAnnotation");

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_shouldThrowIllegalArgumentException_whenAstIsNull() {
        // Arrange
        DetailAST ast = null;

        // Act
        AnnotationUtility.containsAnnotation(ast, "MyAnnotation");

        // Assert
        // Expected exception
    }

    @Test
    public void testContainsAnnotation_shouldReturnTrue_whenHolderBranchContainsValidBranch() {
        // Arrange
        DetailAST holder = new DetailAST(159);
        ast.addChild(holder);

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_shouldReturnFalse_whenHolderBranchContainsInvalidBranch() {
        // Arrange
        DetailAST holder = new DetailAST(158);
        ast.addChild(holder);

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_shouldReturnFalse_whenHolderIsNull() {
        // Arrange
        DetailAST holder = null;

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_shouldThrowIllegalArgumentException_whenAstIsNull() {
        // Arrange
        DetailAST ast = null;

        // Act
        AnnotationUtility.containsAnnotation(ast);

        // Assert
        // Expected exception
    }

    @Test
    public void testGetAnnotationHolder_shouldReturnAnnotationHolder_whenAstIsClassOrInterfaceTypeOrAnnotableType() {
        // Arrange
        DetailAST ast = new DetailAST(155);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolder_shouldReturnAnnotationHolder_whenAstIsNotClassOrInterfaceTypeOrAnnotableType() {
        // Arrange
        DetailAST ast = new DetailAST(10);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_shouldThrowIllegalArgumentException_whenAstIsNull() {
        // Arrange
        DetailAST ast = null;

        // Act
        AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        // Expected exception
    }

    @Test
    public void testGetAnnotation_shouldReturnCorrectAnnotation_whenAnnotationIsPresent() {
        // Arrange
        DetailAST annotation1 = createAnnotation("MyAnnotation1");
        DetailAST annotation2 = createAnnotation("MyAnnotation2");
        ast.addAnnotation(annotation1);
        ast.addAnnotation(annotation2);

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation2");

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_shouldReturnNull_whenAnnotationIsNotPresent() {
        // Arrange
        DetailAST annotation = createAnnotation("MyAnnotation");
        ast.addAnnotation(annotation);

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "SomeAnnotation");

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_shouldThrowIllegalArgumentException_whenAstIsNull() {
        // Arrange
        DetailAST ast = null;

        // Act
        AnnotationUtility.getAnnotation(ast, "MyAnnotation");

        // Assert
        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_shouldThrowIllegalArgumentException_whenAnnotationIsNull() {
        // Arrange
        String annotation = null;

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_shouldThrowIllegalArgumentException_whenAnnotationIsEmptyOrSpaces() {
        // Arrange
        String annotation = "   ";

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        // Expected exception
    }

    private DetailAST createAnnotation(String name) {
        DetailAST annotation = new DetailAST(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new FullIdent());
        annotation.setFirstChild(firstChild);
        return annotation;
    }
}
