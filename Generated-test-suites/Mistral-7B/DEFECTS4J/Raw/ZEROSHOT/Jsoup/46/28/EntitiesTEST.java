package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "gt"};
        for (String name : names) {
            assertTrue(entities.isNamedEntity(name));
        }

        String[] invalidNames = {"invalidEntity", "&", "#"};
        for (String name : invalidNames) {
            assertFalse(entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "gt"};
        for (String name : names) {
            assertFalse(entities.isBaseNamedEntity(name));
        }

        String[] xhtmlNames = {"quot", "amp", "lt", "gt"};
        for (String name : xhtmlNames) {
            assertTrue(entities.isBaseNamedEntity(name));
        }

        String[] invalidNames = {"invalidEntity", "&", "#"};
        for (String name : invalidNames) {
            assertFalse(entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] names = {"lt", "amp", "quot", "gt"};
        Character[] expectedValues = {'\u003C', '&', '\u0022', '\u003E'};

        for (int i = 0; i < names.length; i++) {
            Character actualValue = entities.getCharacterByName(names[i]);
            assertEquals(expectedValues[i], actualValue);
        }

        String invalidName = "invalidEntity";
        assertNull(entities.getCharacterByName(invalidName));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "& < > \" &nbsp; &nbsp; &lt; &gt; &amp; &quot; &amp;lt; &amp;gt; &amp;quot;";
        String expectedOutput = "&amp;amp; &lt; &amp;lt; &gt; &amp;gt; &amp;amp;quot; &amp;quot; &amp;amp;lt; &amp;amp;gt; &amp;amp;quot;";

        StringBuilder output = new StringBuilder();
        entities.escape(output, input, null, false, false, false);

        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;amp; &lt; &amp;lt; &gt; &amp;gt; &amp;amp;quot; &amp;quot; &amp;amp;lt; &amp;amp;gt; &amp;amp;quot;";
        String expectedOutput = "& & <  > & > & \" & \" & < & > & \"";

        String output = entities.unescape(input, false);

        assertEquals(expectedOutput, output);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesWithMissingFile() {
        Entities entities = new Entities();
        entities.loadEntities("missingFile.properties");
        fail("Expected MissingResourceException but didn't get it.");
    }
}