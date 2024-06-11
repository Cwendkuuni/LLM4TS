import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertThat(result, contains("apple", "banana", "orange"));
    }

    @Test
    public void testOnEmptyStringSeparator() {
        Splitter splitter = Splitter.on("");
        Iterable<String> result = splitter.split("abc");
        assertThat(result, contains("a", "b", "c"));
    }

    @Test
    public void testOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("12345");
        assertThat(result, contains("12", "34", "5"));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split(",apple,,banana,,");
        assertThat(result, contains("apple", "banana"));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertThat(result, contains("apple", "banana,orange"));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" apple , banana , orange ");
        assertThat(result, contains("apple", "banana", "orange"));
    }
    
    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2");
        assertThat(result.get("key1"), is("value1"));
        assertThat(result.get("key2"), is("value2"));
    }

    @Test
    public void testInvalidEntryException() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        try {
            mapSplitter.split("key1:value1,key2");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString("Chunk [key2] is not a valid entry"));
        }
    }

    // Include more test cases for other methods and edge cases

}