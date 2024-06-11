package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
   
    @Test
    public void testContainsAnnotationWithNullAst() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            AnnotationUtility.containsAnnotation(ast, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            AnnotationUtility.containsAnnotation(ast, "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationReturnsTrue() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        ast.addChild(annotation);
        boolean result = AnnotationUtility.containsAnnotation(ast, "TestAnnotation");
        assertTrue(result);
    }
    
    @Test
    public void testContainsAnnotationReturnsFalse() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "TestAnnotation");
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotationHolderWithNullAst() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolderWithClassOrInterface() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(158, annotationHolder.getType());
    }
    
    @Test
    public void testContainsAnnotationHolderWithOtherTypes() {
        DetailAST ast = new DetailAST();
        ast.setType(123);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(5, annotationHolder.getType());
    }
    
    @Test
    public void testGetAnnotationWithNullAst() {
        try {
            AnnotationUtility.getAnnotation(null, "TestAnnotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithNullAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        try {
            DetailAST ast = new DetailAST();
            AnnotationUtility.getAnnotation(ast, "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationReturnsAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST child = new DetailAST();
        child.setType(FullIdent.getTypeByName("TestAnnotation"));
        annotation.addChild(child);
        annotationHolder.addChild(annotation);
        
        DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertEquals(result, annotation);
    }
    
    @Test
    public void testGetAnnotationReturnsNull() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertNull(result);
    }
}