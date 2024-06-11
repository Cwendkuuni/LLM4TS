
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotationWithNullAST() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationWithValidASTAndInvalidAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.containsAnnotation(ast, null));
        try {
            AnnotationUtility.containsAnnotation(ast, "");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        try {
            AnnotationUtility.containsAnnotation(ast, "   ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
        
        ast.setType(155);
        assertNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
        
        ast.setType(16);
        assertNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
        
        DetailAST holder = ast.findFirstToken(158);
        assertNotNull(holder);
        DetailAST annotation = new DetailAST();
        holder.addChild(annotation);
        
        assertNotNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolderWithValidAST() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        
        ast.setType(155);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        
        ast.setType(16);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        
        DetailAST holder = ast.findFirstToken(158);
        assertNotNull(holder);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
        
        ast.setType(5);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationWithNullAST() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithValidASTAndInvalidAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
        try {
            AnnotationUtility.getAnnotation(ast, "");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        
        ast.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        
        ast.setType(16);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        
        DetailAST holder = ast.findFirstToken(158);
        assertNotNull(holder);
        DetailAST annotation = new DetailAST();
        holder.addChild(annotation);
        
        DetailAST firstChild = new DetailAST();
        annotation.addChild(firstChild);
        firstChild.setNextSibling(new DetailAST());
        firstChild.getNextSibling().addChild(new DetailAST());
        firstChild.getNextSibling().getFirstChild().setType(159);
        firstChild.getNextSibling().getFirstChild().setNextSibling(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().setType(6);
        firstChild.getNextSibling().getFirstChild().getNextSibling().addChild(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().setType(61);
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().setNextSibling(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().setType(16);
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().addChild(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().setType(60);
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().addChild(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().getFirstChild().setType(14);
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().setNextSibling(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().setType(23);
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().setNextSibling(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getNextSibling().setType(80);
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getNextSibling().addChild(new DetailAST());
        firstChild.getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getFirstChild().getNextSibling().getNextSibling().getFirstChild().setType(61);
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "annotation"));
        assertNull(AnnotationUtility.getAnnotation(ast, "not-existing-annotation"));
    }
}
