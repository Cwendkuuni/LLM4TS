
import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withNotNullASTAndExistingAnnotation_returnsTrue() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
    }

    // ... other test cases ...

    @Test
    public void testGetAnnotation_withNotNullASTAndWhitespaceAnnotation_throwsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotation(ast, "  ");
        });
    }
}