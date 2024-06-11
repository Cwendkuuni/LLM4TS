package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;

public class AnnotationUtilityTest {

   // Test case for containsAnnotation method with valid input
   @Test
   public void testContainsAnnotation_ValidInput() {
      DetailAST ast = new DetailAST();
      boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast, "TestAnnotation");
      assertFalse(containsAnnotation);
   }
   
   // Test case for containsAnnotation method with null input
   @Test(expected = IllegalArgumentException.class)
   public void testContainsAnnotation_NullInput() {
      AnnotationUtility.containsAnnotation(null, "TestAnnotation");
   }
   
   // Test case for containsAnnotation method with null annotation
   @Test(expected = IllegalArgumentException.class)
   public void testContainsAnnotation_NullAnnotation() {
      DetailAST ast = new DetailAST();
      AnnotationUtility.containsAnnotation(ast, null);
   }
   
   // Test case for containsAnnotation method with empty annotation
   @Test(expected = IllegalArgumentException.class)
   public void testContainsAnnotation_EmptyAnnotation() {
      DetailAST ast = new DetailAST();
      AnnotationUtility.containsAnnotation(ast, "");
   }
   
   // Test case for containsAnnotation method with whitespace annotation
   @Test(expected = IllegalArgumentException.class)
   public void testContainsAnnotation_WhitespaceAnnotation() {
      DetailAST ast = new DetailAST();
      AnnotationUtility.containsAnnotation(ast, "   ");
   }
   
   // Test case for containsAnnotation method with annotation present in branch 159
   @Test
   public void testContainsAnnotation_Branch159_AnnotationPresent() {
      DetailAST ast = new DetailAST();
      // Add a branch with type 159 to ast
      ast.addBranch(new DetailAST(159));
      
      boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast);
      assertTrue(containsAnnotation);
   }
   
   // Test case for containsAnnotation method with annotation not present in branch 159
   @Test
   public void testContainsAnnotation_Branch159_AnnotationNotPresent() {
      DetailAST ast = new DetailAST();
      boolean containsAnnotation = AnnotationUtility.containsAnnotation(ast);
      assertFalse(containsAnnotation);
   }
   
   // Test case for getAnnotationHolder method with ast containing annotation
   @Test
   public void testGetAnnotationHolder_WithAnnotation() {
      DetailAST ast = new DetailAST();
      // Add a token with type 158 to ast
      ast.addToken(new DetailAST(158));
      
      DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
      assertNotNull(annotationHolder);
   }
   
   // Test case for getAnnotationHolder method with ast not containing annotation
   @Test
   public void testGetAnnotationHolder_WithoutAnnotation() {
      DetailAST ast = new DetailAST();
      DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
      assertNull(annotationHolder);
   }
   
   // Test case for getAnnotation method with valid input and matching annotation
   @Test
   public void testGetAnnotation_ValidInput() {
      DetailAST ast = new DetailAST();
      // Add a branch with type 159 to ast
      ast.addBranch(new DetailAST(159));
      
      // Add a child DetailAST with type 159 and FullIdent with text "TestAnnotation"
      ast.getBranch().addToken(new DetailAST(159));
      ast.getBranch().getLastChild().addChild(new DetailAST(123, new FullIdent("TestAnnotation")));

      DetailAST annotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
      assertNotNull(annotation);
   }
   
   // Test case for getAnnotation method with valid input and non-matching annotation
   @Test
   public void testGetAnnotation_NonMatchingAnnotation() {
      DetailAST ast = new DetailAST();
      // Add a branch with type 159 to ast
      ast.addBranch(new DetailAST(159));
      
      // Add a child DetailAST with type 159 and FullIdent with text "TestAnnotation"
      ast.getBranch().addToken(new DetailAST(159));
      ast.getBranch().getLastChild().addChild(new DetailAST(123, new FullIdent("NotTestAnnotation")));

      DetailAST annotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
      assertNull(annotation);
   }
   
   // Test case for getAnnotation method with null input ast
   @Test(expected = IllegalArgumentException.class)
   public void testGetAnnotation_NullInput() {
      AnnotationUtility.getAnnotation(null, "TestAnnotation");
   }
   
   // Test case for getAnnotation method with null input annotation
   @Test(expected = IllegalArgumentException.class)
   public void testGetAnnotation_NullAnnotation() {
      DetailAST ast = new DetailAST();
      AnnotationUtility.getAnnotation(ast, null);
   }
   
   // Test case for getAnnotation method with empty input annotation
   @Test(expected = IllegalArgumentException.class)
   public void testGetAnnotation_EmptyAnnotation() {
      DetailAST ast = new DetailAST();
      AnnotationUtility.getAnnotation(ast, "");
   }
   
   // Test case for getAnnotation method with whitespace input annotation
   @Test(expected = IllegalArgumentException.class)
   public void testGetAnnotation_WhitespaceAnnotation() {
      DetailAST ast = new DetailAST();
      AnnotationUtility.getAnnotation(ast, "   ");
   }
   

}