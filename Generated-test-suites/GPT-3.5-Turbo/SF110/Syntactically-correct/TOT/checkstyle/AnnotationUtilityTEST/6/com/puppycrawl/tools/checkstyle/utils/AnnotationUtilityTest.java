import org.junit.*;
import com.puppycrawl.tools.checkstyle.api.*;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withExistingAnnotation_returnsTrue() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        boolean result = AnnotationUtility.containsAnnotation(ast, "Annotation");

        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withNonExistingAnnotation_returnsFalse() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        boolean result = AnnotationUtility.containsAnnotation(ast, "NonExistingAnnotation");

        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST_throwsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_withNullAST_returnsFalse() {
        boolean result = AnnotationUtility.containsAnnotation(null, "Annotation");

        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationHolder_withValidAST_returnsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertNotNull(result);
    }

    @Test
    public void testContainsAnnotationHolder_withInvalidAST_returnsNull() {
        DetailAST ast = new DetailAST();
        ast.setType(200);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAST_throwsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "Annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAnnotation_throwsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withEmptyAnnotation_throwsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());

        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test
    public void testGetAnnotation_withExistingAnnotation_returnsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        DetailAST annotation = new DetailAST();
        DetailAST name = new DetailAST();
        name.setType(89);
        name.setText("Annotation");
        annotation.addChild(name);
        ast.addChild(annotation);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "Annotation");

        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_withNonExistingAnnotation_returnsNull() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        DetailAST annotation = new DetailAST();
        DetailAST name = new DetailAST();
        name.setType(89);
        name.setText("Annotation");
        annotation.addChild(name);
        ast.addChild(annotation);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "NonExistingAnnotation");

        assertNull(result);
    }
}