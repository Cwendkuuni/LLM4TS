package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WhitespaceAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        AnnotationUtility.containsAnnotation(ast, "   ");
    }

    @Test
    public void testContainsAnnotation_NotNull_NotEmpty_NotFound() {
        DetailAST ast = mock(DetailAST.class);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NotNull_NotEmpty_Found() {
        DetailAST ast = mock(DetailAST.class);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WhitespaceAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_NotNull_NotEmpty_NotFound() {
        DetailAST ast = mock(DetailAST.class);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_NotNull_NotEmpty_Found() {
        DetailAST ast = mock(DetailAST.class);
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_NotNull_NotEmpty() {
        DetailAST ast = mock(DetailAST.class);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
}