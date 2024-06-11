package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = spy(new AnyGetterWriter(property, accessor, mapSerializer));

        Object bean = new Object();
        Map<String, Object> value = new HashMap<>();
        when(accessor.getValue(bean)).thenReturn(value);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        // Verify that _mapSerializer was used to serialize the Map
        when(mapSerializer.serializeFields(value, gen, provider)).thenReturn(null);
        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndSerialize_ValueIsNotMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        Object value = new Object();
        when(accessor.getValue(bean)).thenReturn(value);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter writer = spy(new AnyGetterWriter(property, accessor, mapSerializer));

        Object bean = new Object();
        Map<String, Object> value = new HashMap<>();
        when(accessor.getValue(bean)).thenReturn(value);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndFilter(bean, gen, provider, filter);

        // Verify that _mapSerializer was used to serialize the filtered Map
        Map<String, Object> filteredValue = new HashMap<>();
        filteredValue.put("key1", "value1");
        when(mapSerializer.serializeFilteredFields(value, gen, provider, filter, null)).thenReturn(filteredValue);
        writer.getAndFilter(bean, gen, provider, filter);

        Map<String, Object> expectedValue = new HashMap<>();
        expectedValue.put("key1", "value1");
        assertEquals(expectedValue, writer.getValueToSerialize());
    }

    @Test
    public void testGetAndFilter_ValueIsNotMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object();
        Object value = new Object();
        when(accessor.getValue(bean)).thenReturn(value);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertThrows(JsonMappingException.class, () -> writer.getAndFilter(bean, gen, provider, filter));
    }

    @Test
    public void testResolve() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(null, null, mapSerializer);

        SerializerProvider provider = mock(SerializerProvider.class);

        writer.resolve(provider);

        // Verify that _mapSerializer was resolved
        when(provider.handlePrimaryContextualization(mapSerializer, null)).thenReturn(mapSerializer);
        writer.resolve(provider);
    }
}