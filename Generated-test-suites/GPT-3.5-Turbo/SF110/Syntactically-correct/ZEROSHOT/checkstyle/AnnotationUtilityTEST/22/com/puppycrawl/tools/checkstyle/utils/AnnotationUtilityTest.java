
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilityTest {
     
    // Test containsAnnotation() method with valid ast and annotation
    @Test
    public void testContainsAnnotation_ValidAstAndAnnotation() {
        DetailAST ast = createDetailAST();
        String annotation = "testAnnotation";
        
        boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
        
        Assert.assertTrue(result);
    }
    
    // Test containsAnnotation() method with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst() {
        String annotation = "testAnnotation";
        
        AnnotationUtility.containsAnnotation(null, annotation);
    }
    
    // Test containsAnnotation() method with valid ast and null annotation
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ValidAstNullAnnotation() {
        DetailAST ast = createDetailAST();
        
        AnnotationUtility.containsAnnotation(ast, null);
    }
    
    // Test containsAnnotation() method with valid ast and empty annotation
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ValidAstEmptyAnnotation() {
        DetailAST ast = createDetailAST();
        String annotation = "";
        
        AnnotationUtility.containsAnnotation(ast, annotation);
    }
    
    // Test containsAnnotation() method with valid ast and spaces annotation
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ValidAstSpacesAnnotation() {
        DetailAST ast = createDetailAST();
        String annotation = "   ";
        
        AnnotationUtility.containsAnnotation(ast, annotation);
    }
    
    // Test containsAnnotation() method with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst_NoAnnotation() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    // Test getAnnotationHolder() method with valid ast
    @Test
    public void testGetAnnotationHolder_ValidAst() {
        DetailAST ast = createDetailAST();
        
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        Assert.assertNotNull(result);
    }
    
    // Test getAnnotationHolder() method with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    // Test getAnnotation() method with valid ast and annotation
    @Test
    public void testGetAnnotation_ValidAstAndAnnotation() {
        DetailAST ast = createDetailAST();
        String annotation = "testAnnotation";
        
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        
        Assert.assertNotNull(result);
    }
    
    // Test getAnnotation() method with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst() {
        String annotation = "testAnnotation";
        
        AnnotationUtility.getAnnotation(null, annotation);
    }
    
    // Test getAnnotation() method with valid ast and null annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_ValidAstNullAnnotation() {
        DetailAST ast = createDetailAST();
        
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    // Test getAnnotation() method with valid ast and empty annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_ValidAstEmptyAnnotation() {
        DetailAST ast = createDetailAST();
        String annotation = "";
        
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    // Test getAnnotation() method with valid ast and spaces annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_ValidAstSpacesAnnotation() {
        DetailAST ast = createDetailAST();
        String annotation = "   ";
        
        AnnotationUtility.getAnnotation(ast, annotation);
    }
    
    // Helper method to create a dummy DetailAST for testing
    private DetailAST createDetailAST() {
        // Implement this method as per your requirement
    }
}

