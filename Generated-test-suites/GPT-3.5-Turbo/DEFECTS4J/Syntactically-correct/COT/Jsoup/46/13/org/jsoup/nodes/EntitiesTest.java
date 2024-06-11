
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities.CoreCharset;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.CharsetEncoder;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    private static final String XHTML_FILENAME = "entities-xhtml.properties";
    private static final String BASE_FILENAME = "entities-base.properties";
    private static final String FULL_FILENAME = "entities-full.properties";

    @Before
    public void setUp() {
        // Load necessary resources or dependencies for the tests
    }

    @Test
    public void testIsNamedEntity_full() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity_base() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName_full() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "<p>Hello, world!</p>";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "&lt;p&gt;Hello, world!&lt;/p&gt;";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;p&gt;Hello, world!&lt;/p&gt;";
        String expectedOutput = "<p>Hello, world!</p>";
        assertFalse(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;p&gt;Hello, world!&lt;/p&gt;";
        String expectedOutput = "<p>Hello, world!</p>";
        assertFalse(expectedOutput, Entities.unescape(input, true));
    }

    @Test
    public void testEscape_withAttributes() {
        String input = "John &amp; Jane";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(EscapeMode.xhtml);
        String expectedOutput = "John &amp;amp; Jane";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_withNormalizeWhite() {
        String input = "   John   &amp;   Jane   ";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.normalizeWhitespace(true);
        String expectedOutput = " John &amp; Jane ";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_withStripLeadingWhite() {
        String input = "   John   &amp;   Jane   ";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.stripLeadingWhitespace(true);
        String expectedOutput = "John   &amp;   Jane   ";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_withAttributesAndWhitespace() {
        String input = "   John   &amp;   Jane   ";
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(EscapeMode.xhtml);
        settings.normalizeWhitespace(true);
        settings.stripLeadingWhitespace(true);
        String expectedOutput = "John &amp;amp; Jane   ";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_charCodeLessThanSupplementary() {
        String input = "Hello, world!";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "Hello, world!";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_charCodeEqualToSupplementary() {
        String input = "&#x1D306;";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "&#x1D306;";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_charCodeGreaterThanSupplementary() {
        String input = "&#x1F600;";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "&#x1F600;";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_nullInput() {
        String input = null;
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = null;
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_emptyInput() {
        String input = "";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = "";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testEscape_blankInput() {
        String input = " ";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expectedOutput = " ";
        assertEquals(expectedOutput, Entities.escape(input, settings));
    }

    @Test
    public void testUnescape_nullInput() {
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescape_emptyInput() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescape_blankInput() {
        String input = " ";
        String expectedOutput = " ";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescapeStrict_nullInput() {
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    @Test
    public void testUnescapeStrict_emptyInput() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    @Test
    public void testUnescapeStrict_blankInput() {
        String input = " ";
        String expectedOutput = " ";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    @Test
    public void testCoreCharset_byName_ascii() {
        CoreCharset charset = CoreCharset.byName("US-ASCII");
        assertEquals(CoreCharset.ascii, charset);
    }

    @Test
    public void testCoreCharset_byName_utf() {
        CoreCharset charset = CoreCharset.byName("UTF-8");
        assertEquals(CoreCharset.utf, charset);
    }

    @Test
    public void testCoreCharset_byName_fallback() {
        CoreCharset charset = CoreCharset.byName("ISO-8859-1");
        assertEquals(CoreCharset.fallback, charset);
    }

    @Test
    public void testCanEncode_ascii() {
        CharsetEncoder encoder = null; // Some encoder for ASCII charset
        boolean encoded = Entities.canEncode(CoreCharset.ascii, '<', encoder);
        assertTrue(encoded);
    }

    @Test
    public void testCanEncode_utf() {
        CharsetEncoder encoder = null; // Some encoder for UTF charset
        boolean encoded = Entities.canEncode(CoreCharset.utf, '<', encoder);
        assertTrue(encoded);
    }

    @Test
    public void testCanEncode_fallback() {
        CharsetEncoder encoder = null; // Some encoder for fallback charset
        boolean encoded = Entities.canEncode(CoreCharset.fallback, '<', encoder);
        assertTrue(encoded);
    }

    @Test
    public void testLoadEntities_xhtml() {
        Map<String, Character> entities = Entities.loadEntities(XHTML_FILENAME);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testLoadEntities_base() {
        Map<String, Character> entities = Entities.loadEntities(BASE_FILENAME);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testLoadEntities_full() {
        Map<String, Character> entities = Entities.loadEntities(FULL_FILENAME);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = Entities.loadEntities(FULL_FILENAME);
        Map<Character, String> characterKeyMap = Entities.toCharacterKey(entities);
        assertFalse(characterKeyMap.isEmpty());
    }
}

