package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.TypeDeserializerForArray;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.ReferenceType;

public class CollectionDeserializerTest {

    @org.junit.BeforeClass
    public static void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
    }

    @org.junit.Test
    public void testCollectionDeserializer_ObjectType() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<Object> result = new ArrayList<>();

        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(new Object());

        collectionDeserializer.deserialize(jsonParser, context, result);
        assertEquals(1, result.size());
    }

    @org.junit.Test
    public void testCollectionDeserializer_CustomTypeDeserializer() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        when(valueDeserializer.deserializeWithType(jsonParser, context, valueTypeDeserializer))
                .thenReturn(new String("test"));
        when(valueTypeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn(new String("test"));

        TypeDeserializerForArray typeDeserializer = new TypeDeserializerForArray();
        when(valueTypeDeserializer.getTypeDeserializerForArray(String.class)).thenReturn(typeDeserializer);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn(new String("test"));

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<String> result = new ArrayList<>();

        collectionDeserializer.deserialize(jsonParser, context, result);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @org.junit.Test
    public void testCollectionDeserializer_ValueInstantiator() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(1);
        when(valueInstantiator.createUsingDefault(context)).thenReturn(2);

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<Integer> result = new ArrayList<>();

        collectionDeserializer.deserialize(jsonParser, context, result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
    }

    @org.junit.Test
    public void testCollectionDeserializer_DelegateDeserializer() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        Boolean unwrapSingle = null;

        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(new String("test"));
        when(delegateDeserializer.deserialize(jsonParser, context)).thenReturn(new String("test"));

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<String> result = new ArrayList<>();

        collectionDeserializer.deserialize(jsonParser, context, result);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @org.junit.Test
    public void testCollectionDeserializer_EmptyArray() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<Object> result = new ArrayList<>();

        collectionDeserializer.deserialize(jsonParser, context, result);
        assertEquals(0, result.size());
    }

    @org.junit.Test
    public void testCollectionDeserializer_Null() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<Object> result = new ArrayList<>();

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        collectionDeserializer.deserialize(jsonParser, context, result);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_UnexpectedToken() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<Object> result = new ArrayList<>();

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        collectionDeserializer.deserialize(jsonParser, context, result);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testCollectionDeserializer_NotArray() throws IOException, JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        List<Object> result = new ArrayList<>();

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);

        collectionDeserializer.deserialize(jsonParser, context, result);
    }
}