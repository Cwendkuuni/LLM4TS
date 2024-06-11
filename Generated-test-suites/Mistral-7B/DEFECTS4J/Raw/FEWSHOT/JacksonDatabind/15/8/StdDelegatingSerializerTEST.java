package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.Version.VERSION_2_13;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialization() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mock(Converter.class);
        when(converter.convert(any())).thenReturn(1);
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer delegatedSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        String input = "123";
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        delegatedSerializer.serialize(input, gen, provider);

        assertEquals("123", input);
        verify(delegateSerializer).serialize(1, gen, provider);
    }

    @Test
    public void testSerializationWithType() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mock(Converter.class);
        when(converter.convert(any())).thenReturn(1);
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = new TypeSerializerImpl(mapper.getFactory());

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer delegatedSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        String input = "123";
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer serializerType = mock(TypeSerializer.class);

        delegatedSerializer.serializeWithType(input, gen, provider, serializerType);

        assertEquals("123", input);
        verify(delegateSerializer).serializeWithType(1, gen, provider, serializerType);
    }

    @Test
    public void testSerializationWithNullInput() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mock(Converter.class);
        when(converter.convert(any()))
            .thenReturn(null)
            .thenReturn(1);
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer delegatedSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        String input = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        delegatedSerializer.serialize(input, gen, provider);

        assertNull(input);
        verify(delegateSerializer).serialize(null, gen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mock(Converter.class);
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        when(delegateSerializer.getSchema(any(), any()))
            .thenReturn(null)
            .thenReturn(mapper.getFactory().createObjectNode());

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer delegatedSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Integer.class;

        JsonNode result = delegatedSerializer.getSchema(provider, typeHint);

        assertNull(result);
        verify(delegateSerializer).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchemaOptional() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mock(Converter.class);
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        when(delegateSerializer.getSchema(any(), any()))
            .thenReturn(null)
            .thenReturn(mapper.getFactory().createObjectNode());

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer delegatedSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Integer.class;
        boolean isOptional = true;

        JsonNode result = delegatedSerializer.getSchema(provider, typeHint, isOptional);

        assertNull(result);
        verify(delegateSerializer).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<String, Integer> converter = mock(Converter.class);
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer delegatedSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        delegatedSerializer.acceptJsonFormatVisitor(visitor, null);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, null);
    }
}