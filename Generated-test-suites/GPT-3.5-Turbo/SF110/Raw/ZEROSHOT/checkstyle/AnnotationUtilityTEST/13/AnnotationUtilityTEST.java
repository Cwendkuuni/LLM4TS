package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotationWithNonNullAstAndNonNullAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        Assert.assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test
    public void testContainsAnnotationWithNonNullAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, null);
        Assert.assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithBlankAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, "    ");
    }
    
    @Test
    public void testContainsAnnotationWithNonNullAstAndNonNullHolder() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        Assert.assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstInHolder() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testGetAnnotationHolderWithNonNullAst() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        Assert.assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationWithNonNullAstAndNonNullAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        Assert.assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNonNullAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithBlankAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "    ");
    }
}


