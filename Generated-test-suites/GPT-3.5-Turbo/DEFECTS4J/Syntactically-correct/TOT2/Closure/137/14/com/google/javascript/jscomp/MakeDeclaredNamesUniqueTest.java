import static org.junit.Assert.*;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("testName");
    String replacementName = renamer.getReplacementName("testName");
    assertNotNull(replacementName);
    assertNotEquals("testName", replacementName);
  }

  @Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("testName");
    String replacementName = renamer.getReplacementName("testName");
    assertNotNull(replacementName);
  }

  @Test
  public void testInlineRenamer() {
    MakeDeclaredNamesUnique.InlineRenamer inlineRenamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "prefix", false);
    inlineRenamer.addDeclaredName("testName");
    String replacementName = inlineRenamer.getReplacementName("testName");
    assertNotNull(replacementName);
  }

  @Test
  public void testStripConstIfReplced() {
    MakeDeclaredNamesUnique.InlineRenamer inlineRenamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "prefix", true);
    assertTrue(inlineRenamer.stripConstIfReplaced());
  }

}