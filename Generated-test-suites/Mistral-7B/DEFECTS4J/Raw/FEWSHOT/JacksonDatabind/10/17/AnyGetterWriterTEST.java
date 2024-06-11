package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;

public class AnyGetterWriterTest {

    private ObjectNode testMap;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private AnyGetterWriter writer;
    private MapSerializer mapSerializer;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private PropertyFilter filter;

    @Before
    public void setup() {
        testMap = mock(ObjectNode.class);
        property = mock(BeanProperty.class);
        accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("testProperty");
        mapSerializer = mock(MapSerializer.class);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        writer = new AnyGetterWriter(property, accessor, mapSerializer);
        filter = mock(PropertyFilter.class);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = mock(Object.class);
        Object mapValue = new HashMap<>();
        when(accessor.getValue(bean)).thenReturn(mapValue);

        writer.getAndSerialize(bean, gen, provider);

        // Verify that the map was serialized
        ObjectNode serializedMap = (ObjectNode) testMap;
        // You may want to add assertions to verify the contents of the serialized map
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        Object bean = mock(Object.class);
        when(accessor.getValue(bean)).thenReturn(null);

        writer.getAndSerialize(bean, gen, provider);

        // No serialization should have occurred
        assertNull(testMap);
    }

    @Test
    public void testGetAndSerialize_NonMapValue() throws Exception {
        Object bean = mock(Object.class);
        Object nonMapValue = new Object();
        when(accessor.getValue(bean)).thenReturn(nonMapValue);

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = mock(Object.class);
        Object mapValue = new HashMap<>();
        when(accessor.getValue(bean)).thenReturn(mapValue);

        writer.getAndFilter(bean, gen, provider, filter);

        // Verify that the map was filtered
        // You may want to add assertions to verify the contents of the filtered map
    }

    @Test
    public void testResolve() throws JsonMappingException {
        writer.resolve(provider);

        // Verify that _mapSerializer was set correctly
        Assert.assertNotNull(writer._mapSerializer);
    }
}