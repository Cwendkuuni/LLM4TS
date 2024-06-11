
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation_withNonNullAstAndValidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        boolean result = AnnotationUtility.containsAnnotation(ast, "Annotation");
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_withNullAst() {
        try {
            AnnotationUtility.containsAnnotation(null, "Annotation");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_withNonNullAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        try {
            AnnotationUtility.containsAnnotation(ast, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_withNonNullAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        try {
            AnnotationUtility.containsAnnotation(ast, "   ");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_withNonNullAstAndAnnotationHolderContainsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_withNonNullAstAndAnnotationHolderDoesNotContainAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }
    
    @Test
    public void testGetAnnotationHolder_withNonNullAstAndAnnotationTypeIs155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_withNonNullAstAndAnnotationTypeIs16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        ast.addChild(new DetailAST());
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_withNonNullAstAndAnnotationTypeIsNot155Or16() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        ast.addChild(new DetailAST());
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_withNonNullAstAndValidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Annotation");
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_withNullAst() {
        try {
            AnnotationUtility.getAnnotation(null, "Annotation");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_withNonNullAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_withNonNullAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_withNonNullAstAndAnnotationHolderContainsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(1);
        ast.getFirstChild().getFirstChild().setNextSibling(new DetailAST());
        ast.getFirstChild().getFirstChild().getNextSibling().setType(1);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Annotation");
        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotation_withNonNullAstAndAnnotationHolderDoesNotContainAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(1);
        ast.getFirstChild().getFirstChild().setNextSibling(new DetailAST());
        ast.getFirstChild().getFirstChild().getNextSibling().setType(2);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "Annotation");
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_withNonNullAstAndAnnotationHolderContainsAnnotationButDifferentName() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(159);
        ast.getFirstChild().addChild(new DetailAST());
        ast.getFirstChild().getFirstChild().setType(1);
        ast.getFirstChild().getFirstChild().setNextSibling(new DetailAST());
        ast.getFirstChild().getFirstChild().getNextSibling().setType(1);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "DifferentAnnotation");
        assertNull(result);
    }
}
