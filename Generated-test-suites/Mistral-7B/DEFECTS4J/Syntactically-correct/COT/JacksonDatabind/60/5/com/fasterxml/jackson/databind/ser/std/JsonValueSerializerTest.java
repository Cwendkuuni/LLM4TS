package com.example.jsonvalueserializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructors() {
        JsonValueSerializer serializer1 = new JsonValueSerializer(null, null);
        JsonValueSerializer serializer2 = new JsonValueSerializer(null, null, new BeanProperty(), null, null);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(Object.class) {};
        BeanProperty property = new BeanProperty(null, null, null, null, null);
        JsonValueSerializer contextualSerializer = serializer.withResolved(property, valueSerializer, false);
        assertNotSame(serializer, contextualSerializer);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Object bean = new Object() {};
        String expectedValue = "expectedValue";
        when(bean._accessorMethod.getValue()).thenReturn(expectedValue);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.findTypedValueSerializer(String.class)).thenReturn(valueSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(bean, gen, provider);

        verify(gen).writeString(expectedValue);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Object bean = new Object() {};
        String expectedValue = "expectedValue";
        when(bean._accessorMethod.getValue()).thenReturn(expectedValue);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.findValueSerializer(String.class, null)).thenReturn(valueSerializer);
        when(provider.getTypeSerializer()).thenReturn(typeSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serializeWithType(bean, gen, provider, typeSerializer);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("@class");
        verify(typeSerializer).writeTypePrefixForScalar(bean, gen);
        verify(valueSerializer).serializeWithType(expectedValue, gen, provider, typeSerializer);
        verify(typeSerializer).writeTypeSuffixForScalar(bean, gen);
        verify(gen).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Object bean = new Object() {};
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(bean, gen, provider);

        verify(gen, never()).writeString(anyString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithException() throws JsonGenerationException, JsonMappingException, IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null);
        Object bean = new Object() {};
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(bean._accessorMethod.getValue()).thenThrow(new RuntimeException("Unexpected Exception"));

        try {
            serializer.serialize(bean, gen, provider);
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        verify(gen, never()).writeString(anyString());
    }
}