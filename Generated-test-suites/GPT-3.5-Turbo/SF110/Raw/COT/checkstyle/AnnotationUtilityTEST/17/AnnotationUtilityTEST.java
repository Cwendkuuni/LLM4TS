
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        String annotation = "TestAnnotation";
        AnnotationUtility.containsAnnotation(null, annotation);
    }
    
    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, null);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        String annotation = "TestAnnotation";
        AnnotationUtility.getAnnotation(null, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }
}
