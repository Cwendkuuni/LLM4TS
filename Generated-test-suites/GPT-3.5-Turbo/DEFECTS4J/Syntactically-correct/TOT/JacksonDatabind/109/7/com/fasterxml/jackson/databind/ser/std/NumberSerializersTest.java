import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        Assert.assertNotNull(allDeserializers);

        // Test if all expected serializers are present in the map
        Assert.assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        Assert.assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        Assert.assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        Assert.assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        Assert.assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        Assert.assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        Assert.assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        Assert.assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        Assert.assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        Assert.assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
        Assert.assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        Assert.assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 42;
        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"42\"", json);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 42;
        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("integer", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("42", json);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 42;
        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("integer", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("42", json);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 42L;
        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("42", json);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 42.0f;
        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("42.0", json);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 42.0;
        JsonNode schema = serializer.getSchema(null, null);
        Assert.assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("42.0", json);
    }
}