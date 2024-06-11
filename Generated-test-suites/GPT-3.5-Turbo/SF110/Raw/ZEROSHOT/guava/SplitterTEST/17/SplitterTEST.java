import java.util.regex.*;
import javax.annotation.*;
import com.google.common.annotations.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.splitToList("a-b-c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\."));
        List<String> result = splitter.splitToList("a.b.c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testOnPatternString() {
        Splitter splitter = Splitter.onPattern("\\.");
        List<String> result = splitter.splitToList("a.b.c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.splitToList("abcde");
        assertEquals(Arrays.asList("ab", "cd", "e"), result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        List<String> result = splitter.splitToList("a,,b,,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        List<String> result = splitter.splitToList("a,b,c,d,e");
        assertEquals(Arrays.asList("a", "b,c,d,e"), result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList(" a , b , c ");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testTrimResultsCharMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        List<String> result = splitter.splitToList(" a , b , c ");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        List<String> expectedResult = Arrays.asList("a", "b", "c");
        List<String> actualResult = new ArrayList<>();
        for (String str : result) {
            actualResult.add(str);
        }
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter.SplitterStrategy strategy = new Splitter.SplitterStrategy() {
            @Override
            public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return new Splitter.SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int start) {
                        return -1;
                    }

                    @Override
                    int separatorEnd(int separatorPosition) {
                        return -1;
                    }
                };
            }
        };

        Splitter outerSplitter = new Splitter(strategy);
        Splitter entrySplitter = new Splitter(strategy);

        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(",");
        Map<String, String> resultMap = mapSplitter.split("a,b,c");

        Map<String, String> expectedResultMap = new LinkedHashMap<>();
        expectedResultMap.put("a", "b,c");

        assertEquals(expectedResultMap, resultMap);
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.SplitterStrategy strategy = new Splitter.SplitterStrategy() {
            @Override
            public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return new Splitter.SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int start) {
                        return -1;
                    }

                    @Override
                    int separatorEnd(int separatorPosition) {
                        return -1;
                    }
                };
            }
        };

        Splitter outerSplitter = new Splitter(strategy);
        Splitter entrySplitter = new Splitter(strategy);

        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(',');
        Map<String, String> resultMap = mapSplitter.split("a,b,c");

        Map<String, String> expectedResultMap = new LinkedHashMap<>();
        expectedResultMap.put("a", "b,c");

        assertEquals(expectedResultMap, resultMap);
    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter.SplitterStrategy strategy = new Splitter.SplitterStrategy() {
            @Override
            public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return new Splitter.SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int start) {
                        return -1;
                    }

                    @Override
                    int separatorEnd(int separatorPosition) {
                        return -1;
                    }
                };
            }
        };

        Splitter outerSplitter = new Splitter(strategy);
        Splitter entrySplitter = new Splitter(strategy);

        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        Map<String, String> resultMap = mapSplitter.split("a,b,c");

        Map<String, String> expectedResultMap = new LinkedHashMap<>();
        expectedResultMap.put("a", "b,c");

        assertEquals(expectedResultMap, resultMap);
    }
}