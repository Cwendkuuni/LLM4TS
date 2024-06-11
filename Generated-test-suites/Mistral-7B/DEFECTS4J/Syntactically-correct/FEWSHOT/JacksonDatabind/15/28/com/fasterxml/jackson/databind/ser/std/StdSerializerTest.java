package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<Object> serializer1 = new StdSerializer<Object>(Object.class) {};
        JsonSerializer<Object> serializer2 = mock(JsonSerializer.class);
        when(serializer2.getClass()).thenReturn(MyCustomSerializer.class);

        assertTrue(StdSerializer.isDefaultSerializer(serializer1));
        assertFalse(StdSerializer.isDefaultSerializer(serializer2));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        BeanProperty prop = mock(BeanProperty.class);
        when(prop.getMember()).thenReturn(null);

        SerializerProvider provider = mock(SerializerProvider.class);
        Object valueToFilter = new Object();

        Converter<Object, Object> converterMock = mock(Converter.class);
        when(converterMock.getOutputType(Mockito.any(JsonTypeFactory.class))).thenReturn(String.class);

        JsonSerializer<String> existingSerializerMock = mock(JsonSerializer.class);

        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {};

        Object filterId = "filterId";

        // Test when no existing serializer is passed
        JsonSerializer<String> resultSerializer1 = serializer.findConvertingContentSerializer(provider, prop, null);
        assertNull(resultSerializer1);

        // Test when existing serializer is passed
        when(provider.findValueSerializer(String.class)).thenReturn(existingSerializerMock);
        JsonSerializer<String> resultSerializer2 = serializer.findConvertingContentSerializer(provider, prop, existingSerializerMock);
        assertNotNull(resultSerializer2);
        assertSame(existingSerializerMock.getClass(), resultSerializer2.getClass());

        // Test when converter is not found
        when(provider.converterInstance(prop.getMember(), filterId)).thenReturn(converterMock);
        when(converterMock.getOutputType(Mockito.any(JsonTypeFactory.class))).thenThrow(new IllegalStateException());
        assertNull(serializer.findConvertingContentSerializer(provider, prop, existingSerializerMock));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        Object filterId = "filterId";
        Object valueToFilter = new Object();

        SerializerProvider provider = mock(SerializerProvider.class);
        FilterProvider filters = mock(FilterProvider.class);

        PropertyFilter filterMock = mock(PropertyFilter.class);

        when(provider.getFilterProvider()).thenReturn(filters);
        when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(filterMock);

        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {};

        PropertyFilter resultFilter = serializer.findPropertyFilter(provider, filterId, valueToFilter);
        assertSame(filterMock.getClass(), resultFilter.getClass());

        // Test when filter is not found
        when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(null);
        assertNull(serializer.findPropertyFilter(provider, filterId, valueToFilter));
    }
}