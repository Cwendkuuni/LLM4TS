package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testStdSerializer_handleType() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testStdSerializer_acceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null);
        intSerializer.acceptJsonFormatVisitor(visitor, null);
        assertNull(visitor.getFormatId());
    }

    @Test
    public void testStdSerializer_createSchemaNode() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = stringSerializer.createSchemaNode("string");
        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testStdSerializer_createSchemaNodeWithOptional() {
        StdSerializer<Boolean> booleanSerializer = new StdSerializer<Boolean>(Boolean.class);
        ObjectNode schemaNode = booleanSerializer.createSchemaNode("boolean");
        assertEquals("boolean", schemaNode.get("type").asText());
        assertFalse(schemaNode.get("required").isBoolean());
    }

    @Test
    public void testStdSerializer_wrapAndThrow() throws IOException {
        SerializerProvider provider = new SerializerProvider();
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class);
        assertThrows(JsonMappingException.class, () -> intSerializer.wrapAndThrow(provider, new IllegalArgumentException(), null, ""));
    }

    @Test
    public void testStdSerializer_findPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        Object valueToFilter = new Object();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public void serializeAsSchema(JsonFormatVisitorWrapper visitor, SerializerProvider serializers)
                    throws JsonMappingException {
            }

            @Override
            public void serializeAsSchema(JsonFormatVisitorWrapper visitor, JavaType typeHint, SchemaAware schema)
                    throws JsonMappingException {
            }
        };
        FilterProvider filters = new SimpleFilterProvider();
        filters.addFilter("myFilterId", filter);
        Object filterId = "myFilterId";
        assertEquals(filter, intSerializer.findPropertyFilter(provider, filterId, valueToFilter));
    }

    @Test
    public void testStdSerializer_findConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        Object targetType = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getOutputType(provider.getTypeFactory())).thenReturn(targetType);
        JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);
        when(provider.converterInstance(property.getMember(), eq(PropertySerializerBindings.Filtered.class))).thenReturn(converter);
        when(provider.findValueSerializer(targetType)).thenReturn(existingSerializer);
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class, false);
        JsonSerializer<Object> result = serializer.findConvertingContentSerializer(provider, property, existingSerializer);
        assertEquals(new StdDelegatingSerializer(converter, targetType, existingSerializer), result);
    }
}