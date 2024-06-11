package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;

public class MakeDeclaredNamesUniqueTest {
  
  private MakeDeclaredNamesUnique.Renamer renamer;
  
  @Before
  public void setUp() {
    renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
  }
  
  @Test
  public void testAddDeclaredName_UniqueName() {
    renamer.addDeclaredName("foo");
    String replacementName = renamer.getReplacementName("foo");
    assertNull(replacementName);
  }
  
  @Test
  public void testAddDeclaredName_DuplicateName() {
    renamer.addDeclaredName("foo");
    renamer.addDeclaredName("foo");
    String replacementName = renamer.getReplacementName("foo");
    assertNotNull(replacementName);
    assertTrue(replacementName.matches("foo\\$\\$\\d+"));
  }
  
  @Test
  public void testGetReplacementName_NonExistentName() {
    String replacementName = renamer.getReplacementName("bar");
    assertNull(replacementName);
  }
  
  @Test
  public void testGetReplacementName_DuplicateName() {
    renamer.addDeclaredName("bar");
    renamer.addDeclaredName("bar");
    String replacementName1 = renamer.getReplacementName("bar");
    assertNotNull(replacementName1);
    assertTrue(replacementName1.matches("bar\\$\\$\\d+"));
    
    renamer.addDeclaredName("bar");
    String replacementName2 = renamer.getReplacementName("bar");
    assertNotNull(replacementName2);
    assertTrue(replacementName2.matches("bar\\$\\$\\d+"));
    assertNotEquals(replacementName1, replacementName2);
  }
  
  @Test
  public void testStripConstIfReplaced() {
    boolean stripConstness = renamer.stripConstIfReplaced();
    assertFalse(stripConstness);
  }
}