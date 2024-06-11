package org.jsoup.nodes;

import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EntitiesTest {

    private static Entities.EscapeMode escapeMode;
    private static Entity[] entities;

    @BeforeClass
    public static void setUp() {
        escapeMode = Entities.EscapeMode.xhtml;
        entities = new Entity[]{
                new Entity("lt", "<"),
                new Entity("gt", ">"),
                new Entity("amp", "&"),
                new Entity("quot", "\""),
                new Entity("apos", "'")
        };
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsNamedEntity_BaseSet() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("apos"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", escapeMode));
        assertEquals("&gt;", Entities.escape(">", escapeMode));
        assertEquals("&amp;", Entities.escape("&", escapeMode));
        assertEquals("&quot;", Entities.escape("\"", escapeMode));
        assertEquals("&apos;", Entities.escape("'", escapeMode));
        assertEquals("&#60;", Entities.escape("&#60;", escapeMode));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("&#60;", Entities.unescape("&#60;"));
    }

    @Test
    public void testUnescape_Strict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals(">", Entities.unescape("&gt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals("'", Entities.unescape("&apos;", true));
        assertEquals("&#60;", Entities.unescape("&#60;", true));
    }

    // Help Class to manage test data
    private static class Entity {
        private String name;
        private String character;

        Entity(String name, String character) {
            this.name = name;
            this.character = character;
        }

        public String getName() {
            return name;
        }

        public String getCharacter() {
            return character;
        }
    }
}