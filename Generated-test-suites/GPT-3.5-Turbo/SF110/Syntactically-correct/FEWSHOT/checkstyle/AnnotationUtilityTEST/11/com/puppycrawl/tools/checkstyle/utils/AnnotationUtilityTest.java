package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotation_withNonExistentAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "nonExistent"));
    }

    @Test
    public void testContainsAnnotation_withExistingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(155);
        ast.addChild(annotationHolder);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "existing"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAstInMethod() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testContainsAnnotation_withNonExistentAnnotationInMethod() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_withExistingAnnotationInMethod() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(16);
        ast.addChild(annotationHolder);

        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_withAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(155);
        ast.addChild(annotationHolder);

        assertSame(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_withNonAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST nonAnnotationHolder = new DetailAST();
        nonAnnotationHolder.setType(5);
        ast.addChild(nonAnnotationHolder);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void testGetAnnotation_withNonExistentAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "nonExistent"));
    }

    @Test
    public void testGetAnnotation_withExistingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(155);
        ast.addChild(annotationHolder);

        DetailAST annotation = new DetailAST();
        annotation.setType(159);
        annotationHolder.addChild(annotation);

        DetailAST firstChild = new DetailAST();
        annotation.addChild(firstChild);

        firstChild.setType(FullIdent.IDENT);
        firstChild.setText("existing");

        assertSame(annotation, AnnotationUtility.getAnnotation(ast, "existing"));
    }
}