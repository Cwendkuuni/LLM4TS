import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test
    public void testContainsAnnotation_NoAnnotationHolder() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_NoAnnotationHolder() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_SpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "    ");
    }

}