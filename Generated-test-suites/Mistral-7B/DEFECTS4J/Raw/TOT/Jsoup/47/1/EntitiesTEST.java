package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.full;
        Character lt = Entities.getCharacterByName("lt");

        // Test known named entities
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertEquals(lt, Entities.getCharacterByName("lt"));

        // Test unknown named entities
        Assert.assertFalse(Entities.isNamedEntity("rt")); // right angle bracket is not a valid named entity
        Assert.assertNull(Entities.getCharacterByName("rt"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.baseByVal;
        Character amp = Entities.getCharacterByName("amp");

        // Test known base named entities
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertEquals(amp, Entities.getCharacterByName("amp"));

        // Test unknown base named entities
        Assert.assertFalse(Entities.isBaseNamedEntity("rt")); // right angle bracket is not a valid base named entity
        Assert.assertNull(Entities.getCharacterByName("rt"));
    }

    @Test
    public void testEscapeAndUnescape() throws IOException {
        String input = "&amp;lt;xml version=\"1.0\" encoding=\"UTF-8\"?&gt;";
        String expectedOutput = "&amp;amp;lt;xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;";

        String escapedOutput = Entities.escape(input, Document.OutputSettings.xml());
        String unescapedOutput = Entities.unescape(escapedOutput);

        // Test escape and unescape functions
        Assert.assertEquals(expectedOutput, escapedOutput);
        Assert.assertEquals(input, unescapedOutput);
    }

    @Test
    public void testEscapeNonAscii() throws IOException {
        String input = "こんにちは世界";
        String expectedOutput = "&#x3042;&#x3093;&#x3055;&#x3081;&#x3044;&#x308A;&#x304B;&#x308B;&#x304E;&#x3087;&#x3042;&#x308B;&#x3066;&#x304B;&#x3083;&#x3088;&#x3046;&#x306A;&#x3044;&#x3052;&#x308A;&#x3046;&#x308B;&#x304B;&#x3083;&#x3089;&#x3046;";

        String escapedOutput = Entities.escape(input, Document.OutputSettings.xml());
        String unescapedOutput = Entities.unescape(escapedOutput);

        // Test escape and unescape functions for non-ASCII characters
        Assert.assertEquals(expectedOutput, escapedOutput);
        Assert.assertEquals(input, unescapedOutput);
    }

    @Test
    public void testCanEncode() {
        // Test encoding of common characters
        Assert.assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
        Assert.assertTrue(Entities.canEncode(CoreCharset.utf, 'A', null));
        Assert.assertTrue(Entities.canEncode(CoreCharset.utf, 'é', null));

        // Test encoding of invalid characters
        Assert.assertFalse(Entities.canEncode(CoreCharset.ascii, '\uD800', null));
        Assert.assertFalse(Entities.canEncode(CoreCharset.utf, '\uD800', null));
    }

    @Test
    public void testLoadEntities() throws IOException {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Character amp = Character.valueOf('&');

        // Test loading entities from properties file
        Assert.assertEquals(Entities.getCharacterByName("amp"), entities.get("amp"));
        Assert.assertEquals(amp, entities.get("&"));
    }
}