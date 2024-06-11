import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SplitterTest {
    
    @Test
    public void testOn_char() {
        // Arrange
        char separator = ';';
        String input = "abc;def;ghi";
        Splitter splitter = Splitter.on(separator);
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testOn_CharMatcher() {
        // Arrange
        String input = "abc,def,ghi";
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testOn_String() {
        // Arrange
        String separator = ",";
        String input = "abc,def,ghi";
        Splitter splitter = Splitter.on(separator);
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testOn_Pattern() {
        // Arrange
        String pattern = "\\s";
        String input = "abc def ghi";
        Splitter splitter = Splitter.onPattern(pattern);
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testFixedLength() {
        // Arrange
        int length = 3;
        String input = "abcdefghi";
        Splitter splitter = Splitter.fixedLength(length);
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testOmitEmptyStrings() {
        // Arrange
        String input = "abc,,def,,ghi";
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testLimit() {
        // Arrange
        String input = "abc,def,ghi,jkl,mno";
        Splitter splitter = Splitter.on(",").limit(3);
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi,jkl,mno]");
    }
    
    @Test
    public void testTrimResults() {
        // Arrange
        String input = " abc , def , ghi ";
        Splitter splitter = Splitter.on(",").trimResults();
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testTrimResults_CharMatcher() {
        // Arrange
        String input = " abc , def , ghi ";
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc , def , ghi]");
    }
    
    @Test
    public void testSplit() {
        // Arrange
        String input = "abc,def,ghi";
        Splitter splitter = Splitter.on(",");
        
        // Act
        Iterable<String> result = splitter.split(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testSplitToList() {
        // Arrange
        String input = "abc,def,ghi";
        Splitter splitter = Splitter.on(",");
        
        // Act
        List<String> result = splitter.splitToList(input);
        
        // Assert
        assertEquals(result.toString(), "[abc, def, ghi]");
    }
    
    @Test
    public void testWithKeyValueSeparator_String() {
        // Arrange
        String input = "key1=value1,key2=value2";
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        
        // Act
        Map<String, String> resultMap = mapSplitter.split(input);
        
        // Assert
        assertEquals(resultMap.size(), 2);
        assertEquals(resultMap.get("key1"), "value1");
        assertEquals(resultMap.get("key2"), "value2");
    }
    
    @Test
    public void testWithKeyValueSeparator_char() {
        // Arrange
        String input = "key1:value1,key2:value2";
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(':');
        
        // Act
        Map<String, String> resultMap = mapSplitter.split(input);
        
        // Assert
        assertEquals(resultMap.size(), 2);
        assertEquals(resultMap.get("key1"), "value1");
        assertEquals(resultMap.get("key2"), "value2");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitter_InvalidEntry() {
        // Arrange
        String input = "abc,def,ghi,jkl";
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator('=');
        
        // Act
        mapSplitter.split(input);
        
        // Assert
        fail("Expected IllegalArgumentException to be thrown");
    }
}