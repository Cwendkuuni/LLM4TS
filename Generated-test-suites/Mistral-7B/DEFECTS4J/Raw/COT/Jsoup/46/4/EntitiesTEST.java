package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.jsoup.nodes.Entities.base;
import static org.jsoup.nodes.Entities.extended;
import static org.jsoup.nodes.Entities.xhtml;

public class EntitiesTest {
    

    // Test all public methods of Entities class

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isNamedEntity(value);
            Assert.assertEquals("isNamedEntity test failed for character: " + key, expected, actual);
        }

        // Test with invalid input
        String invalidInput = "invalid_input";
        boolean actual = Entities.isNamedEntity(invalidInput);
        Assert.assertFalse("isNamedEntity test failed for invalid input: " + invalidInput, actual);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseMap = Entities.base;
        for (Map.Entry<Character, String> entry : baseMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isBaseNamedEntity(value);
            Assert.assertEquals("isBaseNamedEntity test failed for character: " + key, expected, actual);
        }

        // Test with invalid input
        String invalidInput = "invalid_input";
        boolean actual = Entities.isBaseNamedEntity(invalidInput);
        Assert.assertFalse("isBaseNamedEntity test failed for invalid input: " + invalidInput, actual);

        // Test with a character not in base set
        Character notInBase = '\u0003'; // ASCII "!" character, not in base set
        boolean actual = Entities.isBaseNamedEntity(Character.toString(notInBase));
        Assert.assertFalse("isBaseNamedEntity test failed for character not in base set: " + notInBase, actual);
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Character expected = key;
            Character actual = Entities.getCharacterByName(value);
            Assert.assertEquals("getCharacterByName test failed for character: " + key, expected, actual);
        }

        // Test with invalid input
        String invalidInput = "invalid_input";
        Character actual = Entities.getCharacterByName(invalidInput);
        Assert.assertNull("getCharacterByName test failed for invalid input: " + invalidInput, actual);
    }

    @Test
    public void testEscape() {
        String string = "This is a test string with special characters: &<>()\"';`";
        StringBuilder expected = new StringBuilder();
        Entities.escape(expected, string, null, false, false, false); // Calling the method with default arguments

        String actual = Entities.escape(string, null);
        Assert.assertEquals("escape test failed", expected.toString(), actual);

        // Test with different escape modes
        Document.OutputSettings outputSettingsXhtml = new Document.OutputSettings();
        outputSettingsXhtml.escapeMode(Entities.EscapeMode.xhtml);
        StringBuilder expectedXhtml = new StringBuilder();
        Entities.escape(expectedXhtml, string, outputSettingsXhtml, false, false, false);
        String actualXhtml = Entities.escape(string, outputSettingsXhtml);
        Assert.assertEquals("escape test (xhtml mode) failed", expectedXhtml.toString(), actualXhtml);

        Document.OutputSettings outputSettingsBase = new Document.OutputSettings();
        outputSettingsBase.escapeMode(Entities.EscapeMode.base);
        StringBuilder expectedBase = new StringBuilder();
        Entities.escape(expectedBase, string, outputSettingsBase, false, false, false);
        String actualBase = Entities.escape(string, outputSettingsBase);
        Assert.assertEquals("escape test (base mode) failed", expectedBase.toString(), actualBase);

        Document.OutputSettings outputSettingsExtended = new Document.OutputSettings();
        outputSettingsExtended.escapeMode(Entities.EscapeMode.extended);
        StringBuilder expectedExtended = new StringBuilder();
        Entities.escape(expectedExtended, string, outputSettingsExtended, false, false, false);
        String actualExtended = Entities.escape(string, outputSettingsExtended);
        Assert.assertEquals("escape test (extended mode) failed", expectedExtended.toString(), actualExtended);

        // Test with different input strings
        String stringWithSurrogatePair = "\uD800\uDC00";
        StringBuilder expectedSurrogatePair = new StringBuilder();
        Entities.escape(expectedSurrogatePair, stringWithSurrogatePair, null, false, false, false);
        String actualSurrogatePair = Entities.escape(stringWithSurrogatePair, null);
        Assert.assertEquals("escape test (surrogate pair) failed", expectedSurrogatePair.toString(), actualSurrogatePair);
    }

    @Test
    public void testUnescape() {
        String stringWithEntities = "This is a test &string& with &special entities: &lt; &gt; &amp; &apos; &quot;";
        String expected = "This is a test string with special characters: < > & ' \"";
        String actual = Entities.unescape(stringWithEntities, false);
        Assert.assertEquals("unescape test failed", expected, actual);

        // Test with strict mode
        String actualStrict = Entities.unescape(stringWithEntities, true);
        Assert.assertEquals("unescape test (strict mode) failed", expected, actualStrict);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullInput() {
        Entities.unescape(null, false);
    }

    @Test
    public void testCanEncode() {
        CoreCharset ascii = CoreCharset.ascii;
        CharsetEncoder fallback = CharsetEncoder.forName("UTF-8");

        for (char c = 0; c < 255; c++) {
            boolean expected = c < 0x80; // Ascii character
            boolean actual = Entities.canEncode(ascii, c, fallback);
            Assert.assertEquals("canEncode test failed for character: " + c, expected, actual);
        }

        // Test with non-ascii character
        char nonAscii = '\u0003'; // ASCII "!" character
        boolean actual = Entities.canEncode(ascii, nonAscii, fallback);
        Assert.assertFalse("canEncode test failed for non-ascii character: " + nonAscii, actual);

        // Test with UTF character
        CharsetEncoder utfEncoder = CharsetEncoder.forName("UTF-16");
        char utfCharacter = '\uD800'; // High surrogate
        char utfCharacter2 = '\uDC00'; // Low surrogate
        boolean actualUtf = Entities.canEncode(CoreCharset.utf, utfCharacter, utfEncoder);
        Assert.assertTrue("canEncode test failed for UTF character: " + utfCharacter, actualUtf);
        boolean actualUtf2 = Entities.canEncode(CoreCharset.utf, utfCharacter2, utfEncoder);
        Assert.assertTrue("canEncode test failed for UTF character: " + utfCharacter2, actualUtf2);
        boolean actualUtfSurrogatePair = Entities.canEncode(CoreCharset.utf, utfCharacter, utfEncoder) &&
                Entities.canEncode(CoreCharset.utf, utfCharacter2, utfEncoder);
        Assert.assertTrue("canEncode test failed for UTF character surrogate pair", actualUtfSurrogatePair);
    }

    
}