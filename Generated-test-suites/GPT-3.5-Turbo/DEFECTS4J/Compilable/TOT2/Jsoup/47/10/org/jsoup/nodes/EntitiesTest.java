package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<div>Hello World!</div>", new Document.OutputSettings());
        assertEquals("&lt;div&gt;Hello World!&lt;/div&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;div&gt;Hello World!&lt;/div&gt;");
        assertEquals("<div>Hello World!</div>", unescaped);
    }

    @Test
    public void testEscape_edgeCases() {
        String escaped = Entities.escape("<>&\"'\n\t", new Document.OutputSettings());
        assertEquals("&lt;&gt;&amp;&quot;&#39;\n\t", escaped);
    }

    @Test
    public void testUnescape_edgeCases() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;&quot;&#39;&#xA;&#x9;");
        assertEquals("<>&\"' \n\t", unescaped);
    }

}