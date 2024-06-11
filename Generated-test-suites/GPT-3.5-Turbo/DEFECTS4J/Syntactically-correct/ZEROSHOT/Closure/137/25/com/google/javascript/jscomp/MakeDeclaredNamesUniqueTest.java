import org.junit.Test;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    private MakeDeclaredNamesUnique.Renamer inlineRenamer;

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "test", false);
        renamer.addDeclaredName("name");
        assertEquals("name$$test1", renamer.getReplacementName("name"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "test", false);
        renamer.addDeclaredName("name");
        assertEquals("name$$test1", renamer.getReplacementName("name"));
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer parentRenamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "test", false);
        parentRenamer.addDeclaredName("name");
        MakeDeclaredNamesUnique.Renamer childRenamer = parentRenamer.forChildScope();
        childRenamer.addDeclaredName("name");
        assertEquals("name$$test1", childRenamer.getReplacementName("name"));
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "test", true);
        assertTrue(renamer.stripConstIfReplaced());
    }

}