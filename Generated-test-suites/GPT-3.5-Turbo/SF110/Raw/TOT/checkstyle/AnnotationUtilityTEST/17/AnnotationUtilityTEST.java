package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test
    public void testContainsAnnotation_NotContains() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotation_Contains() {
        DetailAST ast = new DetailAST();
        ast.setText("annotation");
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAstWithoutAnnotation() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_WithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(155);
        ast.setType(155);
        ast.setFirstChild(holder);
        
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_WithoutAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(16);
        ast.setType(16);
        ast.setFirstChild(holder);
        
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_WithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.setType(155);
        ast.setFirstChild(annotationHolder);
        
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        assertNotNull(result);
        assertEquals(annotationHolder, result);
    }
    
    @Test
    public void testGetAnnotationHolder_WithoutAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.setType(16);
        ast.setFirstChild(annotationHolder);
        
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        
        assertNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_SpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "  ");
    }
    
    @Test
    public void testGetAnnotation_NotFound() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.setFirstChild(holder);
        
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_Found() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        DetailAST child = new DetailAST();
        DetailAST firstChild = new DetailAST();
        firstChild.setText("annotation");
        child.setType(159);
        child.setFirstChild(firstChild);
        holder.setFirstChild(child);
        ast.setFirstChild(holder);
        
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        
        assertNotNull(result);
        assertEquals(child, result);
    }
}