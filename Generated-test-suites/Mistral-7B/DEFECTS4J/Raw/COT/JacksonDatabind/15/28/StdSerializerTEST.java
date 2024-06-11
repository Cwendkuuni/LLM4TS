package com.example.test.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class StdSerializerTest {

    @Test
    public void testStdSerializerConstructor() {
        Class<String> handledType = String.class;
        StdSerializer<String> stdSerializer = new StdSerializer<>(handledType);
        assertEquals(handledType, stdSerializer.handledType());
    }

    @Test
    public void testStdSerializerConstructor2() {
        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(String.class);
        StdSerializer<String> stdSerializer = new StdSerializer<>(javaType);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testStdSerializerConstructor3() {
        Class<String> handledType = String.class;
        StdSerializer<String> stdSerializer = new StdSerializer<>(handledType, false);
        assertEquals(handledType, stdSerializer.handledType());
    }

    @Test
    public void testSerialize() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        Object obj = "Test";
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(stdSerializer.serialize(obj, jsonGenerator, serializerProvider)).thenCallRealMethod();

        stdSerializer.serialize(obj, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString(obj.toString());
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = String.class;

        ObjectNode actualSchema = stdSerializer.getSchema(serializerProvider, typeHint);
        ObjectNode expectedSchema = createSchemaNode("string");

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetSchemaOptional() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = String.class;
        boolean isOptional = true;

        ObjectNode actualSchema = stdSerializer.getSchema(serializerProvider, typeHint, isOptional);
        ObjectNode expectedSchema = createSchemaNode("string", isOptional);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = String.class;

        stdSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectAnyFormat(typeHint);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullProvider() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        Throwable throwable = new IOException();
        Object bean = new Object();
        String fieldName = "test";

        stdSerializer.wrapAndThrow(null, throwable, bean, fieldName);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullBean() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        Throwable throwable = new IOException();
        String fieldName = "test";

        stdSerializer.wrapAndThrow(mock(SerializerProvider.class), throwable, null, fieldName);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowNullFieldName() {
        StdSerializer<String> stdSerializer = mock(StdSerializer.class);
        Throwable throwable = new IOException();
        Object bean = new Object();

        stdSerializer.wrapAndThrow(mock(SerializerProvider.class), throwable, bean, null);
    }

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<String> jsonSerializer = mock(StdSerializer.class);

        assertTrue(StdSerializer.class.isInstance(jsonSerializer));
        assertTrue(StdSerializer.isDefaultSerializer(jsonSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty prop = mock(BeanProperty.class);
        Converter<Object, Object> converter = mock(Converter.class);
        ObjectNode node = createObjectNode();

        when(serializerProvider.converterInstance(eq(prop.getMember()), any())).thenReturn(converter);
        when(converter.getOutputType(eq(serializerProvider.getTypeFactory()))).thenReturn(IntType.INSTANCE);
        JsonSerializer<Integer> existingSerializer = mock(StdSerializer.class);

        JsonSerializer<Object> result = stdSerializer.findConvertingContentSerializer(serializerProvider, prop, existingSerializer);

        assertEquals(StdSerializer.class.cast(existingSerializer), result);
    }

    @Test
    public void testFindConvertingContentSerializerNoExistingSerializer() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty prop = mock(BeanProperty.class);
        Converter<Object, Object> converter = mock(Converter.class);
        ObjectNode node = createObjectNode();

        when(serializerProvider.converterInstance(eq(prop.getMember()), any())).thenReturn(converter);
        when(converter.getOutputType(eq(serializerProvider.getTypeFactory()))).thenReturn(IntType.INSTANCE);

        JsonSerializer<Object> result = stdSerializer.findConvertingContentSerializer(serializerProvider, prop, null);

        assertEquals(StdSerializer.class.cast(new StdDelegatingSerializer(converter, IntType.INSTANCE, null)), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullProvider() {
        StdSerializer<Object> stdSerializer = mock(StdSerializer.class);
        Object filterId = "filterId";
        Object valueToFilter = new Object();

        stdSerializer.findPropertyFilter(null, filterId, valueToFilter);
    }

    @Test
    public void testFindPropertyFilter() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        FilterProvider filters = mock(FilterProvider.class);
        Object filterId = "filterId";
        Object valueToFilter = new Object();

        when(serializerProvider.getFilterProvider()).thenReturn(filters);
        when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(mock(PropertyFilter.class));

        PropertyFilter result = stdSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);

        assertNotNull(result);
    }

    @Test
    public void testFindPropertyFilterNullFilter() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Object filterId = "filterId";
        Object valueToFilter = new Object();

        PropertyFilter result = stdSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);

        assertNull(result);
    }

    private ObjectNode createSchemaNode(String type) {
        return createSchemaNode(type, false);
    }

    private ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schema = createObjectNode();
        schema.put("type", type);
        if (!isOptional) {
            schema.put("required", !isOptional);
        }
        return schema;
    }

    private ObjectNode createObjectNode() {
        return JsonNodeFactory.instance.objectNode();
    }
}