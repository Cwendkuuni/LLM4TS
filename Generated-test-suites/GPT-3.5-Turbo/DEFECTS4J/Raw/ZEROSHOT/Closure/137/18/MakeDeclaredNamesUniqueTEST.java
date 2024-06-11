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
        renamer.addDeclaredName("var1");
        renamer.addDeclaredName("var2");
        assertEquals("var1", renamer.getReplacementName("var1"));
        assertEquals("var2$$1", renamer.getReplacementName("var2"));
    }

    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("var1");
        renamer.addDeclaredName("var2");
        assertEquals("var1", renamer.getReplacementName("var1"));
        assertEquals("var2$$1", renamer.getReplacementName("var2"));
        assertNull(renamer.getReplacementName("var3"));
    }

    @Test
    public void testStripConstIfReplaced() {
        renamer.addDeclaredName("var1");
        assertTrue(renamer.stripConstIfReplaced());
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        assertTrue(childRenamer instanceof MakeDeclaredNamesUnique.ContextualRenamer);
    }
}