
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
    public void testContainsAnnotation_NullAST_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("the ast is null", ex.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_ValidAST_ReturnsFalse() {
        boolean hasAnnotation = AnnotationUtility.containsAnnotation(ast);
        assertFalse(hasAnnotation);
    }

    @Test
    public void testContainsAnnotation_ASTWithHolder_ReturnsTrue() {
        DetailAST holderAST = new DetailAST();
        ast.addChild(holderAST);

        boolean hasAnnotation = AnnotationUtility.containsAnnotation(ast);
        assertTrue(hasAnnotation);
    }

    @Test
    public void testContainsAnnotation_ASTWithoutHolder_ReturnsFalse() {

        boolean hasAnnotation = AnnotationUtility.containsAnnotation(ast);
        assertFalse(hasAnnotation);
    }

    @Test
    public void testContainsAnnotation_ASTWithBranch_ReturnsTrue() {
        ast.setType(16);
        DetailAST branchAST = new DetailAST();
        branchAST.setType(159);
        ast.addChild(branchAST);

        boolean hasAnnotation = AnnotationUtility.containsAnnotation(ast);
        assertTrue(hasAnnotation);
    }

    @Test
    public void testGetAnnotationHolder_NullAST_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("the ast is null", ex.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_ReturnsFirstToken() {
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
        assertEquals(5, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotationHolder_ASTWithType155_ReturnsFirstToken() {
        ast.setType(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
        assertEquals(158, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotation_ValidAST_ThrowsIllegalArgumentException_AnnotationIsNull() {
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("the annotation is null", ex.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_ValidAST_ThrowsIllegalArgumentException_AnnotationIsEmpty() {
        try {
            AnnotationUtility.getAnnotation(ast, "");
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("the annotation is empty or spaces", ex.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_ValidAST_ReturnsNull() {
        DetailAST holderAST = new DetailAST();
        ast.addChild(holderAST);

        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertNull(annotation);
    }

    @Test
    public void testGetAnnotation_ValidAST_ReturnsChildAnnotation() {
        DetailAST holderAST = new DetailAST();
        ast.addChild(holderAST);

        DetailAST childAnnotation = new DetailAST();
        childAnnotation.setType(159);
        DetailAST firstChild = new DetailAST();
        childAnnotation.addChild(firstChild);
        holderAST.addChild(childAnnotation);
        firstChild.setNextSibling(FullIdent.createFullIdent("TestAnnotation"));

        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertNotNull(annotation);
        assertEquals(159, annotation.getType());
    }

}
