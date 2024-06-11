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
    public void testContainsAnnotation_withValidArguments() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        ast.addChild(new DetailAST());
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        DetailAST child1 = new DetailAST();
        ast.addChild(child1);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        DetailAST child1 = new DetailAST();
        ast.addChild(child1);
        child1.setType(158);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        DetailAST child1 = new DetailAST();
        ast.addChild(child1);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation1"));
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation1");
    }
    
    @Test
    public void testGetAnnotationHolder_withValidAST() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        
        ast = new DetailAST();
        ast.setType(16);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        
        ast = new DetailAST();
        ast.setType(5);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotation_withValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        ast.setType(16);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        ast.setType(5);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        holder = new DetailAST();
        ast.addChild(holder);
        holder.setType(158);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        holder = new DetailAST();
        ast.addChild(holder);
        holder.setType(158);
        DetailAST child1 = new DetailAST();
        holder.addChild(child1);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
        
        ast = new DetailAST();
        holder = new DetailAST();
        ast.addChild(holder);
        holder.setType(158);
        child1 = new DetailAST();
        holder.addChild(child1);
        child1.setType(159);
        DetailAST child2 = new DetailAST();
        child1.addChild(child2);
        child2.setText("TestAnnotation1");
        assertNotNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation1"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAST() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "    ");
    }
}