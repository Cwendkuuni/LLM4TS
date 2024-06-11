package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {
    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private PropertyFilter propertyFilter;
    private MapSerializer mapSerializer;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        propertyFilter = mock(PropertyFilter.class);
        mapSerializer = mock(MapSerializer.class);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mock(BeanProperty.class), accessor, mapSerializer);
        when(mapSerializer.serializeFields((Map<?,?>) anyObject, jsonGenerator, serializerProvider)).thenReturn(null);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        verify(mapSerializer).serializeFields((Map<?,?>) anyObject, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNullValue() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mock(BeanProperty.class), accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(bean)).thenReturn(new Integer(1));

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mock(BeanProperty.class), accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new Object();
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mock(BeanProperty.class), accessor, mapSerializer);
        when(mapSerializer.serializeFilteredFields((Map<?,?>) anyObject, jsonGenerator, serializerProvider, propertyFilter, null)).thenReturn(null);

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, propertyFilter);

        verify(mapSerializer).serializeFilteredFields((Map<?,?>) anyObject, jsonGenerator, serializerProvider, propertyFilter, null);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(serializerProvider.handlePrimaryContextualization(mapSerializer, anyBeanProperty)).thenReturn(mapSerializer);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(anyBeanProperty, accessor, mapSerializer);
        anyGetterWriter.resolve(serializerProvider);

        verify(serializerProvider).handlePrimaryContextualization(mapSerializer, anyBeanProperty);
    }
}