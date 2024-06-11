
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class StringCollectionDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        String json = "[\"string1\", \"string2\", \"string3\"]";
        Collection<String> expectedResult = Arrays.asList("string1", "string2", "string3");
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json));
        
        assertEquals(expectedResult, result);
    }
    
    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        String json = "\"string1\", \"string2\", \"string3\"";
        ObjectMapper mapper = new ObjectMapper();
        deserializer.deserialize(mapper.getFactory().createParser(json));
    }
    
    @Test(expected = MismatchedInputException.class)
    public void testDeserialize_NonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        String json = "\"string1\"";
        ObjectMapper mapper = new ObjectMapper();
        deserializer.deserialize(mapper.getFactory().createParser(json));
    }
    
    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        String json = "[]";
        Collection<String> expectedResult = Collections.emptyList();
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json));
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserialize_NullValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        String json = "[\"string1\", null, \"string3\"]";
        Collection<String> expectedResult = Arrays.asList("string1", null, "string3");
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json));
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserialize_WithEmptyValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        
        String json = "[\"string1\", \"\", \"string3\"]";
        Collection<String> expectedResult = Arrays.asList("string1", "", "string3");
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json));
        
        assertEquals(expectedResult, result);
    }
    
}
