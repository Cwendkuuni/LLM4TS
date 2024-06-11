package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotatedMember;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty mockProperty;
    private AnnotatedMember mockAccessor;
    private MapSerializer mockSerializer;

    @Before
    public void setup() {
        mockProperty = mock(BeanProperty.class);
        mockAccessor = mock(AnnotatedMember.class);
        mockSerializer = mock(MapSerializer.class);

        anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        // Mock the getValue() method to return a Map
        Map<Object, Object> mockMap = mock(Map.class);
        when(mockAccessor.getValue(any())).thenReturn(mockMap);

        anyGetterWriter.getAndSerialize(new Object(), mockGenerator, mockProvider);

        // Verify that the serializeFields() method was called on the mockSerializer
        verify(mockSerializer).serializeFields(mockMap, mockGenerator, mockProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithInvalidValue() throws Exception {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        // Mock the getValue() method to return a non-Map object
        when(mockAccessor.getValue(any())).thenReturn(new Object());

        anyGetterWriter.getAndSerialize(new Object(), mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);

        // Mock the getValue() method to return a Map
        Map<Object, Object> mockMap = mock(Map.class);
        when(mockAccessor.getValue(any())).thenReturn(mockMap);

        anyGetterWriter.getAndFilter(new Object(), mockGenerator, mockProvider, mockFilter);

        // Verify that the serializeFilteredFields() method was called on the mockSerializer
        verify(mockSerializer).serializeFilteredFields(mockMap, mockGenerator, mockProvider, mockFilter, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithInvalidValue() throws Exception {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);

        // Mock the getValue() method to return a non-Map object
        when(mockAccessor.getValue(any())).thenReturn(new Object());

        anyGetterWriter.getAndFilter(new Object(), mockGenerator, mockProvider, mockFilter);
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        anyGetterWriter.resolve(mockProvider);

        // Verify that the handlePrimaryContextualization() method was called on the mockProvider
        verify(mockProvider).handlePrimaryContextualization(mockSerializer, mockProperty);
    }
}