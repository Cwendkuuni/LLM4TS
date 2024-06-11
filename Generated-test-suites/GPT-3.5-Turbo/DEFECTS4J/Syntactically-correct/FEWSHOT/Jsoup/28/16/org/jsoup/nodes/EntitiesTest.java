package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("ltt"));
        assertFalse(Entities.isNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(null, Entities.getCharacterByName("arrow"));
    }
    
    @Test
    public void testEscape() {
        String string = "<div>Hello World!</div>";
        String expected = "&lt;div&gt;Hello World!&lt;/div&gt;";
        OutputSettings out = new OutputSettings();
        assertEquals(expected, Entities.escape(string, out));
    }
    
    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;Hello World!&lt;/div&gt;";
        String expected = "<div>Hello World!</div>";
        assertEquals(expected, Entities.unescape(string));
    }
    
    @Test
    public void testUnescapeWithStrictFalse() {
        String string = "&lt;div&gt;Hello World!";
        String expected = "<div>Hello World!";
        assertEquals(expected, Entities.unescape(string, false));
    }
    
    @Test
    public void testUnescapeWithStrictTrue() {
        String string = "&lt;div&gt;Hello World!";
        String expected = "&lt;div&gt;Hello World!";
        assertEquals(expected, Entities.unescape(string, true));
    }
}