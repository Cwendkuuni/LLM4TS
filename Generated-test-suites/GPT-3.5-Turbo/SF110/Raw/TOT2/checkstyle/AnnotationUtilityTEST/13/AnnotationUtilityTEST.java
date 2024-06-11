package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse("Checking if annotation is not present", AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        assertNull("Checking if annotation holder is null", AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull("Checking if annotation is null", AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
      
        assertTrue("Checking if annotation is present", AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
      
        assertNotNull("Checking if annotation holder is not null", AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull("Checking if annotation is null for empty annotation", AnnotationUtility.getAnnotation(ast, ""));
    }
}