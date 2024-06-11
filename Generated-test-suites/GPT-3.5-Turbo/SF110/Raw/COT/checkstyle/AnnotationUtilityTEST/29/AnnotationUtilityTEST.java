import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndValidAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithoutAnnotationHolder() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullASTInNoHolder() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidASTAndValidAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }
}