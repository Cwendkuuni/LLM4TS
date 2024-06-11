package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entities in the full entity set
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));

        // Test for unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));

        // Test for empty string
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entities in the base entity set
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));

        // Test for unknown named entity
        assertFalse(Entities.isBaseNamedEntity("unknown"));

        // Test for empty string
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for known named entities in the full entity set
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertEquals('>', Entities.getCharacterByName("gt"));

        // Test for unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));

        // Test for empty string
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        String input = "&amp;lt;hello&gt; world &quot; & '";
        StringBuilder expected = new StringBuilder();
        Entities.escape(expected, input, null, false, false, false);
        String output = expected.toString();
        assertEquals("&amp;amp;lt; hello &amp;gt; world &amp;quot; &amp;apos; ", output);

        // Test with inAttribute set to true
        expected.setLength(0);
        Entities.escape(expected, input, null, true, false, false);
        output = expected.toString();
        assertEquals("&amp;lt;amp;&amp;lt; hello &amp;gt; &amp;quot; &amp;apos; ", output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;lt; hello &amp;gt; world &amp;quot; &amp;apos; ";
        String output = Entities.unescape(input, false);
        assertEquals("&lt; hello &gt; world &quot; ' ", output);

        // Test with strict set to true
        output = Entities.unescape(input, true);
        assertEquals("&lt;&gt;hello&gt;&quot;&apos; ", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeThrowsException() {
        // Test for illegal arguments
        CoreCharset.byName("invalid");
    }
}