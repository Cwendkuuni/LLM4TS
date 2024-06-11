package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class EntitiesTest {

    private Entities entities;
    private String name;
    private Character expectedResult;

    public EntitiesTest(String name, Character expectedResult) {
        this.name = name;
        this.expectedResult = expectedResult;
    }

    @Parameters
    public static Map<String, Character> data() {
        Map<String, Character> map = new HashMap<>();
        map.putAll(Entities.full);
        return map;
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(entities.isNamedEntity(name));
        assertEquals(expectedResult, entities.getCharacterByName(name));
    }

    @Test(expected = AssertionError.class)
    public void testIsNamedEntity_whenNameIsNotNamedEntity() {
        assertFalse(entities.isNamedEntity("RandomName"));
        try {
            entities.getCharacterByName("RandomName");
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testEscape_whenInputIsNull() {
        String input = null;
        StringBuilder accumulator = new StringBuilder();
        try {
            Entities.escape(accumulator, input, null, false, false, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEscape_whenInputIsEmpty() {
        String input = "";
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, null, false, false, false);
        assertEquals("", accumulator.toString());
    }

    @Test
    public void testEscape_whenInputContainsNamedEntity() {
        String input = "&amp;lt;html&gt;";
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, null, false, false, false);
        assertEquals("&amp;lt;html&gt;", accumulator.toString());
    }

    @Test
    public void testEscape_whenInputContainsNamedEntityInAttributeValue() {
        String input = "<html&quot; id=&quot;test&quot;>&lt;p&gt; &amp;lt; &amp;gt; &quot;&#34; &amp;quot; test &amp;amp; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#xA0; &amp;#x22;";
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, null, true, false, false);
        assertEquals("<html&quot; id=&quot;test&quot;>&lt;p&gt; &amp;lt; &amp;gt; &quot;&#34; &amp;quot; test &amp;amp; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#xA0; &amp;#x22;", accumulator.toString());
    }

    @Test
    public void testEscape_whenInputContainsUnicodeCharacter() {
        String input = "你好，世界!";
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, null, false, false, false);
        assertEquals("你好，世界!", accumulator.toString());

        StringBuilder accumulatorWithEncoder = new StringBuilder();
        Entities.escape(accumulatorWithEncoder, input, null, false, true, false);
        assertEquals("你好，世界!", accumulatorWithEncoder.toString());

        StringBuilder accumulatorWithEncoderAndXHTML = new StringBuilder();
        Entities.escape(accumulatorWithEncoderAndXHTML, input, Entities.EscapeMode.xhtml, false, true, false);
        assertEquals("&#x4F60;&#x597D;&#x7684;&#x597D;", accumulatorWithEncoderAndXHTML.toString());
    }

    @Test
    public void testUnescape_whenInputContainsNamedEntity() {
        String input = "&amp;lt;html&gt;";
        String expectedResult = "<html>";
        String result = Entities.unescape(input);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUnescape_whenInputContainsNamedEntityInAttributeValue() {
        String input = "<html&quot; id=&quot;test&quot;>&lt;p&gt; &amp;lt; &amp;gt; &quot;&#34; &amp;quot; test &amp;amp; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#xA0; &amp;#x22;";
        String expectedResult = "<html\" id=\"test\">&lt;p&gt; &lt; &gt; &quot;&quot; test &amp;amp; &amp;#x26; &lt; &gt; &nbsp; &quot;";
        String result = Entities.unescape(input, true);
        assertEquals(expectedResult, result);
    }

    @Test(expected = AssertionError.class)
    public void testUnescape_whenInputContainsUnexpectedEntity() {
        String input = "&RandomEntity;";
        String result = Entities.unescape(input);
        fail("Expected AssertionError");
    }
}