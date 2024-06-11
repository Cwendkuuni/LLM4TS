package com.google.javascript.jscomp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  private MakeDeclaredNamesUnique.Renamer renamer;

  @Before
  public void setup() {
    renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
  }

  @Test
  public void testAddDeclaredName() {
    renamer.addDeclaredName("var1");
    assertEquals("var1$$0", renamer.getReplacementName("var1"));
  }

  @Test
  public void testAddDeclaredName_NoConflict() {
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var2");
    assertEquals("var1$$0", renamer.getReplacementName("var1"));
    assertEquals("var2$$0", renamer.getReplacementName("var2"));
  }

  @Test
  public void testAddDeclaredName_Conflict() {
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var1");
    assertEquals("var1", renamer.getReplacementName("var1"));
  }
  
  @Test
  public void testGetReplacementName_DeclaredName() {
    renamer.addDeclaredName("var1");
    assertEquals("var1$$0", renamer.getReplacementName("var1"));
  }
  
  @Test
  public void testGetReplacementName_UndeclaredName() {
    assertEquals(null, renamer.getReplacementName("var1"));
  }
  
  @Test
  public void testStripConstIfReplaced() {
    assertEquals(false, renamer.stripConstIfReplaced());
  }
  
  @Test
  public void testForChildScope() {
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
    assertNotEquals(childRenamer, renamer);
  }
}