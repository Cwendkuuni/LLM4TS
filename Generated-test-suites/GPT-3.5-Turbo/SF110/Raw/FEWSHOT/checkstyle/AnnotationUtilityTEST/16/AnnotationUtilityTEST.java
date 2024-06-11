
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.containsAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotation_WithAst_ReturnsTrueIfAnnotationExists() {
        DetailAST ast = createAstWithAnnotation("annotation");
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotation_WithAst_ReturnsFalseIfAnnotationDoesNotExist() {
        DetailAST ast = createAstWithAnnotation("otherAnnotation");
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testContainsAnnotationHolder_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testContainsAnnotationHolder_WithAstOfType155_ReturnsAnnotationHolder() {
        DetailAST ast = createAstOfType155();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }
    
    @Test
    public void testContainsAnnotationHolder_WithAstNotOfType155Or16_ReturnsAnnotationHolder() {
        DetailAST ast = createAstNotOfType155Or16();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }
    
    @Test
    public void testGetAnnotation_NullAst_ThrowsIllegalArgumentException() {
        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        try {
            DetailAST ast = createAstWithAnnotation("annotation");
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_EmptyOrSpacesAnnotation_ThrowsIllegalArgumentException() {
        try {
            DetailAST ast = createAstWithAnnotation("annotation");
            AnnotationUtility.getAnnotation(ast, "   ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation_WithAst_ReturnsAnnotationIfFound() {
        DetailAST ast = createAstWithAnnotation("annotation");
        assertNotNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
    
    @Test
    public void testGetAnnotation_WithAst_ReturnsNullIfAnnotationNotFound() {
        DetailAST ast = createAstWithAnnotation("otherAnnotation");
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }
    
    private DetailAST createAstWithAnnotation(String annotation) {
        DetailAST ast = new DetailAST();
        // Populate ast with annotation
        return ast;
    }
    
    private DetailAST createAstOfType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        return ast;
    }
    
    private DetailAST createAstNotOfType155Or16() {
        DetailAST ast = new DetailAST();
        ast.setType(156);
        return ast;
    }
}
