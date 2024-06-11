package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Test cases for class {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

    private static final NameTransformer NAME_TRANSFORMER = new NameTransformer() {
        @Override
        public String transform(String name, JsonSerializer<?> s) {
            return name;
        }
    };

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Object bean = new Object();
        MapSerializer mapSerializer = new MapSerializer(ObjectNode.class, new StandardSerializer<>(ObjectNode.class), null, NAME_TRANSFORMER);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        verify(mapSerializer, times(1)).serializeFields(map, gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithInvalidValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Object bean = new Object();
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);

        Object invalidValue = new Object();

        when(accessor.getValue(bean)).thenReturn(invalidValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(bean, gen, provider);

        verify(provider, times(1)).handleUnexpectedValue(eq(accessor), eq(invalidValue), any());
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Object bean = new Object();
        MapSerializer mapSerializer = spy(new MapSerializer(ObjectNode.class, Serializers.findSerializabelValueSerializer(ObjectNode.class), null, NAME_TRANSFORMER));
        PropertyFilter filter = mock(PropertyFilter.class);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(map);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndFilter(bean, gen, provider, filter);

        verify(mapSerializer, times(1)).serializeFilteredFields(map, gen, provider, eq(filter), any());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNullMapSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Object bean = new Object();
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);

        writer.getAndFilter(bean, gen, provider, filter);

        verify(provider, times(1)).handleMissingSerializer(eq(AnyGetterWriter.class), eq("getAndFilter"));
    }
}