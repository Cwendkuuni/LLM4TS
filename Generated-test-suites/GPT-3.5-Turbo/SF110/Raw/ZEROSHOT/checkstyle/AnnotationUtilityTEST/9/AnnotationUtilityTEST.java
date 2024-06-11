import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAnnotation() {
        AnnotationUtility.containsAnnotation(new DetailAST(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_EmptyAnnotation() {
        AnnotationUtility.containsAnnotation(new DetailAST(), "");
    }
    
    @Test
    public void testContainsAnnotation_ValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        // Perform setup for test case
        
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        
        // Assert the result
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST2() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_ValidAST2() {
        DetailAST ast = new DetailAST();
        // Perform setup for test case
        
        boolean result = AnnotationUtility.containsAnnotation(ast);
        
        // Assert the result
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_InvalidASTType() {
        DetailAST ast = new DetailAST();
        // Perform setup for test case
        
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert the result
    }
    
    @Test
    public void testGetAnnotationHolder_ValidASTType1() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        // Perform setup for test case
        
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert the result
    }
    
    @Test
    public void testGetAnnotationHolder_ValidASTType2() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        // Perform setup for test case
        
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert the result
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        AnnotationUtility.getAnnotation(new DetailAST(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        AnnotationUtility.getAnnotation(new DetailAST(), "");
    }
    
    @Test
    public void testGetAnnotation_ValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        // Perform setup for test case
        
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        
        // Assert the result
    }

}