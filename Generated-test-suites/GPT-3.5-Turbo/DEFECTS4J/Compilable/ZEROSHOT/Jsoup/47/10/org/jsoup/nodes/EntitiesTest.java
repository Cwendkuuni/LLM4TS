package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertTrue(result);

        result = Entities.isNamedEntity("gt");
        assertTrue(result);

        result = Entities.isNamedEntity("amp");
        assertTrue(result);

        result = Entities.isNamedEntity("quot");
        assertTrue(result);

        result = Entities.isNamedEntity("invalid");
        assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        assertTrue(result);

        result = Entities.isBaseNamedEntity("gt");
        assertTrue(result);

        result = Entities.isBaseNamedEntity("amp");
        assertTrue(result);

        result = Entities.isBaseNamedEntity("quot");
        assertTrue(result);

        result = Entities.isBaseNamedEntity("invalid");
        assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        assertEquals('<', result.charValue());

        result = Entities.getCharacterByName("gt");
        assertEquals('>', result.charValue());

        result = Entities.getCharacterByName("amp");
        assertEquals('&', result.charValue());

        result = Entities.getCharacterByName("quot");
        assertEquals('"', result.charValue());

        result = Entities.getCharacterByName("invalid");
        assertNull(result);
    }

    @Test
    public void testEscape() {
        String input = "& < > \"";
        String result = Entities.escape(input, null);
        assertEquals("&amp; &lt; &gt; &quot;", result);

        input = "hello world";
        result = Entities.escape(input, null);
        assertEquals("hello world", result);
    }

    @Test
    public void testUnescape() {
        String input = "&amp; &lt; &gt; &quot;";
        String result = Entities.unescape(input);
        assertEquals("& < > \"", result);

        input = "&invalid;";
        result = Entities.unescape(input);
        assertEquals("&invalid;", result);
    }
}