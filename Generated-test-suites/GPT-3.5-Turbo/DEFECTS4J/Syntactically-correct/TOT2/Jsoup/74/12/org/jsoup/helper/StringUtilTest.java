import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.*;

public class StringUtilTest {

    private String[] strings = {"Hello", "World", "!"};
    private Collection<String> stringCollection = Arrays.asList(strings);
    private Iterator<String> stringIterator = stringCollection.iterator();
    private String sep = ",";
    private String[] singleStringArray = {"Hello"};
    private String[] emptyArray = {};

    @Test
    public void testJoinCollection() {
        String expected = "Hello,World,!";
        assertEquals(expected, join(stringCollection, sep));
    }

    @Test
    public void testJoinIterator() {
        String expected = "Hello,World,!";
        assertEquals(expected, join(stringIterator, sep));
    }

    @Test
    public void testJoinArray() {
        String expected = "Hello,World,!";
        assertEquals(expected, join(strings, sep));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithNegativeWidth() {
        padding(-1);
    }

    @Test
    public void testPadding() {
        String expected = "         ";
        assertEquals(expected, padding(9));
    }

    @Test
    public void testIsBlank() {
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank(" "));
        assertFalse(isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(isNumeric("12345"));
        assertFalse(isNumeric("123a45"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(isWhitespace(' '));
        assertTrue(isWhitespace('\t'));
        assertFalse(isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(isActuallyWhitespace(' '));
        assertTrue(isActuallyWhitespace('\t'));
        assertFalse(isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String expected = "Hello World!";
        assertEquals(expected, normaliseWhitespace("Hello  World   !"));
    }

    @Test
    public void testIn() {
        assertTrue(in("Hello", "Hello", "World"));
        assertFalse(in("Hi", "Hello", "World"));
    }

    @Test
    public void testInSorted() {
        assertTrue(inSorted("Hello", new String[]{"Hello", "World"}));
        assertFalse(inSorted("Hi", new String[]{"Hello", "World"}));
    }

    @Test
    public void testResolveWithURL() throws MalformedURLException {
        String base = "https://www.example.com";
        String relUrl = "/page";
        String expected = "https://www.example.com/page";
        assertEquals(expected, resolve(new URL(base), relUrl).toString());
    }

    @Test
    public void testResolveWithString() {
        String base = "https://www.example.com";
        String relUrl = "/page";
        String expected = "https://www.example.com/page";
        assertEquals(expected, resolve(base, relUrl));
    }
}