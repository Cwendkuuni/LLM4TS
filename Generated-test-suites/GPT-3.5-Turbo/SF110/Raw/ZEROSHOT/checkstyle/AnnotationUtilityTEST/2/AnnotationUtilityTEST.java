package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ASTIsNull() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithAnnotation_ASTIsNull() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithAnnotation_AnnotationIsNull() {
        AnnotationUtility.containsAnnotation(createMockAst(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithAnnotation_AnnotationIsEmpty() {
        AnnotationUtility.containsAnnotation(createMockAst(), "");
    }
    
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = createMockAst();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testGetAnnotationHolder_ASTIsNull() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_ASTIsNull() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithAnnotation_ASTIsNull() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithAnnotation_AnnotationIsNull() {
        try {
            AnnotationUtility.getAnnotation(createMockAst(), null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithAnnotation_AnnotationIsEmpty() {
        try {
            AnnotationUtility.getAnnotation(createMockAst(), "");
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    // Helper method to create a mock DetailAST
    private DetailAST createMockAst() {
        // Implementation of the mock DetailAST
        return new DetailAST();
    }
}