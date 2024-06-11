import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");

        assertEquals("name1$$1", renamer.getReplacementName("name1"));
        assertEquals("name2$$1", renamer.getReplacementName("name2"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");

        assertEquals("name1$$1", renamer.getReplacementName("name1"));
        assertNull(renamer.getReplacementName("nonExistentName"));
    }

    @Test
    public void testContextualRenamerStripConst() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertTrue(renamer.stripConstIfReplaced());
    }

    @Test
    public void testInlineRenamerAddDeclaredName() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "ID1", "prefix", false);
        renamer.addDeclaredName("var1");

        assertEquals("var1$$prefixID1", renamer.getReplacementName("var1"));
    }

    @Test
    public void testInlineRenamerStripConst() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "ID1", "prefix", true);
        assertFalse(renamer.stripConstIfReplaced());
    }
}