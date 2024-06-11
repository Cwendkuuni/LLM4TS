import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAST() {
        String annotation = "Test";
        AnnotationUtility.containsAnnotation(null, annotation);
    }

    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationHolderNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAST() {
        String annotation = "Test";
        AnnotationUtility.getAnnotation(null, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

}


class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAST() {
        String annotation = "Test";
        AnnotationUtility.containsAnnotation(null, annotation);
    }

    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationHolderNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAST() {
        String annotation = "Test";
        AnnotationUtility.getAnnotation(null, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

}
