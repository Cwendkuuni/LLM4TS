package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNotNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
        ast.setType(16);
        assertNotNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
        ast.setType(10);
        assertNull(AnnotationUtility.containsAnnotation(ast, "annotation"));
        assertNull(AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithoutName() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
        ast.setType(16);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
        ast.setType(10);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
        assertFalse(AnnotationUtility.containsAnnotation(null));
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
        ast.setType(16);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
        ast.setType(10);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        ast.setType(16);
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        ast.setType(10);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
        assertNull(AnnotationUtility.getAnnotation(null, "annotation"));
        ast.setType(155);
        assertNull(AnnotationUtility.getAnnotation(ast, null));
        assertEquals("", AnnotationUtility.getAnnotation(ast, ""));
        assertEquals("", AnnotationUtility.getAnnotation(ast, "   "));
    }
}