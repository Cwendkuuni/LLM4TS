package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        String name = "lt";
        boolean result = Entities.isNamedEntity(name);
        assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        String name = "amp";
        boolean result = Entities.isBaseNamedEntity(name);
        assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        String name = "gt";
        Character result = Entities.getCharacterByName(name);
        assertEquals(Character.valueOf('>'), result);
    }

    @Test
    public void testEscape() {
        String string = "This is a <test> string";
        Document.OutputSettings out = new Document.OutputSettings();
        String result = Entities.escape(string, out);
        assertEquals("This is a &lt;test&gt; string", result);
    }

    @Test
    public void testUnescape() {
        String string = "This is a &lt;test&gt; string";
        String result = Entities.unescape(string);
        assertEquals("This is a <test> string", result);
    }

}