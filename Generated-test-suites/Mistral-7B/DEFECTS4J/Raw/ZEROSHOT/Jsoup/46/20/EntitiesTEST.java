package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> full = Entities.full;

        // Test true cases
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Assert.assertTrue("Named entity " + value + " is not recognized", Entities.isNamedEntity(value));
        }

        // Test false cases - entities not in the full set
        Assert.assertFalse("lt is a named entity", Entities.isNamedEntity("lt"));
        Assert.assertFalse("amp is a named entity", Entities.isNamedEntity("amp"));
        Assert.assertFalse("lt is a base named entity", Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse("amp is a base named entity", Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();

        // Test known named entities
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            Character character = Entities.getCharacterByName(value);
            Assert.assertEquals("Named entity " + value + " does not map to correct character", key, character);
        }

        // Test unknown named entities
        Assert.assertNull("Unknown named entity should return null", Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> base = Entities.base;

        // Test true cases
        for (Object[] entity : Entities.xhtmlArray) {
            Character key = Character.valueOf((char) ((Integer) entity[1]).intValue());
            String value = (String) entity[0];
            Assert.assertTrue("Named entity " + value + " is not in base set", Entities.isBaseNamedEntity(value));
        }

        // Test false cases - entities not in the base set
        Character nonBaseEntity = Character.valueOf('©');
        Assert.assertFalse("Non-base named entity is in base set", Entities.isBaseNamedEntity("copyright"));
        Assert.assertFalse("Non-base named entity is in base set", Entities.isBaseNamedEntity(nonBaseEntity.toString()));
    }

    @Test
    public void testEscapeAndUnescape() {
        Entities entities = new Entities();
        StringBuilder input = new StringBuilder();
        StringBuilder expectedOutput = new StringBuilder();

        // Test basic entities
        input.append("&amp;lt; &quot; &apos; &gt; &amp;quot; &amp;apos;");
        expectedOutput.append("&amp;lt; &quot; &apos; &gt; &quot; &apos;");
        entities.escape(expectedOutput, input, null, false, false, false);
        Assert.assertEquals("Input not escaped correctly", expectedOutput.toString(), entities.escape(input.toString(), null));

        input.setLength(0);
        expectedOutput.setLength(0);
        input.append(entities.escape(expectedOutput, "&amp;lt; &quot; &apos; &gt; &amp;quot; &amp;apos;", null));
        Assert.assertEquals("Output not identical to input after escaping", input.toString(), expectedOutput.toString());

        expectedOutput.setLength(0);
        entities.unescape(expectedOutput, input.toString());
        Assert.assertEquals("Input not unescaped correctly", expectedOutput.toString(), "&amp;lt; &quot; &apos; &gt; &amp;quot; &amp;apos;");

        // Test surrogate pairs
        input.setLength(0);
        char highSurrogate = '\uD800';
        char lowSurrogate = '\uDC00';
        expectedOutput.append(highSurrogate).append(lowSurrogate);
        entities.escape(expectedOutput, input, null, false, false, false);
        Assert.assertEquals("Input not escaped correctly (surrogate pair)", expectedOutput.toString(), entities.escape(input.append(highSurrogate).append(lowSurrogate).toString(), null));

        // Test non-ASCII characters
        input.setLength(0);
        String nonAsciiString = "Müeller";
        expectedOutput.append(nonAsciiString);
        entities.escape(expectedOutput, input, null, false, false, false);
        Assert.assertEquals("Input not escaped correctly (non-ASCII)", expectedOutput.toString(), entities.escape(input.append(nonAsciiString).toString(), null));

        // Test different encoders
        input.setLength(0);
        String utf8String = "Müeller";
        expectedOutput.append(utf8String);
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEncoder(encoder);
        entities.escape(expectedOutput, input, outputSettings, false, false, false);
        Assert.assertEquals("Input not escaped correctly (UTF-8)", expectedOutput.toString(), entities.escape(input.append(utf8String).toString(), outputSettings));
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.utf;

        // Test ASCII characters
        for (char c = 0; c < 128; c++) {
            Assert.assertTrue("Character " + (char) c + " can be encoded with UTF-8 encoder", entities.canEncode(coreCharset, (char) c, entities.getCharacterEncoder()));
        }

        // Test non-ASCII characters
        char nonAsciiCharacter = '\uD800';
        Assert.assertFalse("Character " + nonAsciiCharacter + " cannot be encoded with UTF-8 encoder", entities.canEncode(coreCharset, nonAsciiCharacter, entities.getCharacterEncoder()));
    }

    @Test
    public void testLoadEntities() {
        Properties properties = new Properties();
        Map<Character, String> entities = new HashMap<>();

        try {
            InputStream in = Entities.class.getResourceAsStream("/entities-full.properties");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Error loading entities resource", e);
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Character character = null;
            if (key instanceof String) {
                character = Character.valueOf((String) key);
            } else if (key instanceof Character) {
                character = (Character) key;
            }

            if (character == null) {
                throw new RuntimeException("Invalid key type in entities file");
            }

            String entityName = value.toString();
            entities.put(character, entityName);
        }

        Map<Character, String> expectedEntities = Entities.full;
        Assert.assertEquals("Entities not loaded correctly", expectedEntities, entities);
    }

    private CharsetEncoder getCharacterEncoder() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        return encoder;
    }
}