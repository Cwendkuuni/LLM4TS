package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("abc")); // Typical use case, name is not a known entity
        assertTrue(Entities.isNamedEntity("lt")); // Typical use case, name is a known entity
        assertFalse(Entities.isNamedEntity("123")); // Error scenario, name is not a valid entity name
        assertTrue(Entities.isNamedEntity("gt")); // Edge case, name is a known entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt")); // Typical use case, name is a known entity in the base set
        assertFalse(Entities.isBaseNamedEntity("amp")); // Typical use case, name is not a known entity in the base set
        assertFalse(Entities.isBaseNamedEntity("123")); // Error scenario, name is not a valid entity name
        assertFalse(Entities.isBaseNamedEntity("abc")); // Edge case, name is not a known entity in the base set
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char)0x0003E), Entities.getCharacterByName("gt")); // Typical use case, name is a known entity
        assertNull(Entities.getCharacterByName("abc")); // Typical use case, name is not a known entity
        assertNull(Entities.getCharacterByName("123")); // Error scenario, name is not a valid entity name
        assertEquals(Character.valueOf((char)0x0003C), Entities.getCharacterByName("lt")); // Edge case, name is a known entity
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        assertEquals("This &amp; that", Entities.escape("This & that", out)); // Typical use case, string contains amp entity
        assertEquals("Hello &lt; World", Entities.escape("Hello < World", out)); // Typical use case, string contains lt entity
        assertEquals("This is &quot;quoted&quot;", Entities.escape("This is \"quoted\"", out)); // Typical use case, string contains quot entity
        assertEquals("This &amp; that &gt; those", Entities.escape("This & that > those", out)); // Edge case, string contains multiple entities 
    }

    @Test
    public void testUnescape() {
        assertEquals("This < that", Entities.unescape("This &lt; that")); // Typical use case, string contains lt entity
        assertEquals("This & that", Entities.unescape("This &amp; that")); // Typical use case, string contains amp entity
        assertEquals("This is \"quoted\"", Entities.unescape("This is &quot;quoted&quot;")); // Typical use case, string contains quot entity
        assertEquals("This & that > those", Entities.unescape("This &amp; that &gt; those")); // Edge case, string contains multiple entities 
    }

}