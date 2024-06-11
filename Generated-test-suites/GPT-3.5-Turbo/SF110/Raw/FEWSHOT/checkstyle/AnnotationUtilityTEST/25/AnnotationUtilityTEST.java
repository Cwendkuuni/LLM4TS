package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotation_ASTWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_ASTWithAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        ast.addChild(annotation);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_2() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_EmptyAST() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_ASTWithAnnotation_2() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(155);
        ast.addChild(holder);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_InvalidASTType() {
        DetailAST ast = new DetailAST();
        ast.setType(10);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_ASTType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_ASTType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
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
    
    @Test
    public void testGetAnnotation_NoMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_MatchingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.addChild(holder);
        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST child = new DetailAST();
        child.setType(123);
        annotation.addChild(child);
        FullIdent fullIdent = new FullIdent(null);
        fullIdent.setText("annotation");
        child.addChild(fullIdent);
        holder.addChild(annotation);
        
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "annotation"));
    }
}