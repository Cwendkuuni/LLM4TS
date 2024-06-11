package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            assertTrue(Entities.isNamedEntity(entry.getKey()));
        }

        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            assertEquals(entry.getValue(), Entities.getCharacterByName(entry.getKey()));
        }

        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String input = "&lt;test&amp;&apos; '&quot; &gt; &apos; &quot; &#123; &#x1F44D;";
        String expected = "&amp;lt;test&amp;&amp;apos; &amp;quot; &amp;gt; &amp;apos; &amp;quot; &amp;#123; &amp;#x1F44D;";
        assertEquals(expected, Entities.escape(input, settings));
    }

    @Test
    public void testEscapeWithEncoding() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String input = "𠮷";
        String expected = "&#55365;&#57281;";
        assertEquals(expected, Entities.escape(input, encoder, Entities.EscapeMode.extended));
    }

    @Test
    public void testEscapeWithXHTMLMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Document.OutputSettings.EscapeMode.xhtml);
        String input = "&amp;&lt; &gt; &apos; &quot;";
        String expected = "&amp;amp; &amp;lt; &amp;gt; &amp;apos; &amp;quot;";
        assertEquals(expected, Entities.escape(input, settings));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;test&amp;&apos; &apos; &quot; &gt; &apos; &quot; &#123; &#x1F44D;";
        String expected = "<<test>&apos; &apos; " + "\"\" > &apos; " + "\"\"";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescapeWithTrailingSemicolon() {
        String input = "&amp;lt;test&amp;&apos; &apos; &quot; &gt; &apos; &quot; &#123;&#038;";
        String expected = "<<test>&apos; &apos; " + "\"\" > &apos; " + "\"\"";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithInvalidCharacter() {
        String input = "&abc;";
        Entities.unescape(input);
    }

    @Test
    public void testUnescapeWithInvalidEntityName() {
        String input = "& ";
        assertNull(Entities.unescape(input));
    }

    @Test
    public void testUnescapeWithInvalidEntityValue() {
        String input = "&foobar;";
        assertNull(Entities.unescape(input));
    }

    @Test
    public void testUnescapeWithNullInput() {
        assertNull(Entities.unescape(null));
    }

    @Test
    public void testLoadEntities() {
        String filename = "entities-base.properties";
        Map<String, Character> entities = Entities.loadEntities(filename);
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            assertEquals(entry.getValue(), entities.get(entry.getKey()));
        }

        filename = "entities-full.properties";
        entities = Entities.loadEntities(filename);
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            assertEquals(entry.getValue(), entities.get(entry.getKey()));
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<>();
        entities.put("quot", '\u00022');
        entities.put("amp", '\u00026');
        entities.put("apos", '\u00027');
        entities.put("lt", '\u0003C');
        entities.put("gt", '\u0003E');

        Map<Character, String> expected = Entities.toCharacterKey(entities);
        Map<Character, String> mapping = new HashMap<>();
        mapping.put('\u00022', "quot");
        mapping.put('\u00026', "amp");
        mapping.put('\u00027', "apos");
        mapping.put('\u0003C', "lt");
        mapping.put('\u0003E', "gt");

        for (Map.Entry<Character, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), entry.getKey().toString());
            assertEquals(entry.getKey(), mapping.get(entry.getKey()));
        }
    }

    @Test
    public void testEscapeModeValues() {
        assertEquals(Entities.EscapeMode.xhtml, Entities.EscapeMode.valueOf("xhtml"));
        assertEquals(Entities.EscapeMode.base, Entities.EscapeMode.valueOf("base"));
        assertEquals(Entities.EscapeMode.extended, Entities.EscapeMode.valueOf("extended"));
    }

    @Test
    public void testUtilities() {
        Pattern pattern = Entities.unescapePattern;
        Matcher matcher = pattern.matcher("abc&def&ghi&jkl&mno");
        assertEquals(6, matcher.findCount());
        assertEquals(6, matcher.groupCount());

        String string = "&amp;lt;test&amp;&apos; &apos; &quot; &gt; &apos; &quot; &#123; &#x1F44D;";
        assertEquals("<<test>&apos; &apos; " + "\"\" > &apos; " + "\"\"", Entities.unescape(string));
    }
}