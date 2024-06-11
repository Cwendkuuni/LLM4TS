
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalidEntity"));
        assertNull(eEntities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
        assertEquals("&nbsp;", Entities.escape("\u00A0", new Document.OutputSettings()));
        assertEquals("A&#x00A0;B", Entities.escape("A\u00A0B", new Document.OutputSettings()));
        assertEquals("A B", Entities.escape("A B", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
        assertEquals("A\u00A0B", Entities.unescape("A&#x00A0;B"));
    }
    
}
