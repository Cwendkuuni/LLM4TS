
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    // Test cases for containsAnnotation(final DetailAST ast, final String annotation) method

    @Test
    public void testContainsAnnotation_WithValidAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        // Mocking an annotation to test
        String annotation = "@MockAnnotation";
        
        // Mocking the ast containing an annotation
        DetailAST annotationAST = new DetailAST();
        // Set the annotation AST with the mocked annotation
        ast.setAnnotation(annotationAST);
        // Set the annotation name
        annotationAST.setText(annotation);
        
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }
    
    @Test
    public void testContainsAnnotation_WithInvalidAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        // Mocking an annotation to test
        String annotation = "@MockAnnotation";
        
        // Mocking the ast not containing any annotation
        DetailAST annotationAST = null;
        // Set the annotation AST with the mocked annotation
        ast.setAnnotation(annotationAST);
        
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAST_ThrowsIllegalArgumentException() {
        String annotation = "@MockAnnotation";
        
        AnnotationUtility.containsAnnotation(null, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        
        AnnotationUtility.containsAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithEmptyAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        // Mocking an empty annotation to test
        String annotation = "";
        
        AnnotationUtility.containsAnnotation(ast, annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithBlankSpacesInAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();
        // Mocking an annotation with blank spaces to test
        String annotation = "     ";
        
        AnnotationUtility.containsAnnotation(ast, annotation);
    }
    

    // Test cases for containsAnnotation(final DetailAST ast) method
    
    @Test
    public void testContainsAnnotation_WithValidAST_ReturnsTrue() {
        DetailAST ast = new DetailAST();
        // Mocking an annotation holder to test
        DetailAST annotationHolder = new DetailAST();
        // Set the detail type to indicate an annotation holder
        annotationHolder.setType(155);
        
        ast.setAnnotationHolder(annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test
    public void testContainsAnnotation_WithInvalidAST_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        // Mocking an annotation holder with invalid branch value to test
        DetailAST annotationHolder = new DetailAST();
        // Set the detail type to indicate an annotation holder
        annotationHolder.setType(155);
        // Set an invalid branch value
        annotationHolder.setBranch(158);
        
        ast.setAnnotationHolder(annotationHolder);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_WithNullAST_ThrowsIllegalArgumentException() {
        AnnotationUtility.containsAnnotation(null);
    }
    

    // Test cases for getAnnotationHolder(final DetailAST ast) method
    
    @Test
    public void testGetAnnotationHolder_WithAnnotationType_ReturnsExpectedHolder() {
        DetailAST ast = new DetailAST();
        // Mocking an ast with annotation type
        ast.setType(155);
        // Mocking annotation holder AST
        DetailAST annotationHolder = new DetailAST();
        ast.setAnnotationHolder(annotationHolder);
        
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolder_WithOtherType_ReturnsExpectedHolder() {
        DetailAST ast = new DetailAST();
        // Mocking an ast with other type (not 155 or 16)
        ast.setType(10);
        // Mocking annotation holder AST
        DetailAST annotationHolder = new DetailAST();
        ast.setAnnotationHolder(annotationHolder);
        
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_WithNullAST_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    

    // Test cases for getAnnotation(final DetailAST ast, final String annotation) method
    
    @Test
    public void testGetAnnotation_WithValidASTAndAnnotation_ReturnsExpectedAnnotation() {
        DetailAST ast = new DetailAST();
        // Mocking annotation holder AST
        DetailAST annotationHolder = new DetailAST();
        ast.setAnnotationHolder(annotationHolder);
        
        // Mocking a child annotation AST
        DetailAST annotationAST = new DetailAST();
        // Set the detail type to indicate an annotation
        annotationAST.setType(159);
        // Mocking annotation name
        String annotation = "@MockAnnotation";
        // Mocking a full identifier of the annotation name
        annotationAST.createFirstChild();
        FullIdent.createFullIdent(annotationAST.getFirstChild().getNextSibling()).setText(annotation);
        
        // Adding the child annotation AST to the annotation holder AST
        annotationHolder.addChild(annotationAST);
        
        assertEquals(annotationAST, AnnotationUtility.getAnnotation(ast, annotation));
    }
    
    @Test
    public void testGetAnnotation_WithInvalidASTAndValidAnnotation_ReturnsNull() {
        DetailAST ast = new DetailAST();
        // Mocking annotation holder AST with invalid branch
        DetailAST annotationHolder = new DetailAST();
        ast.setAnnotationHolder(annotationHolder);
        
        // Mocking a child annotation AST
        DetailAST annotationAST = new DetailAST();
        // Set the detail type to indicate an annotation
        annotationAST.setType(159);
        // Mocking annotation name
        String annotation = "@MockAnnotation";
        // Mocking a full identifier of the annotation name
        annotationAST.createFirstChild();
        FullIdent.createFullIdent(annotationAST.getFirstChild().getNextSibling()).setText(annotation);
        
        // Adding the child annotation AST to the annotation holder AST
        annotationHolder.addChild(annotationAST);
        
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithNullASTAndAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(null, "@MockAnnotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithValidASTAndNullAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(new DetailAST(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithValidASTAndEmptyAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(new DetailAST(), "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WithValidASTAndBlankSpacesInAnnotation_ThrowsIllegalArgumentException() {
        AnnotationUtility.getAnnotation(new DetailAST(), "     ");
    }

}

