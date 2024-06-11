package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty mockBeanProperty;
    private AnnotatedMember mockAnnotatedMember;
    private MapSerializer mockMapSerializer;
    
    @Before
    public void setUp() {
        mockBeanProperty = mock(BeanProperty.class);
        mockAnnotatedMember = mock(AnnotatedMember.class);
        mockMapSerializer = mock(MapSerializer.class);
    
        anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAnnotatedMember, mockMapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verify(mockAnnotatedMember).getValue(bean);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verify(mockAnnotatedMember).getValue(bean);
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider provider = mock(SerializerProvider.class);
        
        anyGetterWriter.resolve(provider);
        
        verify(provider).handlePrimaryContextualization(mockMapSerializer, mockBeanProperty);
    }
}