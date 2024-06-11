
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAST_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_ReturnsTrueWhenAnnotationExists() {
        DetailAST ast = new DetailAST();
        // Add logic to set up the ast object if necessary
        boolean result = AnnotationUtility.containsAnnotation(ast, "Annotation");
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotation_ReturnsFalseWhenAnnotationDoesNotExist() {
        DetailAST ast = new DetailAST();
        // Add logic to set up the ast object if necessary
        boolean result = AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation");
        assertFalse(result);
    }

    @Test
    public void testGetAnnotationHolder_NullAST_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_NullAST_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(null, "Annotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        try {
            DetailAST ast = new DetailAST();
            // Add logic to set up the ast object if necessary
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation_ThrowsIllegalArgumentException() {
        try {
            DetailAST ast = new DetailAST();
            // Add logic to set up the ast object if necessary
            AnnotationUtility.getAnnotation(ast, "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_WhitespaceAnnotation_ThrowsIllegalArgumentException() {
        try {
            DetailAST ast = new DetailAST();
            // Add logic to set up the ast object if necessary
            AnnotationUtility.getAnnotation(ast, "   ");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    // Add more test cases for remaining methods if necessary

}

