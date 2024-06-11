package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSerializerTest {

    @Test
    public void testDefaultConstructor() {
        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class);
        assertEquals(Object.class, ser.handledType());
    }

    @Test
    public void testConstructFromJavaType() {
        StdSerializer<Object> ser = new StdSerializer<Object>(ObjectTypeFactory.defaultInstance.constructType(Object.class));
        assertEquals(Object.class, ser.handledType());
    }

    @Test
    public void testConstructFromClassAndBoolean() {
        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class, false);
        assertEquals(Object.class, ser.handledType());
    }

    @Test
    public void testSerialization() throws JsonProcessingException, JsonGenerationException {
        StdSerializer<String> ser = new StdSerializer<String>(String.class);
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        ser.serialize(null, new JsonGenerator(node), new SerializerProvider());
        assertEquals("null", node.toString());

        String value = "foo";
        node = JsonNodeFactory.instance.objectNode();
        ser.serialize(value, new JsonGenerator(node), new SerializerProvider());
        assertEquals("\"foo\"", node.toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class);
        ObjectNode schema = ser.getSchema(new SerializerProvider(), Object.class);
        assertEquals("\"string\"", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        ser.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testWrapAndThrow() {
        SerializerProvider provider = new SerializerProvider();
        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class);
        try {
            ser.wrapAndThrow(provider, new RuntimeException("Test"), null, "");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            ser.wrapAndThrow(provider, new IOException("Test"), null, "");
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            ser.wrapAndThrow(provider, new JsonMappingException("Test"), null, "");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            ser.wrapAndThrow(provider, new Error("Test"), null, "");
            fail();
        } catch (Error e) {
            // expected
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> ser1 = new StdSerializer<Object>(Object.class);
        StdSerializer<Object> ser2 = new com.fasterxml.jackson.databind.JsonSerializer.Std(Object.class);
        assertTrue(ser1.isDefaultSerializer(ser1));
        assertFalse(ser1.isDefaultSerializer(ser2));
        assertFalse(ser2.isDefaultSerializer(ser1));
        assertFalse(ser2.isDefaultSerializer(ser2));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public Converter<Object, Object> findValueSerializer(AnnotatedMember member, JavaType typeHint) {
                return new StdConvertingSerializer<Object, Number>(TypeFactory.defaultInstance.constructType(Number.class));
            }
        };

        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class);
        BeanProperty prop = new BeanProperty("test", Object.class, null, null, null, null, null);
        JsonSerializer<Number> existingSerializer = provider.findValueSerializer(prop.getMember(), NumberTypeFactory.numberType(Long.class));
        JsonSerializer<Object> resultSerializer = ser.findConvertingContentSerializer(provider, prop, existingSerializer);
        assertTrue(resultSerializer instanceof StdDelegatingSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        FilterProvider filters = new SimpleFilterProvider();
        filters.setFilterID("foo", new PropertyFilter() {
            @Override
            public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                gen.writeString("filtered");
            }
        });
        provider.setFilterProvider(filters);

        StdSerializer<Object> ser = new StdSerializer<Object>(Object.class);
        Object filterId = "foo";
        Object valueToFilter = "test";
        PropertyFilter filter = ser.findPropertyFilter(provider, filterId, valueToFilter);
        assertNotNull(filter);
        JsonGenerator gen = new JsonGenerator(new TestJsonFactory());
        filter.serializeAsField(valueToFilter, gen, provider);
        assertEquals("filtered", gen.getOutputBuffer().toString());
    }
}