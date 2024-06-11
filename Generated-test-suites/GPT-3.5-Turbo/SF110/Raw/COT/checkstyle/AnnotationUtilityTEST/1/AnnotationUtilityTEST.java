import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    // Test containsAnnotation(DetailAST ast, String annotation)
    @Test
    public void testContainsAnnotation_withNonNullASTAndNonNullAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withNullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(null, "annotation");
        });
    }

    // Test containsAnnotation(DetailAST ast)
    @Test
    public void testContainsAnnotation_noAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    // Test getAnnotationHolder(DetailAST ast)
    @Test
    public void testGetAnnotationHolder_withAnnotation_ReturnsNonNullAST() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolder_noAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    // Test getAnnotation(DetailAST ast, String annotation)
    @Test
    public void testGetAnnotation_withExistingAnnotation_ReturnsNonNullAST() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_withNonExistingAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "nonExistingAnnotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_withNullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(null, "annotation");
        });
    }

    @Test
    public void testGetAnnotation_withNullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, null);
        });
    }

    @Test
    public void testGetAnnotation_withEmptyAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "");
        });
    }
}

