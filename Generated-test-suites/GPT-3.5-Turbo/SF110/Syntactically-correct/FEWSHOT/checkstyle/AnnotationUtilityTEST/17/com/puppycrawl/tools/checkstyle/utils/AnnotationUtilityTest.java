package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation_Positive() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotation_Negative() {
        DetailAST ast = null;
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testGetAnnotationHolder_Positive() {
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_Negative() {
        DetailAST ast = null;
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotation_Positive() {
        DetailAST ast = new DetailAST();
        String annotation = "annotation";
        assertNotNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
    
    @Test
    public void testGetAnnotation_Negative() {
        DetailAST ast = null;
        String annotation = "annotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
    
    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "";
        try {
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
}