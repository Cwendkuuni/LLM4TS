import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("nonexistent"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscape() {
        String input = "<html>";
        String expected = "&lt;html&gt;";
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;html&gt;";
        String expected = "<html>";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testEscapeWithAttributes() {
        String input = "<div class=\"important\">";
        String expected = "&lt;div class=&quot;important&quot;&gt;";
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testUnescapeWithStrictMode() {
        String input = "&lt;html&rt;";
        String expected = "&lt;html&rt;"; // Should not unescape since strict mode is enabled
        assertEquals(expected, Entities.unescape(input, true));
    }

    @Test
    public void testUnescapeWithoutStrictMode() {
        String input = "&lt;html&rt;";
        String expected = "<html>"; // Should unescape since strict mode is disabled
        assertEquals(expected, Entities.unescape(input, false));
    }

    @Test
    public void testEscapeModeGetXhtmlMap() {
        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");
        expected.put('&', "amp");
        expected.put('"', "quot");
        EscapeMode escapeMode = EscapeMode.xhtml;
        assertEquals(expected, escapeMode.getMap());
    }

    @Test
    public void testEscapeModeGetBaseMap() {
        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");
        expected.put('&', "amp");
        expected.put('"', "quot");
        EscapeMode escapeMode = EscapeMode.base;
        assertEquals(expected, escapeMode.getMap());
    }

    @Test
    public void testEscapeModeGetExtendedMap() {
        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");
        expected.put('&', "amp");
        expected.put('"', "quot");
        EscapeMode escapeMode = EscapeMode.extended;
        assertEquals(expected, escapeMode.getMap());
    }

    @Test
    public void testEscapeWithAttributeAndNormaliseWhite() {
        String input = "<div class=\"  important  \">";
        String expected = "&lt;div class=&quot;important&quot;&gt;";
        Entities.escape(new StringBuilder(), input, null, true, true, true);
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testEscapeWithoutAttributeAndNormaliseWhite() {
        String input = "<div class=\"  important  \">";
        String expected = "&lt;div class=\"  important  \"&gt;";
        Entities.escape(new StringBuilder(), input, null, false, true, true);
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testCanEncodeCharsetAscii() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '\u00E4', null));
    }

    @Test
    public void testCanEncodeCharsetUtf() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '\u00E4', null));
    }

    @Test
    public void testCanEncodeCharsetFallback() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'a', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, '\u00E4', null));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> expected = new HashMap<>();
        expected.put("lt", '<');
        expected.put("gt", '>');
        expected.put("amp", '&');
        expected.put("quot", '"');
        Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");
        assertEquals(expected, loadedEntities);
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> input = new HashMap<>();
        input.put("lt", '<');
        input.put("gt", '>');
        input.put("amp", '&');
        input.put("quot", '"');
        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");
        expected.put('&', "amp");
        expected.put('"', "quot");
        assertEquals(expected, Entities.toCharacterKey(input));
    }

    
}