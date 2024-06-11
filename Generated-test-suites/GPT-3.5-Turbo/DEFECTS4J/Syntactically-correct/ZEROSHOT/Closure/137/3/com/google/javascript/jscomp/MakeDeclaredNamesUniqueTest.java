import org.junit.Test;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        renamer.addDeclaredName("name3");
        assertEquals("name1$$0", renamer.getReplacementName("name1"));
        assertEquals("name2$$0", renamer.getReplacementName("name2"));
        assertEquals("name3$$0", renamer.getReplacementName("name3"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        renamer.addDeclaredName("name3");
        assertEquals("name1$$0", renamer.getReplacementName("name1"));
        assertEquals("name2$$0", renamer.getReplacementName("name2"));
        assertEquals("name3$$0", renamer.getReplacementName("name3"));
        assertNull(renamer.getReplacementName("name4"));
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertTrue(renamer.stripConstIfReplaced());
    }
}