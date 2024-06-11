package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> fullMap = entities.getFullMap();

        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.isNamedEntity(value));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseMap = entities.getBaseMap();

        for (Map.Entry<Character, String> entry : baseMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertTrue(entities.isBaseNamedEntity(value));
        }

        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertFalse(entities.isBaseNamedEntity(value));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<Character, String> fullMap = entities.getFullMap();

        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Character result = entities.getCharacterByName(value);
            assertEquals(key, result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityNullName() {
        Entities entities = new Entities();
        entities.isNamedEntity(null);
    }

    @Test
    public void testEscapeMethodBasic() {
        Entities entities = new Entities();
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();

        String input = "&amp;<lt>Hello & World&gt;";

        entities.escape(actual, input, null, false, false, false);

        expected.append("&amp;amp;;&lt;lt&gt;Hello & World&gt;");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEscapeMethodWithOutputSettings() {
        Entities entities = new Entities();
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = new StringBuilder();

        String input = "&amp;<lt>Hello & World&gt;";

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);

        entities.escape(actual, input, outputSettings, false, false, false);

        expected.append("&amp;amp;;&lt; & gt;");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testUnescapeMethodBasic() {
        Entities entities = new Entities();
        String expected = "&amp;<lt>Hello & World&gt;";
        String actual = entities.unescape(entities.escape("Hello & World", null), false);

        assertEquals(expected, actual);
    }

    @Test
    public void testUnescapeMethodWithStrict() {
        Entities entities = new Entities();
        String expected = "&amp;<lt>Hello & World&gt;";
        String actual = entities.unescape(entities.escape("Hello & World", null), true);

        assertEquals(expected, actual);
    }

    @Test
    public void testCanEncodeMethodBasic() {
        Entities entities = new Entities();
        for (CoreCharset charset : CoreCharset.values()) {
            for (char c = 0; c < Character.MAX_VALUE; c++) {
                boolean result = entities.canEncode(charset, c, null);
                if (Character.isLowSurrogate(c) || Character.isHighSurrogate(c)) {
                    // surrogate pairs are handled by canEncode separately
                    assertTrue(result);
                } else {
                    // check only ASCII characters in ascii charset
                    if (charset == CoreCharset.ascii && c > 0x7F) {
                        assertFalse(result);
                    } else {
                        assertTrue(result);
                    }
                }
            }
        }
    }

    @Test
    public void testCanEncodeMethodSurrogatePairs() {
        Entities entities = new Entities();
        for (CoreCharset charset : CoreCharset.values()) {
            for (int i = 0; i < 0xD800 && i <= 0xDFFF; i++) {
                char hi = (char) i;
                for (int j = 0xDC00; j <= 0xDFFF; j++) {
                    char lo = (char) j;
                    char combinedChar = Character.toChars(Character.toIntArray(new char[] { hi, lo })[0]);
                    boolean result = entities.canEncode(charset, combinedChar, null);
                    assertTrue(result);
                }
            }
        }
    }
}