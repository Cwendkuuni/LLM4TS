import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "testAnnotation";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        String annotation = "testAnnotation";
        try {
            AnnotationUtility.containsAnnotation(null, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.containsAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "   ";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationNoAnnotationHolder() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test
    public void testContainsAnnotationNoAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.containsAnnotation(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolderWithType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithType5() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        String annotation = "testAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithNameNotEqual() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST child = new DetailAST();
        annotationHolder.addChild(child);
        child.setType(159);

        String annotation = "testAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithNameEqual() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST child = new DetailAST();
        annotationHolder.addChild(child);
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        child.addChild(firstChild);

        FullIdent fullIdent = new FullIdent();
        firstChild.setNextSibling(fullIdent);

        String name = "testAnnotation";
        fullIdent.setText(name);

        String annotation = "testAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(result);
    }
}