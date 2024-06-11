import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // Test case 1: Joining an empty collection
        assertEquals("", StringUtil.join(Collections.emptyList(), ","));
        
        // Test case 2: Joining a collection with one element
        Collection<String> collection1 = Arrays.asList("Hello");
        assertEquals("Hello", StringUtil.join(collection1, ","));
        
        // Test case 3: Joining a collection with multiple elements
        Collection<String> collection2 = Arrays.asList("Hello", "World", "!");
        assertEquals("Hello,World,!", StringUtil.join(collection2, ","));
    }

    @Test
    public void testJoinIterator() {
        // Test case 1: Joining an empty iterator
        Iterator<String> iterator1 = Collections.emptyIterator();
        assertEquals("", StringUtil.join(iterator1, ","));

        // Test case 2: Joining an iterator with one element
        Iterator<String> iterator2 = Arrays.asList("Hello").iterator();
        assertEquals("Hello", StringUtil.join(iterator2, ","));

        // Test case 3: Joining an iterator with multiple elements
        Iterator<String> iterator3 = Arrays.asList("Hello", "World", "!").iterator();
        assertEquals("Hello,World,!", StringUtil.join(iterator3, ","));
    }

    @Test
    public void testJoinArray() {
        // Test case 1: Joining an empty array
        String[] array1 = new String[0];
        assertEquals("", StringUtil.join(array1, ","));

        // Test case 2: Joining an array with one element
        String[] array2 = new String[]{"Hello"};
        assertEquals("Hello", StringUtil.join(array2, ","));

        // Test case 3: Joining an array with multiple elements
        String[] array3 = new String[]{"Hello", "World", "!"};
        assertEquals("Hello,World,!", StringUtil.join(array3, ","));
    }

    @Test
    public void testPadding() {
        // Test case 1: Padding of width 0
        assertEquals("", StringUtil.padding(0));

        // Test case 2: Padding of width 5
        assertEquals("     ", StringUtil.padding(5));

        // Test case 3: Padding of width 10
        assertEquals("          ", StringUtil.padding(10));
    }

    @Test
    public void testIsBlank() {
        // Test case 1: Blank string
        assertTrue(StringUtil.isBlank(""));

        // Test case 2: Null string
        assertTrue(StringUtil.isBlank(null));

        // Test case 3: Non-blank string
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        // Test case 1: Numeric string
        assertTrue(StringUtil.isNumeric("123"));

        // Test case 2: Non-numeric string
        assertFalse(StringUtil.isNumeric("Hello"));

        // Test case 3: Null string
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsWhitespace() {
        // Test case 1: Whitespace character (' ')
        assertTrue(StringUtil.isWhitespace(' '));

        // Test case 2: Whitespace character ('\t')
        assertTrue(StringUtil.isWhitespace('\t'));

        // Test case 3: Non-whitespace character ('A')
        assertFalse(StringUtil.isWhitespace('A'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        // Test case 1: Whitespace character (' ')
        assertTrue(StringUtil.isActuallyWhitespace(' '));

        // Test case 2: Whitespace character ('\t')
        assertTrue(StringUtil.isActuallyWhitespace('\t'));

        // Test case 3: Non-whitespace character ('A')
        assertFalse(StringUtil.isActuallyWhitespace('A'));
    }

    @Test
    public void testNormaliseWhitespace() {
        // Test case 1: Normalising an empty string
        assertEquals("", StringUtil.normaliseWhitespace(""));

        // Test case 2: Normalising a string with multiple spaces
        assertEquals("Hello World!", StringUtil.normaliseWhitespace("Hello  World!"));

        // Test case 3: Normalising a string with multiple whitespace characters
        assertEquals("Hello World!", StringUtil.normaliseWhitespace("Hello\tWorld\n!"));
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        // Test case 1: Appending normalised whitespace to an empty StringBuilder
        StringBuilder sb1 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb1, "", false);
        assertEquals("", sb1.toString());

        // Test case 2: Appending normalised whitespace to a non-empty StringBuilder
        StringBuilder sb2 = new StringBuilder("Hello");
        StringUtil.appendNormalisedWhitespace(sb2, "  World!  ", false);
        assertEquals("Hello World!", sb2.toString());

        // Test case 3: Appending normalised whitespace and stripping leading whitespace
        StringBuilder sb3 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb3, "  Hello World!", true);
        assertEquals("Hello World!", sb3.toString());
    }

    @Test
    public void testIn() {
        // Test case 1: Needle is in the haystack
        assertTrue(StringUtil.in("Hello", "Hello", "World"));

        // Test case 2: Needle is not in the haystack
        assertFalse(StringUtil.in("foo", "Hello", "World"));

        // Test case 3: Empty haystack
        assertFalse(StringUtil.in("Hello"));

        // Test case 4: Empty needle
        assertFalse(StringUtil.in("", "Hello", "World"));
    }

    @Test
    public void testInSorted() {
        // Test case 1: Needle is in the sorted haystack
        assertTrue(StringUtil.inSorted("Hello", new String[]{"Hello", "World"}));

        // Test case 2: Needle is not in the sorted haystack
        assertFalse(StringUtil.inSorted("foo", new String[]{"Hello", "World"}));

        // Test case 3: Empty sorted haystack
        assertFalse(StringUtil.inSorted("Hello", new String[]{}));

        // Test case 4: Empty needle
        assertFalse(StringUtil.inSorted("", new String[]{"Hello", "World"}));
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        // Test case 1: Resolving a relative URL
        URL base1 = new URL("http://www.example.com/");
        URL resolved1 = StringUtil.resolve(base1, "foo.html");
        assertEquals("http://www.example.com/foo.html", resolved1.toString());

        // Test case 2: Resolving an absolute URL
        URL base2 = new URL("http://www.example.com/");
        URL resolved2 = StringUtil.resolve(base2, "http://www.example.com/foo.html");
        assertEquals("http://www.example.com/foo.html", resolved2.toString());

        // Test case 3: Resolving a URL with a query parameter
        URL base3 = new URL("http://www.example.com/");
        URL resolved3 = StringUtil.resolve(base3, "?foo=bar");
        assertEquals("http://www.example.com/?foo=bar", resolved3.toString());
    }

    @Test
    public void testResolveString() {
        // Test case 1: Resolving a relative URL
        String resolved1 = StringUtil.resolve("http://www.example.com/", "foo.html");
        assertEquals("http://www.example.com/foo.html", resolved1);

        // Test case 2: Resolving an absolute URL
        String resolved2 = StringUtil.resolve("http://www.example.com/", "http://www.example.com/foo.html");
        assertEquals("http://www.example.com/foo.html", resolved2);

        // Test case 3: Resolving a URL with a query parameter
        String resolved3 = StringUtil.resolve("http://www.example.com/", "?foo=bar");
        assertEquals("http://www.example.com/?foo=bar", resolved3);

        // Test case 4: Resolving a malformed base URL
        String resolved4 = StringUtil.resolve("malformed.com", "foo.html");
        assertEquals("", resolved4);
    }

    @Test
    public void testStringBuilder() {
        // Test case 1: Getting a StringBuilder with empty contents
        StringBuilder sb1 = StringUtil.stringBuilder();
        assertEquals("", sb1.toString());

        // Test case 2: Reusing the StringBuilder with different contents
        StringBuilder sb2 = StringUtil.stringBuilder();
        sb2.append("Hello");
        assertEquals("Hello", sb2.toString());

        // Test case 3: Getting a new StringBuilder after exceeding the maximum cache size
        StringBuilder sb3 = StringUtil.stringBuilder();
        sb3.append("12345678");
        assertEquals("12345678", sb3.toString());
        StringBuilder sb4 = StringUtil.stringBuilder();
        assertEquals("", sb4.toString());
    }

}