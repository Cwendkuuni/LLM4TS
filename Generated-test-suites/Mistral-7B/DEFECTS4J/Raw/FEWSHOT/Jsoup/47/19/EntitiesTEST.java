package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((char) 0x0003C, Entities.getCharacterByName("lt"));
        assertEquals((char) 0x00026, Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt>foo&gt;bar&quot;quux";
        String expected = "&amp;amp;<amp>&lt;foo&gt;amp;gt;bar&amp;quot;quux";

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, input, null, false, false, false);
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;<amp>&lt;foo&gt;amp;gt;bar&amp;quot;quux";
        String expected = "&amp;<lt>foo&gt;bar&quot;quux";

        String unescaped = Entities.unescape(input);
        assertEquals(expected, unescaped);
    }
}