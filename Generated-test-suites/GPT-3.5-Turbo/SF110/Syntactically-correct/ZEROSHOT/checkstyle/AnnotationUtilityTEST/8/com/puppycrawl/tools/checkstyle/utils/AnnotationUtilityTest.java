package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test
    public void testContainsAnnotation_ValidAST() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        Assert.assertFalse("Expected false, but got true", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_WithoutAnnotation() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_ValidAST_WithoutAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertFalse("Expected false, but got true", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_ValidAST_WithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull("Expected null, but got a non-null annotation holder", annotationHolder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test
    public void testGetAnnotation_ValidAST_WithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNull("Expected null, but got a non-null annotation", annotation);
    }

}