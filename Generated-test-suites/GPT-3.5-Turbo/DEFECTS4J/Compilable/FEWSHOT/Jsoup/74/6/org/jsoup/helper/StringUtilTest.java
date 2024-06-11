package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        assertEquals("Hello World !", StringUtil.join(strings, " "));
    }

    @Test
    public void testJoinIterator() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        Iterator<String> iterator = strings.iterator();
        assertEquals("Hello World !", StringUtil.join(iterator, " "));
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        assertEquals("Hello World !", StringUtil.join(strings, " "));
    }

    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("    ", StringUtil.padding(4));
        assertEquals("                    ", StringUtil.padding(20));
        assertEquals("                                                                                                                                                                                                                                                                ", StringUtil.padding(200));
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\r"));
        assertFalse(StringUtil.isBlank("Hello"));
        assertFalse(StringUtil.isBlank(" World! "));
    }

    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("abc"));
        assertFalse(StringUtil.isNumeric("1.23"));
        assertFalse(StringUtil.isNumeric("12-3"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('1'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('1'));
    }

    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals("Hello World!", StringUtil.normaliseWhitespace("Hello       World!"));
        assertEquals(" Hello World! ", StringUtil.normaliseWhitespace("  Hello     \t  World! \n "));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "", false);
        assertEquals("", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   Hello  \t  World! \n ", false);
        assertEquals(" Hello World! ", sb.toString());

        sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   Hello  \t  World! \n ", true);
        assertEquals("Hello World! ", sb.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("Hello", "Hello", "World", "!"));
        assertTrue(StringUtil.in("World", "Hello", "World", "!"));
        assertFalse(StringUtil.in("Goodbye", "Hello", "World", "!"));
    }

    @Test
    public void testInSorted() {
        String[] sortedArray = {"!", "Hello", "World"};
        assertTrue(StringUtil.inSorted("Hello", sortedArray));
        assertFalse(StringUtil.inSorted("Goodbye", sortedArray));
    }

    @Test
    public void testResolveWithUrlObject() throws MalformedURLException {
        URL base = new URL("https://www.example.com/path/");
        String relativeUrl = "../file.html";
        assertEquals("https://www.example.com/file.html", StringUtil.resolve(base, relativeUrl).toString());
    }

    @Test
    public void testResolveWithUrlString() {
        String baseUrl = "https://www.example.com/path/";
        String relativeUrl = "../file.html";
        assertEquals("https://www.example.com/file.html", StringUtil.resolve(baseUrl, relativeUrl));
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        sb1.append("Hello");
        sb1.append(" World");
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertEquals("", sb2.toString());
    }
}