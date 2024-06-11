package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_True() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_False() {
        DetailAST ast = null;
        try {
            AnnotationUtility.containsAnnotation(ast, "annotation");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.setType(155);
        ast.addChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testContainsAnnotationHolder_NullAST() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        DetailAST child1 = new DetailAST();
        DetailAST child2 = new DetailAST();
        child1.setType(159);
        child2.setType(155);
        annotationHolder.addChild(child1);
        annotationHolder.addChild(child2);
        ast.addChild(annotationHolder);
        assertEquals(child1, AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAST() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotation(ast, "annotation");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
}