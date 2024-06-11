import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_NullAST() {
        try {
            String annotation = "Test";
            AnnotationUtility.containsAnnotation(null, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            AnnotationUtility.containsAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            String annotation = "";
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_NullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_NullAST() {
        try {
            String annotation = "Test";
            AnnotationUtility.getAnnotation(null, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            String annotation = "";
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
}