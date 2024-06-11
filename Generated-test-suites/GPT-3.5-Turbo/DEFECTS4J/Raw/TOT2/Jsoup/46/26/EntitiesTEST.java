import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String input = "This is & < > \" test";
        String expected = "This is &amp; &lt; &gt; &quot; test";
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "This is &amp; &lt; &gt; &quot; test";
        String expected = "This is & < > \" test";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testEscapeWithOutputSettings() {
        String input = "This is & < > \" test";
        String expected = "This is &amp; &lt; &gt; &quot; test";
        assertEquals(expected, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "This is &lt; &gt";
        String expected = "This is < >";
        assertEquals(expected, Entities.unescape(input, true));
    }

    // More test cases for edge cases and exception handling scenarios can be added here

}