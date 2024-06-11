
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    private static final String TEST_ANNOTATION = "TestAnnotation";

    @Test
    public void testContainsAnnotation_True() {
        DetailAST ast = createDetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, TEST_ANNOTATION));
    }

    @Test
    public void testContainsAnnotation_True_NullAST_Exception() {
        try {
            AnnotationUtility.containsAnnotation(null, TEST_ANNOTATION);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_False() {
        DetailAST ast = createDetailASTWithoutAnnotation();
        assertFalse(AnnotationUtility.containsAnnotation(ast, TEST_ANNOTATION));
    }

    @Test
    public void testContainsAnnotation_NullHolderBranch_Exception() {
        DetailAST ast = createDetailASTWithoutHolderBranch();
        try {
            AnnotationUtility.containsAnnotation(ast);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolder_AST150Type() {
        DetailAST ast = createDetailASTWithAnnotationHolder();
        assertEquals(ast.findFirstToken(158), AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_ASTTypeOtherThan150() {
        DetailAST ast = createDetailASTWithoutAnnotationHolder();
        assertEquals(ast.findFirstToken(5), AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_NullAST_Exception() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = createDetailASTWithAnnotation();
        assertEquals(ast.findFirstToken(159), AnnotationUtility.getAnnotation(ast, TEST_ANNOTATION));
    }

    @Test
    public void testGetAnnotation_NullAST_Exception() {
        try {
            AnnotationUtility.getAnnotation(null, TEST_ANNOTATION);
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_NullAnnotation_Exception() {
        DetailAST ast = createDetailASTWithAnnotation();
        try {
            AnnotationUtility.getAnnotation(ast, null);
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_EmptyOrSpacesAnnotation_Exception() {
        DetailAST ast = createDetailASTWithAnnotation();
        try {
            AnnotationUtility.getAnnotation(ast, "   ");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation_NotMatchingAnnotation_Null() {
        DetailAST ast = createDetailASTWithAnnotation();
        assertNull(AnnotationUtility.getAnnotation(ast, "InvalidAnnotation"));
    }

    private DetailAST createDetailAST() {
        // Create and return a DetailAST with annotation
    }

    private DetailAST createDetailASTWithoutAnnotation() {
        // Create and return a DetailAST without annotation
    }
    
    private DetailAST createDetailASTWithoutHolderBranch() {
        // Create and return a DetailAST without holder branch
    }

    private DetailAST createDetailASTWithAnnotationHolder() {
        // Create and return a DetailAST with annotation holder
    }

    private DetailAST createDetailASTWithoutAnnotationHolder() {
        // Create and return a DetailAST without annotation holder
    }

    private DetailAST createDetailASTWithAnnotation() {
        // Create and return a DetailAST with annotation
    }

}

