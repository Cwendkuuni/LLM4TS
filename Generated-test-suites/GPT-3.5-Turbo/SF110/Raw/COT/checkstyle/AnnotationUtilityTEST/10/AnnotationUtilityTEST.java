
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNonNullASTAndNonNullAnnotation() {
        DetailAST ast = new DetailAST();
        Assert.assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNonNullASTAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNonNullASTAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, "");
    }

    @Test
    public void testContainsAnnotationWithNonNullASTAndAnnotationNotFound() {
        DetailAST ast = new DetailAST();
        Assert.assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNonNullASTAndAnnotationFound() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        Assert.assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        try {
            AnnotationUtility.containsAnnotation(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithNonNullASTAndHolderNotExist() {
        DetailAST ast = new DetailAST();
        Assert.assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNonNullASTAndHolderExists() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        Assert.assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithType155Or16() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        Assert.assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithOtherTypes() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        ast.addChild(new DetailAST());
        Assert.assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNonNullASTAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNonNullASTAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNonNullASTAndAnnotationNotFound() {
        DetailAST ast = new DetailAST();
        Assert.assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNonNullASTAndAnnotationFound() {
        DetailAST ast = new DetailAST();
        DetailAST child = new DetailAST();
        DetailAST identifier = new DetailAST();
        identifier.setText("annotation");
        child.addChild(identifier);
        ast.addChild(child);
        Assert.assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
}
