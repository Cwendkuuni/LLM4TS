
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        
        try {
            AnnotationUtility.containsAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Test"));
        
        ast.addChild(ast);
        
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
    }
    
    @Test
    public void testContainsAnnotationNoAnnotationHolder() {
        DetailAST ast = new DetailAST();
        
        try {
            AnnotationUtility.containsAnnotation(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        ast.addChild(ast);
        
        assertFalse(AnnotationUtility.containsAnnotation(ast));
        
        DetailAST child = new DetailAST();
        child.setType(159);
        ast.addChild(child);
        
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        ast.addChild(ast);
        
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(158);
        ast.addChild(child);
        
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        try {
            AnnotationUtility.getAnnotation(ast, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        ast.addChild(ast);
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        ast.setType(155);
        
        DetailAST child = new DetailAST();
        child.setType(159);
        ast.addChild(child);
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        DetailAST firstChild = new DetailAST();
        firstChild.setType(123);
        child.addChild(firstChild);
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.setNextSibling(firstChild);
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().setType(159);
        firstChild.getNextSibling().addChild(firstChild);
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().setType(159);
        firstChild.getNextSibling().getNextSibling().addChild(firstChild);
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().setType(163);
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().setType(155);
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling().setType(145);
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling().setNextSibling(firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling());
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling().getNextSibling().setType(155);
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling().getNextSibling().setNextSibling(firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling().getNextSibling());
        
        assertNull(AnnotationUtility.getAnnotation(ast, "Test"));
        
        firstChild.getNextSibling().getNextSibling().getFirstChild().getNextSibling().getNextSibling().getNextSibling().setType(163);
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Test"));
    }
}
