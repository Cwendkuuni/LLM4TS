package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withNullAST_expectIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void testContainsAnnotation_withNonExistingAnnotation_expectFalse() {
        DetailAST ast = new DetailAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "nonExistingAnnotation");
        assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_withoutAST_expectIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }
    
    @Test
    public void testContainsAnnotation_withExistingAnnotation_expectTrue() {
        DetailAST ast = new DetailAST();
        ast.setBranchContains(159);
        boolean result = AnnotationUtility.containsAnnotation(ast);
        assertTrue(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_withNullAST_expectIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolder_withASTType155_expectNotNull() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAST_expectIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withNullAnnotation_expectIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withEmptyAnnotation_expectIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_withWhitespaceAnnotation_expectIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "     ");
    }
    
    @Test
    public void testGetAnnotation_withNonExistingAnnotation_expectNull() {
        DetailAST ast = new DetailAST();
        ast.setBranchContains(159);
        DetailAST child = new DetailAST();
        child.setType(160);
        ast.addChild(child);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "nonExistingAnnotation");
        assertNull(result);
    }
    
    @Test
    public void testGetAnnotation_withExistingAnnotation_expectNotNull() {
        DetailAST ast = new DetailAST();
        ast.setBranchContains(159);
        DetailAST child = new DetailAST();
        child.setType(159);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(160);
        FullIdent ident = new FullIdent(firstChild.getNextSibling());
        ident.setText("existingAnnotation");
        child.addChild(firstChild);
        ast.addChild(child);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "existingAnnotation");
        assertNotNull(result);
    }
}