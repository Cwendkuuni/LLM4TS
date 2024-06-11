package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotationNotNullAst() {
        DetailAST ast = new DetailAST();
        boolean contains = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertFalse(contains);
    }

    @Test
    public void testContainsAnnotationHolderNullAst() {
        DetailAST result = AnnotationUtility.getAnnotationHolder(null);
        assertNull(result);
    }

    @Test
    public void testContainsAnnotationHolderType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testContainsAnnotationHolderType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testContainsAnnotationHolderDefault() {
        DetailAST ast = new DetailAST();
        ast.setType(100);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullName() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyName() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationSpacesName() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotationNoMatch() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationMatch() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST grandChild = new DetailAST();
        grandChild.setType(123);
        FullIdent fullIdent = new FullIdent();
        fullIdent.setText("annotation");
        grandChild.setNextSibling(fullIdent);
        child.setFirstChild(grandChild);
        ast.setFirstChild(child);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNotNull(result);
    }
}