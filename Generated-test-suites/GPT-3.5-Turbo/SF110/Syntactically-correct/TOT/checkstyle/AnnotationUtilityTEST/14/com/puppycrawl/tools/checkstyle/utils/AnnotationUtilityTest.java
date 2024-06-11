import static org.junit.Assert.*;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    private final DetailAST NULL_AST = null;

    // Test for containsAnnotation(DetailAST ast, String annotation)
    @Test
    public void testContainsAnnotationWithNullAst() {
        try {
            AnnotationUtility.containsAnnotation(NULL_AST, "TestAnnotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithValidAstAndNullAnnotation() {
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithValidAstAndEmptyAnnotation() {
        try {
            AnnotationUtility.containsAnnotation(new DetailAST(), "   ");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    // Test for containsAnnotation(DetailAST ast)
    @Test
    public void testContainsAnnotationWithNullAst2() {
        try {
            AnnotationUtility.containsAnnotation(NULL_AST);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    // Test for getAnnotationHolder(DetailAST ast)
    @Test
    public void testGetAnnotationHolderWithNullAst() {
        try {
            AnnotationUtility.getAnnotationHolder(NULL_AST);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    // Test for getAnnotation(DetailAST ast, String annotation)
    @Test
    public void testGetAnnotationWithNullAst() {
        try {
            AnnotationUtility.getAnnotation(NULL_AST, "TestAnnotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithValidAstAndNullAnnotation() {
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithValidAstAndEmptyAnnotation() {
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), "   ");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithValidAstAndNonExistingAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(new DetailAST(), "NonExistingAnnotation"));
    }

    // Add more test cases for each method if needed

}