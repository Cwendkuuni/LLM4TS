package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null));
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, "   "));
    }

}