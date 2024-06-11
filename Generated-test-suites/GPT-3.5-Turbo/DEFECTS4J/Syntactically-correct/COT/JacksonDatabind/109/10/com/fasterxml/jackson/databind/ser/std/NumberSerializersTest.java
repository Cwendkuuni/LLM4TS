
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Check if all the required serializers are added
        assertEquals(9, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        JsonNode schema = shortSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "12";
        String actualJson = mapper.writeValueAsString((short) 12);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);

        JsonNode schema = integerSerializer.getSchema(null, null);
        assertEquals("integer", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "55";
        String actualJson = mapper.writeValueAsString(55);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();

        JsonNode schema = intLikeSerializer.getSchema(null, null);
        assertEquals("integer", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "123";
        String actualJson = mapper.writeValueAsString(123);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);

        JsonNode schema = longSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "789";
        String actualJson = mapper.writeValueAsString(789L);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();

        JsonNode schema = floatSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "1.23";
        String actualJson = mapper.writeValueAsString(1.23f);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);

        JsonNode schema = doubleSerializer.getSchema(null, null);
        assertEquals("number", schema.get("type").asText());

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = "4.56";
        String actualJson = mapper.writeValueAsString(4.56);
        assertEquals(expectedJson, actualJson);
    }

}
