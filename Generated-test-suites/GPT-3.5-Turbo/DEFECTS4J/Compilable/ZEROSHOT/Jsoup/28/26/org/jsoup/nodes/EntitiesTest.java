package org.jsoup.nodes;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("test"));
    }

    @Test
    public void testEscape1() {
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape("Hello & World", out);
        assertEquals("Hello &amp; World", result);
    }

    @Test
    public void testEscape2() {
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape("Java < Test > ", out);
        assertEquals("Java &lt; Test &gt; ", result);
    }

    @Test
    public void testEscape3() {
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape("&nbsp;", out);
        assertEquals("&amp;nbsp;", result);
    }

    @Test
    public void testUnescape1() {
        String result = Entities.unescape("Hello &amp; World");
        assertEquals("Hello & World", result);
    }

    @Test
    public void testUnescape2() {
        String result = Entities.unescape("Java &lt; Test &gt; ");
        assertEquals("Java < Test > ", result);
    }

    @Test
    public void testUnescape3() {
        String result = Entities.unescape("&#65;&#66;&#67;");
        assertEquals("ABC", result);
    }

}