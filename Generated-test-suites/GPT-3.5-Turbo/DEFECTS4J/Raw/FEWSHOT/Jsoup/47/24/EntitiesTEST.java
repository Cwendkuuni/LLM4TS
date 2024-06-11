package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("blah"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("blah"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(null, Entities.getCharacterByName("blah"));
    }

    @Test
    public void testEscape() {
        assertEquals("&amp; &lt; &gt; &quot;", Entities.escape("& < > \"", new Document.OutputSettings()));
        assertEquals("spam&nbsp;jam", Entities.escape("spam\u00A0jam", new Document.OutputSettings()));
        assertEquals("&#xA0;", Entities.escape("spam\u00A0jam", new Document.OutputSettings(EscapeMode.xhtml)));
    }

    @Test
    public void testEscapeStringBuilder() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "& < > \"", new Document.OutputSettings(), false, false, false);
        assertEquals("&amp; &lt; &gt; &quot;", accum.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("& < > \"", Entities.unescape("&amp; &lt; &gt; &quot;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("& < > \" &amp;", Entities.unescape("&amp; &lt; &gt; &quot; &amp;", true));
    }
}