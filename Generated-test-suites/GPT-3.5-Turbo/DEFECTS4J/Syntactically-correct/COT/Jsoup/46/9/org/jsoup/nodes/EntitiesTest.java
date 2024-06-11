package org.jsoup.nodes;

import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        String string = "<div>Sample Text</div>";
        String escaped = Entities.escape(string, new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("&lt;div&gt;Sample Text&lt;/div&gt;", escaped);
    }

    @Test
    public void testEscapeWithAttribute() {
        String string = "\"Sample Text\"";
        String escaped = Entities.escape(string, new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("&quot;Sample Text&quot;", escaped);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;Sample Text&lt;/div&gt;";
        String unescaped = Entities.unescape(string);
        assertEquals("<div>Sample Text</div>", unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String string = "<div>Sample Text</div>";
        String unescaped = Entities.unescape(string, true);
        assertEquals("<div>Sample Text</div>", unescaped);
    }

    @Test
    public void testUnescapeNonStrict() {
        String string = "<div>Sample Text</div>";
        String unescaped = Entities.unescape(string, false);
        assertEquals("<div>Sample Text</div>", unescaped);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Character.valueOf('<'), entities.get("lt"));
        assertEquals(Character.valueOf('>'), entities.get("gt"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<String, Character>();
        inMap.put("lt", '<');
        inMap.put("gt", '>');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertEquals("lt", outMap.get('<'));
        assertEquals("gt", outMap.get('>'));
    }
}