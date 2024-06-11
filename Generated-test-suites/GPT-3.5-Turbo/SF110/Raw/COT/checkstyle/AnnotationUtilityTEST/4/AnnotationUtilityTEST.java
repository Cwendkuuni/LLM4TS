
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAST() {
        try {
            AnnotationUtility.containsAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_ValidAST() {
        DetailAST ast = createValidAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotation_InvalidAST() {
        DetailAST ast = createInvalidAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotationHolder_NullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationHolder_ValidAST() {
        DetailAST ast = createValidAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_NullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = createValidAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = createValidAST();
        try {
            AnnotationUtility.getAnnotation(ast, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_ValidAST() {
        DetailAST ast = createValidAST();
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }

    // Helper method to create a valid AST with an annotation
    private DetailAST createValidAST() {
        DetailAST ast = new DetailAST();
        // Add annotation holder
        DetailAST holder = new DetailAST();
        holder.addChild(new DetailAST());
        // Add annotation
        DetailAST annotation = new DetailAST();
        annotation.addChild(new DetailAST());
        annotation.addChild(new DetailAST());
        holder.addChild(annotation);
        ast.addChild(holder);
        return ast;
    }

    // Helper method to create an invalid AST without an annotation
    private DetailAST createInvalidAST() {
        DetailAST ast = new DetailAST();
        return ast;
    }
}
