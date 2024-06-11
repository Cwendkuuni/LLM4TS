import org.jsoup.nodes.Entities;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("notAnEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("notABaseEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("notAnEntity"));
    }

    @Test
    public void testEscape() {
        String input = "<div>Hello World!</div>";
        String expectedOutput = "&lt;div&gt;Hello World!&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.html()));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Hello World!&lt;/div&gt;";
        String expectedOutput = "<div>Hello World!</div>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testEscapeEdgeCases() {
        // Test if quotes are properly escaped and unescaped
        String input = "\"Quotes\"";
        String expectedOutput = "&quot;Quotes&quot;";
        assertEquals(expectedOutput, Entities.escape(input, Document.OutputSettings.html()));
        assertEquals(input, Entities.unescape(Entities.escape(input, Document.OutputSettings.html())));

        // Test if non-breaking space is escaped based on the escape mode
        assertEquals("&nbsp;", Entities.escape(Character.toString((char) 0xA0), Document.OutputSettings.html()));
        assertEquals("&#xa0;", Entities.escape(Character.toString((char) 0xA0), Document.OutputSettings.xhtml()));

        // Test unknown character encoding
        assertEquals("&#x1f409;", Entities.escape(Character.toString((char) 0x1f409), Document.OutputSettings.html()));
    }

    @Test(expected = IOException.class)
    public void testLoadEntitiesException() throws IOException {
        Entities.loadEntities("nonExistentFile.properties");
    }
}