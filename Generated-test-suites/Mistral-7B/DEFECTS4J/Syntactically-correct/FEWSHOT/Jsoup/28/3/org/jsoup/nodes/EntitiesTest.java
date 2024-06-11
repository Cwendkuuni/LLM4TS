package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            assertTrue(Entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsNamedEntity_false() {
        assertFalse(Entities.isNamedEntity("randomString"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscape() {
        String input = "&amp;< >'\"";
        String xhtmlOutput = Entities.escape(input, Document.OutputSettings.xhtml(), null);
        String baseOutput = Entities.escape(input, Document.OutputSettings.base(), null);
        String extendedOutput = Entities.escape(input, Document.OutputSettings.extended(), null);

        assertEquals("&amp;amp;lt; &gt; apos; quot;", xhtmlOutput);
        assertEquals("&amp;lt;amp;&gt; &lt; >'&quot;", baseOutput);
        assertEquals("&amp;#38;#60;#62; amp;gt; &apos; &quot;", extendedOutput);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;#38;#60;#62; amp;gt; &apos; &quot;";
        String output = Entities.unescape(input);
        assertEquals("&amp;< >'\"", input);
        assertEquals("&amp;< &gt; '\"", output);
    }

    @Test
    public void testUnescape_strict() {
        String input = "&amp;#38;#60;#62; amp;gt; &apos; &quot;";
        String output = Entities.unescape(input, true);
        assertEquals("&amp;< &gt; '\"", input);
        assertEquals("&amp;< &gt; &apos; &quot;", output);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Entities.base.size(), entities.size());
    }

    @Test(expected = org.jsoup.ParseException.class)
    public void testLoadEntities_missingResource() {
        Entities.loadEntities("missing-entities.properties");
    }
}