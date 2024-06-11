package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation_NullAST() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.containsAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_WhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.containsAnnotation(ast, "  ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_WithAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(7);
        child.setFirstChild(firstChild);
        ast.setFirstChild(child);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotation_WithoutAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(7);
        child.setFirstChild(firstChild);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "otherAnnotation"));
    }
    
    @Test
    public void testContainsAnnotationHolder_NullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolder_WithAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(7);
        child.setFirstChild(firstChild);
        ast.addChild(child);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(child, annotationHolder);
    }
    
    @Test
    public void testContainsAnnotationHolder_WithoutAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotation_NullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_WhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast, "  ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_WithMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(7);
        child.setFirstChild(firstChild);
        ast.addChild(child);
        assertEquals(child, AnnotationUtility.getAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testGetAnnotation_WithoutMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(7);
        child.setFirstChild(firstChild);
        assertNull(AnnotationUtility.getAnnotation(ast, "otherAnnotation"));
    }
    
    @Test
    public void testGetAnnotation_WithInvalidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(5);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().setType(7);
        child.setFirstChild(firstChild);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation  "));
    }
}