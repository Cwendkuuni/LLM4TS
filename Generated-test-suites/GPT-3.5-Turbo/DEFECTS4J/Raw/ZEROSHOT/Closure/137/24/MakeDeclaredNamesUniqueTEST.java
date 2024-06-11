import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name");
    Assert.assertEquals("name", renamer.getReplacementName("name"));
  }

  @Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name");
    Assert.assertEquals("name123", renamer.getReplacementName("name"));
  }

  @Test
  public void testStripConstIfReplaced() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    renamer.addDeclaredName("name");
    Assert.assertFalse(renamer.stripConstIfReplaced());
  }

  @Test
  public void testForChildScope() {
    MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.ContextualRenamer childRenamer = (MakeDeclaredNamesUnique.ContextualRenamer) renamer.forChildScope();
    childRenamer.addDeclaredName("name");
    Assert.assertEquals("name123", childRenamer.getReplacementName("name"));
  }

  @Test
  public void testgetOrginalName() {
    Assert.assertEquals("name", MakeDeclaredNamesUnique.ContextualRenameInverter.getOrginalName("name123"));
  }

  @Test
  public void testgetNameSuffix() {
    Assert.assertEquals("123", MakeDeclaredNamesUnique.ContextualRenameInverter.getNameSuffix("name123", 4));
  }
}