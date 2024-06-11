import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NumberSerializersTest {

    @DisplayName("Test addAll method")
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);

        Assert.assertEquals(8, deserializers.size());
        Assert.assertNotNull(deserializers.get(Integer.class.getName()));
        Assert.assertNotNull(deserializers.get(Integer.TYPE.getName()));
        Assert.assertNotNull(deserializers.get(Long.class.getName()));
        Assert.assertNotNull(deserializers.get(Long.TYPE.getName()));
        Assert.assertNotNull(deserializers.get(Byte.class.getName()));
        Assert.assertNotNull(deserializers.get(Byte.TYPE.getName()));
        Assert.assertNotNull(deserializers.get(Short.class.getName()));
        Assert.assertNotNull(deserializers.get(Short.TYPE.getName()));
    }

    // Add tests for each concrete serializer method here...

    @DisplayName("Test ShortSerializer serialization")
    @Test
    public void testShortSerializerSerialization() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
        Short value = Short.MIN_VALUE;

        ObjectMapper mapper = new ObjectMapper();
        byte[] serialized = mapper.writeValueAsBytes(value);
        Short deserialized = mapper.readValue(new ByteArrayInputStream(serialized), Short.class);

        assertEquals(value, deserialized);
    }

    @DisplayName("Test IntegerSerializer serialization")
    @Test
    public void testIntegerSerializerSerialization() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer value = Integer.MIN_VALUE;

        ObjectMapper mapper = new ObjectMapper();
        byte[] serialized = mapper.writeValueAsBytes(value);
        Integer deserialized = mapper.readValue(new ByteArrayInputStream(serialized), new TypeReference<Integer>() {});

        assertEquals(value, deserialized);
    }

    // Add tests for other concrete serializer methods here...

    @DisplayName("Test serializeWithType method for IntegerSerializer")
    @Test
    public void testIntegerSerializerSerializeWithType() throws JsonProcessingException {
        JsonSerializer<Object> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer value = Integer.MIN_VALUE;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode serialized = mapper.valueToTree(value);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        integerSerializer.serializeWithType(value, gen, mapper.getSerializerProvider(), typeSerializer);
        gen.flush();
        byte[] serializedData = ((ByteArrayOutputStream) gen.getOutputState()).toByteArray();

        JsonNode deserialized = mapper.readTree(new ByteArrayInputStream(serializedData));
        Integer deserializedInt = mapper.treeToValue(deserialized, new TypeReference<Integer>() {});

        assertEquals(value, deserializedInt);
    }

    // Add tests for other concrete serializer methods' serializeWithType...
}