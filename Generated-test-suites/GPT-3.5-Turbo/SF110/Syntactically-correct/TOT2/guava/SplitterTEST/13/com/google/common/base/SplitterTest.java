package com.google.common.base;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class SplitterTest {

    // Test on(char separator)
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,orange,banana");
        String[] expectedResult = {"apple", "orange", "banana"};
        int i = 0;
        for (String s : result) {
            assertEquals(expectedResult[i], s);
            i++;
        }
    }

    // Test on(String separator)
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        Iterable<String> result = splitter.split("apple-orange-banana");
        String[] expectedResult = {"apple", "orange", "banana"};
        int i = 0;
        for (String s : result) {
            assertEquals(expectedResult[i], s);
            i++;
        }
    }

    // Test onPattern(String separatorPattern)
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\d+");
        Iterable<String> result = splitter.split("apple123orange456banana");
        String[] expectedResult = {"apple", "orange", "banana"};
        int i = 0;
        for (String s : result) {
            assertEquals(expectedResult[i], s);
            i++;
        }
    }

    // Test fixedLength(int length)
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("appleorangebanana");
        String[] expectedResult = {"ap", "pl", "eo", "ra", "ng", "eb", "an", "an", "a"};
        int i = 0;
        for (String s : result) {
            assertEquals(expectedResult[i], s);
            i++;
        }
    }

    // Test omitEmptyStrings()
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',');
        splitter = splitter.omitEmptyStrings();
        Iterable<String> result = splitter.split("apple,,orange,,banana");
        String[] expectedResult = {"apple", "orange", "banana"};
        int i = 0;
        for (String s : result) {
            assertEquals(expectedResult[i], s);
            i++;
        }
    }

    // Test limit(int limit)
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        Iterable<String> result = splitter.split("apple-orange-banana");
        String[] expectedResult = {"apple", "orange-banana"};
        int i = 0;
        for (String s : result) {
            assertEquals(expectedResult[i], s);
            i++;
        }
    }

    // Test withKeyValueSeparator(String separator)
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(';').withKeyValueSeparator(':');
        String input = "key1:value1;key2:value2;key3:value3";
        Map<String, String> resultMap = mapSplitter.split(input);
        assertEquals("value1", resultMap.get("key1"));
        assertEquals("value2", resultMap.get("key2"));
        assertEquals("value3", resultMap.get("key3"));
    }

}