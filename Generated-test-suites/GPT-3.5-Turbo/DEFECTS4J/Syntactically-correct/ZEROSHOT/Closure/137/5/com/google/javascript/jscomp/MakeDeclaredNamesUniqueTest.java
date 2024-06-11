import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
  
  @Test
  public void testEnterScope() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
    childRenamer.addDeclaredName("name");
    assertEquals("name$$0", childRenamer.getReplacementName("name"));
  }
  
  @Test
  public void testExitScope() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
    childRenamer.addDeclaredName("name");
    childRenamer.addDeclaredName("name$$0");
    childRenamer.addDeclaredName("name$$1");
    assertEquals(null, childRenamer.getReplacementName("name"));
  }
  
  @Test
  public void testShouldTraverse() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
    childRenamer.addDeclaredName("name");
    assertEquals(true, childRenamer.stripConstIfReplaced());
  }
  
  @Test
  public void testVisit() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
    childRenamer.addDeclaredName("name");
    assertEquals("name$$0", childRenamer.getReplacementName("name"));
  }

}