package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ASTIsNull() {
        AnnotationUtility.containsAnnotation(null, "");
    }
    
    @Test
    public void testContainsAnnotation_ValidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
    }
    
    @Test
    public void testContainsAnnotation_InvalidAnnotation() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Invalid"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NoAST() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_ASTHasAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_ASTNoAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.initialize(1, "Test");
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_ASTIsNull() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_ASTHasAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_ASTNoAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.initialize(1, "Test");
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_ASTIsNull() {
        AnnotationUtility.getAnnotation(null, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_AnnotationIsNull() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_SpaceAnnotation() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        AnnotationUtility.getAnnotation(ast, "   ");
    }
    
    @Test
    public void testGetAnnotation_AnnotationFound() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        ast.addFirstChild(new DetailAST(159));
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
    }
    
    @Test
    public void testGetAnnotation_AnnotationNotFound() {
        DetailAST ast = new DetailAST();
        ast.initialize(155, "Test");
        ast.addFirstChild(new DetailAST(159));
        assertNull(AnnotationUtility.getAnnotation(ast, "Invalid"));
    }
    
}