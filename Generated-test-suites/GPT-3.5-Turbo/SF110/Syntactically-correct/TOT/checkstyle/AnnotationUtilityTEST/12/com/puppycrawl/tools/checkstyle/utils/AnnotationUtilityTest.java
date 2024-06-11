
import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_astIsNull() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast, "annotation");
    }
    
    @Test(timeout = 1000)
    public void testContainsAnnotation_astContainsAnnotation() {
        DetailAST ast = new DetailAST();
        // add annotation to the AST
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test(timeout = 1000)
    public void testContainsAnnotation_astDoesNotContainAnnotation() {
        DetailAST ast = new DetailAST();
        // empty AST
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test(timeout = 1000)
    public void testContainsAnnotationHolder_astIsNull() {
        DetailAST ast = null;
        assertEquals(null, AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(timeout = 1000)
    public void testContainsAnnotationHolder_astOfType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        // add annotation holder to the AST
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(timeout = 1000)
    public void testContainsAnnotationHolder_astOfType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        // add annotation holder to the AST
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(timeout = 1000)
    public void testContainsAnnotationHolder_astNotOfType155Or16() {
        DetailAST ast = new DetailAST();
        // empty AST
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_astIsNull() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotation(ast, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_annotationIsNull() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_annotationIsEmpty() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    @Test(timeout = 1000)
    public void testGetAnnotation_annotationExists() {
        DetailAST ast = new DetailAST();
        // add annotation to the AST
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
    
    @Test(timeout = 1000)
    public void testGetAnnotation_annotationDoesNotExist() {
        DetailAST ast = new DetailAST();
        // empty AST
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
}

