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
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity(" "));
        assertFalse(Entities.isNamedEntity("\n"));
        assertFalse(Entities.isNamedEntity("\r"));
        assertFalse(Entities.isNamedEntity("\t"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("nbsp;"));
        assertFalse(Entities.isBaseNamedEntity(" "));
        assertFalse(Entities.isBaseNamedEntity("\n"));
        assertFalse(Entities.isBaseNamedEntity("\r"));
        assertFalse(Entities.isBaseNamedEntity("\t"));
        assertTrue(Entities.isBaseNamedEntity("amp;"));
        assertTrue(Entities.isBaseNamedEntity("quot;"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("abcd"));
        assertNull(Entities.getCharacterByName(" "));
        assertNull(Entities.getCharacterByName("\n"));
        assertNull(Entities.getCharacterByName("\r"));
        assertNull(Entities.getCharacterByName("\t"));
        assertNull(Entities.getCharacterByName("nbsp;"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo &amp; bar &quot;quoted&quot; &gt;baz &nbsp;foobar&nbsp; &amp;amp; &quot;quoted&amp;quoted&quot;";
        String output = Entities.escape(input, Document.OutputSettings.base());
        assertEquals("&amp;lt;foo &amp;amp;amp;amp;amp;lt; &amp;amp;amp;amp;amp;amp;amp;amp;quot;quoted&amp;quot; &amp;amp;gt;baz &nbsp;foobar&nbsp; &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;lt; &amp;amp;quot;quoted&amp;amp;quot; &amp;amp;gt; &amp;amp;amp;amp;amp;amp;amp;amp;quot;quoted&amp;amp;amp;amp;quot;", output);

        String input2 = "&lt;foo &amp; bar &quot;quoted&quot; &gt;baz &nbsp;foobar&nbsp; &amp;amp; &quot;quoted&amp;quoted&quot;";
        String output2 = Entities.escape(input2, Document.OutputSettings.xhtml());
        assertEquals("&amp;lt; &amp;amp; &amp;quot;quoted&amp;quot; &amp;gt;baz &nbsp; &amp;nbsp;foobar&nbsp; &amp;amp; &amp;quot;quoted&amp;quoted&amp;quot;", output2);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo &amp;amp;amp;amp;amp;lt; &amp;amp;quot;quoted&amp;quot; &amp;gt;baz &nbsp;foobar&nbsp; &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;lt; &amp;amp;quot;quoted&amp;amp;quot; &amp;amp;gt; &amp;amp;amp;amp;amp;amp;amp;amp;quot;quoted&amp;amp;amp;amp;quot;";
        String output = Entities.unescape(input);
        assertEquals("&lt;foo &lt;&lt; &quot;quoted&quot; &gt;baz &nbsp;foobar&nbsp; &amp;amp; &quot;quotedquoted&quot;", output);

        String input2 = "&lt;foo &amp; bar &quot;quoted&quot; &gt;baz &nbsp;foobar&nbsp; &amp;amp; &quot;quoted&amp;quoted&quot;";
        String output2 = Entities.unescape(input2, true);
        assertEquals("<foo & bar \"quoted\" >baz foobar ", output2);
    }

    @Test
    public void testCanEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("ascii test".getBytes(StandardCharsets.UTF_8));
        CoreCharset charset = Entities.CoreCharset.ascii;
        assertTrue(Entities.canEncode(charset, 'a'));
        assertTrue(Entities.canEncode(charset, 'e'));
        assertTrue(Entities.canEncode(charset, 'i'));
        assertTrue(Entities.canEncode(charset, 's'));
        assertTrue(Entities.canEncode(charset, 't'));
        assertFalse(Entities.canEncode(charset, '\u00E5')); // Latin Small Letter Å
    }

    @Test
    public void testLoadEntities() throws IOException {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Entities.getCharacterByName("quot"), entities.get("quot"));
        assertEquals(Entities.getCharacterByName("amp"), entities.get("amp"));
        assertEquals(Entities.getCharacterByName("lt"), entities.get("lt"));
        assertEquals(Entities.getCharacterByName("gt"), entities.get("gt"));
        assertNull(entities.get("abcd"));
        assertNull(entities.get(" "));
        assertNull(entities.get("\n"));
        assertNull(entities.get("\r"));
        assertNull(entities.get("\t"));
        assertNull(entities.get("nbsp;"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = new HashMap<>();
        entities.put("quot", '\"');
        entities.put("amp", '&');
        entities.put("lt", '<');
        entities.put("gt", '>');
        entities.put("nbsp;", '\u00A0');

        Map<Character, String> entitiesReversed = Entities.toCharacterKey(entities);

        assertEquals("quot", entitiesReversed.get('\""'));
        assertEquals("amp", entitiesReversed.get('&'));
        assertEquals("lt", entitiesReversed.get('<'));
        assertEquals("gt", entitiesReversed.get('>'));
        assertEquals("nbsp;", entitiesReversed.get('\u00A0'));
        assertNull(entitiesReversed.get(' '));
        assertNull(entitiesReversed.get('\n'));
        assertNull(entitiesReversed.get('\r'));
        assertNull(entitiesReversed.get('\t'));
        assertNull(entitiesReversed.get('\u00B0'));
    }
}