import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123.45}");
        JsonNode expected = mapper.readTree("123.45");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 12345678901234567890}");
        JsonNode expected = mapper.readTree("12345678901234567890");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123}");
        JsonNode expected = mapper.readTree("123");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeLong() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123L}");
        JsonNode expected = mapper.readTree("123");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeDouble() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123.45}");
        JsonNode expected = mapper.readTree("123.45");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeFloat() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123.45f}");
        JsonNode expected = mapper.readTree("123.45");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeByte() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123}");
        JsonNode expected = mapper.readTree("123");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeShort() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": 123}");
        JsonNode expected = mapper.readTree("123");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
    
    @Test
    public void testSerializeOther() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\": \"123\"}");
        JsonNode expected = mapper.readTree("123");
        assertEquals(expected, serializer.serialize(node, null, null));
    }
}