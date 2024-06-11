
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    // Test cases for containsAnnotation(DetailAST ast, String annotation)

    @Test
    public void testContainsAnnotation_WithValidASTAndExistingAnnotation_ReturnTrue() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(159)); // Add a child node of type 159 (annotation token)
        ast.getFirstChild().addChild(new DetailAST(104, new Token(1, "Test"))); // Add a child node of type 104 (identifier token) with text "Test"
        String annotation = "Test";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_WithValidASTAndNonExistingAnnotation_ReturnFalse() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(159)); // Add a child node of type 159 (annotation token)
        ast.getFirstChild().addChild(new DetailAST(104, new Token(1, "Test"))); // Add a child node of type 104 (identifier token) with text "Test"
        String annotation = "Test123";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAST_ThrowIllegalArgumentException() {
        // Arrange
        String annotation = "Test";

        // Act
        AnnotationUtility.containsAnnotation(null, annotation);

        // Assert
        // Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAnnotation_ThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;

        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        // Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithEmptyAnnotation_ThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        // Act
        AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        // Exception expected
    }

    // Test cases for containsAnnotation(DetailAST ast)

    @Test
    public void testContainsAnnotation_WithValidASTAndExistingAnnotationHolder_ReturnTrue() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(155)); // Add a child node of type 155 (modifier token)
        ast.getFirstChild().addChild(new DetailAST(158)); // Add a child node of type 158 (annotation holder token)
        ast.getFirstChild().getFirstChild().addChild(new DetailAST(159)); // Add a child node of type 159 (annotation token)
        ast.getFirstChild().getFirstChild().getFirstChild().addChild(new DetailAST(104, new Token(1, "Test"))); // Add a child node of type 104 (identifier token) with text "Test"

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_WithValidASTAndNonExistingAnnotationHolder_ReturnFalse() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(155)); // Add a child node of type 155 (modifier token)
        ast.getFirstChild().addChild(new DetailAST(158)); // Add a child node of type 158 (annotation holder token)
        String annotation = "Test";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAST_ThrowIllegalArgumentException() {
        // Arrange

        // Act
        AnnotationUtility.containsAnnotation(null);

        // Assert
        // Exception expected
    }

    // Test cases for getAnnotationHolder(DetailAST ast)

    @Test
    public void testGetAnnotationHolder_WithValidASTAndAnnotationHolderPresent_ReturnAnnotationHolder() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(155)); // Add a child node of type 155 (modifier token)
        ast.getFirstChild().addChild(new DetailAST(158)); // Add a child node of type 158 (annotation holder token)
        DetailAST expected = ast.getFirstChild().getFirstChild(); // Get the first child of the first child

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testGetAnnotationHolder_WithValidASTAndAnnotationHolderNotPresent_ReturnNull() {
        // Arrange
        DetailAST ast = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_WithNullAST_ThrowIllegalArgumentException() {
        // Arrange

        // Act
        AnnotationUtility.getAnnotationHolder(null);

        // Assert
        // Exception expected
    }

    // Test cases for getAnnotation(DetailAST ast, String annotation)

    @Test
    public void testGetAnnotation_WithValidASTAndExistingAnnotation_ReturnAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(158));
        ast.getFirstChild().addChild(new DetailAST(159)); // Add a child node of type 159 (annotation token)
        ast.getFirstChild().getFirstChild().addChild(new DetailAST(104, new Token(1, "Test"))); // Add a child node of type 104 (identifier token) with text "Test"
        String annotation = "Test";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_WithValidASTAndNonExistingAnnotation_ReturnNull() {
        // Arrange
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST(158));
        ast.getFirstChild().addChild(new DetailAST(159)); // Add a child node of type 159 (annotation token)
        ast.getFirstChild().getFirstChild().addChild(new DetailAST(104, new Token(1, "Test"))); // Add a child node of type 104 (identifier token) with text "Test"
        String annotation = "Test123";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithNullAST_ThrowIllegalArgumentException() {
        // Arrange
        String annotation = "Test";

        // Act
        AnnotationUtility.getAnnotation(null, annotation);

        // Assert
        // Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithNullAnnotation_ThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = null;

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        // Exception expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithEmptyAnnotation_ThrowIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        // Act
        AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        // Exception expected
    }
}
