
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class SplitterTest {
    
    @Test
    public void testOnChar_SeparatorExists_ReturnsSplitString() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,orange,banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnChar_SeparatorDoesNotExist_ReturnsOriginalString() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple.orange.banana");
        List<String> expected = Collections.singletonList("apple.orange.banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnChar_EmptyString_ReturnsOriginalString() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("");
        List<String> expected = Collections.singletonList("");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnChar_LimitSet_ReturnsSplitStringAccordingly() {
        Splitter splitter = Splitter.on(',').limit(3);
        Iterable<String> result = splitter.split("apple,orange,banana,grape");
        List<String> expected = Arrays.asList("apple", "orange", "banana,grape");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString_SeparatorExists_ReturnsSplitString() {
        Splitter splitter = Splitter.on("##");
        Iterable<String> result = splitter.split("apple##orange##banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString_SeparatorDoesNotExist_ReturnsOriginalString() {
        Splitter splitter = Splitter.on("##");
        Iterable<String> result = splitter.split("apple.orange.banana");
        List<String> expected = Collections.singletonList("apple.orange.banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString_EmptyString_ReturnsOriginalString() {
        Splitter splitter = Splitter.on("##");
        Iterable<String> result = splitter.split("");
        List<String> expected = Collections.singletonList("");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString_LimitSet_ReturnsSplitStringAccordingly() {
        Splitter splitter = Splitter.on("##").limit(2);
        Iterable<String> result = splitter.split("apple##orange##banana");
        List<String> expected = Arrays.asList("apple", "orange##banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength_SplitStringLengthIsMultipleOfLength_ReturnsSplitString() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("appleorangebanana");
        List<String> expected = Arrays.asList("app", "leo", "ran", "ge");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength_SplitStringLengthIsNotMultipleOfLength_ReturnsSplitStringWithLastChunkLessThanLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("appleorangebananaa");
        List<String> expected = Arrays.asList("app", "leo", "ran", "ge", "naa");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength_SplitStringLengthIsLessThanLength_ReturnsOriginalString() {
        Splitter splitter = Splitter.fixedLength(20);
        Iterable<String> result = splitter.split("appleorangebanana");
        List<String> expected = Collections.singletonList("appleorangebanana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings_EmptyStringExists_ReturnsSplitStringWithoutEmptyString() {
        Splitter splitter = Splitter.on('.').omitEmptyStrings();
        Iterable<String> result = splitter.split("apple..orange..banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings_EmptyStringNotExists_ReturnsOriginalString() {
        Splitter splitter = Splitter.on('.').omitEmptyStrings();
        Iterable<String> result = splitter.split("apple.orange.banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testLimit_LimitSetToZero_ReturnsOriginalString() {
        Splitter splitter = Splitter.on(',').limit(0);
        Iterable<String> result = splitter.split("apple,orange,banana");
        List<String> expected = Collections.singletonList("apple,orange,banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults_WhitespaceExists_ReturnsSplitStringWithTrimmedResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" apple, orange , banana ");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults_WhitespaceNotExists_ReturnsSplitString() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split("apple,orange,banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplitToList_SeparatorExists_ReturnsSplitStringAsList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("apple,orange,banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplitToList_SeparatorDoesNotExist_ReturnsOriginalStringAsList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("apple.orange.banana");
        List<String> expected = Collections.singletonList("apple.orange.banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator_ValidKeyValueSeparator_ReturnsMapSplitter() {
        Splitter splitter = Splitter.on(":");
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        assertNotNull(mapSplitter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithKeyValueSeparator_InvalidKeyValueSeparator_ThrowsIllegalArgumentException() {
        Splitter splitter = Splitter.on(":");
        splitter.withKeyValueSeparator("");
    }
    
    @Test
    public void testMapSplitter_SplitStringIsValid_ReturnsMap() {
        Splitter outerSplitter = Splitter.on(";").omitEmptyStrings();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1;key2=value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitter_SplitStringHasDuplicateKey_ThrowsIllegalArgumentException() {
        Splitter outerSplitter = Splitter.on(";").omitEmptyStrings();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator("=");
        mapSplitter.split("key1=value1;key2=value2;key1=value3");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitter_SplitStringDoesNotHaveValidEntry_ThrowsIllegalArgumentException() {
        Splitter outerSplitter = Splitter.on(";").omitEmptyStrings();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator("=");
        mapSplitter.split("key1;key2=value2");
    }
}
