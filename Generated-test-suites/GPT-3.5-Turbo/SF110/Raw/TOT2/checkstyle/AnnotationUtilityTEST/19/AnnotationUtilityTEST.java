package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NoAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAST() {
        AnnotationUtility.containsAnnotation(null, "Annotation");
    }

    @Test
    public void testGetAnnotationHolderWhenAnnotationType155() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWhenAnnotationType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);  // Setting the type to 16 for testing
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }

    @Test
    public void testGetAnnotationWithExistingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);  // Setting the type to 155 for testing
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "ExistingAnnotation");
        assertNotNull(annotation);
    }

    @Test
    public void testGetAnnotationWithNonExistingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);  // Setting the type to 155 for testing
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "NonExistingAnnotation");
        assertNull(annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        AnnotationUtility.getAnnotation(null, "Annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> {
            AnnotationUtility.getAnnotationHolder(null);
        });
    }
}