package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.jsoup.nodes.Entities.base;
import static org.jsoup.nodes.Entities.full;
import static org.jsoup.nodes.Entities.getCharacterByName;
import static org.jsoup.nodes.Entities.isBaseNamedEntity;
import static org.jsoup.nodes.Entities.isNamedEntity;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> map = full;
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;
            boolean result = isNamedEntity(name);
            Assert.assertEquals("isNamedEntity test for name " + name, expectedResult, result);
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> map = base;
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;
            boolean result = isBaseNamedEntity(name);
            Assert.assertEquals("isBaseNamedEntity test for name " + name, expectedResult, result);
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> map = full;
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            Character key = entry.getKey();
            Character expectedResult = key;
            Character result = getCharacterByName(entry.getValue());
            Assert.assertEquals("getCharacterByName test for name " + entry.getValue() + " expected " + expectedResult + " but got " + result, expectedResult, result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithInvalidName() {
        isNamedEntity("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsBaseNamedEntityWithInvalidName() {
        isBaseNamedEntity("invalidName");
    }

    @Test(expected = NullPointerException.class)
    public void testGetCharacterByNameWithNullName() {
        getCharacterByName(null);
    }

    @Test
    public void testEscapeMethodWithASCIIInput() {
        StringBuilder expectedAccum = new StringBuilder("&nbsp;<&amp;&quot;>");
        String input = " Hello & World ! ";
        String output = Entities.escape(input, Document.OutputSettings.base());
        StringBuilder actualAccum = new StringBuilder(output.length());
        Entities.escape(actualAccum, input, Document.OutputSettings.base(), false, false, false);
        Assert.assertEquals("escape test for ASCII input", expectedAccum.toString(), actualAccum.toString());
    }

    @Test
    public void testEscapeMethodWithNonASCIIInput() {
        StringBuilder expectedAccum = new StringBuilder("&#x5C0F;&#x6211;&#x4E16;&#x522B;&#x5927;&#x5C45;&#x4E2C;&#x5B57;&#x5E94;&#x4EAC;&#x5B8B;&#x5B02;");
        String input = "你好 world !";
        String output = Entities.escape(input, Document.OutputSettings.base());
        StringBuilder actualAccum = new StringBuilder(output.length());
        Entities.escape(actualAccum, input, Document.OutputSettings.base(), false, false, false);
        Assert.assertEquals("escape test for Non-ASCII input", expectedAccum.toString(), actualAccum.toString());
    }

    @Test
    public void testUnescapeMethodWithValidInput() {
        String input = "&nbsp;<&amp;&quot;>你好 world !";
        String expectedOutput = " Hello world ! 你好 world !";
        String output = Entities.unescape(input, false);
        Assert.assertEquals("unescape test for valid input", expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodWithInvalidInput() {
        String input = "&#xFA;&#xFB;&#xFC";
        String expectedOutput = "";
        String output = Entities.unescape(input, false);
        Assert.assertEquals("unescape test for invalid input", expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithNullInput() {
        Entities.unescape(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeWithASCIIInput() {
        CoreCharset charset = CoreCharset.ascii;
        char c = 'A';
        boolean expectedResult = true;
        boolean result = Entities.canEncode(charset, c, null);
        Assert.assertEquals("canEncode test for ASCII input", expectedResult, result);
    }

    @Test
    public void testCanEncodeWithNonASCIIInput() {
        CoreCharset charset = CoreCharset.utf;
        char c = '你';
        boolean expectedResult = true;
        boolean result = Entities.canEncode(charset, c, null);
        Assert.assertEquals("canEncode test for Non-ASCII input", expectedResult, result);
    }

    @Test
    public void testCanEncodeWithUnsupportedInput() {
        CoreCharset charset = CoreCharset.fallback;
        char c = '你';
        boolean expectedResult = false;
        boolean result = Entities.canEncode(charset, c, null);
        Assert.assertEquals("canEncode test for Unsupported input", expectedResult, result);
    }
}