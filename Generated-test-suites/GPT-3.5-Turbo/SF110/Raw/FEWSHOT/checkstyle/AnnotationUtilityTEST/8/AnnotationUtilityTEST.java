package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = createValidAst();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");

        assertEquals(true, result);
    }

    @Test
    public void testContainsAnnotationWithInvalidAst() {
        DetailAST ast = createInvalidAst();
        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");

        assertEquals(false, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstNoAnnotation() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotationWithValidAstNoAnnotation() {
        DetailAST ast = createValidAst();
        boolean result = AnnotationUtility.containsAnnotation(ast);

        assertEquals(false, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = createValidAst();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertEquals(ast.getFirstChild(), result);
    }

    @Test
    public void testGetAnnotationHolderWithInvalidAst() {
        DetailAST ast = createInvalidAst();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithValidAstNullAnnotation() {
        DetailAST ast = createValidAst();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithValidAstEmptyAnnotation() {
        DetailAST ast = createValidAst();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithValidAstSpacesAnnotation() {
        DetailAST ast = createValidAst();
        AnnotationUtility.getAnnotation(ast, "     ");
    }

    @Test
    public void testGetAnnotationWithValidAstValidAnnotation() {
        DetailAST ast = createValidAst();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");

        assertEquals(ast.getFirstChild(), result);
    }

    @Test
    public void testGetAnnotationWithValidAstInvalidAnnotation() {
        DetailAST ast = createValidAst();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "invalid");

        assertNull(result);
    }

    private DetailAST createValidAst() {
        DetailAST ast = new DetailAST();
        ast.setType(155);

        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(158);
        ast.addChild(annotationHolder);

        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        annotationHolder.addChild(annotation);

        DetailAST identifier = new DetailAST();
        identifier.setText("annotation");
        annotation.addChild(identifier);

        return ast;
    }

    private DetailAST createInvalidAst() {
        DetailAST ast = new DetailAST();
        ast.setType(156);

        return ast;
    }
}