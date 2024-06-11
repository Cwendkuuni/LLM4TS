package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.util.Map;

public class AnyGetterWriterTest {

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private AnnotatedMember annotatedMember;

    @Mock
    private MapSerializer mapSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private PropertyFilter propertyFilter;

    @Test
    public void testGetAndSerialize() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        Map<String, String> map = mock(Map.class);
        when(annotatedMember.getValue(map)).thenReturn(map);
        anyGetterWriter.getAndSerialize(map, jsonGenerator, serializerProvider);
        verify(mapSerializer).serializeFields(map, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_ThrowException() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        anyGetterWriter.getAndSerialize(new Object(), jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        Map<String, String> map = mock(Map.class);
        when(annotatedMember.getValue(map)).thenReturn(map);
        anyGetterWriter.getAndFilter(map, jsonGenerator, serializerProvider, propertyFilter);
        verify(mapSerializer).serializeFilteredFields(map, jsonGenerator, serializerProvider, propertyFilter, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_ThrowException() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        anyGetterWriter.getAndFilter(new Object(), jsonGenerator, serializerProvider, propertyFilter);
    }

    @Test
    public void testResolve() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        anyGetterWriter.resolve(serializerProvider);
        verify(serializerProvider).handlePrimaryContextualization(mapSerializer, beanProperty);
    }
}