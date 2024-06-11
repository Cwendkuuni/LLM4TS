package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.nodes.Entities.canEncode;
import static org.jsoup.nodes.Entities.CoreCharset;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.jsoup.nodes.Entities.getCharacterByName;
import static org.jsoup.nodes.Entities.isBaseNamedEntity;
import static org.jsoup.nodes.Entities.isNamedEntity;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (String namedEntity : Arrays.asList("lt", "gt", "amp", "quot")) {
            Assert.assertTrue(isNamedEntity(namedEntity));
        }

        Assert.assertFalse(isNamedEntity(""));
        Assert.assertFalse(isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (String namedEntity : Arrays.asList("lt", "gt", "amp", "quot")) {
            Assert.assertFalse(isBaseNamedEntity(namedEntity));
        }

        Assert.assertTrue(isBaseNamedEntity("nbsp"));
        Assert.assertTrue(isBaseNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, Character> expected = new HashMap<>();
        expected.put(getCharacterByName("amp"), '&');
        expected.put(getCharacterByName("apos"), '\'');
        expected.put(getCharacterByName("quot"), '"');
        expected.put(getCharacterByName("lt"), '<');
        expected.put(getCharacterByName("gt"), '>');
        expected.put(getCharacterByName("nbsp"), (char) 0xA0);

        for (Map.Entry<Character, Character> entry : expected.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            Assert.assertEquals(value, getCharacterByName(String.valueOf(key)));
        }
    }

    @Test
    public void testEscape() {
        String original = "&amp;lt;abc&gt;def&quot;<>&apos;";
        String xhtmlOutput = Entities.escape(original, XHTML);
        String baseOutput = Entities.escape(original, BASE);
        String extendedOutput = Entities.escape(original, EXTENDED);

        String xhtmlExpected = "&amp;lt; &amp;lt;a&gt;b&amp;gt; &amp;quot; &amp;lt;&amp;gt; &amp;amp; &amp;apos;";
        String baseExpected = "&amp;lt;abc&gt;def&quot;&lt;&gt;&amp;amp;&apos;";
        String extendedExpected = "&#x26;amp;&#x26;lt;&#x26;lt;abc&#x26;gt;&#x26;gt;def&#x26;quot;&#x003C;&#x003E;&#x26;amp;&#x26;apos;";

        Assert.assertEquals(xhtmlExpected, xhtmlOutput);
        Assert.assertEquals(baseExpected, baseOutput);
        Assert.assertEquals(extendedExpected, extendedOutput);
    }

    @Test
    public void testUnescape() {
        String xhtmlEscaped = "&amp;lt; &amp;lt;a&gt;b&amp;gt; &amp;quot; &amp;lt;&amp;gt; &amp;amp; &amp;apos;";
        String baseEscaped = "&amp;lt;abc&gt;def&quot;&lt;&gt;&amp;amp;&apos;";
        String extendedEscaped = "&#x26;amp;&#x26;lt;&#x26;lt;abc&#x26;gt;&#x26;gt;def&#x26;quot;&#x003C;&#x003E;&#x26;amp;&#x26;apos;";

        String xhtmlUnescaped = Entities.unescape(xhtmlEscaped, true);
        String baseUnescaped = Entities.unescape(baseEscaped, false);
        String extendedUnescaped = Entities.unescape(extendedEscaped, false);

        String originalXhtml = "&lt; &lt;a&gt;b&gt; &quot; &lt;&gt; &amp; &apos;";
        String originalBase = "&lt;abc&gt;def&quot;&lt;&gt;&amp; &apos;";
        String originalExtended = "<abc>def\"<>&amp;'";

        Assert.assertEquals(originalXhtml, xhtmlUnescaped);
        Assert.assertEquals(originalBase, baseUnescaped);
        Assert.assertEquals(originalExtended, extendedUnescaped);
    }

    @Test
    public void testCanEncode() {
        Character[] ascii = {'\u0000', 'A', 'Z', '.', '!', '?'};
        Character[] nonAscii = {'\u00A0', '\u01F4', '\u1F00', '\uD800', '\uD840'};
        Map<Character, CoreCharset> charsetMap = new HashMap<>();
        charsetMap.put((char) 0, CoreCharset.ascii);
        charsetMap.put((char) 0x0001, CoreCharset.utf);

        for (Character character : ascii) {
            Assert.assertTrue("ASCII character " + character + " should be encodable by default charset", canEncode(CoreCharset.ascii, character, null));
        }

        for (Character character : nonAscii) {
            Assert.assertFalse("Non-ASCII character " + character + " should not be encodable by default charset", canEncode(CoreCharset.ascii, character, null));

            for (Map.Entry<Character, CoreCharset> entry : charsetMap.entrySet()) {
                CharsetEncoder encoder = Character.encodingFor(entry.getKey()).newEncoder();
                Assert.assertTrue("Non-ASCII character " + character + " should be encodable by " + entry.getValue().name(), canEncode(entry.getValue(), character, encoder));
            }
        }
    }
}