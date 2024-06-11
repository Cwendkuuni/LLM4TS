package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnnotationUtilityTest {
  
  @Test
  public void testContainsAnnotation_ReturnsTrue_WhenAnnotationExists() {
    // arrange
    DetailAST ast = new DetailAST();
    String annotation = "TestAnnotation";
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
    
    // assert
    assertTrue(result);
  }
  
  @Test
  public void testContainsAnnotation_ReturnsFalse_WhenAnnotationDoesNotExist() {
    // arrange
    DetailAST ast = new DetailAST();
    String annotation = "TestAnnotation";
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast, annotation);
    
    // assert
    assertFalse(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsAnnotation_ThrowsIllegalArgumentException_WhenASTIsNull() {
    // arrange
    DetailAST ast = null;
    String annotation = "TestAnnotation";
    
    // act
    AnnotationUtility.containsAnnotation(ast, annotation);
    
    // assert (expected exception)
  }
  
  @Test
  public void testContainsAnnotation_ReturnsTrue_WhenHolderBranchContains159() {
    // arrange
    DetailAST ast = new DetailAST();
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast);
    
    // assert
    assertTrue(result);
  }
  
  @Test
  public void testContainsAnnotation_ReturnsFalse_WhenHolderBranchDoesNotContain159() {
    // arrange
    DetailAST ast = new DetailAST();
    
    // act
    boolean result = AnnotationUtility.containsAnnotation(ast);
    
    // assert
    assertFalse(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsAnnotation_ThrowsIllegalArgumentException_WhenASTIsNull() {
    // arrange
    DetailAST ast = null;
    
    // act
    AnnotationUtility.containsAnnotation(ast);
    
    // assert (expected exception)
  }
  
  @Test
  public void testGetAnnotationHolder_ReturnsNotNull_WhenASTTypeIs155() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(155);
    
    // act
    DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
    
    // assert
    assertNotNull(result);
  }
  
  @Test
  public void testGetAnnotationHolder_ReturnsNotNull_WhenASTTypeIs16() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(16);
    
    // act
    DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
    
    // assert
    assertNotNull(result);
  }
  
  @Test
  public void testGetAnnotationHolder_ReturnsNotNull_WhenASTTypeIsOtherThan155And16() {
    // arrange
    DetailAST ast = new DetailAST();
    ast.setType(10);
    
    // act
    DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
    
    // assert
    assertNotNull(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotationHolder_ThrowsIllegalArgumentException_WhenASTIsNull() {
    // arrange
    DetailAST ast = null;
    
    // act
    AnnotationUtility.getAnnotationHolder(ast);
    
    // assert (expected exception)
  }
  
  @Test
  public void testGetAnnotation_ReturnsNotNull_WhenAnnotationExists() {
    // arrange
    DetailAST ast = new DetailAST();
    String annotation = "TestAnnotation";
    
    // act
    DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert
    assertNotNull(result);
  }
  
  @Test
  public void testGetAnnotation_ReturnsNull_WhenAnnotationDoesNotExist() {
    // arrange
    DetailAST ast = new DetailAST();
    String annotation = "TestAnnotation";
    
    // act
    DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert
    assertNull(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_ThrowsIllegalArgumentException_WhenASTIsNull() {
    // arrange
    DetailAST ast = null;
    String annotation = "TestAnnotation";
    
    // act
    AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert (expected exception)
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_ThrowsIllegalArgumentException_WhenAnnotationIsNull() {
    // arrange
    DetailAST ast = new DetailAST();
    String annotation = null;
    
    // act
    AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert (expected exception)
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotation_ThrowsIllegalArgumentException_WhenAnnotationIsEmptyOrSpaces() {
    // arrange
    DetailAST ast = new DetailAST();
    String annotation = "   ";
    
    // act
    AnnotationUtility.getAnnotation(ast, annotation);
    
    // assert (expected exception)
  }
}