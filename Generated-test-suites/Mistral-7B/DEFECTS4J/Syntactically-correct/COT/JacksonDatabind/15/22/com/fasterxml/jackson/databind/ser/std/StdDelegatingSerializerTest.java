import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // Test case for constructor with Converter
    @Test
    public void testStdDelegatingSerializerConstructorWithConverter() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertNotNull(serializer);
    }

    // Test case for constructor with Class and Converter
    @Test
    public void testStdDelegatingSerializerConstructorWithClassAndConverter() throws Exception {
        Converter<String, Object> converter = new SimpleStringConverter();
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<>(String.class, converter);
        Assert.assertNotNull(serializer);
    }

    // Test case for constructor with Converter, JavaType, and JsonSerializer
    @Test
    public void testStdDelegatingSerializerConstructorWithConverterJavaTypeAndJsonSerializer() throws Exception {
        Converter<String, Integer> converter = new SimpleStringToIntConverter();
        JavaType javaType = objectMapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Integer> jsonSerializer = objectMapper.findValueSerializer(javaType);
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<>(converter, javaType, jsonSerializer);
        Assert.assertNotNull(serializer);
    }

    // Test case for serialize method with null value
    @Test
    public void testSerializeWithNullValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        Object nullValue = null;
        byte[] result = serializer.serialize(nullValue, objectMapper.getFactory().createGenerator(objectMapper), objectMapper.getSerializerProvider());
        Assert.assertArrayEquals(new byte[0], result);
    }

    // Test case for serialize method with valid value
    @Test
    public void testSerializeWithValidValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        String value = "test";
        byte[] result = serializer.serialize(value, objectMapper.getFactory().createGenerator(objectMapper), objectMapper.getSerializerProvider());
        Assert.assertArrayNotEquals(new byte[0], result);
    }

    // Test case for serializeWithType method with null value
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithTypeWithNullValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        Object value = null;
        serializer.serializeWithType(value, objectMapper.getFactory().createGenerator(objectMapper), objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
    }

    // Test case for serializeWithType method with valid value
    @Test
    public void testSerializeWithTypeWithValidValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        String value = "test";
        byte[] result = serializer.serializeWithType(value, objectMapper.getFactory().createGenerator(objectMapper), objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        Assert.assertArrayNotEquals(new byte[0], result);
    }

    // Test case for isEmpty method with null value
    @Test
    public void testIsEmptyWithNullValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        Object value = null;
        boolean result = serializer.isEmpty(value);
        Assert.assertTrue(result);
    }

    // Test case for isEmpty method with empty list value
    @Test
    public void testIsEmptyWithEmptyListValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<List<Object>> serializer = new StdDelegatingSerializer<>(converter);
        List<Object> value = new ArrayList<>();
        boolean result = serializer.isEmpty(value);
        Assert.assertTrue(result);
    }

    // Test case for isEmpty method with non-empty list value
    @Test
    public void testIsEmptyWithNonEmptyListValue() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<List<Object>> serializer = new StdDelegatingSerializer<>(converter);
        List<Object> value = new ArrayList<>();
        value.add("test");
        boolean result = serializer.isEmpty(value);
        Assert.assertFalse(result);
    }

    // Test case for getSchema method
    @Test
    public void testGetSchema() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        JsonNode result = serializer.getSchema(objectMapper.getSerializerProvider(), Object.class.getType());
        Assert.assertNotNull(result);
    }

    // Test case for acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitorWrapper = mapper.schemaFactory().findSchemaVisitor(Object.class, null, null);
        serializer.acceptJsonFormatVisitor(visitorWrapper, Object.class.getType());
    }
}

class SimpleStringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}

class SimpleStringToIntConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String text) {
        return Integer.parseInt(text);
    }
}