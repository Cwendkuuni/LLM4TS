import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        // Typical use case
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        // Edge case: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolder() {
        // Typical use case
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));

        // Edge case: ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation() {
        // Typical use case
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        // Edge case: ast is null
        try {
            AnnotationUtility.getAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Edge case: annotation is null
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }

        // Edge case: annotation is empty or spaces
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

}