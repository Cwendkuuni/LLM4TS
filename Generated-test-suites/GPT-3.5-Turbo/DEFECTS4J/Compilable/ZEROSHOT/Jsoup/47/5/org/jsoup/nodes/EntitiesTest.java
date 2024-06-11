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
        Character character = Entities.getCharacterByName("lt");
        assertEquals(Character.valueOf('<'), character);

        character = Entities.getCharacterByName("gt");
        assertEquals(Character.valueOf('>'), character);

        character = Entities.getCharacterByName("amp");
        assertEquals(Character.valueOf('&'), character);

        character = Entities.getCharacterByName("quot");
        assertEquals(Character.valueOf('"'), character);

        character = Entities.getCharacterByName("invalid");
        assertNull(character);
    }

    @Test
    public void testEscape() {
        String input = "&lt;div&gt;Hello &amp; World&quot;</div>";
        String expectedOutput = "&amp;lt;div&amp;gt;Hello &amp;amp; World&amp;quot;&lt;/div&gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;div&amp;gt;Hello &amp;amp; World&amp;quot;&lt;/div&gt;";
        String expectedOutput = "&lt;div&gt;Hello &amp; World&quot;</div>";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }
}