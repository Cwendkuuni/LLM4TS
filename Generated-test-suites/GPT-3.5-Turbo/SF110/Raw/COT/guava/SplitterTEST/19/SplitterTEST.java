
import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    // Test case for public static Splitter on(char separator)
    @Test
    public void testOnCharSeparator() {
        char separator = ',';
        Splitter splitter = Splitter.on(separator);
        assertNotNull(splitter);
    }

    // Test case for public static Splitter on(CharMatcher separatorMatcher)
    @Test
    public void testOnCharMatcherSeparator() {
        CharMatcher separatorMatcher = CharMatcher.is(',');
        Splitter splitter = Splitter.on(separatorMatcher);
        assertNotNull(splitter);
    }

    // Test case for public static Splitter on(String separator)
    @Test
    public void testOnStringSeparator() {
        String separator = ",";
        Splitter splitter = Splitter.on(separator);
        assertNotNull(splitter);
    }

    // Test case for public static Splitter on(Pattern separatorPattern)
    @Test
    public void testOnPatternSeparator() {
        Pattern separatorPattern = Pattern.compile(",");
        Splitter splitter = Splitter.on(separatorPattern);
        assertNotNull(splitter);
    }

    // Test case for public static Splitter onPattern(String separatorPattern)
    @Test
    public void testOnPattern() {
        String separatorPattern = ",";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        assertNotNull(splitter);
    }

    // Test case for public static Splitter fixedLength(int length)
    @Test
    public void testFixedLength() {
        int length = 5;
        Splitter splitter = Splitter.fixedLength(length);
        assertNotNull(splitter);
    }

    // Test case for public Splitter omitEmptyStrings()
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = new Splitter(null).omitEmptyStrings();
        assertNotNull(splitter);
    }

    // Test case for public Splitter limit(int limit)
    @Test
    public void testLimit() {
        int limit = 10;
        Splitter splitter = new Splitter(null).limit(limit);
        assertNotNull(splitter);
    }

    // Test case for public Splitter trimResults()
    @Test
    public void testTrimResults() {
        Splitter splitter = new Splitter(null).trimResults();
        assertNotNull(splitter);
    }

    // Test case for public Splitter trimResults(CharMatcher trimmer)
    @Test
    public void testTrimResultsWithTrimmer() {
        CharMatcher trimmer = CharMatcher.WHITESPACE;
        Splitter splitter = new Splitter(null).trimResults(trimmer);
        assertNotNull(splitter);
    }

    // Test case for public Iterable<String> split(CharSequence sequence)
    @Test
    public void testSplit() {
        CharSequence sequence = "Hello,World";
        Splitter splitter = new Splitter(null);
        Iterable<String> result = splitter.split(sequence);
        assertNotNull(result);
    }

    // Test case for public List<String> splitToList(CharSequence sequence)
    @Test
    public void testSplitToList() {
        CharSequence sequence = "Hello,World";
        Splitter splitter = new Splitter(null);
        List<String> result = splitter.splitToList(sequence);
        assertNotNull(result);
    }

    // Test case for public MapSplitter withKeyValueSeparator(String separator)
    @Test
    public void testWithKeyValueSeparatorString() {
        String separator = ",";
        Splitter splitter = new Splitter(null);
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(separator);
        assertNotNull(mapSplitter);
    }

    // Test case for public MapSplitter withKeyValueSeparator(char separator)
    @Test
    public void testWithKeyValueSeparatorChar() {
        char separator = ',';
        Splitter splitter = new Splitter(null);
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(separator);
        assertNotNull(mapSplitter);
    }

    // Test case for public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter)
    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter keyValueSplitter = new Splitter(null);
        Splitter splitter = new Splitter(null);
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(keyValueSplitter);
        assertNotNull(mapSplitter);
    }

    // Additional test case for abstract class SplittingIterator
    @Test
    public void testSplittingIterator() {
        Splitter splitter = new Splitter(null);
        CharSequence sequence = "Hello,World";
        Splitter.SplittingIterator iterator = splitter.new SplittingIterator(splitter, sequence) {
            @Override
            int separatorStart(int p0) {
                return 0;
            }

            @Override
            int separatorEnd(int p0) {
                return 0;
            }
        };
        assertNotNull(iterator);
    }

}
