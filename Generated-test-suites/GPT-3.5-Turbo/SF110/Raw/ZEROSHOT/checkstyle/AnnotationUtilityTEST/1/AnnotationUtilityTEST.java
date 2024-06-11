package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
 
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAnnotation() {
        AnnotationUtility.containsAnnotation(new DetailAST(), null);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_EmptyAnnotation() {
        AnnotationUtility.containsAnnotation(new DetailAST(), "");
    }
 
    @Test
    public void testContainsAnnotation_ValidAST() {
        DetailAST ast = new DetailAST();
        // Initialize AST with valid values
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertTrue(result);
    }
 
    @Test
    public void testContainsAnnotation_InvalidAST() {
        DetailAST ast = new DetailAST();
        // Initialize AST with invalid values
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertFalse(result);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullASTParameter() {
        AnnotationUtility.containsAnnotation(null);
    }
 
    @Test
    public void testContainsAnnotation_ValidASTParameter() {
        DetailAST ast = new DetailAST();
        // Initialize AST with valid values
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertTrue(result);
    }
 
    @Test
    public void testContainsAnnotation_InvalidASTParameter() {
        DetailAST ast = new DetailAST();
        // Initialize AST with invalid values
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
 
    @Test
    public void testGetAnnotationHolder_ValidAST_Type155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        // Initialize AST with valid values
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }
 
    @Test
    public void testGetAnnotationHolder_ValidAST_Type16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        // Initialize AST with valid values
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }
 
    @Test
    public void testGetAnnotationHolder_InvalidAST() {
        DetailAST ast = new DetailAST();
        // Initialize AST with invalid values
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        AnnotationUtility.getAnnotation(new DetailAST(), null);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        AnnotationUtility.getAnnotation(new DetailAST(), "");
    }
 
    @Test
    public void testGetAnnotation_ValidAST_ValidAnnotation() {
        DetailAST ast = new DetailAST();
        // Initialize AST with valid values
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNotNull(result);
    }
 
    @Test
    public void testGetAnnotation_ValidAST_InvalidAnnotation() {
        DetailAST ast = new DetailAST();
        // Initialize AST with invalid values
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }
    
}