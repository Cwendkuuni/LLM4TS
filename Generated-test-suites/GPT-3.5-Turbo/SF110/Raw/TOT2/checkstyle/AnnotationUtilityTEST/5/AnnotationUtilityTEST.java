import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_ValidAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_ThrowsException() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_ReturnsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_ValidAnnotation_ReturnsAnnotationAST() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        holder.addChild(annotation);
        
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST_ThrowsException() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation_ThrowsException() {
        AnnotationUtility.getAnnotation(new DetailAST(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation_ThrowsException() {
        AnnotationUtility.getAnnotation(new DetailAST(), "");
    }
}