package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToTest2;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Test cases for {@link StdDelegatingSerializer}.
 */
public class StdDelegatingSerializerTest {

    @Test
    public void testDefaultConstructor() {
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        assertNull(serializer._converter);
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testConverterConstructor() {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertEquals(converter, serializer._converter);
        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testTypeConstructor() {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(String.class, converter);
        assertEquals(javaType, serializer._delegateType);
        assertEquals(converter, serializer._converter);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testFullyInitializedConstructor() {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, JavaType.constructType(String.class), delegateSerializer);
        assertEquals(javaType, serializer._delegateType);
        assertEquals(converter, serializer._converter);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        final JsonSerializer<Object> createdSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertEquals(delegateSerializer, createdSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        serializer.resolve(mapper.getSerializerProvider());
        assertNotNull(serializer._delegateSerializer);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        final Object delegateValue = converter.convert("foo");
        final StringWriter stringWriter = new StringWriter();
        final JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        serializer.serialize(delegateValue, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("\"foo\"", stringWriter.toString());
    }

    @Test
    public void testSerializeWithType() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        final Object delegateValue = converter.convert("foo");
        final StringWriter stringWriter = new StringWriter();
        final JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        final TypeSerializer typeSerializer = mapper.getSerializerProvider().findTypeSerializer(String.class,Object.class);
        serializer.serializeWithType(delegateValue, jsonGenerator, mapper.getSerializerProvider(), typeSerializer);
        assertEquals("\"foo\"", stringWriter.toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        final SchemaFactory schemaFactory = mapper.getSerializerProvider().getSchemaFactory();
        assertEquals(SchemaFactory.EmptySchema.class, serializer.getSchema(mapper.getSerializerProvider(), String.class.getType()).getClass());
        if (delegateSerializer instanceof SchemaAware) {
            final Schema schema = ((SchemaAware)delegateSerializer).getSchema(mapper.getSerializerProvider(), String.class.getType());
            assertEquals(SchemaFactory.EmptySchema.class, schema.getSchemaProvider().getSchema(String.class.getType()).getClass());
            assertEquals(Schema.Type.STRING, schema.getType());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        final JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultVisitors();
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
        if (delegateSerializer instanceof JsonFormatVisitable) {
            final JsonFormatVisitorWrapper visitor2 = mapper.getSerializerProvider().getDefaultVisitors();
            ((JsonFormatVisitable) delegateSerializer).acceptJsonFormatVisitor(visitor2, String.class.getType());
            assertEquals(visitor2, serializer.acceptJsonFormatVisitor(visitor, String.class.getType()));
        }
    }

    @Test
    public void testConvertValue() {
        final ObjectMapper mapper = new ObjectMapper();
        final Converter<Object, String> converter = mapper.getTypeFactory().constructConverter(Object.class, String.class);
        final StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        Object value = convertObjectToTest2();
        Object delegateValue = serializer.convertValue(value);
        assertEquals(value.getClass(), delegateValue.getClass());
        assertEquals(value.toString(), converter.convert(value).toString());
    }
}