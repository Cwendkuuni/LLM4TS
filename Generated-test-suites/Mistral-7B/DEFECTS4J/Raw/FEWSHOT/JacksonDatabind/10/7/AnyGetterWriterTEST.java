package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private AnyGetterWriter getTestInstance(BeanProperty property, AnnotatedMember accessor, MapSerializer mapSerializer) {
        return new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No exception expected
    }

    @Test
    public void testGetAndSerializeNullMap() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No output, no exception expected
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Map<String, Object> emptyMap = new HashMap<>();

        when(accessor.getValue(bean)).thenReturn(emptyMap);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No exception expected, empty map should be serialized
    }

    @Test
    public void testGetAndSerializeMap() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);

        when(mapSerializer.serializeFields(map, gen, provider)).thenCallRealMethod();

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No exception expected, map should be serialized
    }

    @Test
    public void testGetAndSerializeMapWithNullSerializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Map<String, Object> map = new HashMap<>();

        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, null);

        JsonMappingException expectedException = new JsonMappingException("Map serializer not set");

        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndSerialize(bean, gen, provider));

        // Expected exception
    }

    @Test
    public void testGetAndFilterNullBean() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No exception expected
    }

    @Test
    public void testGetAndFilterNullMap() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No output, no exception expected
    }

    @Test
    public void testGetAndFilterEmptyMap() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        Map<String, Object> emptyMap = new HashMap<>();

        when(accessor.getValue(bean)).thenReturn(emptyMap);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No exception expected, empty map should not be serialized
    }

    @Test
    public void testGetAndFilterMap() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        Converter<Object, Object> converter = mock(Converter.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);
        when(mapSerializer.serializeFilteredFields(map, gen, provider, filter, converter)).thenCallRealMethod();

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No exception expected, map should be filtered
    }

    @Test
    public void testGetAndFilterMapWithNullFilter() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Map<String, Object> map = new HashMap<>();

        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = getTestInstance(property, accessor, mapSerializer);

        PropertyFilter filter = null;

        JsonMappingException expectedException = new JsonMappingException("Filter cannot be null");

        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndFilter(bean, gen, provider, filter));

        // Expected exception
    }
}