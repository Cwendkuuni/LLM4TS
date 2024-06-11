package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testEnterScope_Function() {
    // TODO: write test case for enterScope() when the declaration root is a function
  }

  @Test
  public void testEnterScope_Block() {
    // TODO: write test case for enterScope() when the declaration root is a block
  }

  @Test
  public void testExitScope() {
    // TODO: write test case for exitScope()
  }

  @Test
  public void testShouldTraverse_Function() {
    // TODO: write test case for shouldTraverse() when the node type is a function
  }

  @Test
  public void testShouldTraverse_Catch() {
    // TODO: write test case for shouldTraverse() when the node type is a catch
  }

  @Test
  public void testShouldTraverse_Other() {
    // TODO: write test case for shouldTraverse() when the node type is neither function nor catch
  }

  @Test
  public void testVisit_Name() {
    // TODO: write test case for visit() when the node type is a NAME
  }

  @Test
  public void testVisit_Function() {
    // TODO: write test case for visit() when the node type is a FUNCTION
  }

  @Test
  public void testVisit_Catch() {
    // TODO: write test case for visit() when the node type is a CATCH
  }

  @Test
  public void testVisit_Other() {
    // TODO: write test case for visit() when the node type is neither NAME, FUNCTION, nor CATCH
  }

  @Test
  public void testGetReplacementName() {
    // TODO: write test case for getReplacementName()
  }

  @Test
  public void testAddDeclaredName() {
    // TODO: write test case for addDeclaredName()
  }

  @Test
  public void testGetUniqueName() {
    // TODO: write test case for getUniqueName()
  }

  @Test
  public void testInlineRenamer_ShouldStripConstWhenReplaced() {
    // TODO: write test case for InlineRenamer's stripConstIfReplaced() method
  }

}