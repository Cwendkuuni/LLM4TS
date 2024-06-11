package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast does not contain annotation
        DetailAST ast1 = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast1, "annotation"));
        
        // Test case 3: ast contains annotation
        DetailAST ast2 = new DetailAST();
        DetailAST annotation = ast2.addChild(new DetailAST());
        annotation.setType(159);
        DetailAST name = annotation.addChild(new DetailAST());
        name.setType(155);
        name.setText("annotation");
        assertTrue(AnnotationUtility.containsAnnotation(ast2, "annotation"));
    }
    
    @Test
    public void testGetAnnotation() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: annotation is null
        DetailAST ast1 = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast1, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test case 2: annotation is empty or spaces
        DetailAST ast2 = new DetailAST();
        try {
            AnnotationUtility.getAnnotation(ast2, "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test case 3: ast does not contain annotation
        DetailAST ast3 = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast3, "annotation"));
        
        // Test case 4: ast contains annotation
        DetailAST ast4 = new DetailAST();
        DetailAST annotation = ast4.addChild(new DetailAST());
        annotation.setType(159);
        DetailAST name = annotation.addChild(new DetailAST());
        name.setType(155);
        name.setText("annotation");
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast4, "annotation"));
    }
    
    @Test
    public void testGetAnnotationHolder() {
        // Test case 1: ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test case 2: ast type is 155 or 16
        DetailAST ast1 = new DetailAST();
        ast1.setType(155);
        ast1.addChild(new DetailAST());
        assertEquals(ast1.getFirstChild(), AnnotationUtility.getAnnotationHolder(ast1));
        
        DetailAST ast2 = new DetailAST();
        ast2.setType(16);
        ast2.addChild(new DetailAST());
        assertEquals(ast2.getFirstChild(), AnnotationUtility.getAnnotationHolder(ast2));
        
        // Test case 3: ast type is not 155 or 16
        DetailAST ast3 = new DetailAST();
        ast3.setType(5);
        ast3.addChild(new DetailAST());
        assertNull(AnnotationUtility.getAnnotationHolder(ast3));
    }
}