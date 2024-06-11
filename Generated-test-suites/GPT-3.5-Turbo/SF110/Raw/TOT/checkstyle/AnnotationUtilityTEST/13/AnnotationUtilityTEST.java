import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withValidAnnotation_returnsTrue() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation1";

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withNullAST_throwsIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;

        try {
            // Act
            AnnotationUtility.containsAnnotation(ast);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_withValidAST_butNoAnnotation_returnsFalse() {
        // Arrange
        DetailAST ast = new DetailAST();

        // Act
        boolean result = AnnotationUtility.containsAnnotation(ast);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetAnnotationHolder_withValidAST_returnsAnnotationHolder() {
        // Arrange
        DetailAST ast = new DetailAST();

        // Act
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_withValidASTAndAnnotation_returnsAnnotation() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation2";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_withNullAST_throwsIllegalArgumentException() {
        // Arrange
        DetailAST ast = null;
        String annotation = "TestAnnotation2";

        try {
            // Act
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withNullAnnotation_throwsIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();

        try {
            // Act
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withEmptyAnnotation_throwsIllegalArgumentException() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "";

        try {
            // Act
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withAnnotationNotFound_returnsNull() {
        // Arrange
        DetailAST ast = new DetailAST();
        String annotation = "NonexistentAnnotation";

        // Act
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);

        // Assert
        assertNull(result);
    }
}