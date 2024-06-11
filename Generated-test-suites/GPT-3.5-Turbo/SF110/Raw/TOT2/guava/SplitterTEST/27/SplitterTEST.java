import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('/'));
        assertNotNull(splitter);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        assertNotNull(splitter);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.DIGIT);
        assertNotNull(splitter);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        assertNotNull(result);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(":");
        assertNotNull(mapSplitter);
    }
}