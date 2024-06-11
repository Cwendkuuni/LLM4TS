import org.junit.*;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {
    

    
    @Test
    public void testContainsAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast does not contain the annotation
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "Annotation");
        assertFalse(result);
        
        // Test case 3: ast contains the annotation
        DetailAST astWithAnnotation = createAstWithAnnotation();
        result = AnnotationUtility.containsAnnotation(astWithAnnotation, "Annotation");
        assertTrue(result);
    }
    
    @Test
    public void testContainsAnnotationWithoutAnnotationParam() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast does not contain the annotation
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
        
        // Test case 3: ast contains the annotation
        DetailAST astWithAnnotation = createAstWithAnnotation();
        result = AnnotationUtility.containsAnnotation(astWithAnnotation);
        assertTrue(result);
    }
    
    @Test
    public void testGetAnnotationHolder() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast is type 155 or 16 (has annotation holder)
        DetailAST astWithType155 = new DetailAST(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(astWithType155);
        assertNotNull(result);
        
        DetailAST astWithType16 = new DetailAST(16);
        result = AnnotationUtility.getAnnotationHolder(astWithType16);
        assertNotNull(result);
        
        // Test case 3: ast is not type 155 or 16
        DetailAST astWithoutAnnotationHolder = new DetailAST();
        result = AnnotationUtility.getAnnotationHolder(astWithoutAnnotationHolder);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: annotation is null
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test case 3: annotation is empty or spaces
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case 4: ast does not contain the annotation
        DetailAST astWithoutAnnotation = createAstWithAnnotation();
        DetailAST result = AnnotationUtility.getAnnotation(astWithoutAnnotation, "NonExistingAnnotation");
        assertNull(result);
        
        // Test case 5: ast contains the annotation
        DetailAST astWithAnnotation = createAstWithAnnotation();
        result = AnnotationUtility.getAnnotation(astWithAnnotation, "Annotation");
        assertNotNull(result);
    }
    

    
    private DetailAST createAstWithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST(155);
        DetailAST annotation = new DetailAST(159);
        DetailAST name = new DetailAST(123);
        name.setText("Annotation");
        annotation.addChild(name);
        annotationHolder.addChild(annotation);
        ast.addChild(annotationHolder);
        return ast;
    }
}