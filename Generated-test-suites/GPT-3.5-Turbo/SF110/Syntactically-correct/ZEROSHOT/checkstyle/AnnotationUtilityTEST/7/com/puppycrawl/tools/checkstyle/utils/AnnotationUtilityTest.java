import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotation_WithAnnotation() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_WithoutAnnotation() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        Assert.assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_Overload() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_WithAnnotation_Overload() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_WithoutAnnotation_Overload() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_AnnotationHolderExists() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolder_AnnotationHolderDoesNotExist() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_AnnotationExists() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_AnnotationDoesNotExist() {
        DetailAST ast = new DetailAST();
        // add test case logic here
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNull(result);
    }
}