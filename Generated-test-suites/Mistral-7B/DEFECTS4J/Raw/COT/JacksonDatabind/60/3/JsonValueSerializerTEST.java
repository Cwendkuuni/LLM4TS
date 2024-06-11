package com.fasterxml.jackson.databind.ser.std; // package import

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private Object testObject;
    private JsonValueSerializer jsonValueSerializer;
    private JsonSerializer<Object> valueSerializer;

    @Before
    public void setup() throws Exception {
        Class testClass = TestClass.class;
        Object testInstance = testClass.newInstance();
        Method getValueMethod = testClass.getMethod("getValue");
        jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(getValueMethod), null);
        valueSerializer = new StdSerializer<Object>(String.class) {};
    }

    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        jsonValueSerializer.serialize(null, generator, new SerializerProvider());
        assertEquals("null", generator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeWithValueSerializer() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        String expectedValue = "expectedValue";
        when(valueSerializer.serialize(any(), any(), any()))
                .thenReturn(expectedValue.getBytes());

        Object testInstance = new TestClass();
        jsonValueSerializer.valueSerializer = valueSerializer;
        jsonValueSerializer.serialize(testInstance, generator, new SerializerProvider());
        assertEquals("expectedValue", generator.getOutputBuffer().toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithException() throws IOException, InvocationTargetException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        Object testInstance = new TestClass();

        doThrow(new RuntimeException("Test Exception"))
                .when(valueSerializer)
                .serialize(any(), any(), any());

        jsonValueSerializer.serialize(testInstance, generator, new SerializerProvider());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SchemaAware schemaAware = mock(JsonSerializer.class);
        when(schemaAware.getSchema(any(), any()))
                .thenReturn(new ObjectMapper().getDefaultSchemaProvider().getDefaultSchemaNode());
        jsonValueSerializer.valueSerializer = schemaAware;

        JsonNode schema = jsonValueSerializer.getSchema(new SerializerProvider(), null);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonStringFormatVisitor jsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        when(jsonFormatVisitorWrapper.expectStringFormat(any()))
                .thenReturn(jsonStringFormatVisitor);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("ONE");
        enums.add("TWO");

        when(jsonStringFormatVisitor.enumTypes(any()))
                .thenReturn(enums);

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(boolean.class, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int.class, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(double.class, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Object.class, null));
    }

    private static class TestClass {
        @JsonValue
        public static int getValue() {
            return 1;
        }
    }
}