package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.SerializationConfig;
import com.fasterxml.jackson.databind.ser.std.SerializerBase;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test class for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static final AnyGetterWriter AGW_INSTANCE = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectNode testObject = mapper().createObjectNode();
        testObject.put("foo", mapper().valueToTree("bar"));
        Object bean = testObject.asSingleValueTree().asJava();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);
        when(provider.findValueSerializer(Map.class)).thenReturn(AGW_INSTANCE._mapSerializer);

        AGW_INSTANCE.getAndSerialize(bean, gen, provider);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("foo");
        verify(gen, times(1)).writeValue(mapper().valueToTree("bar"));
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectNode testObject = mapper().createObjectNode();
        testObject.put("foo", mapper().valueToTree("bar1"));
        testObject.put("bar", mapper().valueToTree("baz"));

        Object bean = testObject.asSingleValueTree().asJava();
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        ObjectWriter writer = mapper().writerWithView(new SimpleBeanPropertyView(Object.class, false));
        ObjectNode result = writer.writeValueAsTree(bean);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(provider.getSerializerProvider()).thenReturn(provider);
        when(provider.findValueSerializer(Map.class)).thenReturn(AGW_INSTANCE._mapSerializer);
        when(filter.includeMember("bar")).thenReturn(true);

        AGW_INSTANCE.getAndFilter(bean, gen, provider, filter);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("foo");
        verify(gen, times(1)).writeValue(mapper().valueToTree("bar1"));
        verify(gen, times(1)).writeFieldName("bar");
        verify(gen, times(1)).writeValue(mapper().valueToTree("baz"));
        verify(gen, times(1)).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithInvalidValueType() throws Exception {
        Object bean = new Object();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getSerializerProvider()).thenReturn(provider);

        AGW_INSTANCE.getAndSerialize(bean, gen, provider);

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testResolve() throws Exception {
        SerializerProvider provider = mock(SerializerProvider.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        AGW_INSTANCE.resolve(provider);

        verify(provider, times(1)).handlePrimaryContextualization(mapSerializer, any(BeanProperty.class));
    }
}