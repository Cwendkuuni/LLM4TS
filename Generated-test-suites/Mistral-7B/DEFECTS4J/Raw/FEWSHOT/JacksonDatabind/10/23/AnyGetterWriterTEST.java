package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testAnyGetterWriter() {
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getName()).thenReturn("anyGetterProperty");
        when(accessor.getValue(any())).thenReturn(new HashMap<>());

        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        assertNotNull(anyGetterWriter);
        assertEquals(property, anyGetterWriter._property);
        assertEquals(accessor, anyGetterWriter._accessor);
        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getName()).thenReturn("anyGetterProperty");
        when(accessor.getValue(any())).thenReturn(new HashMap<>());

        MapSerializer mapSerializer = mock(MapSerializer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        when(mapSerializer.serializeFields((Map<?,?>) anyObject(), eq(gen), eq(provider))).thenReturn(null);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
        verify(mapSerializer).serializeFields((Map<?,?>) eq(anyMap()), eq(gen), eq(provider));
    }

    @Test
    public void testGetAndSerializeWithNullMap() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getName()).thenReturn("anyGetterProperty");
        when(accessor.getValue(any())).thenReturn(null);

        MapSerializer mapSerializer = mock(MapSerializer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getName()).thenReturn("anyGetterProperty");
        when(accessor.getValue(any())).thenReturn("not a map");

        MapSerializer mapSerializer = mock(MapSerializer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndSerialize(bean, gen, provider));
        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getName()).thenReturn("anyGetterProperty");
        when(accessor.getValue(any())).thenReturn(new HashMap<>());

        MapSerializer mapSerializer = mock(MapSerializer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        when(mapSerializer.serializeFilteredFields((Map<?,?>) anyObject(), eq(gen), eq(provider), eq(filter), any()))
                .thenReturn(null);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        verify(mapSerializer).serializeFilteredFields((Map<?,?>) eq(anyMap()), eq(gen), eq(provider), eq(filter), any());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);

        when(provider.handlePrimaryContextualization(any(), any())).thenReturn(mapSerializer);

        anyGetterWriter.resolve(provider);
        verify(anyGetterWriter)._mapSerializer = eq(mapSerializer);
    }

}