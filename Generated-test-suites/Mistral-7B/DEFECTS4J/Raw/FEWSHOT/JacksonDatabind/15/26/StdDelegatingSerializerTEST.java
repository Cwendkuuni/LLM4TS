package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public class StdDelegatingSerializerTest {

    @Test
    public void testEmptyConstructor() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        assertNull(serializer.getConverter());
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testConstructorWithConverter() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        assertEquals(converter, serializer.getConverter());
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testConstructorWithClass() throws Exception {
        Converter<String, Number> converter = new StdConverter<>();
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer(String.class, converter);
        assertEquals(converter, serializer.getConverter());
        assertEquals(String.class, serializer.getType());
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testConstructorWithFullConstructor() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        JavaType delegateType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(String.class) {};
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter, delegateType, delegateSerializer);
        assertEquals(converter, serializer.getConverter());
        assertEquals(delegateType, serializer._delegateType);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }

    @Test
    public void testResolve() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        SerializerProvider provider = new SerializerProvider();
        serializer.resolve(provider);

        JsonSerializer<Object> delegateSerializer = serializer._delegateSerializer;
        assertEquals(delegateSerializer, provider.findValueSerializer(String.class));
    }

    @Test
    public void testCreateContextual() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        SerializerProvider provider = new SerializerProvider();
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(String.class) {};
        StdDelegatingSerializer<Object> contextualSerializer = serializer.createContextual(provider, new BeanProperty(""));

        assertEquals(delegateSerializer, contextualSerializer._delegateSerializer);
        assertNotSame(serializer, contextualSerializer);
    }

    @Test
    public void testSerialize() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        ObjectMapper mapper = new ObjectMapper();
        String input = "hello";
        Object value = mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);

        StringWriter writer = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(writer);

        serializer.serialize(value, gen, new SerializerProvider());

        assertEquals("hello", writer.toString());
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        ObjectMapper mapper = new ObjectMapper();
        String input = "hello";
        Object value = mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);

        StringWriter writer = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(writer);
        TypeSerializer typeSerializer = mapper.getTypeSerializers().findValueSerializer(String.class, null);

        serializer.serializeWithType(value, gen, new SerializerProvider(), typeSerializer);

        assertEquals("\"hello\"", writer.toString());
    }

    @Test
    public void testIsEmpty() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        ObjectMapper mapper = new ObjectMapper();

        assertTrue(serializer.isEmpty(mapper.getFactory().createGenerator(new StringWriter()), new SerializerProvider()));
        assertFalse(serializer.isEmpty(mapper.readValue(new ByteArrayInputStream("1".getBytes()), Object.class), new SerializerProvider()));
    }

    @Test
    public void testGetSchema() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), String.class.getType());
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Converter<Object, String> converter = new StdConverter<>();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonFormatVisitorWrapper visitor = factory.getSerializerProvider().getJsonFormatVisitor(factory.getPolymorphicTypeValidator(), String.class);

        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    static class StdConverter<T> implements Converter<Object, T> {
        @Override
        public T convert(Object value) {
            return (T) value;
        }
    }
}