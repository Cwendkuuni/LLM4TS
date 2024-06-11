import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        // Typical use case
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
        
        // Edge case: ast is null
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(null, "TestAnnotation");
        });
    }

    @Test
    public void testGetAnnotationHolder() {
        // Typical use case
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
        
        // Edge case: ast is null
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotationHolder(null);
        });
    }

    @Test
    public void testGetAnnotation() {
        // Typical use case
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
        
        // Edge case: ast is null
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(null, "TestAnnotation");
        });
        
        // Edge case: annotation is null
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, null);
        });
        
        // Edge case: annotation is empty
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "");
        });
        
        // Edge case: annotation is spaces
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "    ");
        });
    }

}