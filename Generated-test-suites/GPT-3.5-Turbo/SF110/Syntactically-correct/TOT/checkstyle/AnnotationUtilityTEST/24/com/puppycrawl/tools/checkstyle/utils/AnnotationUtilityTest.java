package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void containsAnnotation_NullAst_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void containsAnnotation_ValidAstAndAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void containsAnnotation_ValidAstAndInvalidAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "invalid_annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsAnnotation_NullAstWithoutAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void containsAnnotation_ValidAstWithoutAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void containsAnnotation_ValidAstWithAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        ast.branchContains(159);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotationHolder_NullAst_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void getAnnotationHolder_ValidAstWithType155_ReturnsNonNullHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void getAnnotationHolder_ValidAstWithType16_ReturnsNonNullHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void getAnnotationHolder_ValidAstWithOtherType_ReturnsNonNullHolder() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_NullAst_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_ValidAstWithNullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_ValidAstWithEmptyAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotation_ValidAstWithWhitespaceAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "     ");
    }

    @Test
    public void getAnnotation_ValidAstWithoutAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void getAnnotation_ValidAstWithMatchingAnnotation_ReturnsNotNull() {
        DetailAST ast = new DetailAST();
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(child);
        child.setFirstChild(firstChild);
        ast.setFirstChild(child);

        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void getAnnotation_ValidAstWithNonMatchingAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(child);
        child.setFirstChild(firstChild);
        ast.setFirstChild(child);

        assertNull(AnnotationUtility.getAnnotation(ast, "non_matching_annotation"));
    }

}