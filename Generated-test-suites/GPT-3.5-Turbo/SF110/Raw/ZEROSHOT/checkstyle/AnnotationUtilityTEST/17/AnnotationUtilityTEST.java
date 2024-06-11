package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    private DetailAST ast;
    private DetailAST nullAst;
    private DetailAST annotationHolder;

    @Before
    public void setup() {
        ast = new DetailAST();
        nullAst = null;
        annotationHolder = new DetailAST();
        annotationHolder.setType(158);

        DetailAST child1 = new DetailAST();
        child1.setType(159);
        detailAST.addChild(child1);

        DetailAST annotationName = new DetailAST();
        annotationName.setType(159);
        annotationName.addChild(new DetailAST());
        annotationName.getFirstChild().setType(111);
        annotationName.getFirstChild().setType(12);
        child1.getFirstChild().getNextSibling().getNextSibling().addChild(annotationName);

        DetailAST nullChild = null;
        annotationHolder.addChild(nullChild);

        ast.setType(155);
        ast.addChild(annotationHolder);
    }

    @Test
    public void testContainsAnnotation() {
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        try {
            AnnotationUtility.containsAnnotation(nullAst);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolder() {
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        try {
            AnnotationUtility.getAnnotation(nullAst, "annotation");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        try {
            AnnotationUtility.getAnnotation(ast, "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation() {
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationNotFound() {
        assertNull(AnnotationUtility.getAnnotation(ast, "notfound"));
    }

    @Test
    public void testGetAnnotationHolderWithInvalidAST() {
        ast.setType(16); // Invalid type
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testContainsAnnotationWithInvalidAST() {
        ast.setType(16); // Invalid type
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullASTInGetAnnotationHolder() {
        try {
            AnnotationUtility.getAnnotationHolder(nullAst);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithNullASTInContainsAnnotation() {
        try {
            AnnotationUtility.containsAnnotation(nullAst);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
}