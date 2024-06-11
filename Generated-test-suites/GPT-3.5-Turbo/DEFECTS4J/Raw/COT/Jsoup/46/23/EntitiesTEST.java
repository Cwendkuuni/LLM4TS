
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Test normal escape
        String escaped1 = Entities.escape("<>&quot;", null);
        assertEquals("&lt;&gt;&amp;quot;", escaped1);

        // Test escape with normalising whitespace
        String escaped2 = Entities.escape("  normal   ", null);
        assertEquals("  normal   ", escaped2);

        // Test escape with normalising whitespace and strip leading whitespace
        String escaped3 = Entities.escape("  strip  ", null);
        assertEquals(" strip ", escaped3);
    }

    @Test
    public void testUnescape() {
        // Test normal unescape
        String unescaped1 = Entities.unescape("&lt;&gt;&amp;quot;");
        assertEquals("<>\"&", unescaped1);

        // Test strict unescape
        String unescaped2 = Entities.unescape("abc;&quot;", true);
        assertEquals("abc;&quot;", unescaped2);
    }
}

