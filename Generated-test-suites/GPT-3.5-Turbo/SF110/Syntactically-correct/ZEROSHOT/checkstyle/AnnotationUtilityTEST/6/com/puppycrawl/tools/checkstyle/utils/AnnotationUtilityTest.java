package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_ValidAst_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_AstWithNullHolder_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        boolean result = AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testContainsAnnotation_AstWithHolder_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        holder.setType(159);
        ast.addChild(holder);

        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_ValidAstWithType155_ReturnsNonNullHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolder_ValidAstWithType16_ReturnsNonNullHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(16);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolder_ValidAstWithOtherTypes_ReturnsNonNullHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(2);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_SpacesAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_ValidAst_ReturnsNull() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.addChild(holder);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotation_ValidAstWithMatchingAnnotation_ReturnsAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        ast.addChild(holder);

        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new FullIdent());
        annotation.addChild(firstChild);
        holder.addChild(annotation);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertEquals(annotation, result);
    }
}