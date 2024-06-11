
import org.junit.Assert;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation_NullAST() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_Valid() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotationHolder_NullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolder_Valid() {
        DetailAST ast = new DetailAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull(holder);
    }
    
    @Test
    public void testGetAnnotation_NullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_Valid() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNull(annotation);
    }
}
