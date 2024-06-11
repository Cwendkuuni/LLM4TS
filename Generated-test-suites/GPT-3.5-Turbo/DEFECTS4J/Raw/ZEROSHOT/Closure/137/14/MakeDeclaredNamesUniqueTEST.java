import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    private MakeDeclaredNamesUnique.Renamer renamer;

    @Before
    public void setUp() {
        renamer = new MakeDeclaredNamesUnique.InlineRenamer(new Supplier<String>() {
            @Override
            public String get() {
                return "1";
            }
        }, "test", true);
    }

    @After
    public void tearDown() {
        renamer = null;
    }

    @Test
    public void testAddDeclaredName() {
        renamer.addDeclaredName("foo");
        renamer.addDeclaredName("bar");
        assertEquals("foo$$test1", renamer.getReplacementName("foo"));
        assertEquals("bar$$test1", renamer.getReplacementName("bar"));
    }

    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("foo");
        renamer.addDeclaredName("bar");
        assertEquals("foo$$test1", renamer.getReplacementName("foo"));
        assertEquals("bar$$test1", renamer.getReplacementName("bar"));
        assertNull(renamer.getReplacementName("baz"));
    }

    @Test
    public void testStripConstIfReplaced() {
        assertTrue(renamer.stripConstIfReplaced());
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        assertNotNull(childRenamer);
        assertNotSame(renamer, childRenamer);
    }

}