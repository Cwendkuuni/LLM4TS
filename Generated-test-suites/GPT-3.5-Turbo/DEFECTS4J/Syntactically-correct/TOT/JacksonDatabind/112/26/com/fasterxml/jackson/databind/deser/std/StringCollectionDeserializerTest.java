import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {
    
    @Test
    public void testDeserialize() throws Exception {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, Collection.class);
        
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("cherry");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithNullValues() throws Exception {
        String json = "[\"apple\", null, \"cherry\"]";
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, Collection.class);
        
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add(null);
        expected.add("cherry");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        String json = "[]";
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, Collection.class);
        
        List<String> expected = new ArrayList<>();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithSingleValue() throws Exception {
        String json = "\"apple\"";
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, Collection.class);
        
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithInvalidJson() throws Exception {
        String json = "[\"apple\", \"banana\",]";
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            Collection<String> result = mapper.readValue(json, Collection.class);
            fail("Did not throw expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
    
    @Test
    public void testDeserializeWithTypeInformation() throws Exception {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        Collection<String> result = mapper.readValue(json, collectionType);
        
        List<String> expected = new ArrayList<>();
        expected.add("apple");
        expected.add("banana");
        expected.add("cherry");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithCustomDeserializer() throws Exception {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        
        ObjectMapper mapper = new ObjectMapper();
        Collection<String> result = mapper.readValue(json, Collection.class);
        
        List<String> expected = new ArrayList<>();
        expected.add("APPLE");
        expected.add("BANANA");
        expected.add("CHERRY");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSerialize() throws Exception {
        Collection<String> values = new ArrayList<>();
        values.add("apple");
        values.add("banana");
        values.add("cherry");
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(gen, values);
        gen.close();
        
        String result = sw.toString();
        
        assertEquals("[\"apple\",\"banana\",\"cherry\"]", result);
    }
    
    @Test
    public void testSerializeWithNullValues() throws Exception {
        Collection<String> values = new ArrayList<>();
        values.add("apple");
        values.add(null);
        values.add("cherry");
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(gen, values);
        gen.close();
        
        String result = sw.toString();
        
        assertEquals("[\"apple\",null,\"cherry\"]", result);
    }
    
    @Test
    public void testSerializeWithEmptyArray() throws Exception {
        Collection<String> values = new ArrayList<>();
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(gen, values);
        gen.close();
        
        String result = sw.toString();
        
        assertEquals("[]", result);
    }
    
    @Test
    public void testSerializeWithSingleValue() throws Exception {
        Collection<String> values = new ArrayList<>();
        values.add("apple");
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(gen, values);
        gen.close();
        
        String result = sw.toString();
        
        assertEquals("[\"apple\"]", result);
    }
    
    @Test
    public void testSerializeWithTypeInformation() throws Exception {
        Collection<String> values = new ArrayList<>();
        values.add("apple");
        values.add("banana");
        values.add("cherry");
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        mapper.writeValue(gen, mapper.getTypeFactory().constructCollectionType(Collection.class, String.class));
        gen.close();
        
        String result = sw.toString();
        
        assertEquals("[\"apple\",\"banana\",\"cherry\"]", result);
    }
    
    @Test
    public void testSerializeWithCustomSerializer() throws Exception {
        Collection<String> values = new ArrayList<>();
        values.add("apple");
        values.add("banana");
        values.add("cherry");
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(gen, values);
        gen.close();
        
        String result = sw.toString();
        
        assertEquals("[\"APPLE\",\"BANANA\",\"CHERRY\"]", result);
    }
    
}