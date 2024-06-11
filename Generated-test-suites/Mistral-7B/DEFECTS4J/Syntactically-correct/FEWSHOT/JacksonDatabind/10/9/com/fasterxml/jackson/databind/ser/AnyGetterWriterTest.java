package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private ObjectNode testObjectNode;
    private JsonGenerator testJsonGenerator;
    private SerializerProvider testSerializerProvider;
    private MapSerializer testMapSerializer;
    private PropertyFilter testPropertyFilter;
    private Object testBean;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testObjectNode = mock(ObjectNode.class);
        testJsonGenerator = mock(JsonGenerator.class);
        testSerializerProvider = mock(SerializerProvider.class);
        testMapSerializer = mock(MapSerializer.class);
        testPropertyFilter = mock(PropertyFilter.class);
        BeanPropertyDefinition testPropertyDefinition = new BeanPropertyDefinition("testProperty", String.class, new Class[0], null, null, null);
        AnnotatedMember testAccessor = mock(AnnotatedMember.class);
        when(testAccessor.getValue(any(Object.class))).thenReturn("testValue");
        anyGetterWriter = new AnyGetterWriter(new BeanProperty("testProperty", testPropertyDefinition), testAccessor, testMapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws IOException {
        when(testSerializerProvider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class))).thenReturn(testMapSerializer);
        when(testMapSerializer.serializeFields((Map<?,?>) any(Object.class), testJsonGenerator, testSerializerProvider)).thenReturn(testObjectNode);

        anyGetterWriter.getAndSerialize(testBean, testJsonGenerator, testSerializerProvider);

        verify(testSerializerProvider).handlePrimaryContextualization(_mapSerializer, _property);
        verify(testMapSerializer).serializeFields((Map<?,?>) eq("testValue"), testJsonGenerator, testSerializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws IOException {
        when(testSerializerProvider.handlePrimaryContextualization(any(MapSerializer.class), any(BeanProperty.class))).thenReturn(testMapSerializer);
        when(testBean.getClass()).thenReturn(String.class);

        anyGetterWriter.getAndSerialize(testBean, testJsonGenerator, testSerializerProvider);

        verify(testSerializerProvider).handlePrimaryContextualization(_mapSerializer, _property);
        verify(testMapSerializer).serializeFields(isNull(), testJsonGenerator, testSerializerProvider);
        verify(testJsonGenerator).throwMappingException(eq("Value returned by 'any-getter' (testProperty()) not java.util.Map but " + String.class.getName()));
    }

    @Test
    public void testGetAndFilter() throws IOException {
        ObjectNode testFilteredObjectNode = mock(ObjectNode.class);
        when(testMapSerializer.serializeFilteredFields((Map<?,?>) any(Object.class), testJsonGenerator, testSerializerProvider, eq(testPropertyFilter), anyLong())).thenReturn(testFilteredObjectNode);

        anyGetterWriter.getAndFilter(testBean, testJsonGenerator, testSerializerProvider, testPropertyFilter);

        verify(testSerializerProvider).handlePrimaryContextualization(_mapSerializer, _property);
        verify(testMapSerializer).serializeFilteredFields((Map<?,?>) eq("testValue"), testJsonGenerator, testSerializerProvider, eq(testPropertyFilter), isNull());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        when(testSerializerProvider.handlePrimaryContextualization(_mapSerializer, _property)).thenReturn(testMapSerializer);

        anyGetterWriter.resolve(testSerializerProvider);

        verify(testSerializerProvider).handlePrimaryContextualization(_mapSerializer, _property);
    }

    @Test
    public void testResolveWithNullMapSerializer() throws JsonMappingException {
        when(_mapSerializer).thenReturn(null);

        anyGetterWriter.resolve(testSerializerProvider);

        assertNotNull(_mapSerializer);
    }
}