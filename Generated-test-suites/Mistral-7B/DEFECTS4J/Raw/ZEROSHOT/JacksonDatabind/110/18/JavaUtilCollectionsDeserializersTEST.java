package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType javaTypeSet = new TypeReference<Set<Boolean>>(){}.getType();

        when(context.getTypeFactory()).thenReturn(typeFactory);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeSet);
        StdDelegatingDeserializer delegatingDeserializer = new StdDelegatingDeserializer<>(result);

        Set<Boolean> set = Collections.singleton(Boolean.TRUE);
        Object deserialized = objectMapper.readValue(Arrays.toString(new Object[] { set }), delegatingDeserializer.getOutputType(objectMapper));

        assertEquals(Boolean.TRUE, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType javaTypeList = new TypeReference<List<Boolean>>(){}.getType();

        when(context.getTypeFactory()).thenReturn(typeFactory);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeList);
        StdDelegatingDeserializer delegatingDeserializer = new StdDelegatingDeserializer<>(result);

        List<Boolean> list = Collections.singletonList(Boolean.TRUE);
        Object deserialized = objectMapper.readValue(Arrays.toString(new Object[] { list }), delegatingDeserializer.getOutputType(objectMapper));

        assertEquals(Boolean.TRUE, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType javaTypeMap = new TypeReference<Map<String, String>>(){}.getType();

        when(context.getTypeFactory()).thenReturn(typeFactory);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(context, javaTypeMap);
        StdDelegatingDeserializer delegatingDeserializer = new StdDelegatingDeserializer<>(result);

        Map<String, String> map = Collections.singletonMap("key", "value");
        Object deserialized = objectMapper.readValue(Arrays.toString(new Object[] { map }), delegatingDeserializer.getOutputType(objectMapper));

        assertEquals("value", deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType javaTypeSet = new TypeReference<Set<Boolean>>(){}.getType();

        when(context.getTypeFactory()).thenReturn(typeFactory);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeSet);
        StdDelegatingDeserializer delegatingDeserializer = new StdDelegatingDeserializer<>(result);

        Set<Boolean> set = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
        Object deserialized = objectMapper.readValue(Arrays.toString(new Object[] { set }), delegatingDeserializer.getOutputType(objectMapper));

        assertEquals(Boolean.TRUE, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType javaTypeList = new TypeReference<List<Boolean>>(){}.getType();

        when(context.getTypeFactory()).thenReturn(typeFactory);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeList);
        StdDelegatingDeserializer delegatingDeserializer
               = new StdDelegatingDeserializer<>(result);

        List<Boolean> list = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
        Object deserialized = objectMapper.readValue(Arrays.toString(new Object[] { list }), delegatingDeserializer.getOutputType(objectMapper));

        assertEquals(Boolean.TRUE, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType javaTypeMap = new TypeReference<Map<String, String>>(){}.getType();

        when(context.getTypeFactory()).thenReturn(typeFactory);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(context, javaTypeMap);
        StdDelegategatingDeserializer delegatingDeserializer = new StdDelegatingDeserializer<>(result);

        Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
        Object deserialized = objectMapper.readValue(Arrays.toString(new Object[] { map }), delegatingDeserializer.getOutputType(objectMapper));

        assertEquals("value", deserialized);
    }
}