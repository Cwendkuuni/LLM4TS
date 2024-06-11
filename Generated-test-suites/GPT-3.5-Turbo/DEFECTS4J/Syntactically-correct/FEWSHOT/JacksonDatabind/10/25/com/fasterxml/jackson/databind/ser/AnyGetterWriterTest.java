package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Mock
    private BeanProperty property;

    @Mock
    private MapSerializer serializer;

    @Mock
    private AnnotatedMember accessor;

    private AnyGetterWriter anyGetterWriter;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        when(accessor.getValue(any())).thenReturn(map);
        when(serializer.serializeFields(any(), any(), any())).thenReturn(new Object());

        anyGetterWriter.getAndSerialize(new Object(), mockGenerator, mockProvider);

        verify(accessor, times(1)).getValue(any());
        verify(serializer, times(1)).serializeFields(any(), any(), any());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithInvalidValue() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(accessor.getValue(any())).thenReturn("notMap");

        anyGetterWriter.getAndSerialize(new Object(), mockGenerator, mockProvider);

        verify(accessor, times(1)).getValue(any());
        verifyZeroInteractions(serializer);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);

        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        when(accessor.getValue(any())).thenReturn(map);
        when(serializer.serializeFilteredFields(any(), any(), any(), any(), any())).thenReturn(new Object());

        anyGetterWriter.getAndFilter(new Object(), mockGenerator, mockProvider, mockFilter);

        verify(accessor, times(1)).getValue(any());
        verify(serializer, times(1)).serializeFilteredFields(any(), any(), any(), any(), any());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithInvalidValue() throws Exception {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);

        when(accessor.getValue(any())).thenReturn("notMap");

        anyGetterWriter.getAndFilter(new Object(), mockGenerator, mockProvider, mockFilter);

        verify(accessor, times(1)).getValue(any());
        verifyZeroInteractions(serializer);
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        when(mockProvider.handlePrimaryContextualization(any(), any())).thenReturn(serializer);

        anyGetterWriter.resolve(mockProvider);

        verify(mockProvider, times(1)).handlePrimaryContextualization(any(), any());
        assertEquals(anyGetterWriter._mapSerializer, serializer);
    }

    @Test
    public void testResolveWithNull() throws Exception {
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        when(mockProvider.handlePrimaryContextualization(any(), any())).thenReturn(null);

        anyGetterWriter.resolve(mockProvider);

        verify(mockProvider, times(1)).handlePrimaryContextualization(any(), any());
        assertNull(anyGetterWriter._mapSerializer);
    }
}