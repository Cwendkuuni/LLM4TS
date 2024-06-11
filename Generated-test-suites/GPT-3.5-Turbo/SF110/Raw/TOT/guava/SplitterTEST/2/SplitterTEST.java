import com.google.common.base.Splitter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setup() {
        splitter = Splitter.on(",");
    }

    @Test
    public void testOnWithCharSeparator() {
        String input = "1,2,3,4,5";
        List<String> expectedOutput = Arrays.asList("1", "2", "3", "4", "5");

        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOnWithCharMatcherSeparator() {
        String input = "1, 2, 3, ,4";
        List<String> expectedOutput = Arrays.asList("1", " 2", " 3", " ", "4");

        splitter = Splitter.on(SplitterTest::isCommaOrWhitespace);
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOnWithStringSeparator() {
        String input = "1-2-3-4-5";
        List<String> expectedOutput = Arrays.asList("1", "2", "3", "4", "5");

        splitter = Splitter.on("-");
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOnWithPatternSeparator() {
        String input = "1,2-3,4";
        List<String> expectedOutput = Arrays.asList("1", "2", "3", "4");

        splitter = Splitter.onPattern("[,-]");
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFixedLength() {
        String input = "1234567890";
        List<String> expectedOutput = Arrays.asList("123", "456", "789", "0");

        splitter = Splitter.fixedLength(3);
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOmitEmptyStrings() {
        String input = "1,,2,3";
        List<String> expectedOutput = Arrays.asList("1", "2", "3");

        splitter = Splitter.on(",").omitEmptyStrings();
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLimit() {
        String input = "1,2,3,4,5";
        List<String> expectedOutput = Arrays.asList("1", "2,3,4,5");

        splitter = Splitter.on(",").limit(2);
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTrimResults() {
        String input = "  1  ,  2  ,  3  ";
        List<String> expectedOutput = Arrays.asList("1", "2", "3");

        splitter = Splitter.on(",").trimResults();
        List<String> output = splitter.splitToList(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitToMap() {
        String input = "key1=value1,key2=value2,key3=value3";
        Map<String, String> expectedOutput = Map.of("key1", "value1", "key2", "value2", "key3", "value3");

        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> output = mapSplitter.split(input);

        assertEquals(expectedOutput, output);
    }

    private static boolean isCommaOrWhitespace(char c) {
        return c == ',' || Character.isWhitespace(c);
    }

}