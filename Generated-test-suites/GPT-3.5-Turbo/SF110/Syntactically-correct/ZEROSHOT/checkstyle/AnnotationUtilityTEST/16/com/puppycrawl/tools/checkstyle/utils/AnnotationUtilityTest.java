import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilityTest {

    private DetailAST ast = new DetailAST();

    @Before
    public void setUp() {
        ast.setType(155);
    }

    @Test
    public void testContainsAnnotation_ThrowsIllegalArgumentException_WhenAstIsNull() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_ReturnsTrue_WhenAnnotationIsFound() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(158);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(16);
        child.setFirstChild(firstChild);
        ast.setFirstChild(child);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_ReturnsFalse_WhenAnnotationIsNotFound() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_ThrowsIllegalArgumentException_WhenAstIsNull() {
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_ReturnsTrue_WhenAnnotationHolderIsNotNullAndBranchContains159() {
        assertFalse(AnnotationUtility.containsAnnotation(ast));
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(158);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(5);
        child.setFirstChild(firstChild);
        ast.setFirstChild(child);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolder_ThrowsIllegalArgumentException_WhenAstIsNull() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolder_ReturnsAnnotationHolder_WhenAstTypeIs155Or16AndAnnotationHolderExists() {
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        ast.setType(16);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.setFirstChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_ReturnsAnnotationHolder_WhenAstTypeIsNot155Or16() {
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        ast.setType(5);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(5);
        ast.setFirstChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_ThrowsIllegalArgumentException_WhenAstIsNull() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_ThrowsIllegalArgumentException_WhenAnnotationIsNull() {
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_ThrowsIllegalArgumentException_WhenAnnotationIsEmptyOrSpaces() {
        try {
            AnnotationUtility.getAnnotation(ast, "    ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_ReturnsAnnotation_WhenAnnotationIsFound() {
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(158);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(159);
        firstChild.getNextSibling().setFirstChild(new DetailAST());
        firstChild.getNextSibling().getFirstChild().setNextSibling(new DetailAST());
                
        firstChild.getNextSibling().getFirstChild().getNextSibling().setType(FullIdent.createFullIdent(firstChild.getNextSibling().getFirstChild().getNextSibling()).getText().equals("annotation") ? 159 : 155);
        child.setFirstChild(firstChild);
        ast.setFirstChild(child);
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_ReturnsNull_WhenAnnotationIsNotFound() {
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

}