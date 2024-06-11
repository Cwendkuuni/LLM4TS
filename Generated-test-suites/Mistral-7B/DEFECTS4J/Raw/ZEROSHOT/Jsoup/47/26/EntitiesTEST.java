package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] namedEntities = {"lt", "gt", "amp", "quot"};

        for (String name : namedEntities) {
            assertTrue(entities.isNamedEntity(name));
        }

        String[] invalidEntities = {"invalidName"};

        for (String name : invalidEntities) {
            assertFalse(entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] baseNamedEntities = {"lt", "gt", "amp", "quot"};

        for (String name : baseNamedEntities) {
            assertTrue(entities.isBaseNamedEntity(name));
        }

        String[] invalidBaseNamedEntities = {"nbsp"};

        for (String name : invalidBaseNamedEntities) {
            assertFalse(entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Character[] characters = {'>', '<', '&', '\''};
        String[] names = {"gt", "lt", "amp", "quot"};

        for (int i = 0; i < characters.length; i++) {
            assertEquals(characters[i], entities.getCharacterByName(names[i]));
        }
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Entities entities = new Entities();

        String testString = "& < > \" ' &nbsp; &quot; &amp; &lt; &gt; &nbsp;";
        String expectedEscapedString = "&amp; &lt; &amp; &quot; &amp; &amp;lt; &amp; &amp; &amp; &amp; &amp;gt; &amp; &nbsp; &amp; &amp;nbsp;";

        assertEquals(expectedEscapedString, entities.escape(testString, outputSettings));

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        expectedEscapedString = "&amp;lt; &amp;gt; &amp;amp; &amp;quot;";
        assertEquals(expectedEscapedString, entities.escape(testString, outputSettings));
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String testString = "&amp;lt; &amp;gt; &amp;amp; &amp;quot; &amp;amp;#160; &amp;#34;";
        String expectedUnescapedString = "< > & \"  ";

        assertEquals(expectedUnescapedString, entities.unescape(testString));
    }
}