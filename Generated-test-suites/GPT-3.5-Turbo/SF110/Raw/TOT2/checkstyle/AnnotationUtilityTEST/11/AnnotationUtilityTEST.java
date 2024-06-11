import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationNullAST() {
        try {
            AnnotationUtility.containsAnnotation(null, "MyAnnotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithType5() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertTrue(result != null);
    }

    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertTrue(result != null);
    }

    @Test
    public void testGetAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(155);
        holder.addFirstChild(new DetailAST());
        holder.getFirstChild().addFirstChild(new DetailAST());
        holder.getFirstChild().getFirstChild().setType(159);
        holder.getFirstChild().getFirstChild().addFirstChild(new DetailAST());
        holder.getFirstChild().getFirstChild().getFirstChild().setType(61); // FullIdent
        holder.getFirstChild().getFirstChild().getFirstChild().addFirstChild(new DetailAST());
        holder.getFirstChild().getFirstChild().getFirstChild().getFirstChild().setText("MyAnnotation");
        ast.setType(155);
        ast.addFirstChild(holder);
        ast.addNextSibling(new DetailAST());
        assertEquals(holder.getFirstChild(), AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "MyAnnotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "  ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
}