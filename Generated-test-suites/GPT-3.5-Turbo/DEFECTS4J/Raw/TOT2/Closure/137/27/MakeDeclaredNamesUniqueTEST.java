import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("testName");
        assertEquals("testName$$0", renamer.getReplacementName("testName"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("testName");
        assertEquals("testName$$0", renamer.getReplacementName("testName"));
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertFalse(renamer.stripConstIfReplaced());
    }

}