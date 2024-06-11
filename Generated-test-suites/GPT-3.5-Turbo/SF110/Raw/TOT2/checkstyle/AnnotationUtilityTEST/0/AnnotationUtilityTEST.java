import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null));
    }

    @Test
    public void testGetAnnotationHolder_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotation_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "Test"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(new DetailAST(), null));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(new DetailAST(), ""));
    }

}