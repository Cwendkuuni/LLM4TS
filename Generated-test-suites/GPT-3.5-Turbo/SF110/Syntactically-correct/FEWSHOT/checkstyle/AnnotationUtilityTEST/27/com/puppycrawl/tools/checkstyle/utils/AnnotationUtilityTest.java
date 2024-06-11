
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {
    private static final DetailAST NULL_AST = null;
    private static final String NULL_ANNOTATION = null;
    private static final String EMPTY_ANNOTATION = "";
    private static final String WHITESPACE_ANNOTATION = " ";
    private static final String AST_IS_NULL_EXCEPTION_MESSAGE = "the ast is null";
    private static final String ANNOTATION_IS_NULL_EXCEPTION_MESSAGE = "the annotation is null";
    private static final String ANNOTATION_IS_EMPTY_OR_SPACES_EXCEPTION_MESSAGE = "the annotation is empty or spaces";
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        AnnotationUtility.containsAnnotation(NULL_AST, "annotation");
    }
    
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));

        DetailAST annotationAST = new DetailAST();
        annotationAST.setType(159);
        ast.addChild(annotationAST);
        
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullASTInHolder() {
        AnnotationUtility.containsAnnotation(NULL_AST);
    }
    
    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));

        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(16);
        ast.addChild(annotationHolder);
        
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        AnnotationUtility.getAnnotation(NULL_AST, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, NULL_ANNOTATION);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, EMPTY_ANNOTATION);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, WHITESPACE_ANNOTATION);
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        ast.addChild(annotationHolder);

        DetailAST annotationAST = new DetailAST();
        annotationAST.setType(159);
        DetailAST firstChild = new DetailAST();
        annotationAST.addChild(firstChild);
        DetailAST secondChild = new DetailAST();
        secondChild.setText("annotation");
        firstChild.setNextSibling(secondChild);
        
        annotationHolder.addChild(annotationAST);
        
        assertEquals(annotationAST, AnnotationUtility.getAnnotation(ast, "annotation"));
        assertNull(AnnotationUtility.getAnnotation(ast, "otherAnnotation"));
    }
}
