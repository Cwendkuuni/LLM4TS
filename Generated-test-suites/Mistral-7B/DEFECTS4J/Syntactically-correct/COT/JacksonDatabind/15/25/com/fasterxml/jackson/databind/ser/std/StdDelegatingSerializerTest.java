import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // Test Case for constructor with Converter only
    @Test
    public void testStdDelegatingSerializerWithConverter() throws Exception {
        Converter<Object, ?> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);
        Assert.assertNotNull(delegatingSerializer);
    }

    // Test Case for constructor with class and Converter
    @Test
    public void testStdDelegatingSerializerWithClassAndConverter() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(Integer.class, converter);
        Assert.assertNotNull(delegatingSerializer);
    }

    // Test Case for constructor with Converter, JavaType, and JsonSerializer
    @Test
    public void testStdDelegatingSerializerWithConverterJavaTypeAndJsonSerializer() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        JsonSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class) {};
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, JavaType.constructType(Integer.class), integerSerializer);
        Assert.assertNotNull(delegatingSerializer);
    }

    // Test Case for serialize method with simple data
    @Test
    public void testSerializeWithSimpleData() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Integer.class);
        delegatingSerializer._delegateSerializer = serializer;
        delegatingSerializer._delegateType = JavaType.constructType(Integer.class);
        Integer value = 42;
        byte[] serializedData = delegatingSerializer.serialize(value);
        Object deserializedData = mapper.readValue(new ByteArrayInputStream(serializedData), Integer.class);
        Assert.assertEquals(value, deserializedData);
    }

    // Test Case for serialize method with null data
    @Test
    public void testSerializeWithNullData() throws Exception {
        Converter<Object, String> converter = new Converter.StdConverter<Object, String>() {
            @Override
            public String convert(Object src) {
                return null;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(String.class);
        delegatingSerializer._delegateSerializer = serializer;
        delegatingSerializer._delegateType = JavaType.constructType(String.class);
        Object value = null;
        byte[] serializedData = delegatingSerializer.serialize(value);
        String deserializedData = mapper.readValue(new ByteArrayInputStream(serializedData), String.class);
        Assert.assertNull(deserializedData);
    }

    // Test Case for acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
        delegatingSerializer._delegateSerializer = integerSerializer;
        delegatingSerializer._delegateType = JavaType.constructType(Integer.class);
        AnnotatedClass ac = mapper.getIntrospect().findClassAnnotations(Integer.class);
        JsonFormatVisitorWrapper visitor = ac.findAnnotation(JsonFormat.class)
                .map(annotation -> new JsonArrayFormatVisitor(mapper.getSerializationConfig()))
                .orElseGet(() -> mapper.getSerializationConfig().getDefaultSerializerProvider().getFactory()
                        .findRootValueSerializer(JavaType.constructType(Integer.class)));
        delegatingSerializer.acceptJsonFormatVisitor(visitor, JavaType.constructType(Integer.class));
    }

    // Test Case for getSchema method with simple data
    @Test
    public void testGetSchemaWithSimpleData() throws Exception {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object src) {
                return (Integer) src;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = new ObjectMapper();
        JavaType delegateType = JavaType.constructType(Integer.class);
        JsonNode schema = delegatingSerializer.getSchema(mapper.getSerializationConfig(), delegateType);
        Assert.assertNotNull(schema);
    }

    // Test Case for getSchema method with null data
    @Test
    public void testGetSchemaWithNullData() throws Exception {
        Converter<Object, String> converter = new Converter.StdConverter<Object, String>() {
            @Override
            public String convert(Object src) {
                return null;
            }

            @Override
            public Object convertAndSetAll(Object src, StdSerializer<Object> serializer, TypeSerializer typeSerializer) {
                return converter.convert(src);
            }
        };
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = new ObjectMapper();
        JavaType delegateType = JavaType.constructType(String.class);
        JsonNode schema = delegatingSerializer.getSchema(mapper.getSerializationConfig(), delegateType);
        Assert.assertNull(schema);
    }
}