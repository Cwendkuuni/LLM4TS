package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty beanPropertyMock;
    private MapSerializer mapSerializerMock;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private PropertyFilter propertyFilterMock;

    @Before
    public void setUp() {
        beanPropertyMock = mock(BeanProperty.class);
        mapSerializerMock = mock(MapSerializer.class);
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        propertyFilterMock = mock(PropertyFilter.class);

        anyGetterWriter = new AnyGetterWriter(beanPropertyMock, null, mapSerializerMock);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        when(beanPropertyMock.getValue(any())).thenReturn(map);

        anyGetterWriter.getAndSerialize(new Object(), jsonGeneratorMock, serializerProviderMock);

        verify(mapSerializerMock).serializeFields(map, jsonGeneratorMock, serializerProviderMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        when(beanPropertyMock.getValue(any())).thenReturn("not a map");

        anyGetterWriter.getAndSerialize(new Object(), jsonGeneratorMock, serializerProviderMock);
    }

    @Test
    public void testGetAndSerializeWithNullValue() throws Exception {
        when(beanPropertyMock.getValue(any())).thenReturn(null);

        anyGetterWriter.getAndSerialize(new Object(), jsonGeneratorMock, serializerProviderMock);

        verifyZeroInteractions(mapSerializerMock);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        when(beanPropertyMock.getValue(any())).thenReturn(map);

        anyGetterWriter.getAndFilter(new Object(), jsonGeneratorMock, serializerProviderMock, propertyFilterMock);

        verify(mapSerializerMock).serializeFilteredFields(map, jsonGeneratorMock, serializerProviderMock, propertyFilterMock, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        when(beanPropertyMock.getValue(any())).thenReturn("not a map");

        anyGetterWriter.getAndFilter(new Object(), jsonGeneratorMock, serializerProviderMock, propertyFilterMock);
    }

    @Test
    public void testGetAndFilterWithNullValue() throws Exception {
        when(beanPropertyMock.getValue(any())).thenReturn(null);

        anyGetterWriter.getAndFilter(new Object(), jsonGeneratorMock, serializerProviderMock, propertyFilterMock);

        verifyZeroInteractions(mapSerializerMock);
    }

    @Test
    public void testResolve() throws Exception {
        anyGetterWriter.resolve(serializerProviderMock);

        verify(serializerProviderMock).handlePrimaryContextualization(mapSerializerMock, beanPropertyMock);
    }
}