package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        // Test when ast is null
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test when ast has the given annotation
        DetailAST astWithAnnotation = new DetailAST();
        DetailAST annotation1 = new DetailAST();
        annotation1.setType(159);
        DetailAST annotationName1 = new DetailAST();
        annotationName1.setText("annotation1");
        annotation1.setNextSibling(annotationName1);
        astWithAnnotation.addChild(annotation1);
        assertTrue(AnnotationUtility.containsAnnotation(astWithAnnotation, "annotation1"));
        assertFalse(AnnotationUtility.containsAnnotation(astWithAnnotation, "annotation2"));
        
        // Test when ast does not have any annotations
        DetailAST astWithoutAnnotations = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(astWithoutAnnotations, "annotation"));
        
        // Test when ast is null
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test when ast has an annotation holder
        DetailAST astWithAnnotationHolder = new DetailAST();
        astWithAnnotationHolder.setType(155);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        astWithAnnotationHolder.addChild(annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(astWithAnnotationHolder));
        
        // Test when ast does not have an annotation holder
        DetailAST astWithoutAnnotationHolder = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(astWithoutAnnotationHolder));
    }

    @Test
    public void testGetAnnotationHolder() {
        // Test when ast is null
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test when ast is of type 155
        DetailAST astWithType155 = new DetailAST();
        astWithType155.setType(155);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        astWithType155.addChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(astWithType155));
        
        // Test when ast is of type 16
        DetailAST astWithType16 = new DetailAST();
        astWithType16.setType(16);
        astWithType16.addChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(astWithType16));
        
        // Test when ast is neither of type 155 nor type 16
        DetailAST astWithTypeOther = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(astWithTypeOther));
    }

    @Test
    public void testGetAnnotation() {
        // Test when ast is null
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
        
        // Test when annotation is null
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
        
        // Test when annotation is empty or spaces
        try {
            AnnotationUtility.getAnnotation(new DetailAST(), "   ");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
        
        // Test when ast has the given annotation
        DetailAST astWithAnnotation = new DetailAST();
        DetailAST annotation1 = new DetailAST();
        annotation1.setType(159);
        DetailAST annotationName1 = new DetailAST();
        annotationName1.setText("annotation1");
        annotation1.setNextSibling(annotationName1);
        astWithAnnotation.addChild(annotation1);
        assertEquals(annotation1, AnnotationUtility.getAnnotation(astWithAnnotation, "annotation1"));
        assertNull(AnnotationUtility.getAnnotation(astWithAnnotation, "annotation2"));
        
        // Test when ast does not have any annotations
        DetailAST astWithoutAnnotations = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(astWithoutAnnotations, "annotation"));
    }

}