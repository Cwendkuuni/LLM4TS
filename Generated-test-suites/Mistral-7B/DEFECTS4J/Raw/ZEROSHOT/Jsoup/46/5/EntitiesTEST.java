package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] namedEntities = {"lt", "gt", "amp", "quot"};

        for (String namedEntity : namedEntities) {
            assertTrue(entities.isNamedEntity(namedEntity));
        }

        String[] nonNamedEntities = {"hello", "world", "!"};

        for (String nonNamedEntity : nonNamedEntities) {
            assertFalse(entities.isNamedEntity(nonNamedEntity));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] baseNamedEntities = {"lt", "gt", "amp", "quot"};

        for (String baseNamedEntity : baseNamedEntities) {
            assertTrue(entities.isBaseNamedEntity(baseNamedEntity));
        }

        String[] nonBaseNamedEntities = {"nbsp", "copy", "trade"};

        for (String nonBaseNamedEntity : nonBaseNamedEntities) {
            assertFalse(entities.isBaseNamedEntity(nonBaseNamedEntity));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] namedEntities = {"lt", "gt", "amp", "quot"};
        Character[] expectedValues = {'<', '>', '&', '"'};

        for (int i = 0; i < namedEntities.length; i++) {
            String namedEntity = namedEntities[i];
            Character actualValue = entities.getCharacterByName(namedEntity);
            assertEquals(expectedValues[i], actualValue);
        }
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;hello&gt;world&amp;!";
        String expectedOutput = "&amp;lt;hello&amp;gt;world&amp;amp;!";

        StringBuilder output = new StringBuilder();
        entities.escape(output, input, null, false, false, false);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;hello&amp;gt;world&amp;amp;!";
        String expectedOutput = "&lt;hello&gt;world&amp;!";

        String output = entities.unescape(input);
        assertEquals(expectedOutput, output);
    }
}