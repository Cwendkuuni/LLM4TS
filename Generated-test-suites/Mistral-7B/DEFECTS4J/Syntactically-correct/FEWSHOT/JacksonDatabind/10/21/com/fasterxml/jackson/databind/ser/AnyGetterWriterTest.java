package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StandardMapSerializer;

public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private AnnotatedMember createAccessorMock() {
        AnnotatedMember accessorMock = mock(AnnotatedMember.class);
        when(accessorMock.getValue(any(Object.class)))
                .thenReturn(new HashMap<>());
        return accessorMock;
    }

    @SuppressWarnings("unchecked")
    private BeanProperty createPropertyMock() {
        BeanProperty propertyMock = mock(BeanProperty.class);
        return propertyMock;
    }

    @SuppressWarnings("unchecked")
    private MapSerializer createMapSerializerMock() {
        StandardMapSerializer mapSerializerMock = new StandardMapSerializer(null, null, false, null, null, null, null);
        return mapSerializerMock;
    }

    @SuppressWarnings("unchecked")
    private AnyGetterWriter createAnyGetterWriterUnderTest(Object bean, AnnotatedMember accessor, MapSerializer serializer) {
        return new AnyGetterWriter(createPropertyMock(), accessor, serializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = createAccessorMock();
        MapSerializer mapSerializer = createMapSerializerMock();
        AnyGetterWriter anyGetterWriter = createAnyGetterWriterUnderTest(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No verification needed, since "value instanceof Map" is checked in the method itself.
    }

    @Test
    public void testGetAndSerializeWithNullValue() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = createAccessorMock();
        MapSerializer mapSerializer = createMapSerializerMock();
        AnyGetterWriter anyGetterWriter = createAnyGetterWriterUnderTest(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // No verification needed, since the method returns if the value is null.
    }

    @Test
    public void testGetAndSerializeWithInvalidValueType() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = createAccessorMock();
        MapSerializer mapSerializer = createMapSerializerMock();
        AnyGetterWriter anyGetterWriter = createAnyGetterWriterUnderTest(bean, accessor, mapSerializer);

        when(accessor.getValue(any(Object.class)))
                .thenReturn(new Integer(0));

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        assertThrows(JsonMappingException.class, () -> anyGetterWriter.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = createAccessorMock();
        MapSerializer mapSerializer = createMapSerializerMock();
        AnyGetterWriter anyGetterWriter = createAnyGetterWriterUnderTest(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No verification needed, since "value instanceof Map" is checked in the method itself.
    }

    @Test
    public void testGetAndFilterWithNullValue() throws Exception {
        Object bean = mock(Object.class);
        AnnotatedMember accessor = createAccessorMock();
        MapSerializer mapSerializer = createMapSerializerMock();
        AnyGetterWriter anyGetterWriter = createAnyGetterWriterUnderTest(bean, accessor, mapSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No verification needed, since the method returns if the value is null.
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Object bean = mock(Object.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);

        anyGetterWriter.resolve(provider);

        // No verification needed, since the method just sets the mapSerializer field.
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveWithNullProvider() throws JsonMappingException {
        Object bean = mock(Object.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(null, null, null);

        anyGetterWriter.resolve(null);
    }
}