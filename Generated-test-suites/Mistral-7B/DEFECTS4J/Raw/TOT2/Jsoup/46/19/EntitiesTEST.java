import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_ValidNamedEntity() {
        String entityName = "lt";
        boolean result = Entities.isNamedEntity(entityName);
        assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_InvalidNamedEntity() {
        String entityName = "invalidEntity";
        boolean result = Entities.isNamedEntity(entityName);
        assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity_ValidBaseNamedEntity() {
        String entityName = "amp";
        boolean result = Entities.isBaseNamedEntity(entityName);
        assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity_InvalidBaseNamedEntity() {
        String entityName = "invalidEntity";
        boolean result = Entities.isBaseNamedEntity(entityName);
        assertFalse(result);
    }

    @Test
    public void testGetCharacterByName_ValidNamedEntity() {
        String entityName = "lt";
        Character result = Entities.getCharacterByName(entityName);
        assertEquals('<', result.charValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByName_InvalidNamedEntity() {
        String entityName = "invalidEntity";
        Entities.getCharacterByName(entityName);
    }

    @Test
    public void testEscape_ValidInput() {
        String input = "Hello & World!  <Title> My Website </Title>";
        String expectedOutput = "Hello &amp; World!  &lt;Title&gt; My Website &lt;/Title&gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscape_EmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscape_NullInput() {
        String input = null;
        Entities.escape(input, null);
    }

    @Test
    public void testUnescape_ValidInput() {
        String input = "Hello &amp; World!  &lt;Title&gt; My Website &lt;/Title&gt;";
        String expectedOutput = "Hello & World!  <Title> My Website </Title>";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescape_InvalidInput() {
        String input = "Hello & && World!  &<Title&gt; My Website &<Title&gt;";
        Entities.unescape(input, false);
    }

    @Test
    public void testCanEncode_ASCIICharacter() {
        CoreCharset charset = CoreCharset.ascii;
        char c = 'A';
        boolean result = Entities.canEncode(charset, c, null);
        assertTrue(result);
    }

    @Test
    public void testCanEncode_NonASCIICharacter() {
        CoreCharset charset = CoreCharset.utf;
        char c = 'é';
        boolean result = Entities.canEncode(charset, c, null);
        assertTrue(result);
    }

    @Test
    public void testCanEncode_InvalidCharacter() {
        CoreCharset charset = CoreCharset.ascii;
        char c = '\uD800';
        boolean result = Entities.canEncode(charset, c, null);
        assertFalse(result);
    }

    @Test
    public void testLoadEntities_ValidPropertiesFile() {
        Map<String, Character> entities = new HashMap<>();
        String filename = "entities-full.properties";
        entities = Entities.loadEntities(filename);
        Character result = entities.get("quot");
        assertEquals('"', result.charValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadEntities_InvalidPropertiesFile() {
        Map<String, Character> entities = new HashMap<>();
        String filename = "invalidFile.properties";
        Entities.loadEntities(filename);
    }

    @Test
    public void testToCharacterKey_ValidMap() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("quot", ' ');
        inMap.put("amp", '&');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        Character result = outMap.get(' ');
        assertEquals("quot", result.toString());
    }

    @Test
    public void testToCharacterKey_EmptyMap() {
        Map<String, Character> inMap = new HashMap<>();
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertTrue(outMap.isEmpty());
    }
}