
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast is not null, annotation is null
        DetailAST ast2 = new DetailAST();
        try {
            AnnotationUtility.containsAnnotation(ast2, null);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }

        // Test case 3: ast is not null, annotation is empty
        try {
            AnnotationUtility.containsAnnotation(ast2, "");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }

        // Test case 4: ast is not null, annotation is spaces
        try {
            AnnotationUtility.containsAnnotation(ast2, "    ");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }

        // Test case 5: ast is not null, ast does not contain the annotation
        DetailAST ast5 = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast5, "annotation"));

        // Test case 6: ast is not null, ast contains the annotation
        DetailAST ast6 = new DetailAST();
        DetailAST annotation6 = new DetailAST();
        ast6.addChild(annotation6);
        assertTrue(AnnotationUtility.containsAnnotation(ast6, "annotation"));
    }

    @Test
    public void testContainsAnnotation2() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast is not null, ast does not contain the annotation
        DetailAST ast2 = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast2));

        // Test case 3: ast is not null, ast contains the annotation
        DetailAST ast3 = new DetailAST();
        DetailAST annotationHolder3 = new DetailAST();
        ast3.addChild(annotationHolder3);
        assertTrue(AnnotationUtility.containsAnnotation(ast3));
    }

    @Test
    public void testGetAnnotationHolder() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast is not null, ast.getType() == 155
        DetailAST ast2 = new DetailAST();
        ast2.setType(155);
        DetailAST annotationHolder2 = AnnotationUtility.getAnnotationHolder(ast2);
        assertNull(annotationHolder2);

        // Test case 3: ast is not null, ast.getType() == 16
        DetailAST ast3 = new DetailAST();
        ast3.setType(16);
        DetailAST annotationHolder3 = new DetailAST();
        ast3.addChild(annotationHolder3);
        DetailAST annotationHolderResult3 = AnnotationUtility.getAnnotationHolder(ast3);
        assertEquals(annotationHolder3, annotationHolderResult3);

        // Test case 4: ast is not null, ast.getType() != 155 and ast.getType() != 16
        DetailAST ast4 = new DetailAST();
        DetailAST annotationHolder4 = new DetailAST();
        ast4.addChild(annotationHolder4);
        DetailAST annotationHolderResult4 = AnnotationUtility.getAnnotationHolder(ast4);
        assertEquals(annotationHolder4, annotationHolderResult4);
    }

    @Test
    public void testGetAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }

        // Test case 2: ast is not null, annotation is null
        DetailAST ast2 = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast2, null);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }

        // Test case 3: ast is not null, annotation is empty
        try {
            AnnotationUtility.getAnnotation(ast2, "");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }

        // Test case 4: ast is not null, annotation is spaces
        try {
            AnnotationUtility.getAnnotation(ast2, "    ");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }

        // Test case 5: ast is not null, ast does not contain the annotation
        DetailAST ast5 = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast5, "annotation"));

        // Test case 6: ast is not null, ast contains the annotation
        DetailAST ast6 = new DetailAST();
        DetailAST annotationHolder6 = new DetailAST();
        ast6.addChild(annotationHolder6);

        DetailAST child61 = new DetailAST();
        child61.setType(159);
        DetailAST firstChild61 = new DetailAST();
        firstChild61.setType(6);
        DetailAST nextSibling61 = new DetailAST();
        nextSibling61.setType(7);
        firstChild61.setNextSibling(nextSibling61);
        child61.addChild(firstChild61);

        annotationHolder6.addChild(child61);

        assertEquals(child61, AnnotationUtility.getAnnotation(ast6, "annotation"));
    }
}
