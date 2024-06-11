import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    // Test case for containsAnnotation(DetailAST ast, String annotation)
    @Test
    public void testContainsAnnotationWithAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotationNotPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "Annotation");
    }

    // Test case for containsAnnotation(DetailAST ast)
    @Test
    public void testContainsAnnotationWithoutAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithoutNullAst() {
        AnnotationUtility.containsAnnotation(null);
    }

    // Test case for getAnnotationHolder(DetailAST ast)
    @Test
    public void testGetAnnotationHolderWithAnnotationHolderPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation holder
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithoutAnnotationHolderPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation holder
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    // Test case for getAnnotation(DetailAST ast, String annotation)
    @Test
    public void testGetAnnotationWithAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }

    @Test
    public void testGetAnnotationWithAnnotationNotPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "Annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    //Merge all the individual test cases into a complete JUnit 4 test file
    //for the given Java class
    

    // Test cases for containsAnnotation(DetailAST ast, String annotation)
    @Test
    public void testContainsAnnotationWithAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotationNotPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "Annotation");
    }

    // Test cases for containsAnnotation(DetailAST ast)
    @Test
    public void testContainsAnnotationWithoutAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithoutNullAst() {
        AnnotationUtility.containsAnnotation(null);
    }

    // Test cases for getAnnotationHolder(DetailAST ast)
    @Test
    public void testGetAnnotationHolderWithAnnotationHolderPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation holder
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithoutAnnotationHolderPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation holder
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    // Test cases for getAnnotation(DetailAST ast, String annotation)
    @Test
    public void testGetAnnotationWithAnnotationPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast with the annotation
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }

    @Test
    public void testGetAnnotationWithAnnotationNotPresent() {
        DetailAST ast = new DetailAST();
        // Set up the ast without the annotation
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "Annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    
}