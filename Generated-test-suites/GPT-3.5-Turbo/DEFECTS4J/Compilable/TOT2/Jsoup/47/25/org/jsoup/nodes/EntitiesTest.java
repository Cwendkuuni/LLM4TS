package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String input = "This <tag> & \"quote\"";
        assertEquals("This &lt;tag&gt; &amp; &quot;quote&quot;", Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String input = "This &lt;tag&gt; &amp; &quot;quote&quot;";
        assertEquals("This <tag> & \"quote\"", Entities.unescape(input));
    }

    // Edge case: test for empty string escape
    @Test
    public void testEscapeEmptyString() {
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
    }

    // Exception case: test for unescaping with strict mode and missing trailing semicolon
    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeStrictMode() {
        Entities.unescape("This &lt;tag&gt; &amp; &quot;quote&quot");
    }

    // Exception case: test for unknown named entity
    @Test
    public void testUnknownNamedEntity() {
        assertNull(Entities.getCharacterByName("unknown"));
    }
}