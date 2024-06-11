package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    private DetailAST ast;
    private String annotation;

    @Before
    public void setup() {
        ast = new DetailAST();
        annotation = "test";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST() {
        AnnotationUtility.containsAnnotation(null, annotation);
    }

    @Test
    public void testContainsAnnotation() {
        ast.addToken(TokenTypes.IDENT);
        ast.setText("test");

        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST2() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testGetAnnotationHolder() {
        ast.addToken(TokenTypes.ANNOTATION);

        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        AnnotationUtility.getAnnotation(null, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test
    public void testGetAnnotation() {
        ast.addToken(TokenTypes.ANNOTATION);
        ast.addToken(TokenTypes.IDENT);
        ast.setText("test");

        assertNotNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

}