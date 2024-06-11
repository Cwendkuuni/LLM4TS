package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue().toString();
            Assert.assertTrue(Entities.isNamedEntity(name));
            Assert.assertEquals(key, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> base = Entities.base;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue().toString();
            Assert.assertTrue(Entities.isBaseNamedEntity(name));
            Assert.assertEquals(key, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue().toString();
            Assert.assertEquals(key, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testEscapeMethodWithDefaultArguments() {
        StringBuilder accumulator = new StringBuilder();
        String stringToEscape = "Hello <World> & this is a test";
        Entities.escape(accumulator, stringToEscape, null, false, false, false);
        String escapedString = accumulator.toString();
        String expected = "Hello &lt;World&gt; &amp; this is a test";
        Assert.assertEquals(expected, escapedString);
    }

    @Test
    public void testEscapeMethodWithInAttributeSetToTrue() {
        StringBuilder accumulator = new StringBuilder();
        String stringToEscape = "Hello <World> & this is a test";
        Entities.escape(accumulator, stringToEscape, null, true, false, false);
        String escapedString = accumulator.toString();
        String expected = "Hello <World> &amp; this is a test";
        Assert.assertEquals(expected, escapedString);
    }

    @Test
    public void testEscapeMethodWithNormaliseWhiteSpaceToTrue() {
        StringBuilder accumulator = new StringBuilder();
        String stringToEscape = "Hello \t<World> & this is a test\n";
        Entities.escape(accumulator, stringToEscape, null, false, true, false);
        String escapedString = accumulator.toString();
        String expected = "Hello \t &nbsp;\t<World> &amp; this is a test\n";
        Assert.assertEquals(expected, escapedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullArg() {
        StringBuilder accumulator = new StringBuilder();
        String stringToEscape = null;
        Entities.escape(accumulator, stringToEscape, null, false, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullArgumentInDocumentOutputSettings() {
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings outputSettings = null;
        String stringToEscape = "Hello <World> & this is a test";
        Entities.escape(accumulator, stringToEscape, outputSettings, false, false, false);
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeMethodWithNullDocumentOutputSettingsMap() {
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(null);
        String stringToEscape = "Hello <World> & this is a test";
        Entities.escape(accumulator, stringToEscape, outputSettings, false, false, false);
    }

    @Test
    public void testCanEncodeWithAsciiCharset() {
        CoreCharset ascii = CoreCharset.ascii;
        char c = 'A';
        boolean expected = Entities.canEncode(ascii, c, null);
        Assert.assertTrue(expected);
    }

    @Test
    public void testCanEncodeWithUtfCharset() {
        CoreCharset utf = CoreCharset.utf;
        char c = '𠮷';
        boolean expected = Entities.canEncode(utf, c, null);
        Assert.assertTrue(expected);
    }

    @Test
    public void testCanEncodeWithFallbackCharset() {
        CoreCharset fallback = CoreCharset.fallback;
        char c = '€';
        boolean expected = Entities.canEncode(fallback, c, null);
        Assert.assertTrue(expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithNullCharset() {
        CoreCharset charset = null;
        char c = 'A';
        boolean expected = Entities.canEncode(charset, c, null);
        Assert.assertFalse(expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithNullCharacter() {
        CoreCharset charset = CoreCharset.ascii;
        char c = '\u0000';
        boolean expected = Entities.canEncode(charset, c, null);
        Assert.assertFalse(expected);
    }
}