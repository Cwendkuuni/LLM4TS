package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToObject;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class StdSerializerTest {

    @JsonTest
    public void testIsDefaultSerializer() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(StdSerializer.class.isInstance(serializer.handledType()));
        assertTrue(StdSerializer.class.isAssignableFrom(serializer.handledType().getClass()));
        assertTrue(JsonSerializer.class.isInstance(serializer));
        assertTrue(JsonSerializer.class.isAssignableFrom(serializer.getClass()));
        assertTrue(JsonSerializer.class.isInstance(serializer.handledType()));
        assertTrue(JsonSerializer.class.isAssignableFrom(serializer.handledType().getClass()));
        assertTrue(StdSerializer.class.isInstance(Serializers.instanceNullValueSerializer()));
        assertTrue(StdSerializer.class.isAssignableFrom(Serializers.instanceNullValueSerializer().getClass()));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(Object.class)));
        assertTrue(StdSerializer.class.isAssignableFrom(new StdSerializer<Object>(Object.class).getClass()));
        assertFalse(StdSerializer.class.isInstance(new com.fasterxml.jackson.databind.ser.std.StdSerializer<Object>(Object.class, true)));
        assertFalse(StdSerializer.class.isAssignableFrom(new com.fasterxml.jackson.databind.ser.std.StdSerializer<Object>(Object.class, true).getClass()));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<String>(String[].class)));
        assertTrue(StdSerializer.class.isAssignableFrom(new StdSerializer<String>(String[].class).getClass()));
        assertFalse(StdSerializer.class.isInstance(new StdSerializer<String>(Object[].class)));
        assertFalse(StdSerializer.class.isAssignableFrom(new StdSerializer<String>(Object[].class).getClass()));

        StdSerializer<Object> s1 = new StdSerializer<Object>(Object.class);
        StdSerializer<Object> s2 = new StdSerializer<Object>(Object.class);
        assertNotSame(s1, s2);

        StdSerializer<String> s3 = new StdSerializer<String>(String.class);
        StdSerializer<String> s4 = new StdSerializer<String>(String.class);
        assertNotSame(s3, s4);

        StdSerializer<Object> s5 = new StdSerializer<Object>(Object.class, true);
        StdSerializer<Object> s6 = new StdSerializer<Object>(Object.class, true);
        assertNotSame(s5, s6);

        ObjectSerializer s7 = new StdSerializer<Object>(Object.class);
        ObjectSerializer s8 = new StdSerializer<Object>(Object.class);
        assertNotSame(s7, s8);

        StdSerializer<String> s9 = new StdSerializer<String>(String.class);
        StdSerializer<String> s10 = new StdSerializer<String>(String.class);
        assertNotSame(s9, s10);

        // Test for JacksonStdImpl
        StdSerializer<String> serializerWithAnnotation = new StdSerializer<String>(String.class) {
            @JsonValue
            public Object getValue(String s, JsonSerializerProvider serializers) throws IOException {
                return null;
            }
        };
        assertTrue(StdSerializer.class.isInstance(serializerWithAnnotation));
        assertTrue(StdSerializer.class.isAssignableFrom(serializerWithAnnotation.getClass()));
        assertTrue(JsonSerializer.class.isInstance(serializerWithAnnotation));
        assertTrue(JsonSerializer.class.isAssignableFrom(serializerWithAnnotation.getClass()));
        assertTrue(ClassUtil.isJacksonStdImpl(serializerWithAnnotation));

        // Test for JacksonStdImpl with custom value serializer
        StdSerializer<String> serializerWithCustomValueSerializer = new StdSerializer<String>(String.class) {
            @JsonValue
            public Object getValue(String s, JsonSerializerProvider serializers) throws IOException {
                return null;
            }

            @JsonSerialize
            public void serialize(String value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
                jgen.writeString("CustomValue");
            }
        };
        assertFalse(ClassUtil.isJacksonStdImpl(serializerWithCustomValueSerializer));
    }

    @JsonTest
    public void testCreateSchemaNode() throws Exception {
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class);

        ObjectNode schema = serializer.createSchemaNode("number");
        assertEquals("number", schema.get("type").asText());

        schema = serializer.createSchemaNode("number", true);
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @JsonTest
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class);

        Integer value = 42;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JsonGenerator jgen = mapper.getFactory().createGenerator(bos, JsonEncoding.UTF8);
        mapper.writeValue(jgen, value);
        jgen.flush();
        byte[] serializedValue = bos.toByteArray();

        StdSerializer<Integer> deserializer = new StdSerializer<Integer>(Integer.class);

        JsonParser p = mapper.getFactory().createParser(new ByteArrayInputStream(serializedValue));
        ObjectNode node = deserializer.getSchema(mapper.getSerializerProvider(), null);
        JsonNode expectedNode = createSchemaNode("number");

        assertEquals(expectedNode.toString(), node.toString());

        Object readValue = mapper.readValue(new ByteArrayInputStream(serializedValue), Integer.class);
        assertEquals(value, readValue);

        jgen = mapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        serializer.serialize(value, jgen, null);
        jgen.flush();

        p = mapper.getFactory().createParser(System.in);
        readValue = mapper.readValue(p, Integer.class);
        assertEquals(value, readValue);
    }

    @JsonTest
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class);

        JsonFormatVisitorWrapper visitor = mapper.getFactory().constructDefaultSerializableJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals(StandardSerializableString.class.getName(), visitor.getSerializerProvider().getSerializerProvider().getClass().getName());
        assertEquals("AnyFormat", visitor.getFormatName());
    }

    @JsonTest
    public void testWrapAndThrow() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class);

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new IOException("An IOException"), null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new IOException("An IOException"), null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new IllegalArgumentException("An IllegalArgumentException"), null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new IllegalArgumentException("An IllegalArgumentException"), null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), null, null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), null, null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new RuntimeException("A RuntimeException"), null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new RuntimeException("A RuntimeException"), null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new Error("An Error"), null, "test");
            fail();
        } catch (Error e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new Error("An Error"), null, 1);
            fail();
        } catch (Error e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new AssertionError("An AssertionError"), null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new AssertionError("An AssertionError"), null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new InvocationTargetException(new IOException("An IOException")), null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new InvocationTargetException(new IOException("An IOException")), null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new StackOverflowError("A StackOverflowError"), null, "test");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            serializer.wrapAndThrow(mapper.getSerializerProvider(), new StackOverflowError("A StackOverflowError"), null, 1);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @JsonTest
    public void testFindConvertingContentSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        ObjectNode node = mapper.valueToTree("{\"foo\": [1, \"bar\"]}");
        BeanProperty prop = new BeanProperty("foo", String.class);
        JsonSerializer<?> existingSerializer = null;

        existingSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), prop, existingSerializer);
        assertNull(existingSerializer);

        existingSerializer = mapper.findValueSerializer(JavaType.constructGenericType(List.class, Integer.class));
        StdSerializer<Integer> intSerializer = (StdSerializer<Integer>) existingSerializer;
        existingSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), prop, existingSerializer);
        assertNotNull(existingSerializer);
        assertEquals(intSerializer, existingSerializer);

        existingSerializer = mapper.findValueSerializer(JavaType.constructGenericType(List.class, String.class));
        StdSerializer<String> stringSerializer = (StdSerializer<String>) existingSerializer;
        existingSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), prop, existingSerializer);
        assertNotNull(existingSerializer);
        assertEquals(stringSerializer, existingSerializer);
    }

    @JsonTest
    public void testFindPropertyFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        String filterId = "myCustomFilterId";
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, null);
        assertNull(filter);

        filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, new Object());
        assertNull(filter);

        ObjectNode node = mapper.valueToTree("{\"foo\": [1, \"bar\"], \"filterId\": \"" + filterId + "\"}");
        BeanProperty prop = new BeanProperty("foo", String.class);
        filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), filterId, node);
        assertNull(filter);

        filter = mapper.getFilterProvider().findPropertyFilter(filterId, new Object());
        assertTrue(filter != null);

        filter = mapper.getFilterProvider().findPropertyFilter(filterId, node);
        assertTrue(filter != null);
    }
}