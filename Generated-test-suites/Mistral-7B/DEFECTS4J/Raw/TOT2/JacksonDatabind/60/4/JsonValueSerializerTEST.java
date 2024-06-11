import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        mapper = new ObjectMapper();
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class, null);
        jsonValueSerializer = new JsonValueSerializer(null, null, stringSerializer, false);
    }

    @Test
    public void testSimpleSerialization() throws JsonProcessingException {
        Object testObject = new TestClass("Test");
        String expected = "\"Test\"";
        String actual = jsonValueSerializer.serializeValueAsString(mapper, testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithNullValue() throws JsonProcessingException {
        Object testObject = new TestClass(null);
        String expected = "null";
        String actual = jsonValueSerializer.serializeValueAsString(mapper, testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithCustomSerializer() throws InstantiationException, IllegalAccessException, JsonProcessingException {
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class, null);
        jsonValueSerializer = new JsonValueSerializer(null, null, intSerializer, false);
        Object testObject = new TestClass(42);
        int expected = 42;
        int actual = jsonValueSerializer.serializeValueAsInt(mapper, testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidAccessMethod() throws InstantiationException, IllegalAccessException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        throw new AssertionError("Expected JsonMappingException");
    }

    @Test
    public void testSerializationWithNaturalTypes() throws JsonProcessingException {
        JsonValueSerializer serializerWithNaturalTypes = new JsonValueSerializer(null, null, null, true);
        Object testObject = 42;
        int expected = 42;
        int actual = serializerWithNaturalTypes.serializeValueAsInt(mapper, testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithArray() throws JsonProcessingException {
        Object testObject = new TestClass[]{new TestClass("Test1"), new TestClass("Test2")};
        String expected = "[\"Test1\",\"Test2\"]";
        String actual = jsonValueSerializer.serializeValueAsString(mapper, testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNonExistentAccessMethod() throws InstantiationException, IllegalAccessException {
        TestClass testObject = new TestClass("Test");
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        try {
            serializer.serializeValue(mapper, testObject, null, null);
        } catch (JsonMappingException e) {
            throw new AssertionError("Expected JsonMappingException");
        }
        throw new AssertionError("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNonPublicAccessMethod() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        TestClass testObject = new TestClass("Test");
        JsonValueSerializer serializer = new JsonValueSerializer(TestClass.class.getDeclaredConstructor(), null, null, false);
        serializer.setAccessible(true);
        try {
            serializer.serializeValue(mapper, testObject, null, null);
        } catch (JsonProcessingException e) {
            throw new AssertionError("Expected JsonMappingException");
        }
        throw new AssertionError("Expected JsonMappingException");
    }

    @Test
    public void testDeserialization() throws IOException {
        String json = "{\"Test\":\"TestValue\"}";
        TestClass testObject = mapper.readValue(json, TestClass.class);
        Assert.assertEquals("TestValue", testObject.getValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidValueType() throws IOException {
        String json = "{42:\"TestValue\"}";
        TestClass testObject = mapper.readValue(json, TestClass.class);
        throw new AssertionError("Expected JsonMappingException");
    }

    private static class TestClass {
        private final String value;

        public TestClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        // Private constructor for testing
        private TestClass(Class<?> clazz) {
            this(null);
        }
    }

    private JsonValueSerializer jsonValueSerializer(JsonSerializer<Object> valueSerializer, BeanProperty property, boolean forceTypeInfo) {
        return new JsonValueSerializer(null, null, valueSerializer, property, forceTypeInfo);
    }

    private int serializeValueAsInt(ObjectMapper mapper, Object testObject) throws JsonProcessingException {
        JsonValueSerializer serializer = jsonValueSerializer(mapper.findValueSerializer(testObject.getClass(), null), null, false);
        return serializer.serializeValueAsInt(mapper, testObject);
    }

    private String serializeValueAsString(ObjectMapper mapper, Object testObject) throws JsonProcessingException {
        JsonValueSerializer serializer = jsonValueSerializer(mapper.findValueSerializer(testObject.getClass(), null), null, false);
        return serializer.serializeValueAsString(mapper, testObject);
    }
}

class JsonValueSerializer {
    // The code of the JsonValueSerializer class
    // ...

    public int serializeValueAsInt(ObjectMapper mapper, Object testObject) throws JsonProcessingException {
        Object value = _accessorMethod.getValue(testObject);
        int serializerResult = _valueSerializer.serializeValueAsInt(mapper, value);
        return serializerResult;
    }

    public String serializeValueAsString(ObjectMapper mapper, Object testObject) throws JsonProcessingException {
        Object value = _accessorMethod.getValue(testObject);
        String serializerResult = _valueSerializer.serializeValueAsString(mapper, value);
        return serializerResult;
    }
}