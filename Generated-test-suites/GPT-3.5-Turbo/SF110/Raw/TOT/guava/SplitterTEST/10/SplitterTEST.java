import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.List;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        
        Iterable<String> result = splitter.split("apple,banana,cherry");
        List<String> resultList = splitter.splitToList("apple,banana,cherry");
        String[] expectedResult = {"apple", "banana", "cherry"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
        
        assertEquals(3, splitter.limit(3).splitToList("apple,banana,cherry,grapefruit").size());
        assertEquals(4, splitter.limit(3).splitToList("apple,banana,cherry,grapefruit").size());
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(' '));
        
        Iterable<String> result = splitter.split("Hello World");
        List<String> resultList = splitter.splitToList("Hello World");
        String[] expectedResult = {"Hello", "World"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(" ");
        
        Iterable<String> result = splitter.split("Hello World");
        List<String> resultList = splitter.splitToList("Hello World");
        String[] expectedResult = {"Hello", "World"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile(","));
        
        Iterable<String> result = splitter.split("apple,banana,cherry");
        List<String> resultList = splitter.splitToList("apple,banana,cherry");
        String[] expectedResult = {"apple", "banana", "cherry"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testOnPatternSeparatorInvalidPattern() {
        Pattern pattern = Pattern.compile("");
        Splitter splitter = Splitter.on(pattern);
        
        Iterable<String> result = splitter.split("apple,banana,cherry");
        List<String> resultList = splitter.splitToList("apple,banana,cherry");
        String[] expectedResult = {"apple,banana,cherry"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        
        Iterable<String> result = splitter.split("applebanana");
        List<String> resultList = splitter.splitToList("applebanana");
        String[] expectedResult = {"app", "leb", "ana", "na"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        
        Iterable<String> result = splitter.omitEmptyStrings().split("apple,,banana,cherry");
        List<String> resultList = splitter.omitEmptyStrings().splitToList("apple,,banana,cherry");
        String[] expectedResult = {"apple", "banana", "cherry"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(3);
        
        Iterable<String> result = splitter.split("apple,banana,cherry,grapefruit");
        List<String> resultList = splitter.splitToList("apple,banana,cherry,grapefruit");
        String[] expectedResult = {"apple", "banana", "cherry,grapefruit"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        
        Iterable<String> result = splitter.split(" apple , banana , cherry ");
        List<String> resultList = splitter.splitToList(" apple , banana , cherry ");
        String[] expectedResult = {"apple", "banana", "cherry"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.whitespace());
        
        Iterable<String> result = splitter.split(" apple , banana , cherry ");
        List<String> resultList = splitter.splitToList(" apple , banana , cherry ");
        String[] expectedResult = {"apple", "banana", "cherry"};
        
        int count = 0;
        for (String str : result) {
            assertEquals(expectedResult[count], str);
            count++;
        }
        
        assertEquals(expectedResult.length, resultList.size());
    }
    
    @Test
    public void testSplitToMap() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        
        String input = "apple=red,banana=yellow,cherry=red";
        Map<String, String> resultMap = splitter.withKeyValueSeparator("=").split(input);
        
        assertEquals("red", resultMap.get("apple"));
        assertEquals("yellow", resultMap.get("banana"));
        assertEquals("red", resultMap.get("cherry"));
    }
}