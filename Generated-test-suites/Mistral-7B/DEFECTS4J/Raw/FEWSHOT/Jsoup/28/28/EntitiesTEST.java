package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    // Test isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    // Test getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertEquals("'", Entities.getCharacterByName("apos"));
        assertEquals('>', Entities.getCharacterByName("gt"));
    }

    // Test xhtmlByVal map
    @Test
    public void testXhtmlByValMap() {
        assertEquals("quot", Entities.xhtmlByVal.get((char) 0x00022));
        assertEquals("amp", Entities.xhtmlByVal.get((char) 0x00026));
        assertEquals("apos", Entities.xhtmlByVal.get((char) 0x00027));
        assertEquals("lt", Entities.xhtmlByVal.get((char) 0x0003C));
        assertEquals("gt", Entities.xhtmlByVal.get((char) 0x0003E));
    }

    // Test escape method with default encoding and base escape mode
    @Test
    public void testEscapeWithDefaultEncodingAndBaseEscapeMode() {
        assertEquals("&lt;hello&amp;world&gt;", Entities.escape("hello<world>", null, Entities.EscapeMode.base));
    }

    // Test escape method with custom encoding and base escape mode
    @Test
    public void testEscapeWithCustomEncodingAndBaseEscapeMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        assertEquals("&#x3C;hello&amp;world&#x3E;", Entities.escape("hello<world>", encoder, Entities.EscapeMode.base));
    }

    // Test escape method with default encoding and extended escape mode
    @Test
    public void testEscapeWithDefaultEncodingAndExtendedEscapeMode() {
        assertEquals("&lt;hello&amp;copy;world&gt;", Entities.escape("hello<copy;world>", null, Entities.EscapeMode.extended));
    }

    // Test escape method with custom encoding and extended escape mode
    @Test
    public void testEscapeWithCustomEncodingAndExtendedEscapeMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        assertEquals("&#x3C;hello&amp;copy;world&#x3E;", Entities.escape("hello<copy;world>", encoder, Entities.EscapeMode.extended));
    }

    // Test unescape method with default unescaping
    @Test
    public void testUnescapeWithDefaultUnescaping() {
        assertEquals("<hello&world>", Entities.unescape("&lt;hello&amp;world&gt;"));
    }

    // Test unescape method with strict unescaping
    @Test
    public void testUnescapeWithStrictUnescaping() {
        assertEquals("<hello&world>", Entities.unescape("&lt;hello&amp;world;&gt;"));
    }

    // Test unescape method with custom decoding and base unescaping
    @Test
    public void testUnescapeWithCustomDecodingAndBaseUnescaping() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        assertEquals("<hello&world>", Entities.unescape("&#x3C;hello&#x26;world;", false));
    }

    // Test unescape method with custom decoding and extended unescaping
    @Test
    public void testUnescapeWithCustomDecodingAndExtendedUnescaping() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        assertEquals("<hello&copy;world>", Entities.unescape("&#x3C;hello&#x00A9world;", false));
    }
}