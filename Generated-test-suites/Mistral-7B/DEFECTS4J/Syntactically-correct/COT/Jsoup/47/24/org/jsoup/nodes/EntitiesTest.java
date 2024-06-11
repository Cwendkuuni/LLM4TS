package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue(Entities.isNamedEntity(value));
            assertTrue(Entities.isNamedEntity(String.valueOf(key)));
        }

        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> base = Entities.base;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue(Entities.isBaseNamedEntity(value));
            assertTrue(Entities.isBaseNamedEntity(String.valueOf(key)));
        }

        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            Character result = Entities.getCharacterByName(value);
            assertEquals(key, result);
        }

        assertNull(Entities.getCharacterByName("invalidEntity"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeNullString() {
        Entities.escape(null, Document.OutputSettings.base(), null, false, false, false);
    }

    @Test
    public void testEscapeNormalCase() {
        StringBuilder accumulator = new StringBuilder();
        String input = "This is a test with some & special chars: < > \" ' &";

        Entities.escape(accumulator, input, Document.OutputSettings.base(), false, false, false);
        String expected = "This is a test with some &amp; special chars: &lt; &gt; &quot; &apos; &amp;";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testEscapeAttributeCase() {
        StringBuilder accumulator = new StringBuilder();
        String input = "This is a test with some & special chars: < > \" ' &";

        Entities.escape(accumulator, input, Document.OutputSettings.base(), true, false, false);
        String expected = "This is a test with some &amp;specialchars: < &gt; &quot; &apos; &amp;";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testEscapeNormalizeWhiteSpace() {
        StringBuilder accumulator = new StringBuilder();
        String input = "This is a test\nwith\t some & special chars: < > \" ' &";

        Entities.escape(accumulator, input, Document.OutputSettings.base(), false, true, false);
        String expected = "This is a test\nwith\t some &amp;specialchars: < &gt; &quot; &apos; &amp;";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testEscapeStripLeadingWhiteSpace() {
        StringBuilder accumulator = new StringBuilder();
        String input = "This is a test\nwith\t some & special chars: < > \" ' &";

        Entities.escape(accumulator, input, Document.OutputSettings.base(), false, true, true);
        String expected = "This is a test\nwith some &amp;specialchars: < &gt; &quot; &apos; &amp;";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testUnescapeNullString() {
        String input = null;
        String result = Entities.unescape(input, false);
        assertNull(result);
    }

    @Test
    public void testUnescapeSimpleString() {
        StringBuilder accumulator = new StringBuilder();
        String input = "This is a test with some &amp;specialchars: &lt; &gt; &quot; &apos; &amp;";

        String result = Entities.unescape(input, false);
        String expected = "This is a test with some & special chars: < > \" ' &";
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeStrictString() {
        StringBuilder accumulator = new StringBuilder();
        String input = "This is a test with some &amp;specialchars: &lt;&gt;&quot;&apos;&amp;";

        String result = Entities.unescape(input, true);
        String expected = "This is a test with some & special chars: < > \" ' &";
        assertEquals(expected, result);
    }

    @Test
    public void testCanEncodeASCII() {
        for (char c = 0; c < 128; c++) {
            assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, c, null));
        }
    }

    @Test
    public void testCanEncodeUTF() {
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            assertTrue(Entities.canEncode(Entities.CoreCharset.utf, c, null));
        }
    }

    @Test
    public void testCanEncodeFallback() {
        CharsetEncoder fallback = Character.encodingForName("UTF-8");
        for (char c = Character.MIN_SUPPLEMENTARY_CODE_POINT; c <= Character.MAX_VALUE; c++) {
            if (!Character.isSupplementaryCodePoint(c)) {
                assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, c, fallback));
            }
        }
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> expected = new HashMap<String, Character>(Arrays.asList(
                new AbstractMap.SimpleEntry<>("quot", '\u00022'),
                new AbstractMap.SimpleEntry<>("amp", '\u00026'),
                new AbstractMap.SimpleEntry<>("lt", '\u0003C'),
                new AbstractMap.SimpleEntry<>("gt", '\u0003E'),
                new AbstractMap.SimpleEntry<>("apos", '\u00027'),
                new AbstractMap.SimpleEntry<>("nbsp", '\u00A0'),
                new AbstractMap.SimpleEntry<>("iexcl", '\u00A1'),
                new AbstractMap.SimpleEntry<>("cent", '\u00A2'),
                new AbstractMap.SimpleEntry<>("pound", '\u00A3'),
                new AbstractMap.SimpleEntry<>("curren", '\u00A4'),
                new AbstractMap.SimpleEntry<>("yen", '\u00A5'),
                new AbstractMap.SimpleEntry<>("brvbar", '\u00A6'),
                new AbstractMap.SimpleEntry<>("sect", '\u00A7'),
                new AbstractMap.SimpleEntry<>("uml", '\u00A8'),
                new AbstractMap.SimpleEntry<>("copy", '\u00AA'),
                new AbstractMap.SimpleEntry<>("ordf", '\u00AA'),
                new AbstractMap.SimpleEntry<>("laquo", '\u00AB'),
                new AbstractMap.SimpleEntry<>("not", '\u00AC'),
                new AbstractMap.SimpleEntry<>("shy", '\u00AD'),
                new AbstractMap.SimpleEntry<>("reg", '\u00AE'),
                new AbstractMap.SimpleEntry<>("macr", '\u00AF'),
                new AbstractMap.SimpleEntry<>("deg", '\u00B0'),
                new AbstractMap.SimpleEntry<>("plusmn", '\u00B1'),
                new AbstractMap.SimpleEntry<>("sup2", '\u00B2'),
                new AbstractMap.SimpleEntry<>("sup3", '\u00B3'),
                new AbstractMap.SimpleEntry<>("acute", '\u00B4'),
                new AbstractMap.SimpleEntry<>("micro", '\u00B5'),
                new AbstractMap.SimpleEntry<>("para", '\u00B6'),
                new AbstractMap.SimpleEntry<>("middot", '\u00B7'),
                new AbstractMap.SimpleEntry<>("cedil", '\u00B8'),
                new AbstractMap.SimpleEntry<>("sup1", '\u00B9'),
                new AbstractMap.SimpleEntry<>("ordm", '\u00BA'),
                new AbstractMap.SimpleEntry<>("raquo", '\u00BB'),
                new AbstractMap.SimpleEntry<>("frac14", '\u00BD'),
                new AbstractMap.SimpleEntry<>("frac12", '\u00BE'),
                new AbstractMap.SimpleEntry<>("frac34", '\u00BF'),
                new AbstractMap.SimpleEntry<>("iquest", '\u00BF'),
                new AbstractMap.SimpleEntry<>("Agrave", '\u00C0'),
                new AbstractMap.SimpleEntry<>("Aacute", '\u00C1'),
                new AbstractMap.SimpleEntry<>("Acirc", '\u00C2'),
                new AbstractMap.SimpleEntry<>("Atilde", '\u00C3'),
                new AbstractMap.SimpleEntry<>("Auml", '\u00C4'),
                new AbstractMap.SimpleEntry<>("Aring", '\u00C5'),
                new AbstractMap.SimpleEntry<>("AElig", '\u00C6'),
                new AbstractMap.SimpleEntry<>("Ccedil", '\u00C7'),
                new AbstractMap.SimpleEntry<>("Egrave", '\u00C8'),
                new AbstractMap.SimpleEntry<>("Eacute", '\u00C9'),
                new AbstractMap.SimpleEntry<>("Ecirc", '\u00CA'),
                new AbstractMap.SimpleEntry<>("Euml", '\u00CB'),
                new AbstractMap.SimpleEntry<>("Igrave", '\u00CC'),
                new AbstractMap.SimpleEntry<>("Iacute", '\u00CD'),
                new AbstractMap.SimpleEntry<>("Icirc", '\u00CE'),
                new AbstractMap.SimpleEntry<>("Iuml", '\u00CF'),
                new AbstractMap.SimpleEntry<>("ETH", '\u00D0'),
                new AbstractMap.SimpleEntry<>("Ntilde", '\u00D1'),
                new AbstractMap.SimpleEntry<>("Ograve", '\u00D2'),
                new AbstractMap.SimpleEntry<>("Oacute", '\u00D3'),
                new AbstractMap.SimpleEntry<>("Ocirc", '\u00D4'),
                new AbstractMap.SimpleEntry<>("Otilde", '\u00D5'),
                new AbstractMap.SimpleEntry<>("Ouml", '\u00D6'),
                new AbstractMap.SimpleEntry<>("times", '\u00D7'),
                new AbstractMap.SimpleEntry<>("Oslash", '\u00D8'),
                new AbstractMap.SimpleEntry<>("Ugrave", '\u00D9'),
                new AbstractMap.SimpleEntry<>("Uacute", '\u00DA'),
                new AbstractMap.SimpleEntry<>("Ucirc", '\u00DB'),
                new AbstractMap.SimpleEntry<>("Uuml", '\u00DC'),
                new AbstractMap.SimpleEntry<>("Yacute", '\u00DD'),
                new AbstractMap.SimpleEntry<>("THORN", '\u00DE'),
                new AbstractMap.SimpleEntry<>("szlig", '\u00DF'),
                new AbstractMap.SimpleEntry<>("agrave", '\u00E0'),
                new AbstractMap.SimpleEntry<>("aacute", '\u00E1'),
                new AbstractMap.SimpleEntry<>("acirc", '\u00E2'),
                new AbstractMap.SimpleEntry<>("atilde", '\u00E3'),
                new AbstractMap.SimpleEntry<>("auml", '\u00E4'),
                new AbstractMap.SimpleEntry<>("aring", '\u00E5'),
                new AbstractMap.SimpleEntry<>("aelig", '\u00E6'),
                new AbstractMap.SimpleEntry<>("ccedil", '\u00E7'),
                new AbstractMap.SimpleEntry<>("egrave", '\u00E8'),
                new AbstractMap.SimpleEntry<>("eacute", '\u00E9'),
                new AbstractMap.SimpleEntry<>("ecirc", '\u00EA'),
                new AbstractMap.SimpleEntry<>("euml", '\u00EB'),
                new AbstractMap.SimpleEntry<>("igrave", '\u00EC'),
                new AbstractMap.SimpleEntry<>("iacute", '\u00ED'),
                new AbstractMap.SimpleEntry<>("icirc", '\u00EE'),
                new AbstractMap.SimpleEntry<>("iuml", '\u00EF'),
                new AbstractMap.SimpleEntry<>("eth", '\u00F0'),
                new AbstractMap.SimpleEntry<>("ntilde", '\u00F1'),
                new AbstractMap.SimpleEntry<>("ograve", '\u00F2'),
                new AbstractMap.SimpleEntry<>("oacute", '\u00F3'),
                new AbstractMap.SimpleEntry<>("ocirc", '\u00F4'),
                new AbstractMap.SimpleEntry<>("otilde", '\u00F5'),
                new AbstractMap.SimpleEntry<>("ouml", '\u00F6'),
                new AbstractMap.SimpleEntry<>("divide", '\u00F7'),
                new AbstractMap.SimpleEntry<>("oslash", '\u00F8'),
                new AbstractMap.SimpleEntry<>("ugrave", '\u00F9'),
                new AbstractMap.SimpleEntry<>("uacute", '\u00FA'),
                new AbstractMap.SimpleEntry<>("ucirc", '\u00FB'),
                new AbstractMap.SimpleEntry<>("uuml", '\u00FC'),
                new AbstractMap.SimpleEntry<>("yacute", '\u00FD'),
                new AbstractMap.SimpleEntry<>("thorn", '\u00FE'),
                new AbstractMap.SimpleEntry<>("yuml", '\u00FF')
        ));

        Map<Character, String> actual = Entities.loadEntities("entities-full.properties");
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.keySet(), actual.keySet());
        for (Map.Entry<Character, String> entry : expected.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertEquals(value, String.valueOf(key));
            assertEquals(key, actual.get(value));
        }
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesMissingFile() {
        Map<String, Character> entities = Entities.loadEntities("missing-file.properties");
    }

    @Test
    public void testToCharacterKey() {
        Map<Character, String> full = Entities.full;
        Map<Character, String> expected = new HashMap<Character, String>(full.size());
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            expected.put(key, value);
        }

        Map<Character, String> actual = Entities.toCharacterKey(full);
        assertEquals(expected, actual);
    }
}