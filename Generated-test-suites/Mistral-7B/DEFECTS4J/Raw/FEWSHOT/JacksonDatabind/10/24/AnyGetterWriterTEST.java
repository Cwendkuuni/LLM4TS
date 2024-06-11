package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

/**
 * Test class for AnyGetterWriter
 */
public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private static MapSerializer createMapSerializerMock() {
        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(mapSerializer.serializeFields((Map<?, ?>) anyObject(), eq(any(JsonGenerator.class)), eq(any(SerializerProvider.class)))).thenReturn(null);
        return mapSerializer;
    }

    @SuppressWarnings("unchecked")
    private static MapSerializer createMapSerializerWithSerializedFieldsMock() {
        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(mapSerializer.serializeFilteredFields((Map<?, ?>) anyObject(), eq(any(JsonGenerator.class)), eq(any(SerializerProvider.class)), eq(any(PropertyFilter.class)), anyInt())).thenReturn(null);
        return mapSerializer;
    }

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        Object bean = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        anyGetterWriter.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    @Test
    public void testGetAndSerializeNullValue() throws Exception {
        Object bean = new Object();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndSerializeNonMapValue() throws Exception {
        Object bean = new Object();
        Object value = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getValue(bean)).thenReturn(value);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
        });
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new Object();
        Object value = new HashMap<>();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getValue(bean)).thenReturn(value);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilterNullBean() throws Exception {
        Object bean = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), mock(PropertyFilter.class));
    }

    @Test
    public void testGetAndFilterNullValue() throws Exception {
        Object bean = new Object();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testGetAndFilterNonMapValue() throws Exception {
        Object bean = new Object();
        Object value = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getValue(bean)).thenReturn(value);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        });
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new Object();
        Object value = new HashMap<>();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getValue(bean)).thenReturn(value);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testResolveNullProvider() throws Exception {
        SerializerProvider provider = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.resolve(provider);
        });
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider provider = mock(SerializerProvider.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);
        anyGetterWriter.resolve(provider);
    }

    @Test
    public void testGetAndSerializeWithMapSerializer() throws Exception {
        Object bean = new Object();
        Object value = new HashMap<>();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getValue(bean)).thenReturn(value);
        MapSerializer mapSerializer = createMapSerializerMock();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilterWithMapSerializer() throws Exception {
        Object bean = new Object();
        Object value = new HashMap<>();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        BeanProperty property = mock(BeanProperty.class);
        when(accessor.getValue(bean)).thenReturn(value);
        MapSerializer mapSerializer = createMapSerializerWithSerializedFieldsMock();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }
}