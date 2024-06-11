
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Deprecated";

        assertFalse(AnnotationUtility.containsAnnotation(null, annotation));
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));

        ast.setType(155);
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));

        ast.addFirstChild(new DetailAST());
        ast.getFirstChild().setType(158);
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.setType(155);

        ast.addFirstChild(holder);
        holder.setType(159);

        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullHolderAST() {
        DetailAST ast = new DetailAST();
        ast.setType(155);

        AnnotationUtility.containsAnnotation(ast);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);

        DetailAST annotationHolder = new DetailAST();
        ast.addFirstChild(annotationHolder);
        annotationHolder.setParent(ast);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);

        DetailAST annotationHolder = new DetailAST();
        ast.addFirstChild(annotationHolder);
        annotationHolder.setParent(ast);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = new DetailAST();
        ast.setType(10);

        DetailAST annotationHolder = new DetailAST();
        ast.addFirstChild(annotationHolder);
        annotationHolder.setParent(ast);

        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        AnnotationUtility.getAnnotation(null, "Deprecated");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test
    public void testGetAnnotationWithValidAST() {
        DetailAST ast = new DetailAST();
        ast.setType(10);

        DetailAST holder = new DetailAST();
        ast.addFirstChild(holder);
        holder.setParent(ast);

        DetailAST child1 = new DetailAST();
        holder.addFirstChild(child1);
        child1.setParent(holder);
        child1.setType(159);

        DetailAST child2 = new DetailAST();
        holder.addSecondChild(child2);
        child2.setParent(holder);
        child2.setType(159);

        child1.addFirstChild(new DetailAST());
        child1.getFirstChild().setType(100);
        child2.addFirstChild(new DetailAST());
        child2.getFirstChild().setType(100);

        DetailAST expectedAnnotation = child2;

        assertEquals(expectedAnnotation, AnnotationUtility.getAnnotation(ast, "Annotation"));
    }
}