import org.junit.Test;

import java.nio.charset.CharsetEncoder;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        CharsetEncoder encoder = CharsetEncoder.forName("UTF-8");
        Entities.EscapeMode mode = Entities.EscapeMode.xhtml;
        
        assertEquals("&lt;", Entities.escape("<", settings));
        assertEquals("&amp;", Entities.escape("&", settings));
        assertEquals("&quot;", Entities.escape("\"", settings));
        assertEquals("100", Entities.escape("100", settings));
        assertEquals("&amp;", Entities.escape(String.valueOf('\u0011'), encoder, mode));
    }

    @Test
    public void testUnescape() {
        String escaped = "&lt; &gt; &amp; &#65;";
        String unescaped = "< > & A";
        
        assertEquals(unescaped, Entities.unescape(escaped));
    }

    @Test
    public void testIsNamedEntity_base() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }
}