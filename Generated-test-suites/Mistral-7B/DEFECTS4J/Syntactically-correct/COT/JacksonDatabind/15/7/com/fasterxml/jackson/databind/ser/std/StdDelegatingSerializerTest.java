import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Assert.assertEquals(Object.class, serializer.getDelegatee().getDelegateType());
    }

    @Test
    public void testConstructorWithConverterAndClass() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, null);
        Assert.assertEquals(Object.class, serializer.getDelegatee().getDelegateType());
    }

    @Test
    public void testConstructorWithConvertAndType() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        Assert.assertEquals(Object.class, serializer.getDelegatee().getDelegateType());

        ObjectNode input = ObjectMapper.createObjectNode();
        input.put("delegateType", "java.lang.String");
        input.put("converter", "com.mock.StringToIntegerConverter");

        ObjectMapper mapper = new ObjectMapper();
        try {
            serializer = mapper.readerForUpdating(StdDelegatingSerializer.class)
                    .readValue(input);
            Type delegateType = serializer.getClass().getDeclaredField("_delegateType")
                    .get(serializer);
            Assert.assertEquals(String.class, delegateType.getType());
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResolve() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) {
                if (String.class.isAssignableFrom(type)) {
                    return objectMapper.findValueSerializer(String.class);
                }
                return null;
            }

            @Override
            public JsonSerializer<?> handleSecondaryContextualization(JsonSerializer<?> serializer, BeanProperty property) {
                return serializer;
            }
        };
        serializer.resolve(provider);
        JsonSerializer<?> delegateSerializer = serializer.getDelegatee();
        Assert.assertNotNull(delegateSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) {
                if (String.class.isAssignableFrom(type)) {
                    return objectMapper.findValueSerializer(String.class);
                }
                return null;
            }

            @Override
            public JsonSerializer<?> handleSecondaryContextualization(JsonSerializer<?> serializer, BeanProperty property) {
                return serializer;
            }
        };
        JsonSerializer<?> createdSerializer = serializer.createContextual(provider, null);
        Assert.assertSame(serializer, createdSerializer);

        // Test with a custom serializer
        JsonSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        JsonSerializer<Integer> intSerializer = new IntSerializer(); // Custom serializer
        serializer = new StdDelegatingSerializer(null, String.class, stringSerializer);
        createdSerializer = serializer.createContextual(provider, null);
        Assert.assertSame(intSerializer, createdSerializer);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, String.class, objectMapper.findValueSerializer(String.class));
        Object obj = "Test";
        String expected = "\"Test\"";
        JsonGenerator gen = objectMapper.getFactory().createGenerator();
        serializer.serialize(obj, gen, new SerializerProvider() {});
        gen.flush();
        String actual = gen.getOutputGCString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, String.class, objectMapper.findValueSerializer(String.class));
        Object obj = "Test";
        String expected = "{\"@type\":\"String\"}" + "\"Test\"";
        JsonGenerator gen = objectMapper.getFactory().createGenerator();
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        serializer.serializeWithType(obj, gen, new SerializerProvider(), typeSerializer);
        gen.flush();
        String actual = gen.getOutputGCString();
        ObjectNode jsonNode = ObjectMapper.readTree(actual);
        Assert.assertEquals(expected, jsonNode.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithNullValue() throws JsonGenerationException, JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, String.class, objectMapper.findValueSerializer(String.class));
        Object obj = null;
        JsonGenerator gen = objectMapper.getFactory().createGenerator();
        serializer.serialize(obj, gen, new SerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithInvalidClass() throws JsonGenerationException, JsonMappingException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, Integer.class, objectMapper.findValueSerializer(String.class));
        Object obj = "Test";
        JsonGenerator gen = objectMapper.getFactory().createGenerator();
        serializer.serialize(obj, gen, new SerializerProvider());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, String.class, objectMapper.findValueSerializer(String.class));
        Assert.assertNull(serializer.getSchema(new SerializerProvider(), null));
        JsonSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        serializer = new StdDelegatingSerializer(null, String.class, stringSerializer);
        Assert.assertEquals(stringSerializer.getSchema(new SerializerProvider(), null), serializer.getSchema(new SerializerProvider(), null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, String.class, objectMapper.findValueSerializer(String.class));
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }
}

class IntSerializer extends StdSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.intValue());
    }
}