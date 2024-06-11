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
        renamer.addDeclaredName("name");
        assertTrue(renamer.getReplacementName("name") == null);
        renamer.addDeclaredName("name");
        assertTrue(renamer.getReplacementName("name").equals("name$$1"));
    }

    @Test
    public void testGetReplacementName() {
        assertNull(renamer.getReplacementName("name"));
        renamer.addDeclaredName("name");
        assertNull(renamer.getReplacementName("name"));
        renamer.addDeclaredName("name");
        assertEquals("name$$1", renamer.getReplacementName("name"));
    }

    @Test
    public void testStripConstIfReplaced() {
        assertFalse(renamer.stripConstIfReplaced());
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        assertTrue(childRenamer instanceof MakeDeclaredNamesUnique.ContextualRenamer);
    }
}