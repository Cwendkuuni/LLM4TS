package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.antlr.v4.parse.ANTLRParser.exceptionGroup_return;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "testAnnotation";
        
        // Test when the ast is null
        try {
            AnnotationUtility.containsAnnotation(null, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // TODO: Add more test cases for containsAnnotation() method
    }
    
    @Test
    public void testContainsAnnotationWithAst() {
        DetailAST ast = new DetailAST();
        
        // Test when the ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // TODO: Add more test cases for containsAnnotation() method with ast parameter
    }
    
    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        
        // Test when the ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // TODO: Add more test cases for getAnnotationHolder() method
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "testAnnotation";
        
        // Test when the ast is null
        try {
            AnnotationUtility.getAnnotation(null, annotation);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test when the annotation is null
        try {
            AnnotationUtility.getAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test when the annotation is empty or spaces
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // TODO: Add more test cases for getAnnotation() method
    }
}