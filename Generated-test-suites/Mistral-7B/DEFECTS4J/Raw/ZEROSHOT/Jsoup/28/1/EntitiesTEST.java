import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(isNamedEntity("lt"));
        assertTrue(isNamedEntity("amp"));
        assertTrue(isNamedEntity("quot"));
        assertFalse(isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', getCharacterByName("lt"));
        assertEquals('&', getCharacterByName("amp"));
        assertEquals('\'', getCharacterByName("apos"));
        assertNull(getCharacterByName("unknown"));
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "This is a test &amp; for escaping &lt; and &gt; characters.";
        String escaped = escape(input, Document.OutputSettings.defaultOutputSettings());
        String unescaped = unescape(escaped);

        assertEquals(input, unescaped);
    }

    @Test
    public void testXhtmlEscapeAndUnescape() {
        String input = "This is a test &amp;lt; and &gt; characters.";
        String xhtmlEscaped = escape(input, Document.OutputSettings.xhtmlOutputSettings());
        String xhtmlUnescaped = unescape(xhtmlEscaped, true); // strict unescape

        assertEquals(input, xhtmlUnescaped);
    }

    @Test
    public void testBaseEscapeAndUnescape() {
        String input = "This is a test &amp;lt; and &gt; characters.";
        String baseEscaped = escape(input, Document.OutputSettings.baseOutputSettings());
        String baseUnescaped = unescape(baseEscaped);

        assertEquals(input, baseUnescaped);
    }

    @Test
    public void testExtendedEscapeAndUnescape() {
        String input = "This is a test &copy; &trade; &reg;.";
        String extendedEscaped = escape(input, Document.OutputSettings.extendedOutputSettings());
        String extendedUnescaped = unescape(extendedEscaped);

        assertEquals(input, extendedUnescaped);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingEntityFile() {
        loadEntities("nonexistentfile.properties");
    }
}