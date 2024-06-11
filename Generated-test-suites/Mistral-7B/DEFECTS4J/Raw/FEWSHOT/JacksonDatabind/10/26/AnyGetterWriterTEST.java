package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Class test for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

    private ObjectMapper objectMapper;
    private AnnotatedMember accessor;
    private BeanProperty property;
    private MapSerializer mapSerializer;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        property = mock(BeanProperty.class);
        accessor = mock(AnnotatedMember.class);
        when(accessor.getValue(anyObject())).thenReturn(new HashMap<>());
        mapSerializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = anyObject();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Verify the MapSerializer is used for serialization
        verify(mapSerializer).serializeFields((Map<?,?>) anyMap(), eq(gen), eq(provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = anyObject();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Verify the MapSerializer is used for serialization with filter
        verify(mapSerializer).serializeFilteredFields((Map<?,?>) anyMap(), eq(gen), eq(provider), eq(filter), null);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.resolve(provider);

        verify(provider).handlePrimaryContextualization(eq(mapSerializer), eq(property));
    }

    @Test
    public void testExceptionWhenValueIsNotMap() throws Exception {
        Object bean = anyObject();

        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class)));
    }

    private Object anyObject() {
        return BeanUtil.newInstance(new Class[0]);
    }

    private Map<?, ?> anyMap() {
        return new HashMap<>();
    }
}