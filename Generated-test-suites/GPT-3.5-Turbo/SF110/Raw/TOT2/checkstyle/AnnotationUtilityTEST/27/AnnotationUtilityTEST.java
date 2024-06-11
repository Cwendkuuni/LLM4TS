import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    // Test for containsAnnotation(DetailAST ast, String annotation)
    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "TestAnnotation");
        assertTrue(result);
    }
    
    @Test
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, "TestAnnotation");
        });
    }
    
    // Test for containsAnnotation(DetailAST ast)
    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }
    
    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(holder);
    }
    
    // Test for getAnnotation(DetailAST ast, String annotation)
    @Test
    public void testGetAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertNotNull(annotation);
    }
    
    @Test
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        });
    }
    
    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, null);
        });
    }
    
    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "");
        });
    }
}