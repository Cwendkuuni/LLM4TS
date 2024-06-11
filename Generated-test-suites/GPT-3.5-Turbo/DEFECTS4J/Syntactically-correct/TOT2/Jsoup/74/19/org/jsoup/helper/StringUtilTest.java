package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");

        String result = StringUtil.join(strings, "-");

        assertEquals("one-two-three", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("one", "two", "three");

        String result = StringUtil.join(strings.iterator(), "-");

        assertEquals("one-two-three", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"one", "two", "three"};

        String result = StringUtil.join(strings, "-");

        assertEquals("one-two-three", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);

        assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("    "));
        assertFalse(StringUtil.isBlank("hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("1234"));
        assertFalse(StringUtil.isNumeric("abc"));
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("  hello \t  world  ");

        assertEquals("hello world", result);
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("two", "one", "two", "three"));
        assertFalse(StringUtil.in("four", "one", "two", "three"));
    }

    @Test
    public void testInSorted() {
        String[] array = {"apple", "banana", "orange"};

        assertTrue(StringUtil.inSorted("banana", array));
        assertFalse(StringUtil.inSorted("kiwi", array));
    }

    // Tests for resolve methods will depend on external URLs, hence can be skipped for now

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");

        assertEquals("hello", sb.toString());
    }
}