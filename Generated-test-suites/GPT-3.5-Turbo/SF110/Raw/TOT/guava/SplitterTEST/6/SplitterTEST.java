import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setUp() {
        splitter = new Splitter(new Splitter.Strategy() {
            @Override
            public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return new Splitter.SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int p0) {
                        return 0; // Mocking separatorStart method
                    }

                    @Override
                    int separatorEnd(int p0) {
                        return 0; // Mocking separatorEnd method
                    }
                };
            }
        });
    }

    @Test
    public void testOn_char() {
        Splitter result = Splitter.on('a');
        assertNotNull(result);
    }

    @Test
    public void testOn_CharMatcher() {
        Splitter result = Splitter.on(CharMatcher.WHITESPACE);
        assertNotNull(result);
    }

    @Test
    public void testOn_String() {
        Splitter result = Splitter.on(";");
        assertNotNull(result);
        assertThrows(IllegalArgumentException.class, () -> {
            Splitter.on("");
        });
    }

    @Test
    public void testOn_Pattern() {
        // Mocking SplittingIterator to skip regex check 
        Splitter.SplittingIterator mockIterator = new Splitter.SplittingIterator(splitter, "sequence") {
            @Override
            int separatorStart(int p0) {
                return 0; // Mocking separatorStart method
            }

            @Override
            int separatorEnd(int separatorPosition) {
                return 0; // Mocking separatorEnd method
            }
        };

        Splitter result = Splitter.on(Pattern.compile(";"));
        assertNotNull(result);
        assertThrows(IllegalArgumentException.class, () -> {
            Splitter.on(Pattern.compile(""));
        });
    }

    @Test
    public void testOnPattern() {
        Splitter result = Splitter.onPattern(";");
        assertNotNull(result);
    }

    @Test
    public void testFixedLength() {
        Splitter result = Splitter.fixedLength(5);
        assertNotNull(result);
        assertThrows(IllegalArgumentException.class, () -> {
            Splitter.fixedLength(0);
        });
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter result = splitter.omitEmptyStrings();
        assertNotNull(result);
    }

    @Test
    public void testLimit() {
        Splitter result = splitter.limit(5);
        assertNotNull(result);
        assertThrows(IllegalArgumentException.class, () -> {
            splitter.limit(0);
        });
    }

    @Test
    public void testTrimResults() {
        Splitter result = splitter.trimResults();
        assertNotNull(result);
    }

    @Test
    public void testTrimResults_CharMatcher() {
        Splitter result = splitter.trimResults(CharMatcher.DIGIT);
        assertNotNull(result);
    }

    @Test
    public void testSplit() {
        Iterable<String> result = splitter.split("sequence");
        assertNotNull(result);
    }

    @Test
    public void testSplitToList() {
        List<String> result = splitter.splitToList("sequence");
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparator_char() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator('=');
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(Splitter.on('='));
        assertNotNull(mapSplitter);
    }

    @Test
    public void testMapSplitter_split() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1;key2=value2;key3=value3");
        assertNotNull(result);
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
        assertThrows(IllegalArgumentException.class, () -> {
           mapSplitter.split("invalidEntry");
        });
        assertThrows(IllegalArgumentException.class, () -> {
           mapSplitter.split("duplicateKey=value1;duplicateKey=value2");
        });
    }
}