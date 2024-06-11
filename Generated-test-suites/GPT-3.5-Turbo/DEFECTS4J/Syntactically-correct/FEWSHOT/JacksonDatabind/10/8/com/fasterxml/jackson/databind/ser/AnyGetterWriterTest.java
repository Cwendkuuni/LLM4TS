package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class AnyGetterWriterTest {

    private BeanProperty beanProperty;
    private AnnotatedMember annotatedMember;
    private MapSerializer mapSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private Object bean;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setUp() {
        beanProperty = mock(BeanProperty.class);
        annotatedMember = mock(AnnotatedMember.class);
        mapSerializer = mock(MapSerializer.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        bean = new Object();
        anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
    }

    @Test
    public void testGetAndSerialize_WhenValueIsNull() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(null);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        verify(jsonGenerator, never()).writeObject(any());
    }

    @Test
    public void testGetAndSerialize_WhenValueIsNotMap() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(new Object());

        try {
            anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + annotatedMember.getName() + "()) not java.util.Map but java.lang.Object", e.getMessage());
        }

        verify(jsonGenerator, never()).writeObject(any());
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNull() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(Mockito.anyMap());

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        verify(jsonGenerator, never()).writeObject(any());
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNotNull() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(Mockito.anyMap());
        anyGetterWriter._mapSerializer = mapSerializer;

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        verify(mapSerializer, times(1)).serializeFields(anyMap(), eq(jsonGenerator), eq(serializerProvider));
    }

    @Test
    public void testGetAndFilter_WhenValueIsNull() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(null);

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));

        verify(jsonGenerator, never()).writeObject(any());
    }

    @Test
    public void testGetAndFilter_WhenValueIsNotMap() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(new Object());

        try {
            anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + annotatedMember.getName() + "()) not java.util.Map but java.lang.Object", e.getMessage());
        }

        verify(jsonGenerator, never()).writeObject(any());
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNull() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(Mockito.anyMap());

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));

        verify(jsonGenerator, never()).writeObject(any());
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNotNull() throws Exception {
        when(annotatedMember.getValue(bean)).thenReturn(Mockito.anyMap());
        anyGetterWriter._mapSerializer = mapSerializer;

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));

        verify(mapSerializer, times(1)).serializeFilteredFields(anyMap(), eq(jsonGenerator), eq(serializerProvider), any(PropertyFilter.class), eq(null));
    }

    @Test
    public void testResolve() throws JsonMappingException {
        anyGetterWriter.resolve(serializerProvider);

        verify(serializerProvider, times(1)).handlePrimaryContextualization(eq(mapSerializer), eq(beanProperty));
    }
}