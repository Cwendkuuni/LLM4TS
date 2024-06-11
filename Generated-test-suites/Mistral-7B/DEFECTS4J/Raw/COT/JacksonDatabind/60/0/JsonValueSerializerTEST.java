package com.example.jsonvalueserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() throws Exception {
        objectMapper = new JsonMapper();
        Class<?> testClass = TestClass.class;
        Method getValueMethod = TestClass.class.getMethod("getValue");
        AccessibleObject.setAccessible(getValueMethod, true);
        jsonValueSerializer = new JsonValueSerializer(getValueMethod, mock(JsonSerializer.class));
    }

    @Test
    public void testSerialize() throws IOException {
        Object testObject = new TestClass();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.findValueSerializer(eq(String.class), any())).thenReturn(mock(JsonSerializer.class));

        jsonValueSerializer.serialize(testObject, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeString("test value");
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object testObject = new TestClass();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        jsonValueSerializer.serializeWithType(testObject, jsonGenerator, serializerProvider, typeSerializer);

        verify(typeSerializer).writeTypePrefixForScalar(eq(testObject), jsonGenerator);
        verify(jsonGenerator).writeString("test value");
        verify(typeSerializer).writeTypeSuffixForScalar(eq(testObject), jsonGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithException() throws IOException, InvocationTargetException, IllegalAccessException {
        Object testObject = new TestClass();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        doThrow(new IOException("Test exception"))
                .when(jsonValueSerializer)
                .serialize(any(Object.class), eq(jsonGenerator), eq(serializerProvider));

        jsonValueSerializer.serializeWithType(testObject, jsonGenerator, serializerProvider, mock(TypeSerializer.class));

        verify(jsonGenerator, never()).writeString("test value");
        verify(jsonGenerator).close(); // jsonGenerator.close() is called when JsonMappingException is thrown
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);

        assertEquals(com.fasterxml.jackson.databind.jsonschema.JsonSchema.getDefaultSchemaNode(), jsonValueSerializer.getSchema(objectMapper.getSerializerProvider(), null));
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(EnumTestClass.class);

        assertEquals(true, jsonValueSerializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, EnumTestClass.class));
    }

    private static class TestClass {
        private String value;

        public String getValue() throws Exception {
            return "test value";
        }
    }

    private static class EnumTestClass extends Enum<EnumTestClass> {
        EnumTestClass(String name) {
            super(name);
        }

        @JsonValue
        public String getValue() {
            return name();
        }

        public enum TestEnum {
            TEST_ONE, TEST_TWO
        }
    }
}