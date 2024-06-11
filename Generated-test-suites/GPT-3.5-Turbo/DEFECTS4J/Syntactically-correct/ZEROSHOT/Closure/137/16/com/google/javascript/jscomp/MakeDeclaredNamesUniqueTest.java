import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

   private MakeDeclaredNamesUnique.Renamer renamer;

   @Before
   public void setUp() {
      renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
   }

   @Test
   public void testAddDeclaredName() {
      // Test adding declared names to the renamer
      renamer.addDeclaredName("name1");
      renamer.addDeclaredName("name2");
      assertTrue(renamer.getReplacementName("name1") != null);
      assertTrue(renamer.getReplacementName("name2") != null);
   }

   @Test
   public void testGetReplacementName() {
      // Test getting replacement names from the renamer
      renamer.addDeclaredName("name1");
      renamer.addDeclaredName("name2");
      String replacement1 = renamer.getReplacementName("name1");
      String replacement2 = renamer.getReplacementName("name2");
      assertTrue(replacement1 != null);
      assertTrue(replacement2 != null);
      assertEquals(replacement1, renamer.getReplacementName("name1"));
      assertEquals(replacement2, renamer.getReplacementName("name2"));
   }

   @Test
   public void testStripConstIfReplaced() {
      // Test if the renamer strips constness when a name is replaced
      renamer.addDeclaredName("name");
      assertTrue(renamer.stripConstIfReplaced());
   }

   @Test
   public void testForChildScope() {
      // Test creating a child scope renamer
      MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
      assertTrue(childRenamer instanceof MakeDeclaredNamesUnique.ContextualRenamer);
   }
}