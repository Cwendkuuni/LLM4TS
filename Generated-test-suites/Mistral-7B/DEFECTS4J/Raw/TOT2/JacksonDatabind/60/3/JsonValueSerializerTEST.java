import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        Class<?> testClass = JsonValueSerializer.class.getDeclaredClass();
        Method method = testClass.getDeclaredMethod("createContextual", SerializerProvider.class, BeanProperty.class);
        method.setAccessible(true);
        jsonValueSerializer = (JsonValueSerializer) method.invoke(null, objectMapper.getSerializerProvider(), null);
    }

    @Test
    public void testBasicFunctionality() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("test", 123);

        String json = objectMapper.writeValueAsString(input);

        JsonNode actual = objectMapper.readTree(json);
        JsonNode expected = objectMapper.createObjectNode();
        expected.put("test", 123);

        Assert.assertEquals(expected, actual);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSerializer(Object.class, jsonValueSerializer);

        json = mapper.writeValueAsString(input);

        actual = mapper.readTree(json);
        expected = objectMapper.createObjectNode();
        expected.put("test", 123);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullInput() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();

        String json = objectMapper.writeValueAsString(input);

        jsonValueSerializer.serialize(null, objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValue() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("test", null);

        String json = objectMapper.writeValueAsString(input);

        jsonValueSerializer.serialize(new Object(), objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionThrownByValueSerializer() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("test", new Object());

        JsonValueSerializer valueSerializer = new JsonValueSerializer(null, null, null, true);
        JsonValueSerializer contextualSerializer = jsonValueSerializer.withResolved(null, valueSerializer, true);

        String json = objectMapper.writeValueAsString(input);

        contextualSerializer.serialize(new Object(), objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testStackOverflowError() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("test", new Object() {
            public Object getValue() throws Exception {
                throw new RuntimeException("Stack Overflow Error");
            }
        });

        String json = objectMapper.writeValueAsString(input);

        jsonValueSerializer.serialize(new Object(), objectMapper.getFactory().createGenerator(), objectMapper.getSerializerProvider());
    }

    @Test
    public void testEdgeCases() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("test1", (byte) 127);
        input.put("test2", Byte.MIN_VALUE);
        input.put("test3", (short) 32767);
        input.put("test4", Short.MIN_VALUE);
        input.put("test5", 0);
        input.put("test6", Integer.MAX_VALUE);
        input.put("test7", Long.MIN_VALUE);
        input.put("test8", Long.MAX_VALUE);
        input.put("test9", Float.NaN);
        input.put("test10", Float.NEGATIVE_INFINITY);
        input.put("test11", Double.NaN);
        input.put("test12", Double.NEGATIVE_INFINITY);

        String json = objectMapper.writeValueAsString(input);

        ObjectNode actual = objectMapper.readTree(json);
        ObjectNode expected = objectMapper.createObjectNode();
        expected.put("test1", (byte) 127);
        expected.put("test2", Byte.MIN_VALUE);
        expected.put("test3", (short) 32767);
        expected.put("test4", Short.MIN_VALUE);
        expected.put("test5", 0);
        expected.put("test6", Integer.MAX_VALUE);
        expected.put("test7", Long.MIN_VALUE);
        expected.put("test8", Long.MAX_VALUE);
        expected.put("test9", Float.NaN);
        expected.put("test10", Float.NEGATIVE_INFINITY);
        expected.put("test11", Double.NaN);
        expected.put("test12", Double.NEGATIVE_INFINITY);

        Assert.assertEquals(expected, actual);
    }
}