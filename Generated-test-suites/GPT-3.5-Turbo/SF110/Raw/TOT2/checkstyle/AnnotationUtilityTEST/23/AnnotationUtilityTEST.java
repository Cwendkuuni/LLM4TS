import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
    }
    
    @Test
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, "Test");
        });
    }
    
    @Test
    public void testContainsAnnotationNoAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolderWithType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolderWithTypeDifferent() {
        DetailAST ast = new DetailAST();
        ast.setType(10);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
    }
    
    @Test
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "Test");
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
    
    @Test
    public void testGetAnnotationHolderWithEmptyChild() {
        DetailAST ast = new DetailAST();
        assertThrows(NullPointerException.class, () -> {
            AnnotationUtility.getAnnotationHolder(ast);
        });
    }
}