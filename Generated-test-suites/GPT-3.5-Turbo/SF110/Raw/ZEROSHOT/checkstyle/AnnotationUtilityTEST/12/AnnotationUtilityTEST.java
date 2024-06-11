package com.puppycrawl.tools.checkstyle.utils;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {
    
    private static final String THE_AST_IS_NULL = "the ast is null";
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }
    
    @Test
    public void testContainsAnnotationWithNonNullAstAndNonNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertFalse(containsAnnotation);
    }
    
    @Test
    public void testContainsAnnotationWithNonNullAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast, null);
        assertFalse(containsAnnotation);
    }
    
    @Test
    public void testContainsAnnotationWithNonNullAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast, "");
        assertFalse(containsAnnotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithoutAst() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotationWithNonNullAst() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast);
        assertFalse(containsAnnotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolderWithNonNullAstMethodDef() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }
    
    @Test
    public void testGetAnnotationHolderWithNonNullAstAnnotationDef() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.ANNOTATION_DEF);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNonNullAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNonNullAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test
    public void testGetAnnotationWithNonNullAstAndNonNullAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(DetailAST.METHOD_DEF);
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(annotation);
    }
}