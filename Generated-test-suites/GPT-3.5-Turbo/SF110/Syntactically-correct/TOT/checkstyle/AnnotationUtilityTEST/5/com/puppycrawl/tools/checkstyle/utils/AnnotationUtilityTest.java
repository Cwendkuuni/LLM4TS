
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setText("some text");
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = new DetailAST();
        ast.setText("some text");
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setParent(ast);
        annotationHolder.setFirstChild(null);
        ast.setFirstChild(annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithInvalidAST() {
        DetailAST ast = new DetailAST();
        ast.setText("some text");
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setText("some text");
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setParent(ast);
        annotationHolder.setFirstChild(null);
        ast.setFirstChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setParent(ast);
        annotationHolder.setFirstChild(null);
        ast.setFirstChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        annotationHolder.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(null);
        annotationHolder.setFirstChild(firstChild);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(124);
        FullIdent fullIdent = new FullIdent(null, "annotation");
        firstChild.getNextSibling().addChild(fullIdent);
        assertNull(AnnotationUtility.getAnnotation(ast, "missingAnnotation"));
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
}
