
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical case
        assertFalse(Entities.isNamedEntity("unknown")); // error scenario
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // typical case
        assertFalse(Entities.isBaseNamedEntity("unknown")); // error scenario
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp")); // typical case
        assertNull(Entities.getCharacterByName("unknown")); // error scenario
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\"")); // typical case with multiple special characters
        assertEquals("abc", Entities.escape("abc")); // typical case with no special characters
        assertNotNull(Entities.escape(null)); // error scenario
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;&quot;")); // typical case with multiple escaped characters
        assertEquals("abc", Entities.unescape("abc")); // typical case with no escaped characters
        assertNotNull(Entities.unescape(null)); // error scenario
    }

}
