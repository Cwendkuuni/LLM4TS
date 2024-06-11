import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "This is <test> & \"sample\"";
        String output = Entities.escape(input, new Document.OutputSettings());
        assertEquals("This is &lt;test&gt; &amp; &quot;sample\"", output);
    }

    @Test
    public void testUnescape() {
        String input = "This is &lt;test&gt; &amp; &quot;sample&quot;";
        String output = Entities.unescape(input);
        assertEquals("This is <test> & \"sample\"", output);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}