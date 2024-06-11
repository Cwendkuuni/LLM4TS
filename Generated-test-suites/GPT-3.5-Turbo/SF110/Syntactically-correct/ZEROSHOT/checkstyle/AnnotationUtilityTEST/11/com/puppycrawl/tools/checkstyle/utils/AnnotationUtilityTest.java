package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.*;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        ast.addFirstToken(TokenTypes.ANNOTATIONS);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        DetailAST annotation = new DetailAST();
        ast.addChild(annotation);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        annotation.setType(TokenTypes.ANNOTATION);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        DetailAST firstChild = new DetailAST();
        annotation.addChild(firstChild);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        firstChild.setType(TokenTypes.IDENT);
        firstChild.setText("NotAnnotation");
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));

        firstChild.setText("Annotation");
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        try {
            AnnotationUtility.containsAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));

        ast.addFirstToken(TokenTypes.ANNOTATIONS);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));

        DetailAST annotation = new DetailAST();
        ast.addChild(annotation);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));

        annotation.setType(TokenTypes.ANNOTATION);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        ast.addFirstToken(TokenTypes.ANNOTATIONS);
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        DetailAST annotation = new DetailAST();
        ast.addChild(annotation);
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        annotation.setType(TokenTypes.ANNOTATION);
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        DetailAST firstChild = new DetailAST();
        annotation.addChild(firstChild);
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        firstChild.setType(TokenTypes.AT);
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        firstChild.setType(TokenTypes.IDENT);
        firstChild.setText("NotAnnotation");
        assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));

        firstChild.setText("Annotation");
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
        assertNull(AnnotationUtility.getAnnotation(ast, " "));
        assertNull(AnnotationUtility.getAnnotation(ast, "  "));
    }
}