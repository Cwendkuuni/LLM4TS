package com.example.json; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.ser.impl.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Test class for JsonValueSerializer.
 */
public class JsonValueSerializerTest {

    /**
     * Test method for JsonValueSerializer.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testJsonValueSerializer() throws Exception {
        // Create a sample class with a @JsonValue-annotated method
        class Sample {
            private int id;

            @JsonValue
            public String getName() {
                return "John Doe";
            }
        }

        // Create a mock JsonSerializer for the @JsonValue-annotated method
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        when(stringSerializer.getClass()).thenReturn(String.class);

        // Create an instance of JsonValueSerializer with the mock JsonSerializer
        AnnotatedMethod accessorMethod = new AnnotatedMethod(Sample.class, "getName", Sample.class);
        JsonValueSerializer serializer = new JsonValueSerializer(accessorMethod, stringSerializer);

        // Test serialization
        ObjectMapper mapper = new ObjectMapper();
        Sample sample = new Sample();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), com.fasterxml.jackson.core.util.DefaultPrepreters.StandardPrepreters.Instance);
        serializer.serialize(sample, gen, null);
        assertEquals("\"John Doe\"", gen.toString());

        // Test JSON schema
        JsonSchema schema = serializer.getSchema(mapper.getFactory(), Object.class);
        assertEquals("string", schema.getSchemaName());

        // Test contextual serialization
        JsonSerializer<?> contextualSerializer = mapper.findValueSerializer(Sample.class, null);
        assertEquals(serializer.getClass(), contextualSerializer.getClass());

        // Test type serialization
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        TypeSerializer serializedTypeSerializer = ((ContextualSerializer) serializer).createContextual(mapper.getSerializerProvider(), null);
        assertEquals(typeSerializer.getClass(), serializedTypeSerializer.getClass());
    }
}