package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("x"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("x"));
    }

    @Test
    public void testEscape() {
        String input = "&lt;x&gt;<&amp;&gt;\"&quot;'&apos;";
        String expectedOutput = "&amp;lt;x&amp;gt;&amp;lt;,&amp;amp;&amp;gt;,&amp;quot;,&amp;apos;";

        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, null, false, false, false);

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testEscapeInAttribute() {
        String input = "&lt;x&gt;<&amp;&gt;\"&quot;'&apos;";
        String expectedOutput = "&lt;x&gt;&amp;lt;,&amp;amp;&amp;gt;,&quot;,&quot;,&apos;,&apos;";

        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, null, true, false, false);

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

        assertTrue(Entities.canEncode(CoreCharset.utf, 'a', encoder));
        assertTrue(Entities.canEncode(CoreCharset.utf, 'é', encoder));
        assertFalse(Entities.canEncode(CoreCharset.ascii, 'é', encoder));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;x&amp;gt;&amp;lt;,&amp;amp;&amp;gt;,&quot;,&quot;,&apos;,&apos;";
        String expectedOutput = "&lt;x&gt;&lt;,&amp;&gt;\"\"&apos;&apos;";

        String actualOutput = Entities.unescape(input, false);

        assertEquals(expectedOutput, actualOutput);
    }
}