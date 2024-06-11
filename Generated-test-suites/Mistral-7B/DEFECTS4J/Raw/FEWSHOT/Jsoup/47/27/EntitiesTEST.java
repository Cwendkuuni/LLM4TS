package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertEquals(true, Entities.isNamedEntity("lt"));
        assertEquals(true, Entities.isNamedEntity("amp"));
        assertEquals(true, Entities.isNamedEntity("quot"));
        assertEquals(true, Entities.isNamedEntity("nbsp"));
        assertEquals(true, Entities.isNamedEntity("iexcl"));
        assertEquals(true, Entities.isNamedEntity("heta"));
        assertEquals(false, Entities.isNamedEntity("xxx"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertEquals(true, Entities.isBaseNamedEntity("lt"));
        assertEquals(true, Entities.isBaseNamedEntity("amp"));
        assertEquals(true, Entities.isBaseNamedEntity("quot"));
        assertEquals(false, Entities.isBaseNamedEntity("nbsp"));
        assertEquals(false, Entities.isBaseNamedEntity("iexcl"));
        assertEquals(false, Entities.isBaseNamedEntity("heta"));
        assertEquals(false, Entities.isBaseNamedEntity("xxx"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertEquals('\u00A0', Entities.getCharacterByName("nbsp"));
        assertEquals('\u00A1', Entities.getCharacterByName("iexcl"));
        assertEquals('\u03B2', Entities.getCharacterByName("heta"));
        assertEquals(0, Entities.getCharacterByName("xxx").length);
    }

    @Test
    public void testEscape() {
        String string = "<foo>&<bar>quoted>&quot;";
        assertEquals("&lt;foo&amp;&lt;bar&gt;quoted&amp;quot;", Entities.escape(string, null));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;foo&amp;&lt;bar&gt;quoted&amp;quot;";
        assertEquals("<foo>&<bar>quoted>&\"", Entities.unescape(string, false));
    }

    @Test
    public void testCanEncode() {
        assertEquals(true, Entities.canEncode(CoreCharset.utf, 'A', null));
        assertEquals(true, Entities.canEncode(CoreCharset.ascii, 'A', null));
        assertEquals(false, Entities.canEncode(CoreCharset.ascii, '\u03B2', null));
        assertEquals(true, Entities.canEncode(CoreCharset.utf, '\u03B2', null));
    }
}