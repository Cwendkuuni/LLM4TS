package com.puppycrawl.tools.checkstyle.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    
    @Test
    public void testContainsAnnotation_Positive() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }
    
    @Test
    public void testContainsAnnotation_Negative() {
        DetailAST ast = null;
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }
    
    @Test
    public void testGetAnnotationHolder_IfAnnotationExists() {
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_IfAnnotationNotExists() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotation_Positive() {
        DetailAST ast = new DetailAST();
        assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotation(ast, "Annotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "   ");
    }
}