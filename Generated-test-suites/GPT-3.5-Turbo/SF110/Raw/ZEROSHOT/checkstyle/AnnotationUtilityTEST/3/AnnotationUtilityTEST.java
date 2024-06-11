import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "Annotation");
    }

    @Test
    public void testContainsAnnotation_ValidAST() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "Annotation");
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_WithNoAnnotation() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_ValidAST_WithNoAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_WithAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addToken(158);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_WithNoAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "Annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_ValidAST_WithMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addToken(158);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.addToken(1);
        child.addChild(firstChild);
        ast.addChild(child);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "MatchingAnnotation");
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation_ValidAST_WithNoMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addToken(158);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.addToken(1);
        child.addChild(firstChild);
        ast.addChild(child);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "NonMatchingAnnotation");
        assertNull(result);
    }
}