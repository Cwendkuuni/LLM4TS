package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_ValidAST() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_InvalidAST() {
        DetailAST ast = new DetailAST();
        ast.setType(156);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_EmptyAST() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_String_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_String_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_String_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_String_WhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, "    ");
    }
    
    @Test
    public void testContainsAnnotation_String_ValidAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.addChild(annotationHolder);
        
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        annotationHolder.addChild(annotation);
        
        DetailAST name = new DetailAST();
        name.setText("annotation");
        annotation.addChild(name);
        
        assertNotNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotation_String_InvalidAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.addChild(annotationHolder);
        
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        annotationHolder.addChild(annotation);
        
        DetailAST name = new DetailAST();
        name.setText("annotation");
        annotation.addChild(name);
        
        assertNull(AnnotationUtility.containsAnnotation(ast, "otherAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_ValidAST() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.addChild(annotationHolder);
        
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_InvalidAST() {
        DetailAST ast = new DetailAST();
        
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

}