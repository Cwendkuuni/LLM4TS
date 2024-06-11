package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty mockProperty;
    private AnnotatedMember mockAccessor;
    private MapSerializer mockSerializer;
    private Object bean;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockProvider;
    private PropertyFilter mockFilter;

    @Before
    public void setup() {
        mockProperty = mock(BeanProperty.class);
        mockAccessor = mock(AnnotatedMember.class);
        mockSerializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
        bean = new Object();
        mockJsonGenerator = mock(JsonGenerator.class);
        mockProvider = mock(SerializerProvider.class);
        mockFilter = mock(PropertyFilter.class);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "value");
        when(mockAccessor.getValue(bean)).thenReturn(map);

        anyGetterWriter.getAndSerialize(bean, mockJsonGenerator, mockProvider);

        // assertion goes here
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        when(mockAccessor.getValue(bean)).thenReturn("non-map value");
        when(mockAccessor.getName()).thenReturn("any-getter");

        anyGetterWriter.getAndSerialize(bean, mockJsonGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "value");
        when(mockAccessor.getValue(bean)).thenReturn(map);

        anyGetterWriter.getAndFilter(bean, mockJsonGenerator, mockProvider, mockFilter);

        // assertion goes here
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        when(mockAccessor.getValue(bean)).thenReturn("non-map value");
        when(mockAccessor.getName()).thenReturn("any-getter");

        anyGetterWriter.getAndFilter(bean, mockJsonGenerator, mockProvider, mockFilter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        anyGetterWriter.resolve(mockProvider);

        // assertion goes here
    }
}