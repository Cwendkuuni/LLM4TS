import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
    
    private MakeDeclaredNamesUnique.Renamer renamer;
    
    @Before
    public void setUp() {
        renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "test_", false);
    }

    @Test
    public void testAddDeclaredName() {
        renamer.addDeclaredName("name");
        assertEquals("name$$1", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("name");
        assertEquals("name$$1", renamer.getReplacementName("name"));
        assertNull(renamer.getReplacementName("unknown"));
    }
    
    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        assertNotNull(childRenamer);
        assertNotEquals(childRenamer, renamer);
        assertEquals("name$$1", childRenamer.getReplacementName("name"));
    }
    
    @Test
    public void testStripConstIfReplaced() {
        assertFalse(renamer.stripConstIfReplaced());
    }
}