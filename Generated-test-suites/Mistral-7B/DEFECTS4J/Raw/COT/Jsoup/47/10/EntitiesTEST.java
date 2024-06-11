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
        // Test for base named entities
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot")); // not in base set

        // Test for extended named entities
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for base named entities
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));

        // Test for extended named entities
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
    }

    @Test
    public void testCanEncode() {
        // Test with ASCII character
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));

        // Test with non-ASCII character
        Map<Character, String> fallback = new HashMap<>();
        fallback.put('𠮷', "x");
        assertTrue(Entities.canEncode(CoreCharset.fallback, '𠮷', fallback));
        assertFalse(Entities.canEncode(CoreCharset.ascii, '𠮷', null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithNullCharset() {
        Entities.canEncode(null, 'A', null);
    }

    @Test(expected = NullPointerException.class)
    public void testCanEncodeWithNullCharacter() {
        Entities.canEncode(CoreCharset.ascii, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCanEncodeWithNullFallback() {
        Entities.canEncode(CoreCharset.ascii, 'A', null);
    }

    @Test
    public void testIsNamedEntityWithInvalidEntityName() {
        assertFalse(Entities.isNamedEntity("invalidEntityName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithInvalidEntityName() {
        assertNull(Entities.getCharacterByName("invalidEntityName"));
    }
}