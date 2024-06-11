import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
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

    // Test for constructor with Converter
    @Test
    public void testStdDelegatingSerializerWithConverter() throws JsonMappingException, JsonGenerationException {
        Converter<Object, Object> converter = new Converter.StdConverter<Object, Object>() {
            @Override
            public Object convert(Object src) {
                return src.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object() {
        };
        String expectedResult = value.toString();

        String result = serializeAndDeserialize(serializer, value);

        Assert.assertEquals(expectedResult, result);
    }

    // Test for constructor with Class and Converter
    @Test
    public void testStdDelegatingSerializerWithClassAndConverter() throws JsonMappingException, JsonGenerationException {
        Class<String> stringClass = String.class;
        Converter<String, Object> converter = new Converter.StdConverter<String, Object>() {
            @Override
            public String convert(Object src) {
                return (String) src;
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(stringClass, converter);
        String value = "Test Value";
        String expectedResult = value;

        String result = serializeAndDeserialize(serializer, value);

        Assert.assertEquals(expectedResult, result);
    }

    // Test for constructor with Converter, JavaType, and JsonSerializer
    @Test
    public void testStdDelegatingSerializerWithConverterJavaTypeAndJsonSerializer() throws JsonMappingException, JsonGenerationException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }
        };
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        StdSerializer<Integer> intSerializer = objectMapper.findValueSerializer(intType);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, intType, intSerializer);
        Integer value = 1;
        Integer expectedResult = value;

        Integer result = serializeAndGetIntResult(serializer, value);

        Assert.assertEquals(expectedResult, result);
    }

    // Test for method serialize
    @Test
    public void testSerialize() throws JsonMappingException, JsonGenerationException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }
        };
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        StdSerializer<Integer> intSerializer = objectMapper.findValueSerializer(intType);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, intType, intSerializer);
        Integer value = 1;
        Integer expectedResult = value;

        Integer result = serializeAndGetIntResult(serializer, value);

        Assert.assertEquals(expectedResult, result);
    }

    // Test for method isEmpty
    @Test
    public void testIsEmpty() throws JsonMappingException {
        Converter<Object, Boolean> converter = new Converter.StdConverter<Object, Boolean>() {
            @Override
            public Boolean convert(Object src) {
                return null != src;
            }
        };
        JavaType booleanType = objectMapper.getTypeFactory().constructType(Boolean.class);
        StdSerializer<Boolean> boolSerializer = objectMapper.findValueSerializer(booleanType);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, booleanType, boolSerializer);
        Object value = new Object();

        boolean empty = serializer.isEmpty(null);
        boolean notEmpty = !empty;
        Object notEmptyValue = new Object();

        boolean result = serializer.isEmpty(new SerializerProvider() {}, value);

        Assert.assertEquals(empty, result);
        Assert.assertEquals(notEmpty, !serializer.isEmpty(new SerializerProvider() {}, notEmptyValue));
    }

    // Test for method getSchema
    @Test
    public void testGetSchema() throws JsonMappingException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }
        };
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        StdSerializer<Integer> intSerializer = objectMapper.findValueSerializer(intType);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, intType, intSerializer);

        JsonNode schema = serializer.getSchema(new SerializerProvider(), intType);

        Assert.assertNotNull(schema);
    }

    // Test for method acceptJsonFormatVisitor
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }
        };
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        StdSerializer<Integer> intSerializer = objectMapper.findValueSerializer(intType);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, intType, intSerializer);

        JsonFormatVisitorWrapper visitor = null;

        serializer.acceptJsonFormatVisitor(visitor, intType);
    }

    private <T> T serializeAndDeserialize(JsonSerializer<T> serializer, T value) throws JsonMappingException, JsonGenerationException {
        String jsonString = toJsonString(serializer, value);
        return (T) fromJsonString(jsonString, serializer.getClass());
    }

    private int serializeAndGetIntResult(JsonSerializer<Integer> serializer, Integer value) throws JsonMappingException, JsonGenerationException {
        String jsonString = toJsonString(serializer, value);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, Integer.class);
    }

    private String toJsonString(JsonSerializer<?> serializer, Object value) throws JsonMappingException, JsonGenerationException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = objectMapper.getFactory()
                .createGenerator(stringWriter);
        serializer.serialize(value, generator, new SerializerProvider());
        generator.flush();
        generator.close();
        return stringWriter.toString();
    }

    private <T> T fromJsonString(String jsonString, Class<T> clazz) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, clazz);
    }
}