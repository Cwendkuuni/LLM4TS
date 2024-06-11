import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotationWithNullAst() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "Annotation"));
    }
    
    @Test
    public void testContainsAnnotationReturnsTrue() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }
    
    @Test
    public void testContainsAnnotationReturnsFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "InvalidAnnotation"));
    }
    
    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }
    
    @Test
    public void testGetAnnotationHolderForValidAst() {
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationWithNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, "Annotation"));
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
    
    // Add more test cases for edge cases and error scenarios
    
}