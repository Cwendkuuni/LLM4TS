package com.fasterxml.jackson.databind.ser;

import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        //verify that the correct methods were called
        verify(accessor).getValue(bean);
        verify(gen, never()).writeObject(any());
        verify(serializer).serializeFields(any(Map.class), eq(gen), eq(provider));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeFailure() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        //verify that the correct methods were called
        verify(accessor).getValue(bean);
        verify(gen, never()).writeObject(any());
        verify(serializer).serializeFilteredFields(any(Map.class), eq(gen), eq(provider), eq(filter), eq(null));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterFailure() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.resolve(provider);

        //verify that the correct methods were called
        verify(provider).handlePrimaryContextualization(eq(serializer), eq(property));
    }
}