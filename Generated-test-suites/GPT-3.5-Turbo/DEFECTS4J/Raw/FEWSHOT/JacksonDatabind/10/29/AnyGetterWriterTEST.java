package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty beanProperty;
    private AnnotatedMember annotatedMember;
    private MapSerializer mapSerializer;

    @Before
    public void setUp() {
        beanProperty = mock(BeanProperty.class);
        annotatedMember = mock(AnnotatedMember.class);
        mapSerializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
    }

    @Test
    public void testGetAndSerialize_WhenValueIsNull() throws Exception {
        Object bean = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        // Verify that getValue() was called and returned null
        verify(annotatedMember).getValue(bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_WhenValueIsNotMap() throws Exception {
        Object bean = new Object();
        Object value = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        when(annotatedMember.getValue(bean)).thenReturn(value);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        // Verify that getValue() was called and returned the value
        verify(annotatedMember).getValue(bean);

        // Verify that a JsonMappingException was thrown
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNotNull() throws Exception {
        Object bean = new Object();
        Map<?, ?> value = mock(Map.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        when(annotatedMember.getValue(bean)).thenReturn(value);

        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);

        // Verify that getValue() was called and returned the value
        verify(annotatedMember).getValue(bean);

        // Verify that serializeFields() was called on mapSerializer with the correct arguments
        verify(mapSerializer).serializeFields(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetAndFilter_WhenValueIsNull() throws Exception {
        Object bean = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        PropertyFilter propertyFilter = mock(PropertyFilter.class);

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, propertyFilter);

        // Verify that getValue() was called and returned null
        verify(annotatedMember).getValue(bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_WhenValueIsNotMap() throws Exception {
        Object bean = new Object();
        Object value = new Object();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        PropertyFilter propertyFilter = mock(PropertyFilter.class);

        when(annotatedMember.getValue(bean)).thenReturn(value);

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, propertyFilter);

        // Verify that getValue() was called and returned the value
        verify(annotatedMember).getValue(bean);

        // Verify that a JsonMappingException was thrown
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNotNull() throws Exception {
        Object bean = new Object();
        Map<?, ?> value = mock(Map.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        PropertyFilter propertyFilter = mock(PropertyFilter.class);

        when(annotatedMember.getValue(bean)).thenReturn(value);

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, propertyFilter);

        // Verify that getValue() was called and returned the value
        verify(annotatedMember).getValue(bean);

        // Verify that serializeFilteredFields() was called on mapSerializer with the correct arguments
        verify(mapSerializer).serializeFilteredFields(value, jsonGenerator, serializerProvider, propertyFilter, null);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        anyGetterWriter.resolve(serializerProvider);

        // Verify that handlePrimaryContextualization() was called on serializerProvider with the correct arguments
        verify(serializerProvider).handlePrimaryContextualization(mapSerializer, beanProperty);
    }
}