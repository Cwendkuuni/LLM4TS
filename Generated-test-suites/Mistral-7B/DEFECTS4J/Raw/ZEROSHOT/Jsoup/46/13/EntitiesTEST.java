package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertFalse(entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertNull(entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;test&gt; &amp; &apos; &quot; &x00A0 &quot;<test&quot; &amp; &lt; &gt; &quot; &x26; &x22;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#x00A0; &amp;quot;<test&amp;quot; &amp;amp; &amp;lt; &amp;gt; &amp;quot; &amp;#x26; &amp;#x22;";

        String output = entities.escape(input, outputSettings);
        assertEquals(expected, output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#x00A0; &amp;quot;<test&amp;quot; &amp;amp; &amp;lt; &amp;gt; &amp;quot; &amp;#x26; &amp;#x22;";
        String expected = "&lt;test&gt; &amp; &apos; &quot;  <test";

        String output = entities.unescape(input);
        assertEquals(expected, output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        Character c = 'ä';
        assertTrue(entities.canEncode(CoreCharset.utf, c, entities.getDefaultCharset().encoder()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCanEncodeUnsupportedOperationException() {
        Entities entities = new Entities();
        Character c = 'ä';
        entities.canEncode(null, c, entities.getDefaultCharset().encoder());
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        Map<String, Character> loadedEntities = entities.loadEntities("entities-base.properties");
        assertEquals(Entities.base.size(), loadedEntities.size());

        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            assertEquals(entry.getValue(), loadedEntities.get(entry.getKey()));
        }
    }
}