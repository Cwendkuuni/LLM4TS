package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

    private static Entities entities;
    private static Document.OutputSettings outputSettings;

    @BeforeClass
    public static void setup() {
        entities = new Entities();
        outputSettings = new Document.OutputSettings();
    }

    /**
     * Test isNamedEntity method.
     */
    @Test
    public void testIsNamedEntity() {
        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            for (Map<Character, String> map : new Map[]{Entities.full, Entities.base, Entities.xhtmlByVal}) {
                for (Map.Entry<Character, String> entry : map.entrySet()) {
                    Character key = entry.getKey();
                    String name = entry.getValue();
                    assertTrue(Entities.isNamedEntity(name) && Entities.isNamedEntity(name.toUpperCase()));
                    assertTrue(Entities.isNamedEntity(name) && Entities.isNamedEntity(name, escapeMode));
                    assertFalse(Entities.isNamedEntity(String.valueOf((char) key.codePointAt(0))));
                    assertFalse(Entities.isNamedEntity(String.valueOf((char) (key.codePointAt(0) + 1))));
                }
            }
        }
    }

    /**
     * Test getCharacterByName method.
     */
    @Test
    public void testGetCharacterByName() {
        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            for (Map<Character, String> map : new Map[]{Entities.full, Entities.base, Entities.xhtmlByVal}) {
                for (Map.Entry<Character, String> entry : map.entrySet()) {
                    Character key = entry.getKey();
                    String name = entry.getValue();
                    assertEquals(key, Entities.getCharacterByName(name));
                    assertEquals(key, Entities.getCharacterByName(name.toLowerCase()));
                    assertNull(Entities.getCharacterByName(""));
                    assertNull(Entities.getCharacterByName("invalidEntity"));
                }
            }
        }
    }

    /**
     * Test escape method.
     */
    @Test
    public void testEscape() {
        StringBuilder expected = new StringBuilder();
        String string = "&lt;lt&gt;lt&amp;amp;quot;&quot;<>&quot;";
        String actual = Entities.escape(string, outputSettings);

        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            expected.setLength(0);
            for (Map<Character, String> map : new Map[]{Entities.full, Entities.base, Entities.xhtmlByVal}) {
                for (Map.Entry<Character, String> entry : map.entrySet()) {
                    Character key = entry.getKey();
                    String name = entry.getValue();
                    expected.append(Entities.getCharacterByName(name));
                }
            }
            for (char c : string.toCharArray()) {
                if (!Character.isWhitespace(c)) {
                    if (Character.isWhitespace(Entities.getCharacterByName(String.valueOf(c).toUpperCase()))) {
                        continue;
                    }
                    switch (c) {
                        case '&':
                            expected.append("&amp;");
                            break;
                        case '<':
                            expected.append(Entities.getCharacterByName("lt"));
                            break;
                        case '>':
                            expected.append(Entities.getCharacterByName("gt"));
                            break;
                        case '"':
                            expected.append(Entities.getCharacterByName("quot"));
                            break;
                        default:
                            expected.append(c);
                            break;
                    }
                }
            }
            assertEquals(expected.toString(), actual);
        }
    }

    /**
     * Test isBaseNamedEntity method.
     */
    @Test
    public void testIsBaseNamedEntity() {
        for (Entities.EscapeMode escapeMode : Entities.EscapeMode.values()) {
            for (Map<Character, String> map : new Map[]{Entities.full, Entities.base, Entities.xhtmlByVal}) {
                for (Map.Entry<Character, String> entry : map.entrySet()) {
                    Character key = entry.getKey();
                    String name = entry.getValue();
                    assertTrue(Entities.isBaseNamedEntity(name) && Entities.isBaseNamedEntity(name.toUpperCase()));
                    assertTrue(Entities.isBaseNamedEntity(name) && Entities.isBaseNamedEntity(name, escapeMode));
                    assertFalse(Entities.isBaseNamedEntity(String.valueOf((char) key.codePointAt(0))));
                    assertFalse(Entities.isBaseNamedEntity(String.valueOf((char) (key.codePointAt(0) + 1))));
                }
            }
        }
    }

    /**
     * Test unescape method.
     */
    @Test
    public void testUnescape() {
        String input = "&lt;lt&gt;lt&amp;amp;quot;&quot;<>&quot;";
        String actual = Entities.unescape(input);
        String expected = new String(Parser.parseHtmlDocument("<lt>lt&amp;quot;&quot;<>&quot;").body().html().getBytes());
        assertEquals(expected, actual);
    }

    /**
     * Test canEncode method.
     */
    @Test
    public void testCanEncode() {
        for (Entities.CoreCharset charset : Entities.CoreCharset.values()) {
            for (char c = 0; c < 0x10FFFF; c++) {
                if (Character.isWhitespace(c)) {
                    continue;
                }
                CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
                boolean result = Entities.canEncode(charset, c, encoder);
                if (charset == Entities.CoreCharset.ascii) {
                    assertTrue(result && Character.isISOControl(c));
                } else {
                    assertTrue(result || Charset.forName("UTF-8").newEncoder().canEncode(Character.toString(c)));
                }
            }
        }
    }
}