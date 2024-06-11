package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    @Test
    public void testCreateContextual() throws Exception {
        Class<Object> clazz = Object.class;
        Object value = new Object() {
            @JsonValue
            public String valueMethod() {
                return "test";
            }
        };
        AnnotatedMethod valueMethod = findAnnotatedMethod(value.getClass(), "valueMethod");

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, null);
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(null, null);
        assertEquals(JsonValueSerializer.class, contextualSerializer.getClass());
        assertEquals(valueMethod, contextualSerializer._accessorMethod);
        assertNull(contextualSerializer._valueSerializer);
        assertNull(contextualSerializer._property);
        assertFalse(contextualSerializer._forceTypeInformation);

        // Test with resolved serializer
        BeanProperty property = new BeanProperty(null, "testProperty");
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};
        JsonValueSerializer resolvedSerializer = serializer.withResolved(property, stringSerializer, true);
        JsonSerializer<Object> resolvedContextualSerializer = resolvedSerializer.createContextual(null, null);
        assertEquals(JsonValueSerializer.class, resolvedSerializer.getClass());
        assertEquals(valueMethod, resolvedSerializer._accessorMethod);
        assertEquals(stringSerializer, resolvedSerializer._valueSerializer);
        assertEquals(property, resolvedSerializer._property);
        assertTrue(resolvedSerializer._forceTypeInformation);
    }

    @Test
    public void testSerialize() throws Exception {
        Class<Object> clazz = Object.class;
        Object value = new Object() {
            @JsonValue
            public String valueMethod() {
                return "test";
            }
        };
        AnnotatedMethod valueMethod = findAnnotatedMethod(value.getClass(), "valueMethod");

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, null);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(clazz) {};
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};

        Object testObject = new Object() {
            @JsonSerialize(using = StringSerializer.class)
            public Object getTestObject() {
                return value;
            }
        };

        JsonSerializer<Object> contextualSerializer = serializer.createContextual(null, null);
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        contextualSerializer.serialize(testObject, gen, provider);
        String result = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("\"test\"", result);

        // Test with resolved serializer
        BeanProperty property = new BeanProperty(null, "testProperty");
        JsonValueSerializer resolvedSerializer = serializer.withResolved(property, stringSerializer, true);
        JsonSerializer<Object> resolvedContextualSerializer = resolvedSerializer.createContextual(null, null);
        JsonGenerator gen2 = createJsonGenerator();
        SerializerProvider provider2 = createSerializerProvider();
        resolvedContextualSerializer.serialize(testObject, gen2, provider2);
        String result2 = gen2.getOutputFactory().writeValueAsString(gen2.getBuffer());
        assertEquals("\"test\"", result2);
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Class<Object> clazz = Object.class;
        Object value = new Object() {
            @JsonValue
            public String valueMethod() {
                return "test";
            }
        };
        AnnotatedMethod valueMethod = findAnnotatedMethod(value.getClass(), "valueMethod");

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, null);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(clazz) {};
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};

        Object testObject = new Object() {
            @JsonSerialize(using = StringSerializer.class)
            public Object getTestObject() {
                return value;
            }
        };

        TypeSerializer typeSerializer = new TypeSerializer() {};
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(null, null);
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        contextualSerializer.serializeWithType(testObject, gen, provider, typeSerializer);
        String result = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("\"test\"", result);

        // Test with resolved serializer
        BeanProperty property = new BeanProperty(null, "testProperty");
        JsonValueSerializer resolvedSerializer = serializer.withResolved(property, stringSerializer, true);
        JsonSerializer<Object> resolvedContextualSerializer = resolvedSerializer.createContextual(null, null);
        JsonGenerator gen2 = createJsonGenerator();
        SerializerProvider provider2 = createSerializerProvider();
        TypeSerializer typeSerializer2 = new TypeSerializer() {};
        resolvedContextualSerializer.serializeWithType(testObject, gen2, provider2, typeSerializer2);
        String result2 = gen2.getOutputFactory().writeValueAsString(gen2.getBuffer());
        assertEquals("\"test\":null", result2);
    }

    @Test
    public void testGetSchema() throws Exception {
        Class<Object> clazz = Object.class;
        Object value = new Object() {
            @JsonValue
            public String valueMethod() {
                return "test";
            }
        };
        AnnotatedMethod valueMethod = findAnnotatedMethod(value.getClass(), "valueMethod");

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, null);
        SchemaAware schemaAwareSerializer = new StdSerializer<String>(String.class) {};
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(null, null);

        SchemaFactory schemaFactory = new DefaultSerializableSchema();
        JsonSchema schema = contextualSerializer.getSchema(null, clazz);
        assertTrue(schema instanceof SchemaAware);
        assertEquals(schemaFactory.getRootSchema().getType(), schema.getType());

        // Test with resolved serializer
        BeanProperty property = new BeanProperty(null, "testProperty");
        JsonValueSerializer resolvedSerializer = serializer.withResolved(property, schemaAwareSerializer, true);
        JsonSerializer<Object> resolvedContextualSerializer = resolvedSerializer.createContextual(null, null);
        JsonSchema schema2 = resolvedContextualSerializer.getSchema(null, clazz);
        assertTrue(schema2 instanceof SchemaAware);
        assertEquals(schemaFactory.getRootSchema().getType(), schema2.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Class<Object> clazz = Object.class;
        Object value = new Object() {
            @JsonValue
            public String valueMethod() {
                return "test";
            }
        };
        AnnotatedMethod valueMethod = findAnnotatedMethod(value.getClass(), "valueMethod");

        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, null);
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(null, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonSchemaVisitor();

        contextualSerializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.hasEnumValues());

        // Test with resolved serializer
        BeanProperty property = new BeanProperty(null, "testProperty");
        JsonValueSerializer resolvedSerializer = serializer.withResolved(property, null, true);
        JsonSerializer<Object> resolvedContextualSerializer = resolvedSerializer.createContextual(null, null);
        JsonFormatVisitorWrapper visitor2 = mapper.schemaFactory().getJsonSchemaVisitor();
        resolvedContextualSerializer.acceptJsonFormatVisitor(visitor2, null);
        assertTrue(visitor2.hasEnumValues());
    }

    private AnnotatedMethod findAnnotatedMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found: " + clazz.getName() + "#" + methodName, e);
        }
    }

    private JsonGenerator createJsonGenerator() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createGenerator();
    }

    private SerializerProvider createSerializerProvider() {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper.getSerializerProvider();
    }
}