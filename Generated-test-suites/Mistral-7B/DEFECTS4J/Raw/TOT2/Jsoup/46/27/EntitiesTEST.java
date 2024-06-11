import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        String entityName = "lt";
        assertTrue(Entities.isNamedEntity(entityName));
        assertEquals(Entities.getCharacterByName(entityName), '<');
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        String entityName = "amp";
        assertTrue(Entities.isBaseNamedEntity(entityName));
        assertEquals(Entities.getCharacterByName(entityName), '&');
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        String entityName = "quot";
        assertEquals(Entities.getCharacterByName(entityName), '\'');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithInvalidName() {
        String invalidEntityName = "invalidEntityName";
        assertFalse(Entities.isNamedEntity(invalidEntityName));
    }

    @Test
    public void testEscapeMethodBasicUsage() {
        String input = "Hello & World <Tag>";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expectedOutput = "Hello &amp; World &lt;Tag&gt;";
        assertEquals(Entities.escape(input, outputSettings), expectedOutput);
    }

    @Test
    public void testEscapeMethodEdgeCases() {
        String input = "\u00A0< >\"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expectedOutput = "&#x00A0; &amp; &lt; &gt; &quot;";
        assertEquals(Entities.escape(input, outputSettings), expectedOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String nullInput = null;
        Entities.escape(nullInput, outputSettings);
    }
}