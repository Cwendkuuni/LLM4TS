package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withValidInput_shouldReturnTrue() {
        // Arrange
        DetailAST ast = new DetailAST();
        DetailAST childToken = new DetailAST();
        childToken.setType(159);
        ast.addChild(childToken);
        String annotation = "TestAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    public void testContainsAnnotation_withNullAnnotation_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    public void testContainsAnnotation_withEmptyAnnotation_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    public void testContainsAnnotation_withSpacesAnnotation_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "   ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, annotation);
        });
    }

    @Test
    public void testContainsAnnotation_withValidAstAndNonExistingAnnotation_shouldReturnFalse() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "NonExistingAnnotation";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast);
        });
    }

    @Test
    public void testContainsAnnotation_withValidAstAndExistingAnnotationHolder_shouldReturnTrue() {
        // Arrange
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(159);
        ast.addChild(annotationHolder);

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withValidAstAndNonExistingAnnotationHolder_shouldReturnFalse() {
        // Arrange
        DetailAST ast = new DetailAST();

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetAnnotationHolder_withValidAstAndAnnotationHolder_shouldReturnAnnotationHolder() {
        // Arrange
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_withValidAstAndNoAnnotationHolder_shouldReturnNull() {
        // Arrange
        DetailAST ast = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_withValidAstAndAnnotation_shouldReturnAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        ast.addChild(annotationHolder);
        annotationHolder.addChild(annotation);
        String expectedAnnotation = "TestAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, expectedAnnotation);

        // Assert
        assertNotNull(result);
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotation_withValidAstAndNonExistingAnnotation_shouldReturnNull() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "NonExistingAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    public void testGetAnnotation_withNullAnnotation_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    public void testGetAnnotation_withEmptyAnnotation_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }

    @Test
    public void testGetAnnotation_withSpacesAnnotation_shouldThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "   ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, annotation);
        });
    }
}