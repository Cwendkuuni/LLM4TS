package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class AnyGetterWriterTest {

    private ObjectNode createTestMap() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        Object mapValue = new HashMap<>();
        when(accessor.getValue(bean)).thenReturn(mapValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        verify(provider).findValueSerializer(Map.class, null, null);
    }

    @Test
    public void testGetAndSerializeWithNullValue() throws Exception {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        when(accessor.getValue(bean)).thenReturn(null);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testGetAndSerializeWithNonMapValueType() throws Exception {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        Object nonMapValue = new Object();
        when(accessor.getValue(bean)).thenReturn(nonMapValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        try {
            writer.getAndSerialize(bean, gen, provider);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' () not java.util.Map but " + nonMapValue.getClass().getName(), e.getMessage());
        }

        verifyZeroInteractions(gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        Object mapValue = new HashMap<>();
        when(accessor.getValue(bean)).thenReturn(mapValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndFilter(bean, gen, provider, filter);

        verify(mapSerializer).serializeFilteredFields((Map<?,?>) mapValue, gen, provider, filter, null);
    }

    @Test
    public void testGetAndFilterWithNullValue() throws Exception {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        when(accessor.getValue(bean)).thenReturn(null);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndFilter(bean, gen, provider, filter);

        verifyZeroInteractions(gen, provider);
    }

    @Test
    public void testGetAndFilterWithNonMapValueType() throws Exception {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        Object nonMapValue = new Object();
        when(accessor.getValue(bean)).thenReturn(nonMapValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        try {
            writer.getAndFilter(bean, gen, provider, filter);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' () not java.util.Map but " + nonMapValue.getClass().getName(), e.getMessage());
        }

        verifyZeroInteractions(gen, provider);
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider provider = mock(SerializerProvider.class);
        MapSerializer mapSerializer = new MapSerializer(createTestMap(), Serializers.EmptyObjectSerializer.instance, null, null, null);

        when(provider.findValueSerializer(Map.class, null, null)).thenReturn(mapSerializer);

        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);

        writer.resolve(provider);

        assertEquals(mapSerializer, writer._mapSerializer);
    }
}