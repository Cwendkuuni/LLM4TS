package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test
    public void testContainsAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast contains the specified annotation
        ast.addToken(new DetailAST(159)); // Adding annotation token
        DetailAST child = new DetailAST(159);
        child.addChild(new DetailAST(123)); // Adding dummy child
        ast.addChild(child);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));

        // Test case 3: ast does not contain the specified annotation
        assertFalse(AnnotationUtility.containsAnnotation(ast, "non_existing_annotation"));
    }

    @Test
    public void testContainsAnnotation2() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast contains the annotation holder
        ast.addToken(new DetailAST(158)); // Annotation holder token
        assertTrue(AnnotationUtility.containsAnnotation(ast));

        // Test case 3: ast does not contain the annotation holder
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolder() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast is of type 155 or 16
        DetailAST annotationHolder = new DetailAST(158); // Annotation holder token
        ast.addToken(new DetailAST(155)); // Adding type 155 token
        ast.addChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));

        // Test case 3: ast is not of type 155 or 16
        ast.addToken(new DetailAST(500)); // Adding random token
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: annotation is null
        try {
            AnnotationUtility.getAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }

        // Test case 3: annotation is empty or spaces
        try {
            AnnotationUtility.getAnnotation(ast, "     ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }

        // Test case 4: ast contains the specified annotation
        DetailAST annotationHolder = new DetailAST(158); // Annotation holder token
        ast.addToken(new DetailAST(155)); // Adding type 155 token
        ast.addChild(annotationHolder);

        DetailAST annotation = new DetailAST(159); // Annotation token
        DetailAST child = new DetailAST(124);
        child.addChild(new DetailAST(123)); // Adding dummy child
        annotation.addChild(child);
        annotationHolder.addChild(annotation);

        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));

        // Test case 5: ast does not contain the specified annotation
        assertNull(AnnotationUtility.getAnnotation(ast, "non_existing_annotation"));
    }
}