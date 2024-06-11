import static org.junit.Assert.*;
import org.junit.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_withValidAnnotation_shouldReturnTrue() {
        DetailAST ast = new DetailAST();
        String annotation = "Override";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_withNullAST_shouldThrowIllegalArgumentException() {
        DetailAST ast = null;
        String annotation = "Override";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_withInvalidAnnotation_shouldReturnFalse() {
        DetailAST ast = new DetailAST();
        String annotation = "InvalidAnnotation";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_withNullAnnotation_shouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_withEmptyAnnotation_shouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_withWhitespaceAnnotation_shouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        String annotation = "   ";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_withNullAST_shouldThrowIllegalArgumentException() {
        DetailAST ast = null;
        try {
            AnnotationUtility.containsAnnotation(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_withValidAST_shouldReturnFalse() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotation_withNullAnnotationHolder_shouldReturnFalse() {
        DetailAST ast = new DetailAST();
        ast.setType(100);
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test
    public void testGetAnnotationHolder_withValidAST_shouldReturnAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_withInvalidAST_shouldThrowIllegalArgumentException() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withValidASTAndAnnotation_shouldReturnAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST annotation = new DetailAST();
        annotationHolder.addChild(annotation);
        String annotationName = "Override";
        FullIdent name = new FullIdent(new DetailAST(), annotationName);
        annotation.addChild(name);
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotationName);
        assertNotNull(result);
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotation_withASTWithoutAnnotation_shouldReturnNull() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        String annotationName = "Override";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotationName);
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_withInvalidAST_shouldThrowIllegalArgumentException() {
        DetailAST ast = null;
        String annotationName = "Override";
        try {
            AnnotationUtility.getAnnotation(ast, annotationName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withNullAnnotation_shouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        String annotationName = null;
        try {
            AnnotationUtility.getAnnotation(ast, annotationName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withEmptyAnnotation_shouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        String annotationName = "";
        try {
            AnnotationUtility.getAnnotation(ast, annotationName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_withWhitespaceAnnotation_shouldThrowIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        String annotationName = "   ";
        try {
            AnnotationUtility.getAnnotation(ast, annotationName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

}