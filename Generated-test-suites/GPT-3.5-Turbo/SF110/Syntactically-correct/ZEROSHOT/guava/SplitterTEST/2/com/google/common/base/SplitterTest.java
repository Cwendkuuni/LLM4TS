import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setUp() {
        splitter = new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return null;
            }
        });
    }

    @Test
    public void testOnChar() {
        Splitter result = Splitter.on('a');
        assertNotNull(result);
    }

    @Test
    public void testOnCharMatcher() {
        CharMatcher separatorMatcher = CharMatcher.anyOf(",");
        Splitter result = Splitter.on(separatorMatcher);
        assertNotNull(result);
    }

    @Test
    public void testOnString() {
        String separator = ",";
        Splitter result = Splitter.on(separator);
        assertNotNull(result);
    }

    @Test
    public void testOnPattern() {
        String separatorPattern = "\\s+";
        Splitter result = Splitter.onPattern(separatorPattern);
        assertNotNull(result);
    }

    @Test
    public void testFixedLength() {
        int length = 3;
        Splitter result = Splitter.fixedLength(length);
        assertNotNull(result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter result = splitter.omitEmptyStrings();
        assertNotNull(result);
    }

    @Test
    public void testLimit() {
        int limit = 2;
        Splitter result = splitter.limit(limit);
        assertNotNull(result);
    }

    @Test
    public void testTrimResults() {
        Splitter result = splitter.trimResults();
        assertNotNull(result);
    }

    @Test
    public void testTrimResultsWithTrimmer() {
        CharMatcher trimmer = CharMatcher.anyOf(";");
        Splitter result = splitter.trimResults(trimmer);
        assertNotNull(result);
    }

    @Test
    public void testSplit() {
        CharSequence sequence = "Hello, World";
        Iterable<String> result = splitter.split(sequence);
        assertNotNull(result);
    }

    @Test
    public void testSplitToList() {
        CharSequence sequence = "Hello, World";
        List<String> result = splitter.splitToList(sequence);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparatorOnString() {
        String separator = "=";
        Splitter.MapSplitter result = splitter.withKeyValueSeparator(separator);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparatorOnChar() {
        char separator = '=';
        Splitter.MapSplitter result = splitter.withKeyValueSeparator(separator);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparatorOnSplitter() {
        Splitter keyValueSplitter = Splitter.on("=");
        Splitter.MapSplitter result = splitter.withKeyValueSeparator(keyValueSplitter);
        assertNotNull(result);
    }

    @Test
    public void testMapSplitterSplit() {
        Splitter.MapSplitter mapSplitter = splitter.new MapSplitter(splitter, splitter);
        CharSequence sequence = "key1=value1,key2=value2";
        Map<String, String> result = mapSplitter.split(sequence);
        assertNotNull(result);
    }

}