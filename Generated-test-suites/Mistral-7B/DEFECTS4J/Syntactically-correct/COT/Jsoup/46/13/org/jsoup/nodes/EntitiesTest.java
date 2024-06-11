package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test isNamedEntity method with valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test isNamedEntity method with invalid named entities
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("invalid"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test isBaseNamedEntity method with valid base named entities
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test isBaseNamedEntity method with invalid base named entities
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
        assertFalse(Entities.isBaseNamedEntity(null));

        // Test isBaseNamedEntity method with extended named entities
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity("heta"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test getCharacterByName method with valid named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test getCharacterByName method with invalid named entities
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("invalid"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullString() {
        Entities.escape(null, Document.OutputSettings.base(), new StringBuilder(), false, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithEmptyString() {
        Entities.escape("", Document.OutputSettings.base(), new StringBuilder(), false, false, false);
    }

    @Test
    public void testEscapeWithSimpleStrings() {
        StringBuilder accum = new StringBuilder();
        String input = "Hello & World!";

        Entities.escape(accum, input, Document.OutputSettings.base(), false, false, false);

        assertEquals("Hello &amp; World!", accum.toString());
    }

    @Test
    public void testEscapeWithWhiteSpace() {
        StringBuilder accum = new StringBuilder();
        String input = " Hello World! ";

        Entities.escape(accum, input, Document.OutputSettings.base(), false, true, false);

        assertEquals("Hello&nbsp; World!", accum.toString());
    }

    @Test
    public void testEscapeWithLeadingWhiteSpace() {
        StringBuilder accum = new StringBuilder();
        String input = " Hello World! ";

        Entities.escape(accum, input, Document.OutputSettings.base(), false, true, true);

        assertEquals(" Hello&nbsp; World!", accum.toString());
    }

    @Test
    public void testEscapeWithComplexString() {
        StringBuilder accum = new StringBuilder();
        String input = "Hello & World! &lt;p&gt;This is &amp; a &nbsp; test.&lt;/p&gt;";

        Entities.escape(accum, input, Document.OutputSettings.base(), false, false, false);

        assertEquals("Hello &amp;amp; World! &amp;lt;p&gt;This is &amp;amp;amp; a &amp;nbsp; test.&amp;lt;/p&gt;", accum.toString());
    }

    @Test
    public void testUnescapeWithSimpleString() {
        String input = "&amp; Hello &amp; World!";

        assertEquals("Hello World!", Entities.unescape(input));
    }

    @Test
    public void testUnescapeWithComplexString() {
        String input = "&lt;p&gt; &amp;amp; &amp;lt;b&gt;Hello&amp;lt;/b&gt; &amp;amp; World! &amp;lt;/p&gt;";

        assertEquals("<p> &amp;amp; <b>Hello</b> &amp;amp; World! </p>", Entities.unescape(input));
    }

    @Test
    public void testCanEncodeWithValidCharacters() {
        for (char c = 0; c < 128; c++) {
            assertTrue(Entities.canEncode(CoreCharset.ascii, c, null));
            assertTrue(Entities.canEncode(CoreCharset.utf, c, null));
        }
    }

    @Test
    public void testCanEncodeWithInvalidCharacters() {
        assertFalse(Entities.canEncode(CoreCharset.ascii, 0x1F000, null));
        assertFalse(Entities.canEncode(CoreCharset.utf, '\uD800', null));
    }
}