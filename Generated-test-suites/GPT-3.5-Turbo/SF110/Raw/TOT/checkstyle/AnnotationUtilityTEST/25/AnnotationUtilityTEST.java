import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        // Typical Use Case
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        DetailAST ast2 = new DetailAST();
        ast2.setType(16);
        
        assertFalse(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
        assertFalse(AnnotationUtility.containsAnnotation(ast1, "TestAnnotation"));
        assertFalse(AnnotationUtility.containsAnnotation(ast2, "TestAnnotation"));

        // Edge Cases
        DetailAST ast3 = new DetailAST();
        ast3.setType(100);
        assertFalse(AnnotationUtility.containsAnnotation(ast3, "TestAnnotation"));
        
        DetailAST ast4 = new DetailAST();
        ast4.setType(155);
        assertNull(AnnotationUtility.containsAnnotation(ast4, null));
        
        DetailAST ast5 = new DetailAST();
        ast5.setType(155);
        assertNull(AnnotationUtility.containsAnnotation(ast5, ""));

        // Error Scenarios
        DetailAST ast6 = new DetailAST();
        ast6.setType(155);
        try {
            AnnotationUtility.containsAnnotation(ast6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("the ast is null", ex.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_ast() {
        // Typical Use Case
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        
        assertTrue(AnnotationUtility.containsAnnotation(ast1));
        
        // Edge Cases
        DetailAST ast2 = new DetailAST();
        ast2.setType(100);
        assertFalse(AnnotationUtility.containsAnnotation(ast2));
        
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("the ast is null", ex.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationHolder() {
        // Typical Use Case
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        DetailAST ast2 = new DetailAST();
        ast2.setType(16);
        
        assertNull(AnnotationUtility.getAnnotationHolder(null));
        assertNull(AnnotationUtility.getAnnotationHolder(ast1));
        assertNull(AnnotationUtility.getAnnotationHolder(ast2));
        
        // Edge Cases
        DetailAST ast3 = new DetailAST();
        ast3.setType(100);
        
        assertNull(AnnotationUtility.getAnnotationHolder(ast3));
        
        // Error Scenarios
        DetailAST ast4 = new DetailAST();
        ast4.setType(155);
        try {
            AnnotationUtility.getAnnotationHolder(ast4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("the ast is null", ex.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation() {
        // Typical Use Case
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        DetailAST ast2 = new DetailAST();
        ast2.setType(16);

        assertNull(AnnotationUtility.getAnnotation(null, "TestAnnotation"));
        assertNull(AnnotationUtility.getAnnotation(ast1, "TestAnnotation"));
        assertNull(AnnotationUtility.getAnnotation(ast2, "TestAnnotation"));

        // Edge Cases
        DetailAST ast3 = new DetailAST();
        ast3.setType(556);
        assertNull(AnnotationUtility.getAnnotation(ast3, "TestAnnotation"));
        
        DetailAST ast4 = new DetailAST();
        ast4.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast4, null));
        
        DetailAST ast5 = new DetailAST();
        ast5.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast5, ""));
        
        DetailAST ast6 = new DetailAST();
        ast6.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast6, "     "));

        // Error Scenarios
        DetailAST ast7 = new DetailAST();
        ast7.setType(155);
        try {
            AnnotationUtility.getAnnotation(ast7, "InvalidAnnotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("the annotation is empty or spaces", ex.getMessage());
        }
    }
}