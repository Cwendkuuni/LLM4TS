
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        AnnotationUtility.containsAnnotation(null, "MyAnnotation");
    }

    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(!AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, "   ");
    }

    @Test
    public void testContainsAnnotationWithoutAST() {
        try {
            AnnotationUtility.containsAnnotation(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithAnnotationAST() {
        DetailAST ast = new DetailAST();
        DetailAST annotationAST = new DetailAST(155); // Annotation type
        ast.addChild(annotationAST);
        assertEquals(annotationAST.getFirstChild(), AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithoutAnnotationAST() {
        DetailAST ast = new DetailAST();
        assertEquals(null, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST(158); // Annotation holder type
        DetailAST annotation = new DetailAST(159); // Annotation type
        DetailAST annotationName = new DetailAST();
        annotationName.addChild(new DetailAST(16));
        annotationName.addChild(new DetailAST(5));
        annotationName.addChild(new DetailAST(159)); // Annotation name type
        annotationHolder.addChild(annotation);
        annotationHolder.addChild(annotationName);
        ast.addChild(annotationHolder);
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithInvalidAnnotation() {
        DetailAST ast = new DetailAST();
        assertEquals(null, AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        AnnotationUtility.getAnnotation(null, "MyAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

}
