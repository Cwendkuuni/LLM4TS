import org.junit.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("unknown"));
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("unknown"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscape() {
        String string = "Test &amp; string <";
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        String expected = "Test &amp;amp; string &lt;";
        assertEquals(expected, Entities.escape(string, out));
    }

    @Test
    public void testUnescape1() {
        String string = "Test &amp;amp; string &lt;";
        String expected = "Test &amp; string <";
        assertEquals(expected, Entities.unescape(string));
    }

    @Test
    public void testUnescape2() {
        String string = "Test &amp;amp; string &lt;";
        String expected = "Test &amp;amp; string &lt;";
        assertEquals(expected, Entities.unescape(string, true));
    }
}