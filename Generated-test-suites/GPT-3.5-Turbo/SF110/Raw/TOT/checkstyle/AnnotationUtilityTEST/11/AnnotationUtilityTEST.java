
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
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
    public void testContainsAnnotation_ValidAST_ReturnsTrue() {
        // Arrange
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        ast.addChild(annotation);

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "Test");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_ValidAST_ReturnsFalse() {
        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "Test");

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_ThrowsIllegalArgumentException() {
        // Act
        AnnotationUtility.containsAnnotation(null, "Test");
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation_ReturnsFalse() {
        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_WhitespaceAnnotation_ReturnsFalse() {
        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, "  ");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationHolder_ValidAST_ReturnsNotNull() {
        // Arrange
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.addChild(annotationHolder);

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testContainsAnnotationHolder_NoAnnotation_ReturnsNull() {
        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationHolder_NullAST_ThrowsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotation_ValidAST_ReturnsNotNull() {
        // Arrange
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        ast.addChild(annotation);
        DetailAST ident = new DetailAST();
        ident.setType(53);
        annotation.addChild(ident);

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Test");

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_InvalidAST_ReturnsNull() {
        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Test");

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST_ThrowsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(null, "Test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation_ThrowsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WhitespaceAnnotation_ThrowsIllegalArgumentException() {
        // Act
        AnnotationUtility.getAnnotation(ast, "  ");
    }
}
