package com.example.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestBase;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonValueSerializerTest extends SerializerTestBase {

    private JsonValueSerializer jsonValueSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        Class<?> testClass = JsonValueSerializer.class;
        Method accessorMethod = testClass.getMethod("getValue");
        Object target = new Object() {
            @JsonValue
            public Integer getValue() {
                return 42;
            }
        };

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBasicSerialization() throws JsonProcessingException, IOException {
        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.serialize(any(), any(), any())).thenReturn(mock(JsonGenerator.class));
        jsonValueSerializer = jsonValueSerializer.withResolved(null, valueSerializer, false);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        jsonValueSerializer.serialize(new Object(), jsonGenerator, mock(SerializerProvider.class));

        assertEquals("42", jsonGenerator.getOutputValueAsString());
    }

    @Test
    public void testSerializationWithType() throws JsonProcessingException, IOException {
        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.serializeWithType(any(), any(), any(), any())).thenReturn(mock(JsonGenerator.class));
        jsonValueSerializer = jsonValueSerializer.withResolved(null, valueSerializer, true);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        jsonValueSerializer.serializeWithType(new Object(), jsonGenerator, mock(SerializerProvider.class), mock(TypeSerializer.class));

        assertEquals("42", jsonGenerator.getOutputValueAsString());
    }

    @Test
    public void testNullValue() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        jsonValueSerializer.serialize(new Object(), jsonGenerator, mock(SerializerProvider.class));

        assertNull(jsonGenerator.getOutputValueAsString());
    }

    @Test
    public void testExceptionHandling() throws JsonProcessingException, IOException {
        Class<?> testClass = JsonValueSerializer.class;
        Method accessorMethod = testClass.getMethod("getValue");
        Object target = new Object() {
            @JsonValue
            public String getValue() throws IOException {
                throw new IOException("Test exception");
            }
        };

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);

        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(anyInt())).thenReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);

        try {
            jsonValueSerializer.serialize(target, jsonGenerator, serializerProvider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testSerializationWithCustomSerializer() throws JsonProcessingException, IOException {
        Class<?> testClass = JsonValueSerializer.class;
        Method accessorMethod = testClass.getMethod("getValue");
        Object target = new Object() {
            @JsonValue
            public LinkedHashSet<String> getValue() {
                return new LinkedHashSet<String>() {{
                    add("Test");
                }};
            }
        };

        LinkedHashSet<String> targetValue = (LinkedHashSet<String>) target.getValue();

        JsonSerializer<?> valueSerializer = mock(JsonSerializer.class);
        when(valueSerializer.serialize(targetValue, any(), any())).thenReturn(mock(JsonGenerator.class));
        jsonValueSerializer = jsonValueSerializer.withResolved(null, valueSerializer, false);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        jsonValueSerializer.serialize(new Object(), jsonGenerator, mock(SerializerProvider.class));

        assertEquals("[Test]", jsonGenerator.getOutputValueAsString());
    }
}