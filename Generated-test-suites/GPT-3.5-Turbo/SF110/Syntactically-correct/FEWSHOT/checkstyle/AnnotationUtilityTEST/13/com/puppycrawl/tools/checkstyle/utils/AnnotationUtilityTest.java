
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation_withValidASTAndAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setText("annotation1");
        child.setFirstChild(firstChild);
        ast.addChild(child);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation1"));
    }

    @Test
    public void testContainsAnnotation_withValidASTAndAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setText("annotation1");
        child.setFirstChild(firstChild);
        ast.addChild(child);

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "annotation1");
    }

    @Test
    public void testContainsAnnotation_withNullAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setText("annotation1");
        child.setFirstChild(firstChild);
        ast.addChild(child);

        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withEmptyAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setText("annotation1");
        child.setFirstChild(firstChild);
        ast.addChild(child);

        AnnotationUtility.containsAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withSpaceAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setText("annotation1");
        child.setFirstChild(firstChild);
        ast.addChild(child);

        AnnotationUtility.containsAnnotation(ast, "   ");
    }

    @Test
    public void testContainsAnnotation_withNullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(null)
        );
    }

    @Test
    public void testContainsAnnotation_withValidAST_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST holder = new DetailAST();
        holder.branchContains(159);
        ast.addChild(holder);

        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_withValidAST_ReturnsFalse() {
        DetailAST ast = new DetailAST();

        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullAST_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_withValidASTAndAnnotation_ReturnsDetailAST() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.addChild(annotationHolder);

        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_withValidASTAndNoAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        ast.setType(5);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAST_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "annotation1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(5);

        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withEmptyAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(5);

        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withSpaceAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(5);

        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_withValidASTAndAnnotation_ReturnsDetailAST() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setText("annotation1");
        child.setFirstChild(firstChild);
        ast.addChild(child);

        assertEquals(child, AnnotationUtility.getAnnotation(ast, "annotation1"));
    }

    @Test
    public void testGetAnnotation_withValidASTAndNoAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        ast.setType(5);

        assertNull(AnnotationUtility.getAnnotation(ast, "annotation1"));
    }
}
