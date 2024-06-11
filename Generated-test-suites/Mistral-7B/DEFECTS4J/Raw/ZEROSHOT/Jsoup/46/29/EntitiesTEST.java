package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("Named entity '" + value + "' should be recognized", Entities.isNamedEntity(value));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity '" + key + "' should be recognized as base entity", Entities.isBaseNamedEntity(key));
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String key = entry.getKey();
            Character value = entry.getValue();
            assertEquals("Named entity '" + key + "' should return character '" + value + "'", value, Entities.getCharacterByName(key));
        }
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "&lt;amp&gt; quote: &quot; &apos; &amp; &gt; &apos; &quot; entity: &#10; &#13; &#32; &#160; &#x201C; &#x201D;";
        String expected = "&amp;lt;amp&gt; &amp;quot; &amp;apos; &amp;amp; &amp;gt; &amp;apos; &amp;quot; &amp;#10; &amp;#13; &amp;#32; &amp;#160; &amp;#x201C; &amp;#x201D;";

        String escaped = Entities.escape(input, Document.OutputSettings.base());
        assertEquals("Input should be properly escaped", expected, escaped);

        String unescaped = Entities.unescape(escaped);
        assertEquals("Output should be properly unescaped", input, unescaped);
    }

    @Test
    public void testIsBaseNamedEntityWithXHTML() {
        Entities.EscapeMode xhtml = Entities.EscapeMode.xhtml;
        assertFalse("lt should not be recognized as base entity in xhtml", Entities.isBaseNamedEntity("lt"));
        assertTrue("quot should be recognized as base entity in xhtml", Entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testEscapeWithXHTML() {
        String input = "<amp>&quot; &apos; &lt; &gt; &apos; &quot;";
        String expected = "&amp;lt;amp&gt; &amp;quot; &amp;apos; &amp;lt; &amp;gt; &amp;apos; &amp;quot;";

        Entities.EscapeMode xhtml = Entities.EscapeMode.xhtml;
        String escaped = Entities.escape(input, Document.OutputSettings.xhtml(xhtml));
        assertEquals("Input should be properly escaped with xhtml", expected, escaped);
    }

    @Test
    public void testCanEncode() {
        String input = "éáéíóú";
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

        boolean result = Entities.canEncode(coreCharset, 'é', encoder);
        assertTrue("é should be encodable", result);

        result = Entities.canEncode(coreCharset, 'ñ', encoder);
        assertTrue("ñ should be encodable", result);

        result = Entities.canEncode(coreCharset, 'ü', encoder);
        assertTrue("ü should be encodable", result);

        result = Entities.canEncode(coreCharset, 'ț', encoder);
        assertFalse("ț should not be encodable", result);
    }
}