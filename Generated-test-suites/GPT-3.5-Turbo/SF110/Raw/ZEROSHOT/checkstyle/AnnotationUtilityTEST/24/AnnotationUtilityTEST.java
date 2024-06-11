import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_ExceptionThrown() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_WithAnnotationExists_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        Assert.assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_WithoutAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        Assert.assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST_ExceptionThrown() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_Type155_ReturnsFirstChildToken() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        Assert.assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_Type16_ReturnsFirstChildToken() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        ast.addChild(new DetailAST());
        Assert.assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_OtherTypes_ReturnsTokenOfType5() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        ast.addChild(new DetailAST());
        Assert.assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST_ExceptionThrown() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation_ExceptionThrown() {
        AnnotationUtility.getAnnotation(new DetailAST(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyOrNullAnnotation_ExceptionThrown() {
        AnnotationUtility.getAnnotation(new DetailAST(), "");
    }

    @Test
    public void testGetAnnotation_AnnotationExists_ReturnsAnnotationAST() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        DetailAST annotationAST = new DetailAST();
        annotationAST.setType(159);
        ast.addChild(annotationAST);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_AnnotationDoesNotExist_ReturnsNull() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        DetailAST annotationAST = new DetailAST();
        annotationAST.setType(159);
        ast.addChild(annotationAST);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "someAnnotation");
        Assert.assertNull(result);
    }
}