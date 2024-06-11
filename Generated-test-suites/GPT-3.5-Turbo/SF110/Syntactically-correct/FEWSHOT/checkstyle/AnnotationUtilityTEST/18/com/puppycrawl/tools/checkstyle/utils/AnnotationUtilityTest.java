package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationAST());
        
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Ignore"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "Test");
    }
    
    @Test
    public void testContainsAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationAST());
        
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }
    
    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationAST());
        
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }
    
    @Test
    public void testContainsAnnotation_WhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationAST());
        
        assertFalse(AnnotationUtility.containsAnnotation(ast, "   "));
    }
    
    @Test
    public void testContainsAnnotation_EmptyAST() {
        DetailAST ast = new DetailAST();
        
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Test"));
    }
    
    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationHolderAST());
        
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationHolderAST());
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
        assertNull(AnnotationUtility.getAnnotation(ast, "Ignore"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, "Test");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationHolderAST());
        
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationHolderAST());
        
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test
    public void testGetAnnotation_WhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(createAnnotationHolderAST());
        
        assertNull(AnnotationUtility.getAnnotation(ast, "   "));
    }
    
    private DetailAST createAnnotationAST() {
        DetailAST annotationAST = new DetailAST();
        annotationAST.setType(159);
        
        DetailAST childAST = new DetailAST();
        childAST.setType(12);
        childAST.addFirstChild(createFullIdentAST());
        annotationAST.addFirstChild(childAST);
        
        return annotationAST;
    }
    
    private DetailAST createFullIdentAST() {
        DetailAST fullIdentAST = new DetailAST();
        fullIdentAST.setType(51);
        fullIdentAST.setText("com.puppycrawl.tools.checkstyle.utils.Test");
        
        return fullIdentAST;
    }
    
    private DetailAST createAnnotationHolderAST() {
        DetailAST annotationHolderAST = new DetailAST();
        annotationHolderAST.setType(155);
        
        return annotationHolderAST;
    }
}