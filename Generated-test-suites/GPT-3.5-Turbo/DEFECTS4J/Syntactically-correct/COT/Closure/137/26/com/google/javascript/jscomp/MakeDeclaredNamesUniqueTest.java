import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name");
    assertEquals("name", renamer.getReplacementName("name"));
  }
  
  @Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name");
    assertEquals(null, renamer.getReplacementName("invalid"));
  }
  
  @Test
  public void testStripConstIfReplaced() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(
        () -> "1", "prefix", true);
    assertTrue(renamer.stripConstIfReplaced());
  }

  @Test
  public void testForChildScope() {
    MakeDeclaredNamesUnique.Renamer renamer1 = new MakeDeclaredNamesUnique.InlineRenamer(
        () -> "1", "prefix", true);
    MakeDeclaredNamesUnique.Renamer renamer2 = renamer1.forChildScope();
    assertNotSame(renamer1, renamer2);
  }
}