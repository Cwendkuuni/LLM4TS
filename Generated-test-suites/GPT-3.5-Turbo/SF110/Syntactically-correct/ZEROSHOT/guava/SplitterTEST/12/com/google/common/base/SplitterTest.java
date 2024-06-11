import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(' ').omitEmptyStrings();
        Iterable<String> result = splitter.split("hello world");
        String[] expected = {"hello", "world"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;")).trimResults(CharMatcher.is('_'));
        Iterable<String> result = splitter.split("hello,_world;,how_are;_you");
        String[] expected = {"hello", "world", "how", "are", "you"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(", ").omitEmptyStrings();
        Iterable<String> result = splitter.split("hello, world, , good morning");
        String[] expected = {"hello", "world", "good morning"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\|")).trimResults();
        Iterable<String> result = splitter.split("hello | world | good morning");
        String[] expected = {"hello", "world", "good morning"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("hello");
        String[] expected = {"he", "ll", "o"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("hello,,world,");
        String[] expected = {"hello", "world"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("hello,world,good morning");
        String[] expected = {"hello", "world,good morning"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" hello , world , good morning ");
        String[] expected = {"hello", "world", "good morning"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('_'));
        Iterable<String> result = splitter.split("_hello_,_world_,_good morning_");
        String[] expected = {"hello", "world", "good morning"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("hello,world,good morning");
        String[] expected = {"hello", "world", "good morning"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        List<String> result = splitter.splitToList("hello,world,good morning");
        String[] expected = {"hello", "world", "good morning"};
        assertEquals(Arrays.asList(expected), result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator(",");
        Map<String, String> result = mapSplitter.split("hello,world:good morning,how are you");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("hello", "world");
        expected.put("good morning", "how are you");
        assertEquals(expected, result);
    }
}