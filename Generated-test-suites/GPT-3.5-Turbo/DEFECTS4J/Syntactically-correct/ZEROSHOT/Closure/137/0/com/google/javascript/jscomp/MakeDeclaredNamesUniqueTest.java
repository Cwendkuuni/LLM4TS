import org.junit.Test;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testEnterScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertNotNull(renamer);
    }

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assertEquals("name$$0", replacementName);
    }

    @Test
    public void testGetUniqueName() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "id", "prefix", true);
        String uniqueName = renamer.getUniqueName("name");
        assertEquals("name$$id", uniqueName);
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertNull(renamer.getReplacementName("name"));
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertNotNull(renamer.forChildScope());
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertFalse(renamer.stripConstIfReplaced());
    }

    @Test
    public void testExitScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assertNotNull(replacementName);
    }

    @Test
    public void testVisit() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assertNotNull(replacementName);
    }
}