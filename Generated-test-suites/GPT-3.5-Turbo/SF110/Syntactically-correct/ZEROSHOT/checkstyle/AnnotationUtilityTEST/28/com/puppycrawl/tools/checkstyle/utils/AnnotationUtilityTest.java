// Junit test class AnnotationUtilityTest

package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;

public final class AnnotationUtilityTest
{
    private AnnotationUtilityTest() {
        throw new UnsupportedOperationException("do not instantiate.");
    }

    // Test for containsAnnotation method

    // Test with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    // Test with valid ast and existing annotation
    @Test
    public void testContainsAnnotationWithValidAstAndExistingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationAst = new DetailAST();

        ast.addChild(annotationAst);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    // Test with valid ast and non-existing annotation
    @Test
    public void testContainsAnnotationWithValidAstAndNonExistingAnnotation() {
        DetailAST ast = new DetailAST();

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    // Test for containsAnnotation method

    // Test with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null);
    }

    // Test with valid ast and existing annotation
    @Test
    public void testContainsAnnotationWithValidAstAndExistingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationAst = new DetailAST();

        ast.addChild(annotationAst);

        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    // Test with valid ast and non-existing annotation
    @Test
    public void testContainsAnnotationWithValidAstAndNonExistingAnnotation() {
        DetailAST ast = new DetailAST();

        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    // Test for getAnnotationHolder method

    // Test with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    // Test with annotation ast
    @Test
    public void testGetAnnotationHolderWithAnnotationAst() {
        DetailAST ast = new DetailAST();
        DetailAST annotationAst = new DetailAST();

        ast.addChild(annotationAst);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertNotNull(result);
    }

    // Test with non-annotation ast
    @Test
    public void testGetAnnotationHolderWithNonAnnotationAst() {
        DetailAST ast = new DetailAST();

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertNull(result);
    }

    // Test for getAnnotation method

    // Test with null ast
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    // Test with null annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        AnnotationUtility.getAnnotation(ast, null);
    }

    // Test with empty annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        AnnotationUtility.getAnnotation(ast, "");
    }

    // Test with spaces annotation
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithSpacesAnnotation() {
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    // Test with valid ast and existing annotation
    @Test
    public void testGetAnnotationWithValidAstAndExistingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationAst = new DetailAST();
        DetailAST nameAst = new DetailAST();

        nameAst.setText("annotation");

        annotationAst.addChild(nameAst);
        ast.addChild(annotationAst);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");

        assertNotNull(result);
    }

    // Test with valid ast and non-existing annotation
    @Test
    public void testGetAnnotationWithValidAstAndNonExistingAnnotation() {
        DetailAST ast = new DetailAST();

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");

        assertNull(result);
    }
}