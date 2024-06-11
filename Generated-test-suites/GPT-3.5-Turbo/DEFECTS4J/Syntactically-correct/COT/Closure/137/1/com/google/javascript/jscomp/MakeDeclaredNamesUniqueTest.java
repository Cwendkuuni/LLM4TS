import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;

public class MakeDeclaredNamesUniqueTest {
  private MakeDeclaredNamesUnique.Renamer renamer;

  @Before
  public void setup() {
    renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "test_", true);
  }

  @Test
  public void testAddDeclaredName() {
    renamer.addDeclaredName("name");
    String replacementName = renamer.getReplacementName("name");
    assertEquals("test_name_1", replacementName);
  }

  @Test
  public void testGetReplacementName_NotDeclared() {
    String replacementName = renamer.getReplacementName("name");
    assertEquals(null, replacementName);
  }

  @Test
  public void testGetReplacementName_Declared() {
    renamer.addDeclaredName("name");
    String replacementName = renamer.getReplacementName("otherName");
    assertEquals(null, replacementName);
  }

  @Test
  public void testGetReplacementName_MultipleDeclarations() {
    renamer.addDeclaredName("name");
    renamer.addDeclaredName("name");
    String replacementName = renamer.getReplacementName("name");
    assertEquals("test_name_1", replacementName);
  }

  @Test
  public void testStripConstIfReplaced() {
    boolean stripConst = renamer.stripConstIfReplaced();
    assertEquals(true, stripConst);
  }
}