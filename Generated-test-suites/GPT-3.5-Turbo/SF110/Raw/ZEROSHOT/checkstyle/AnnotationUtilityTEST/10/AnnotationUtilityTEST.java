package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        AnnotationUtility.containsAnnotation(null, "");
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWhenASTIsNull() {
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWhenHolderIsNull() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWhenHolderTypeIsNotCorrect() {
        DetailAST ast = new DetailAST();
        ast.setType(20); // Some random type
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotationWhenHolderTypeIsCorrectAndBranchDoesNotContain159() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // Type is correct for annotation holder
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWhenTypeIsNotCorrect() {
        DetailAST ast = new DetailAST();
        ast.setType(20); // Some random type
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolderWhenTypeIsCorrect() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // Type is correct for annotation holder
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        AnnotationUtility.getAnnotation(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test
    public void testGetAnnotationWhenHolderIsNull() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWhenAnnotationNameDoesNotMatch() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(159); // Annotation type
        ast.setFirstChild(holder);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWhenAnnotationNameMatches() {
        DetailAST ast = new DetailAST();
        DetailAST holder = new DetailAST();
        holder.setType(159); // Annotation type
        DetailAST firstChild = new DetailAST();
        firstChild.setNextSibling(new DetailAST());
        holder.setFirstChild(firstChild);
        ast.setFirstChild(holder);
        assertEquals(holder, AnnotationUtility.getAnnotation(ast, "annotation"));
    }

}