package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.test.AnnotatedClassTestBase;
import com.fasterxml.jackson.databind.ser.test.BeanPropertyWriterTestBase;

public class AnyGetterWriterTest extends AnnotatedClassTestBase<AnyGetterWriter> {

    @Override
    protected AnyGetterWriter createInstance(BeanProperty property, AnnotatedMember accessor, MapSerializer serializer) {
        return new AnyGetterWriter(property, accessor, serializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter writer = spy(createInstance(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class)));

        Map<String, Object> anyMap = new HashMap<>();
        anyMap.put("key1", "value1");
        anyMap.put("key2", "value2");

        when(_property.getName()).thenReturn("anyMap");
        when(_accessor.getValue(anyMap)).thenReturn(anyMap);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(anyMap, gen, provider);

        ObjectWriter writerForMap = mapper.writerForMapType(Map.class);
        ObjectWriter writerForAnyMap = mapper.writerForMapType(Map.class).withResolver(new BeanPropertyWriter.StdSerializer(null, null));
        writerForAnyMap.writeValue(gen, anyMap);

        mapper.verifyWritingTree(writerForAnyMap, anyMap, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter writer = spy(createInstance(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class)));

        Map<String, Object> anyMap = new HashMap<>();
        anyMap.put("key1", "value1");
        anyMap.put("key2", "value2");

        PropertyFilter filter = mock(PropertyFilter.class);
        when(_property.getName()).thenReturn("anyMap");
        when(_accessor.getValue(anyMap)).thenReturn(anyMap);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndFilter(anyMap, gen, provider, filter);

        ObjectWriter writerForMap = mapper.writerForMapType(Map.class).withResolver(new BeanPropertyWriter.StdSerializer(filter, null));
        ObjectWriter writerForAnyMap = mapper.writerForMapType(Map.class).withResolver(new AnyGetterWriter(null, null, null));
        writerForAnyMap.writeValue(gen, anyMap);

        mapper.verifyWritingTree(writerForMap, anyMap, gen, provider);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter writer = createInstance(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));
        SerializerProvider provider = mock(SerializerProvider.class);

        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(provider.handlePrimaryContextualization(mapSerializer, _property)).thenReturn(mapSerializer);

        writer.resolve(provider);

        assertEquals(mapSerializer, _mapSerializer);
    }

    @Test
    public void testValueIsNotMap() throws JsonMappingException {
        AnyGetterWriter writer = createInstance(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(MapSerializer.class));

        when(_accessor.getValue(null)).thenReturn(new Object());

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class)));
    }
}