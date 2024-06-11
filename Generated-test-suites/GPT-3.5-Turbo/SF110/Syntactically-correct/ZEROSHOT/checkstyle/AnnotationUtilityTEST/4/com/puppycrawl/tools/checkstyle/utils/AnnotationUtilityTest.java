package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_NullAST() {
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_NoAnnotationHolder() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }
    
    @Test
    public void testContainsAnnotation_NoAnnotationHolder_NullAST() {
        DetailAST ast = null;
        try {
            AnnotationUtility.containsAnnotation(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationHolder_StatementAst() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_StatementAst_NullAST() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationHolder_ClassDefAst() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotationHolder_ClassDefAst_NullAST() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_WithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST child1 = new DetailAST();
        child1.setType(159);
        DetailAST child2 = new DetailAST();
        child2.setType(7);
        DetailAST child3 = new DetailAST();
        child3.setType(8);
        child1.setFirstChild(child2);
        child2.setNextSibling(child3);
        ast.setFirstChild(child1);
        String annotation = "TestAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_NullAST() {
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        try {
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        try {
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_EmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "   ";
        try {
            AnnotationUtility.getAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
}