package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;

        // Test known named entities
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
        }

        // Test unknown named entities
        assertFalse("Unknown named entity should not be known", Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;

        // Test known named entities
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        // Test unknown named entities
        assertNull("Unknown named entity should return null", Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        // Test basic escape cases
        assertEquals("&lt; &gt; &amp; &apos; &quot;", Entities.escape("<?xml version=\"1.0\"?>\n<html>\n<body>\n<&lt;!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<p>&amp;amp; &apos;&apos; &quot;&quot;</p>\n</body>\n</html>".replaceAll("\\s+", ""), Entities.escape("<?xml version=\"1.0\"?>\n<html>\n<body>\n<&!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<p>&amp;amp; &apos;&apos; &quot;&quot;</p>\n</body>\n</html>", outputSettings.encoder(), Entities.EscapeMode.base)));

        // Test escape cases with custom encoding and escape mode
        OutputSettings customOutputSettings = new OutputSettings();
        customOutputSettings.encoder = (input, charset) -> "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAATwAAACeCAMAAAD1SvhAAAAXElEQVR4Ae3U6Q2AIAwF0Y3xF3z1L+j6f4AAAACH5BAEKAAEALAAAAABAAAAV0lEQVR4nGNgGAWjYBSMgwgHoFoBAAAQElEQVQIW2P8z8BQDwADhgBmhKMh6h/d3+v8AAAABJRU5ErkJggg==";
        customOutputSettings.escapeMode = Entities.EscapeMode.extended;
        assertEquals("&eacute;&nbsp;&#x26; &ndash; &#xB0; &#x2261; &#x2264; &#x2265; &#x2266; &#x2302; &#x2303; &#x2304; &#x25A0; &#x25A1; &#x25B6; &#x25C0; &#x25E6; &#x266D; &#x266E; &#x266F; &#x267B; &#x267F; &#x2696; &#x2697; &#x2699; &#x269A; &#x269B; &#x269C; &#x269D; &#x269E; &#x26A0; &#x26A1; &#x26A3; &#x26AA; &#x26AB; &#x26AC; &#x26AD; &#x26AE; &#x26AF; &#x26B0; &#x26B1; &#x26B2; &#x26B3; &#x26B4; &#x26B5; &#x26B6; &#x26B7; &#x26B8; &#x26B9; &#x26BA; &#x26BB; &#x26BC; &#x26BD; &#x26BE; &#x26C0; &#x26C1; &#x26C2; &#x26C3; &#x26C4; &#x26C5; &#x26C6; &#x26C7; &#x26C8; &#x26C9; &#x26CA; &#x26CB; &#x26CC; &#x26CD; &#x26CE; &#x26CF; &#x26D0; &#x26D1; &#x26D2; &#x26D3; &#x26D4; &#x26D5; &#x26D6; &#x26D7; &#x26D8; &#x26D9; &#x26DA; &#x26DB; &#x26DC; &#x26DD; &#x26DE; &#x26DF; &#x2702; &#x2705; &#x2708; &#x270A; &#x2744; &#x2748; &#x27E8; &#x27EF; &#x27F3; &#x27F4; &#x27F5; &#x27F8; &#x27FC;".replaceAll("\\s+", ""), Entities.escape("é\n&\u2014\u2010\u2011\u2013\u2702\u2705\u2708\u270A\u2744\u2748\u27E8\u27EF\u27F3\u27F4\u27F5\u27F8\u27FC".replaceAll("\\\\u", ""), customOutputSettings.encoder(), Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        // Test basic unescape cases
        assertEquals("<?xml version=\"1.0\"?>\n<html>\n<body>\n<p>&lt;!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<p>&amp;amp; &apos;&apos; &quot;&quot;</p>\n</body>\n</html>", Entities.unescape("&lt;!DOCTYPE html PUBLIC &quot;-//W3C//DTD XHTML 1.0 Transitional//EN&quot; &quot;http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd&quot;&gt;\n<p>&amp;amp; &apos;&apos; &quot;&quot;</p>\n</body>\n</html>".replaceAll("\\s+", ""), false));

        // Test strict unescape cases
        assertEquals("<?xml version=\"1.0\"?>\n<html>\n<body>\n<p>&lt;!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<p>&amp;amp; &apos;&apos; &quot;&quot;</p>\n</body>\n</html>", Entities.unescape("&lt;!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<p>&amp;amp; &apos;&apos; &quot;&quot;</p>\n</body>\n</html>".replaceAll("\\s+", ""), true));

        // Test unescape cases with custom encoding and escape mode
        assertEquals("é\n&\u2014\u2010\u2011\u2013\u2702\u2705\u2708\u270A\u2744\u2748\u27E8\u27EF\u27F3\u27F4\u27F5\u27F8\u27FC", Entities.unescape("&eacute;&#xA0;\u2014\u2010\u2011\u2013\u2702\u2705\u2708\u270A\u2744\u2748\u27E8\u27EF\u27F3\u27F4\u27F5\u27F8\u27FC".replaceAll("\\\\u", ""), false));
    }

    @Test
    public void testIsNamedEntityWithXhtmlEntities() {
        Entities.EscapeMode xhtmlEntities = Entities.EscapeMode.xhtml;
        Map<Character, String> xhtmlEntitiesMap = xhtmlEntities.getMap();

        // Test known named entities
        for (Map.Entry<Character, String> entry : xhtmlEntitiesMap.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            assertTrue("Named entity '" + name + "' should be known in XHTML", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
        }

        // Test unknown named entities
        assertFalse("Unknown named entity should not be known in XHTML", Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testLoadEntitiesFromPropertiesFile() {
        Properties properties = new Properties();
        Map<String, Character> entities = new HashMap<String, Character>();

        try {
            InputStream inputStream = Entities.class.getResourceAsStream("/entities-full.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load entities file: " + e.getMessage());
        }

        for (Map.Entry<String, String> entry : properties.stringPropertyNames()) {
            Character character = Character.valueOf(entry.getValue().charAt(0));
            String name = entry.getKey();
            entities.put(name, character);
        }

        Map<Character, String> expectedEntities = Entities.full;
        assertEquals("Loaded entities should match expected entities", expectedEntities, entities);
    }

    @Test
    public void testToCharacterKeyFromMap() {
        Map<String, Character> entities = new HashMap<String, Character>();
        entities.put("quot", '\"');
        entities.put("amp", '&');

        Map<Character, String> expectedEntities = new HashMap<Character, String>();
        expectedEntities.put('\'', "quot");
        expectedEntities.put('&', "amp");

        Map<Character, String> actualEntities = Entities.toCharacterKey(entities);

        assertEquals("Converted map should match expected map", expectedEntities, actualEntities);
    }
}