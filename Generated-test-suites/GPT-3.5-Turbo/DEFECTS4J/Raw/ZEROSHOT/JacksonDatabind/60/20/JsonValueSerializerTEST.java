package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class JsonValueSerializerTest {

    private class TestValue {
        @JsonSerialize(using=JsonValueSerializer.class)
        public String getValue() {
            return "test";
        }
    }


    @Test
    public void testSerialize() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(new AnnotatedMethod(TestValue.class.getDeclaredMethod("getValue")), null);

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        TestValue value = new TestValue();
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("\"test\"", writer.toString());
    }


    @Test
    public void testSerializeWithType() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(new AnnotatedMethod(TestValue.class.getDeclaredMethod("getValue")), null);

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        TestValue value = new TestValue();
        serializer.serializeWithType(value, generator, null, null);
        generator.flush();

        assertEquals("\"test\"", writer.toString());
    }


    @Test
    public void testCreateContextual() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(new AnnotatedMethod(TestValue.class.getDeclaredMethod("getValue")), null);

        assertTrue(serializer.createContextual(null, null) instanceof JsonValueSerializer);
    }


    @Test
    public void testGetSchema() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(new AnnotatedMethod(TestValue.class.getDeclaredMethod("getValue")), null);

        assertTrue(serializer.getSchema(null, null) instanceof JsonNode);
    }


    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(new AnnotatedMethod(TestValue.class.getDeclaredMethod("getValue")), null);

        assertFalse(serializer.acceptJsonFormatVisitor(null, null, null));
    }


    @Test
    public void testIsNaturalTypeWithStdHandling() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(new AnnotatedMethod(TestValue.class.getDeclaredMethod("getValue")), null);

        assertFalse(serializer.isNaturalTypeWithStdHandling(null, null));
    }
}