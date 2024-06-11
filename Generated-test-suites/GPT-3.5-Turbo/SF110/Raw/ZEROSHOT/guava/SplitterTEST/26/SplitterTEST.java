import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setUp() {
        splitter = Splitter.on(",");
    }

    @Test
    public void testSplitSingleCharSeparator() {
        String input = "a,b,c";
        Iterable<String> output = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testSplitCharMatcherSeparator() {
        String input = "a,b, c";
        Splitter spaceSplitter = Splitter.on(CharMatcher.is(' '));
        Iterable<String> output = spaceSplitter.split(input);
        String[] expected = {"a,b,", "c"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testSplitStringSeparator() {
        String input = "a->b->c";
        Splitter arrowSplitter = Splitter.on("->");
        Iterable<String> output = arrowSplitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testSplitPatternSeparator() {
        String input = "a,b;c";
        Splitter patternSplitter = Splitter.on(Pattern.compile("[,;]"));
        Iterable<String> output = patternSplitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testSplitFixedLength() {
        String input = "abcde";
        Splitter fixedLengthSplitter = Splitter.fixedLength(2);
        Iterable<String> output = fixedLengthSplitter.split(input);
        String[] expected = {"ab", "cd", "e"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testOmitEmptyStrings() {
        String input = ",a,,b,";
        Splitter emptySplitter = splitter.omitEmptyStrings();
        Iterable<String> output = emptySplitter.split(input);
        String[] expected = {"a", "b"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testLimit() {
        String input = "a,b,c,d,e";
        Splitter limitedSplitter = splitter.limit(3);
        Iterable<String> output = limitedSplitter.split(input);
        String[] expected = {"a", "b", "c,d,e"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testTrimResults() {
        String input = "  a  ,  b  ,  c  ";
        Splitter trimmedSplitter = splitter.trimResults();
        Iterable<String> output = trimmedSplitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        String input = "  a  ,  b  ,  c  ";
        Splitter trimmedSplitter = splitter.trimResults(CharMatcher.is(' '));
        Iterable<String> output = trimmedSplitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testSplitToList() {
        String input = "a,b,c,d,e";
        List<String> output = splitter.splitToList(input);
        String[] expected = {"a", "b", "c", "d", "e"};
        assertArrayEquals(expected, output.toArray());
    }

    @Test
    public void testMapSplitter() {
        String input = "key1=value1,key2=value2,key3=value3";
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> output = mapSplitter.split(input);
        assertEquals("value1", output.get("key1"));
        assertEquals("value2", output.get("key2"));
        assertEquals("value3", output.get("key3"));
    }

}