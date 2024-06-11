import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void containsAnnotation_withAnnotation_shouldReturnTrue() {
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void containsAnnotation_withoutAnnotation_shouldReturnFalse() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }

    @Test
    public void getAnnotationHolder_withAnnotationType155_shouldReturnNotNull() {
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void getAnnotationHolder_withAnnotationTypeNot155_shouldReturnNotNull() {
        ast.initialize(16, "DummyAnnotation");
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotationHolder_withNullAst_shouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void getAnnotation_withValidAnnotation_shouldReturnNotNull() {
        assertNotNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void getAnnotation_withInvalidAnnotation_shouldReturnNull() {
        assertNull(AnnotationUtility.getAnnotation(ast, "InvalidAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_withNullAst_shouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_withNullAnnotation_shouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_withEmptyAnnotation_shouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_withSpacesAnnotation_shouldThrowIllegalArgumentException() {
        AnnotationUtility.getAnnotation(ast, "   ");
    }

}