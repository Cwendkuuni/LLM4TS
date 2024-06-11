import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name1");
    assertEquals("name1", renamer.getReplacementName("name1"));
  }

  @Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name1");
    assertEquals("name1$$0", renamer.getReplacementName("name1"));
  }

  @Test
  public void testStripConstIfReplaced() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(
        () -> "id", "prefix", true);
    assertEquals(true, renamer.stripConstIfReplaced());
  }

  @Test
  public void testForChildScope() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
    assertNotEquals(renamer, childRenamer);
  }
}