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
        // Testing with valid named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Testing with invalid named entities
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Testing with valid base named entities
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertTrue(Entities.isBaseNamedEntity("lt"));

        // Testing with invalid named entities
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        // Testing with valid named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Testing with invalid named entities
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscapeMethodBasic() {
        String input = "Hello & World!";
        String expectedOutput = "Hello &amp; World!";

        assertEquals(expectedOutput, Entities.escape(input, null));
    }

    @Test
    public void testEscapeMethodEdgeCases() {
        String input = "<script>alert('XSS')</script>".replaceAll("\\s+", "");

        String expectedOutput = "&lt;script&gt;alert(&#x2713;XSS&lt;/script&gt;";

        assertEquals(expectedOutput, Entities.escape(input, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Entities.escape(null, null);
    }

    @Test
    public void testCanEncode() {
        // Testing common ASCII characters
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.ascii, ' ', null));

        // Testing characters outside the ASCII range
        assertFalse(Entities.canEncode(CoreCharset.ascii, '\u00E9', null));

        // Testing UTF-8 characters
        assertTrue(Entities.canEncode(CoreCharset.utf, '😃', null));

        // Testing invalid characters
        assertFalse(Entities.canEncode(CoreCharset.fallback, '\uD800', null));
        assertFalse(Entities.canEncode(CoreCharset.fallback, '\uDFFF', null));
    }
}