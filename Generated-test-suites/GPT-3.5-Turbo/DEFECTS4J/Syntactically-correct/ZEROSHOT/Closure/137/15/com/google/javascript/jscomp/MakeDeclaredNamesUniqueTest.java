import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name1");
    renamer.addDeclaredName("name2");
    assertEquals("name1$$0", renamer.getReplacementName("name1"));
    assertEquals("name2$$0", renamer.getReplacementName("name2"));
  }

  @Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name1");
    renamer.addDeclaredName("name2");
    assertEquals("name1$$0", renamer.getReplacementName("name1"));
    assertEquals("name2$$0", renamer.getReplacementName("name2"));
    assertNull(renamer.getReplacementName("name3"));
  }

  @Test
  public void testStripConstIfReplaced() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(
        () -> "id1",
        "prefix",
        true);
    renamer.addDeclaredName("name1");
    assertTrue(renamer.stripConstIfReplaced());
  }

  @Test
  public void testForChildScope() {
    MakeDeclaredNamesUnique.Renamer parentRenamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    parentRenamer.addDeclaredName("name");
    MakeDeclaredNamesUnique.Renamer childRenamer = parentRenamer.forChildScope();
    assertNull(childRenamer.getReplacementName("name"));
    childRenamer.addDeclaredName("name2");
    assertEquals("name2$$0", childRenamer.getReplacementName("name2"));
  }

}