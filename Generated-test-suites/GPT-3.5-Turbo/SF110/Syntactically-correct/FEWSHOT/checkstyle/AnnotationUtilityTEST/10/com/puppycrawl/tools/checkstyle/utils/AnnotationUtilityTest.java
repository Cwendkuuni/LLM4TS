package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import com.google.common.base.*;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        
        // Test case: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case: annotation is null
        try {
            AnnotationUtility.containsAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test case: annotation is empty or spaces
        try {
            AnnotationUtility.containsAnnotation(ast, "   ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case: ast is not null, annotation exists in ast
        ast.initialize(155);
        ast.addChild(new DetailAST());
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
        
        // Test case: ast is not null, annotation does not exist in ast
        ast.initialize(155);
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }
    
    @Test
    public void testContainsAnnotation2() {
        DetailAST ast = new DetailAST();
        
        // Test case: ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case: ast is not null, annotation holder exists in ast
        ast.initialize(155);
        ast.addChild(new DetailAST(159));
        assertTrue(AnnotationUtility.containsAnnotation(ast));
        
        // Test case: ast is not null, annotation holder does not exist in ast
        ast.initialize(155);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST result;
        
        // Test case: ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case: ast.getType() == 155
        ast.initialize(155);
        ast.addChild(new DetailAST(158));
        result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
        assertEquals(158, result.getType());
        
        // Test case: ast.getType() == 16
        ast.initialize(16);
        ast.addChild(new DetailAST(158));
        result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
        assertEquals(158, result.getType());
        
        // Test case: ast.getType() != 155 && ast.getType() != 16
        ast.initialize(5);
        ast.addChild(new DetailAST(158));
        result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
        assertEquals(158, result.getType());
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "Test";
        DetailAST result;
        
        // Test case: ast is null
        try {
            AnnotationUtility.getAnnotation(null, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case: annotation is null
        try {
            AnnotationUtility.getAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test case: annotation is empty or spaces
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case: annotation exists in ast
        ast.initialize(155);
        ast.addChild(createChildAnnotation("Test1", 159));
        ast.addChild(createChildAnnotation("Test2", 159));
        result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(result);
        assertEquals("Test1", result.getChild(0).getNextSibling().getText());
        
        // Test case: annotation does not exist in ast
        ast.initialize(155);
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
    
    private DetailAST createChildAnnotation(String name, int type) {
        DetailAST child = new DetailAST(type);
        child.addChild(new DetailAST());
        child.getFirstChild().setNextSibling(new FullIdent(name));
        return child;
    }
}