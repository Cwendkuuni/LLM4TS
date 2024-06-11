import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal;
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
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");

        assertEquals("name1$$0", renamer.getReplacementName("name1"));
        assertEquals("name2$$0", renamer.getReplacementName("name2"));
    }

    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");

        assertEquals("name1$$0", renamer.getReplacementName("name1"));
        assertEquals("name2$$0", renamer.getReplacementName("name2"));
        assertNull(renamer.getReplacementName("name3"));
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.Renamer renamer1 = new MakeDeclaredNamesUnique.ContextualRenamer();
        MakeDeclaredNamesUnique.Renamer renamer2 = new MakeDeclaredNamesUnique.InlineRenamer(() -> "123", "prefix", true);

        assertFalse(renamer1.stripConstIfReplaced());
        assertTrue(renamer2.stripConstIfReplaced());
    }
}