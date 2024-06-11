package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertTrue(Entities.isNamedEntity("amp")); // typical use case
        assertTrue(Entities.isNamedEntity("gt")); // typical use case
        assertTrue(Entities.isNamedEntity("quot")); // typical use case
        assertFalse(Entities.isNamedEntity("invalid")); // error scenario
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // typical use case
        assertTrue(Entities.isBaseNamedEntity("amp")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("gt")); // error scenario
        assertFalse(Entities.isBaseNamedEntity("quot")); // error scenario
        assertFalse(Entities.isBaseNamedEntity("invalid")); // error scenario
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp")); // typical use case
        assertNull(Entities.getCharacterByName("gt")); // error scenario
        assertNull(Entities.getCharacterByName("quot")); // error scenario
        assertNull(Entities.getCharacterByName("invalid")); // error scenario
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("Hello & World < Good & Bad", null);
        assertEquals("Hello &amp; World &lt; Good &amp; Bad", escapedString); // typical use case
    }

    @Test
    public void testEscape_withSpecialChars() {
        String escapedString = Entities.escape("H�ll� & W�rld < Good & B�d", null);
        assertEquals("H&#xE4;ll&#x131; &amp; W&#x131;rld &lt; Good &amp; B&#x131;d", escapedString); // edge case
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &amp; World &lt; Good &amp; Bad");
        assertEquals("Hello & World < Good & Bad", unescapedString); // typical use case
    }

    @Test
    public void testUnescape_withSpecialChars() {
        String unescapedString = Entities.unescape("H&#xE4;ll&#x131; &amp; W&#x131;rld &lt; Good &amp; B&#x131;d");
        assertEquals("H�ll� & W�rld < Good & B�d", unescapedString); // edge case
    }
}

