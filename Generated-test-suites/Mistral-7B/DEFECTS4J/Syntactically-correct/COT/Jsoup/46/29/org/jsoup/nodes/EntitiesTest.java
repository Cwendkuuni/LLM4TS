import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("rt")); // edge case: unknown entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("amp")); // edge case: named entity not in base set
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
    }

    @Test
    public void testEscapeMethod() {
        String input = "&lt;<amp>&gt; Hello &quot; World &nbsp; &amp; &amp;";
        String expected = "&amp;lt;&lt;amp;&gt; &amp;amp; &amp;amp;";
        String output = Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.base));
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Entities.escape(null, new Document.OutputSettings(Entities.EscapeMode.base));
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&amp;lt;&lt;amp;&gt; &amp;amp; &amp;amp;";
        String expected = "&lt;<amp>&gt;  &amp;";
        String output = Entities.unescape(input, false);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullInput() {
        Entities.unescape(null, false);
    }

    @Test
    public void testCanEncodeMethod() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u0304')); // edge case: non-ASCII character
    }

    // Add more test cases for other methods if needed
}