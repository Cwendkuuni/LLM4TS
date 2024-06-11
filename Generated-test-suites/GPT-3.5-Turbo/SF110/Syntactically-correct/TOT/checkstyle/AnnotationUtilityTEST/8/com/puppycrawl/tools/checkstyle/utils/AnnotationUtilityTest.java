package com.puppycrawl.tools.checkstyle.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import static org.junit.Assert.*;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @After
    public void tearDown() {
        ast = null;
    }

    // Test cases for containsAnnotation(DetailAST, String)
    
    @Test
    public void testContainsAnnotation_ReturnsTrue_WhenAnnotationIsPresent() {
        // Arrange
        String annotation = "Override";
        ast.addChild(createAnnotationAST(annotation));
        
        // Act
        boolean contains = AnnotationUtility.containsAnnotation(ast, annotation);
        
        // Assert
        assertTrue(contains);
    }
    
    @Test
    public void testContainsAnnotation_ReturnsFalse_WhenAnnotationIsNotPresent() {
        // Arrange
        String annotation = "SuppressWarnings";
        
        // Act
        boolean contains = AnnotationUtility.containsAnnotation(ast, annotation);
        
        // Assert
        assertFalse(contains);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ThrowsIllegalArgumentException_WhenASTIsNull() {
        // Arrange
        String annotation = "Override";
        
        // Act
        AnnotationUtility.containsAnnotation(null, annotation);
        
        // Assert
        // Exception expected
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_ThrowsIllegalArgumentException_WhenAnnotationIsNull() {
        // Arrange
        ast.addChild(createAnnotationAST("Override"));
        
        // Act
        AnnotationUtility.containsAnnotation(ast, null);
        
        // Assert
        // Exception expected
    }
    
    // Test cases for containsAnnotation(DetailAST)
    
    @Test
    public void testContainsAnnotationWithoutAnnotation_ReturnsFalse_WhenASTIsNull() {
        // Arrange
        ast = null;
        
        // Act
        boolean contains = AnnotationUtility.containsAnnotation(ast);
        
        // Assert
        assertFalse(contains);
    }
    
    @Test
    public void testContainsAnnotationWithoutAnnotation_ReturnsFalse_WhenASTHasNoAnnotationHolder() {
        // Arrange
        ast.addChild(createTokenAST(5));
        
        // Act
        boolean contains = AnnotationUtility.containsAnnotation(ast);
        
        // Assert
        assertFalse(contains);
    }
    
    @Test
    public void testContainsAnnotationWithoutAnnotation_ReturnsTrue_WhenASTHasAnnotationHolder() {
        // Arrange
        ast.addChild(createTokenAST(155));
        ast.getFirstChild().addChild(createTokenAST(158));
        
        // Act
        boolean contains = AnnotationUtility.containsAnnotation(ast);
        
        // Assert
        assertTrue(contains);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithoutAnnotation_ThrowsIllegalArgumentException_WhenASTIsNull() {
        // Arrange
        ast = null;
        
        // Act
        AnnotationUtility.containsAnnotation(null);
        
        // Assert
        // Exception expected
    }
    
    // Test cases for getAnnotationHolder(DetailAST)
    
    @Test
    public void testGetAnnotationHolder_ReturnsNull_WhenASTIsNull() {
        // Arrange
        ast = null;
        
        // Act
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        assertNull(annotationHolder);
    }
    
    @Test
    public void testGetAnnotationHolder_ReturnsNull_WhenASTIsNotAnnotationOrClass() {
        // Arrange
        ast.addChild(createTokenAST(7));
        
        // Act
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        assertNull(annotationHolder);
    }
    
    @Test
    public void testGetAnnotationHolder_ReturnsAnnotationHolder_WhenASTIsAnnotation() {
        // Arrange
        ast.addChild(createTokenAST(155));
        ast.getFirstChild().addChild(createTokenAST(158));
        
        // Act
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        assertNotNull(annotationHolder);
    }
    
    @Test
    public void testGetAnnotationHolder_ReturnsAnnotationHolder_WhenASTIsClass() {
        // Arrange
        ast.addChild(createTokenAST(16));
        ast.getFirstChild().addChild(createTokenAST(158));
        
        // Act
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        
        // Assert
        assertNotNull(annotationHolder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_ThrowsIllegalArgumentException_WhenASTIsNull() {
        // Arrange
        ast = null;
        
        // Act
        AnnotationUtility.getAnnotationHolder(null);
        
        // Assert
        // Exception expected
    }
    
    // Test cases for getAnnotation(DetailAST, String)
    
    @Test
    public void testGetAnnotation_ReturnsNull_WhenASTIsNull() {
        // Arrange
        ast = null;
        
        // Act
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "Override");
        
        // Assert
        assertNull(annotation);
    }
    
    @Test
    public void testGetAnnotation_ReturnsNull_WhenAnnotationDoesNotExist() {
        // Arrange
        ast.addChild(createTokenAST(155));
        ast.getFirstChild().addChild(createFullIdentAST("SuppressWarnings"));
        
        // Act
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "Override");
        
        // Assert
        assertNull(annotation);
    }
    
    @Test
    public void testGetAnnotation_ReturnsAnnotation_WhenAnnotationExists() {
        // Arrange
        ast.addChild(createTokenAST(155));
        ast.getFirstChild().addChild(createFullIdentAST("Override"));
        
        // Act
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "Override");
        
        // Assert
        assertNotNull(annotation);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_ThrowsIllegalArgumentException_WhenASTIsNull() {
        // Arrange
        ast = null;
        
        // Act
        AnnotationUtility.getAnnotation(null, "Override");
        
        // Assert
        // Exception expected
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_ThrowsIllegalArgumentException_WhenAnnotationIsNull() {
        // Arrange
        ast.addChild(createTokenAST(155));
        ast.getFirstChild().addChild(createFullIdentAST("Override"));
        
        // Act
        AnnotationUtility.getAnnotation(ast, null);
        
        // Assert
        // Exception expected
    }
    
    private DetailAST createAnnotationAST(String annotation) {
        DetailAST annotationAST = new DetailAST();
        annotationAST.setType(159);
        annotationAST.addChild(createFullIdentAST(annotation));
        return annotationAST;
    }
    
    private DetailAST createTokenAST(int type) {
        DetailAST tokenAST = new DetailAST();
        tokenAST.setType(type);
        return tokenAST;
    }
    
    private DetailAST createFullIdentAST(String name) {
        DetailAST fullIdentAST = new DetailAST();
        fullIdentAST.setText(name);
        return fullIdentAST;
    }
}