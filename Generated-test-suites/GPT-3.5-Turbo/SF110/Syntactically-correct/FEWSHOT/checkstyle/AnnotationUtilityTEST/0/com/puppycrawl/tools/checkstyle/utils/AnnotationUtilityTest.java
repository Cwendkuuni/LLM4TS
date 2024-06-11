package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }

    @Test
    public void testContainsAnnotation_ValidAst() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));

        ast.attachToken(TokenTypes.PACKAGE_DEF);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));

        DetailAST annotationHolder = new DetailAST();
        annotationHolder.attachToken(TokenTypes.ANNOTATION);
        ast.addChild(annotationHolder);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));

        DetailAST annotation = new DetailAST();
        annotation.attachToken(TokenTypes.ANNOTATION_ELEMENT);
        annotation.attachChild(new DetailAST());
        annotation.getChild(0).attachToken(TokenTypes.IDENT, "TestAnnotation");
        annotationHolder.addChild(annotation);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAstWithoutAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.attachToken(TokenTypes.PACKAGE_DEF);
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testContainsAnnotation_ValidAstWithoutAnnotationHolder() {
        DetailAST ast = new DetailAST();
        ast.attachToken(TokenTypes.PACKAGE_DEF);
        assertFalse(AnnotationUtility.containsAnnotation(ast));

        DetailAST annotationHolder = new DetailAST();
        annotationHolder.attachToken(TokenTypes.ANNOTATION);
        ast.addChild(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast));

        annotationHolder.addChild(new DetailAST());
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder_ValidAst() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));

        ast.attachToken(TokenTypes.PACKAGE_DEF);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));

        DetailAST annotationHolder = new DetailAST();
        annotationHolder.attachToken(TokenTypes.ANNOTATION);
        ast.addChild(annotationHolder);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));

        annotationHolder.addChild(new DetailAST());
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_SpacesAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, " ");
    }

    @Test
    public void testGetAnnotation_ValidAst() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));

        ast.attachToken(TokenTypes.PACKAGE_DEF);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));

        DetailAST annotationHolder = new DetailAST();
        annotationHolder.attachToken(TokenTypes.ANNOTATION);
        ast.addChild(annotationHolder);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));

        DetailAST annotation = new DetailAST();
        annotation.attachToken(TokenTypes.ANNOTATION_ELEMENT);
        annotation.attachChild(new DetailAST());
        annotation.getChild(0).attachToken(TokenTypes.IDENT, "NotTestAnnotation");
        annotationHolder.addChild(annotation);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));

        annotation.getChild(0).attachToken(TokenTypes.IDENT, "TestAnnotation");
        assertNotNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }
}