package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<?> serializer = new StdSerializer<Object>() {};
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanDescription beanDescription = objectMapper.getSerializationConfig().findBeanDescription(Object.class);
        JsonSerializer<Object> existingSerializer = objectMapper.findValueSerializer(JavaType.getTypeQuery(Object.class));

        ObjectNode node = objectMapper.createObjectNode();
        node.put("class", "java.util.ArrayList");
        node.put("value", "[1, 2, 3]");

        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        Object propertyName = "myProperty";
        Mockito.when(member.getName()).thenReturn(propertyName);
        Mockito.when(member.getMember()).thenReturn(null);

        AnnotationIntrospector introspector = objectMapper.getAnnotationIntrospector();
        Object converterAnnotation = new Object() {}; // A dummy converter annotation
        Mockito.when(introspector.findSerializationContentConverter(member)).thenReturn(converterAnnotation);

        Converter<Object, Object> converter = objectMapper.getFactory().getConverterInstance(member, converterAnnotation);
        JavaType delegateType = converter.getOutputType(objectMapper.getTypeFactory());

        StdSerializer<Object> serializer = new StdSerializer<Object>(JavaType.getTypeQuery(Object.class)) {};
        StdSerializer<Object> result = serializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), member, existingSerializer);

        assertTrue(result instanceof StdDelegatingSerializer);
        StdDelegatingSerializer delegatingSerializer = (StdDelegatingSerializer) result;
        assertEquals(converter, delegatingSerializer.getConverter());
        assertEquals(delegateType, delegatingSerializer.getDelegateSerializer().handledType());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        FilterProvider filterProvider = Mockito.mock(FilterProvider.class);
        Object filterId = "myFilter";
        Object valueToFilter = new Object() {};

        PropertyFilter filter = objectMapper.getSerializerProvider().getFilterProvider().findPropertyFilter(filterId, valueToFilter);

        assertNull(filter);

        Mockito.when(filterProvider.findPropertyFilter(filterId, valueToFilter)).thenReturn(new PropertyFilter() {});

        StdSerializer<Object> serializer = new StdSerializer<Object>(JavaType.getTypeQuery(Object.class)) {};
        PropertyFilter result = serializer.findPropertyFilter(objectMapper.getSerializerProvider(), filterId, valueToFilter);

        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithNullFilterProvider() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object filterId = "myFilter";
        Object valueToFilter = new Object() {};

        StdSerializer<Object> serializer = new StdSerializer<Object>(JavaType.getTypeQuery(Object.class)) {};
        serializer.findPropertyFilter(null, filterId, valueToFilter);
    }
}