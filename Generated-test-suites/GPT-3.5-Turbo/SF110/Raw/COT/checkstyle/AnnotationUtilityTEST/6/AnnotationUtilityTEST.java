
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {
    private static final DetailAST AST = new DetailAST();

    @Test
    public void testContainsAnnotationWithNullAst() {
        boolean result = AnnotationUtility.containsAnnotation(null, "annotation");
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithNonNullAst() {
        boolean result = AnnotationUtility.containsAnnotation(AST, "annotation");
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        boolean result = AnnotationUtility.containsAnnotation(AST, null);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        boolean result = AnnotationUtility.containsAnnotation(AST, "");
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithWhitespaceAnnotation() {
        boolean result = AnnotationUtility.containsAnnotation(AST, "   ");
        assertFalse(result);
    }

    // Edge cases

    @Test
    public void testContainsAnnotationWithAnnotationHolderType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    // Exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationThrowsExceptionWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationThrowsExceptionWithNullAnnotation() {
        AnnotationUtility.containsAnnotation(AST, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationThrowsExceptionWithEmptyAnnotation() {
        AnnotationUtility.containsAnnotation(AST, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationThrowsExceptionWithWhitespaceAnnotation() {
        AnnotationUtility.containsAnnotation(AST, "   ");
    }

    // Helper method tests

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST result = AnnotationUtility.getAnnotationHolder(null);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolderWithAnnotationHolderOfType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolderWithAnnotationHolderOfType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        DetailAST result = AnnotationUtility.getAnnotation(null, "annotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST result = AnnotationUtility.getAnnotation(AST, null);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST result = AnnotationUtility.getAnnotation(AST, "");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotation() {
        DetailAST result = AnnotationUtility.getAnnotation(AST, "   ");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }
}
