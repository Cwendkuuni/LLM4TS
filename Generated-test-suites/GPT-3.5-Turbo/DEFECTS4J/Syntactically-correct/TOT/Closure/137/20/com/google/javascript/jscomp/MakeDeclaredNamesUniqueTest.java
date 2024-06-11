import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testAddDeclaredName_GlobalScope() {
    MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    
    // Test adding a declared name to the global scope
    renamer.addDeclaredName("name1");
    
    // Check if the name is added to the declarations map
    assertNull(renamer.getReplacementName("name1"));
    
    // Test adding a duplicate declared name
    renamer.addDeclaredName("name1");
    
    // Check if the duplicate name is given a unique replacement name
    assertNotNull(renamer.getReplacementName("name1"));
  }
  
  @Test
  public void testAddDeclaredName_ChildScope() {
    MakeDeclaredNamesUnique.Renamer parentRenamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    MakeDeclaredNamesUnique.Renamer childRenamer = parentRenamer.forChildScope();
    
    // Test adding a declared name to a child scope
    parentRenamer.addDeclaredName("name1");
    
    // Check if the name is added to the declarations map of the parent scope
    assertNull(parentRenamer.getReplacementName("name1"));
    
    // Check if the name is added to the declarations map of the child scope
    assertNull(childRenamer.getReplacementName("name1"));
    
    // Test adding a duplicate declared name in the child scope
    childRenamer.addDeclaredName("name1");
    
    // Check if the duplicate name in the child scope is given a unique replacement name
    assertNotNull(childRenamer.getReplacementName("name1"));
  }
}