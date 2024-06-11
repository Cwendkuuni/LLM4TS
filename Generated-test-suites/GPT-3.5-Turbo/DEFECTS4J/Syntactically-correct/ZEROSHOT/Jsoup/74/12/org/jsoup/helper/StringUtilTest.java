import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {
    private List<String> stringList;
    private Iterator<String> stringIterator;
    private String[] stringArray;

    @Before
    public void setUp() {
        stringList = new ArrayList<String>();
        stringList.add("one");
        stringList.add("two");
        stringList.add("three");
        
        stringIterator = stringList.iterator();
        
        stringArray = new String[] {"one", "two", "three"};
    }

    @Test
    public void testJoinCollection() {
        String joinedString = StringUtil.join(stringList, ",");
        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testJoinIterator() {
        String joinedString = StringUtil.join(stringIterator, ",");
        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testJoinArray() {
        String joinedString = StringUtil.join(stringArray, ",");
        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testPadding() {
        String paddedString = StringUtil.padding(5);
        assertEquals("     ", paddedString);
    }

    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(" "));
        assertFalse(StringUtil.isBlank("not blank"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric("not numeric"));
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
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalisedString = StringUtil.normaliseWhitespace("   hello   world   ");
        assertEquals(" hello world ", normalisedString);
    }

    @Test
    public void testAppendNormalisedWhitespaceWithoutLeadingStrip() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   hello   world   ", false);
        assertEquals("   hello   world   ", sb.toString());
    }

    @Test
    public void testAppendNormalisedWhitespaceWithLeadingStrip() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   hello   world   ", true);
        assertEquals(" hello world ", sb.toString());
    }

    @Test
    public void testIn() {
        assertTrue(StringUtil.in("one", "one", "two", "three"));
        assertTrue(StringUtil.in("two", "one", "two", "three"));
        assertTrue(StringUtil.in("three", "one", "two", "three"));
        assertFalse(StringUtil.in("four", "one", "two", "three"));
    }

    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("one", new String[] {"one", "two", "three"}));
        assertTrue(StringUtil.inSorted("two", new String[] {"one", "two", "three"}));
        assertTrue(StringUtil.inSorted("three", new String[] {"one", "two", "three"}));
        assertFalse(StringUtil.inSorted("four", new String[] {"one", "two", "three"}));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com/path/");
        String resolvedUrl = StringUtil.resolve(base, "../file.html");
        assertEquals("http://www.example.com/file.html", resolvedUrl);
    }

    @Test
    public void testResolveInvalidBaseUrl() {
        String resolvedUrl = StringUtil.resolve("invalidUrl", "../file.html");
        assertEquals("", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        
        assertSame(sb1, sb2);
    }
}