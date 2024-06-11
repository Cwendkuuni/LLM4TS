import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithAstNotNull() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithAstNull() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithoutAst() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testGetAnnotationHolderWithAstNotNull() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithAstNull() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationWithAstNotNullAndAnnotationNotNull() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNull(annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithAstNull() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithAnnotationNull() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
}