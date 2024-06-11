
import org.junit.Assert;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testEnterScope_withFunctionDeclaration() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();

    childRenamer.addDeclaredName("name");

    String replacementName = childRenamer.getReplacementName("name");
    Assert.assertEquals("name$$1", replacementName);
  }

  @Test
  public void testExitScope() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();

    childRenamer.addDeclaredName("name");
    childRenamer.addDeclaredName("name$$1");

    childRenamer.stripConstIfReplaced();

    childRenamer.exitScope(null);

    String replacementName = renamer.getReplacementName("name");
    Assert.assertEquals("name$$1", replacementName);

    replacementName = renamer.getReplacementName("name$$1");
    Assert.assertEquals(null, replacementName);
  }

  @Test
  public void testEnterScope_withVarDeclaration() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();

    childRenamer.addDeclaredName("name");

    String replacementName = childRenamer.getReplacementName("name");
    Assert.assertEquals("name$$1", replacementName);
  }

  @Test
  public void testVisit() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();

    renamer.addDeclaredName("name");
    renamer.addDeclaredName("name$$1");

    String replacementName = renamer.getReplacementName("name");
    Assert.assertEquals("name$$1", replacementName);

    replacementName = renamer.getReplacementName("name$$1");
    Assert.assertEquals(null, replacementName);
  }

  @Test
  public void testGetUniqueName() {
    MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(
        () -> "1",
        "test",
        true
    );

    String uniqueName = renamer.getUniqueName("name");
    Assert.assertEquals("name$$test1", uniqueName);
  }
}
