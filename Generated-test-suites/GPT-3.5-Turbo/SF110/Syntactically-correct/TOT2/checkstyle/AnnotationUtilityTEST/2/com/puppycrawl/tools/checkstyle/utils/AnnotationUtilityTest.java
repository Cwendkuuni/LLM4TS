package com.puppycrawl.tools.checkstyle.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }
    
    @Test
    public void testNoAnnotation() {
        DetailAST ast = null;
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }
    
    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }
    
    @Test
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }
    
    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }
    
    @Test
    public void testGetAnnotationWithEmptyOrSpaceAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "    "));
    }
    
    @Test
    public void testGetAnnotationWithNameNotFound() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }
    
}