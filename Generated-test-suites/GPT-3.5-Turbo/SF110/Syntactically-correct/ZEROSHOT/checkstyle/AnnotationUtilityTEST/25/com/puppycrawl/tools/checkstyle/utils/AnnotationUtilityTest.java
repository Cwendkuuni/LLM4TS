import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotation_ValidAST() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullASTNoAnnotation() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_ValidASTNoAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_ValidASTClass() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolder_ValidASTMethod() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_SpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_ValidASTNoAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_ValidASTAnnotationNotFound() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.addChild(new DetailAST());
        ast.setType(155);
        ast.addChild(holder);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_ValidASTAnnotationFound() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        DetailAST child = new DetailAST();
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new FullIdent());
        child.addChild(firstChild);
        holder.addChild(child);
        ast.setType(155);
        ast.addChild(holder);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNotNull(result);
    }
}