
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(10, allDeserializers.size());

        Assert.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));

        Assert.assertTrue(allDeserializers.get(Integer.class.getName()) instanceof IntegerSerializer);
        Assert.assertTrue(allDeserializers.get(Integer.TYPE.getName()) instanceof IntegerSerializer);
        Assert.assertTrue(allDeserializers.get(Long.class.getName()) instanceof LongSerializer);
        Assert.assertTrue(allDeserializers.get(Long.TYPE.getName()) instanceof LongSerializer);
        Assert.assertTrue(allDeserializers.get(Byte.class.getName()) instanceof IntLikeSerializer);
        Assert.assertTrue(allDeserializers.get(Byte.TYPE.getName()) instanceof IntLikeSerializer);
        Assert.assertTrue(allDeserializers.get(Short.class.getName()) instanceof ShortSerializer);
        Assert.assertTrue(allDeserializers.get(Short.TYPE.getName()) instanceof ShortSerializer);
        Assert.assertTrue(allDeserializers.get(Double.class.getName()) instanceof DoubleSerializer);
        Assert.assertTrue(allDeserializers.get(Double.TYPE.getName()) instanceof DoubleSerializer);
        Assert.assertTrue(allDeserializers.get(Float.class.getName()) instanceof FloatSerializer);
        Assert.assertTrue(allDeserializers.get(Float.TYPE.getName()) instanceof FloatSerializer);
    }

    @Test
    public void testBaseClassMethods() {
        // Mock objects for test
        Class<?> cls = mock(Class.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JavaType javaType = mock(JavaType.class);
        BeanProperty beanProperty = mock(BeanProperty.class);

        // Create instances for test
        NumberSerializers.Base<?> base = new NumberSerializers.Base<>(cls, mock(JsonParser.NumberType.class), "number");

        // Test getSchema method
        JsonNode schema = base.getSchema(serializerProvider, mock(Type.class));
        Assert.assertNotNull(schema);

        // Test acceptJsonFormatVisitor method
        try {
            base.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), javaType);
        } catch (JsonMappingException e) {
            Assert.fail("Should not throw an exception");
        }

        // Test createContextual method
        try {
            JsonSerializer<?> contextualSerializer = base.createContextual(serializerProvider, beanProperty);
            Assert.assertEquals(base, contextualSerializer);
        } catch (JsonMappingException e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test
    public void testShortSerializer() throws IOException {
        Object value = 1;
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        Object value = 2;
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        serializer.serialize(value, jsonGenerator, serializerProvider);
        serializer.serializeWithType(value, jsonGenerator, serializerProvider, null);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        Object value = 3;
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        Object value = 4L;
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        Object value = 5.0f;
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        Object value = 6.0;
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        serializer.serialize(value, jsonGenerator, serializerProvider);
        serializer.serializeWithType(value, jsonGenerator, serializerProvider, null);
    }
}

