import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("testName");
    assertEquals("testName", renamer.getReplacementName("testName"));
  }

  @Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("testName");
    assertEquals("testName", renamer.getReplacementName("testName"));
    assertNull(renamer.getReplacementName("nonExistentName"));
  }

  @Test
  public void testInlineRenamer() {
    MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "prefix", true);
    renamer.addDeclaredName("testName");
    assertEquals("testName$$prefix1", renamer.getReplacementName("testName"));
  }

  @Test
  public void testStripConstIfReplaced() {
    MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "prefix", true);
    assertTrue(renamer.stripConstIfReplaced());
  }

}