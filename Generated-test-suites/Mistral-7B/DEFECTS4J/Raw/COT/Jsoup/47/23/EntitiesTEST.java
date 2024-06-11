package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }

        String notNamedEntity = "random";
        assertFalse(Entities.isBaseNamedEntity(notNamedEntity));
        assertFalse(Entities.isNamedEntity(notNamedEntity));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        Map<String, Character> base = Entities.base;

        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }

        String notNamedEntity = "random";
        assertNull(Entities.getCharacterByName(notNamedEntity));
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        String nullName = null;
        Entities.getCharacterByName(nullName);
    }

    @Test
    public void testEscapeMethods() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder expected = new StringBuilder();

        String source = "&amp;<lt>hello&gt; world &nbsp; with &quot;quotes&quot;";
        String escaped = Entities.escape(source, out);

        assertEquals("<amp;&lt;hello&gt; world &nbsp; with &quot;quotes&quot;>", escaped);

        expected.append("&amp;lt;").append(Entities.getCharacterByName("lt")).append("&amp;gt;").append("&nbsp;")
                .append("&quot;quotes&quot;");
        assertEquals(expected.toString(), escaped);
    }

    @Test
    public void testUnescapeMethods() {
        String source = "&amp;lt;hello&gt; world &nbsp; with &quot;quotes&quot;";
        String unescaped = Entities.unescape(source);

        assertEquals("<hello>world with quotes>", unescaped);
    }
}