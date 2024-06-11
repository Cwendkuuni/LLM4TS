import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;
    
    @Test
    public void testDeserialize_AllStrings() throws IOException {
        String json = "[\"string1\", \"string2\", \"string3\"]";
        String[] expected = {"string1", "string2", "string3"};
        
        stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(json);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_EdgeCases() throws IOException {
        String json = "[]";
        String[] expected = {};
        
        stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(json);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_NullValues() throws IOException {
        String json = "[null, null, null]";
        String[] expected = {null, null, null};
        
        stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(json);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_MixedValues() throws IOException {
        String json = "[\"string1\", null, \"string2\", \"string3\", null]";
        String[] expected = {"string1", null, "string2", "string3", null};
        
        stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(json);
        
        assertArrayEquals(expected, result);
    }
    
    @Test(expected=IOException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        String json = "invalidJson";
        
        stringArrayDeserializer = new StringArrayDeserializer();
        stringArrayDeserializer.deserialize(json);
    }
    
}