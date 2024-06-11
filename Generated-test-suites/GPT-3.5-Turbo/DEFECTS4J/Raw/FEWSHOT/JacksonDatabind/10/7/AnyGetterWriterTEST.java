package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class AnyGetterWriterTest {

    @Test
    public void testAnyGetterWriterConstructor() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

        assertEquals(property, writer._property);
        assertEquals(accessor, writer._accessor);
        assertEquals(serializer, writer._mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);
        // TODO: Add assertions
    }

    @Test
    public void testGetAndFilter() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        writer.getAndFilter(bean, gen, provider, filter);
        // TODO: Add assertions
    }

    @Test
    public void testResolve() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

        SerializerProvider provider = mock(SerializerProvider.class);

        writer.resolve(provider);
        // TODO: Add assertions
    }
}