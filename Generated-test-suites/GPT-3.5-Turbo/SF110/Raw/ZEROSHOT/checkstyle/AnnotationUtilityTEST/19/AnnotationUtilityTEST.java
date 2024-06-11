import org.junit.*;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast is not null, annotation is null
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test case 3: ast is not null, annotation is empty
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case 4: ast is not null, annotation is spaces
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case 5: ast is not null, annotation is not found
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        assertNull(AnnotationUtility.containsAnnotation(ast1, "annotation"));
        
        // Test case 6: ast is not null, annotation is found
        DetailAST ast2 = new DetailAST();
        ast2.setType(155);
        DetailAST holder = new DetailAST();
        holder.setType(158);
        DetailAST child = new DetailAST();
        child.setType(159);
        child.setFirstChild(new DetailAST());
        child.getFirstChild().setNextSibling(new DetailAST());
        child.getFirstChild().getNextSibling().setText("annotation");
        holder.setFirstChild(child);
        ast2.setFirstChild(holder);
        assertNotNull(AnnotationUtility.containsAnnotation(ast2, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast is not null, annotation holder is null
        DetailAST ast1 = new DetailAST();
        assertNull(AnnotationUtility.containsAnnotation(ast1));
        
        // Test case 3: ast is not null, annotation holder is not null
        DetailAST ast2 = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(158);
        ast2.setFirstChild(holder);
        assertTrue(AnnotationUtility.containsAnnotation(ast2));
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
        
        // Test case 2: ast is not null, annotation holder type is 155
        DetailAST ast2 = new DetailAST();
        ast2.setType(155);
        assertNull(AnnotationUtility.getAnnotationHolder(ast2));
        
        // Test case 3: ast is not null, annotation holder type is 16
        DetailAST ast3 = new DetailAST();
        ast3.setType(16);
        assertNull(AnnotationUtility.getAnnotationHolder(ast3));
        
        // Test case 4: ast is not null, annotation holder type is neither 155 nor 16
        DetailAST ast4 = new DetailAST();
        ast4.setType(5);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast4));
    }

    @Test
    public void testGetAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast is not null, annotation is null
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test case 3: ast is not null, annotation is empty
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case 4: ast is not null, annotation is spaces
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case 5: ast is not null, annotation is not found
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast1, "annotation"));
        
        // Test case 6: ast is not null, annotation is found
        DetailAST ast2 = new DetailAST();
        ast2.setType(155);
        DetailAST holder = new DetailAST();
        holder.setType(158);
        DetailAST child = new DetailAST();
        child.setType(159);
        child.setFirstChild(new DetailAST());
        child.getFirstChild().setNextSibling(new DetailAST());
        child.getFirstChild().getNextSibling().setText("annotation");
        holder.setFirstChild(child);
        ast2.setFirstChild(holder);
        assertNotNull(AnnotationUtility.getAnnotation(ast2, "annotation"));
    }
}