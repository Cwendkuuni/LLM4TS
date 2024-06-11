package com.fasterxml.jackson.databind.ser;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer serializer;
    private Object bean;
    private JsonGenerator gen;
    private SerializerProvider provider;

    @Before
    public void setUp() throws Exception {
        property = mock(BeanProperty.class);
        accessor = mock(AnnotatedMember.class);
        serializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        bean = new Object();
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        when(accessor.getValue(bean)).thenReturn(null);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        verifyZeroInteractions(gen, provider);
        
        Map<Object, Object> map = mock(Map.class);
        when(accessor.getValue(bean)).thenReturn(map);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        verify(serializer).serializeFields(map, gen, provider);
    }

    @Test(expected=JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        when(accessor.getValue(bean)).thenReturn("not a map");
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        when(accessor.getValue(bean)).thenReturn(null);
        anyGetterWriter.getAndFilter(bean, gen, provider, null);
        verifyZeroInteractions(gen, provider);
        
        Map<Object, Object> map = mock(Map.class);
        when(accessor.getValue(bean)).thenReturn(map);
        anyGetterWriter.getAndFilter(bean, gen, provider, null);
        verify(serializer).serializeFilteredFields(map, gen, provider, null, null);
    }

    @Test(expected=JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        when(accessor.getValue(bean)).thenReturn("not a map");
        anyGetterWriter.getAndFilter(bean, gen, provider, null);
    }

    @Test
    public void testResolve() throws Exception {
        anyGetterWriter.resolve(provider);
        verify(provider).handlePrimaryContextualization(serializer, property);
    }
}