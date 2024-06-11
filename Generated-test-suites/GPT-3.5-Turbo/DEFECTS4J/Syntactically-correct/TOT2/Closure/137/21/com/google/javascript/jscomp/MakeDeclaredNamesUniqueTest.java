import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("testName");
        assertEquals("testName$$1", renamer.getReplacementName("testName"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("testName");
        assertEquals("testName$$1", renamer.getReplacementName("testName"));
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        assertEquals(false, renamer.stripConstIfReplaced());
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.ContextualRenamer parentRenamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        parentRenamer.addDeclaredName("parentName");
        MakeDeclaredNamesUnique.ContextualRenamer childRenamer = (MakeDeclaredNamesUnique.ContextualRenamer) parentRenamer.forChildScope();
        childRenamer.addDeclaredName("childName");
        assertEquals("parentName$$1", parentRenamer.getReplacementName("parentName"));
        assertEquals("childName$$1", childRenamer.getReplacementName("childName"));
    }

    @Test
    public void testInlineRenamer() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "123", "ID", true);
        renamer.addDeclaredName("testName");
        assertEquals("testName$$ID123", renamer.getReplacementName("testName"));
    }
}