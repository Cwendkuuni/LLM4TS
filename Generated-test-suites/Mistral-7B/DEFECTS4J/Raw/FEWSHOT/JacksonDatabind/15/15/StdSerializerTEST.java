package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(ClassUtil.isJacksonStdImpl(StdSerializer.class));
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.isDefaultSerializer(stringSerializer));

        MyCustomSerializer myCustomSerializer = new MyCustomSerializer();
        assertFalse(StdSerializer.class.isInstance(myCustomSerializer));
        assertFalse(ClassUtil.isJacksonStdImpl(MyCustomSerializer.class));
        assertFalse(StdSerializer.isDefaultSerializer(myCustomSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("list", new Object[] { 1, 2, 3 });

        JavaType javaType = mapper.getTypeFactory().constructType(Object[].class);
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(javaType);

        StdSerializer<ObjectNode> objectNodeSerializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonSerializer<Object> convertingContentSerializer = objectNodeSerializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, node);

        assertNotNull(convertingContentSerializer);
        assertTrue(StdSerializer.class.isInstance(convertingContentSerializer));
        assertTrue(ObjectNodeSerializer.class.isAssignableFrom(convertingContentSerializer.getClass()));
        assertEquals(existingSerializer, convertingContentSerializer.getDelegate());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectFilter filter = new ObjectFilter();

        FilterProvider filterProvider = mapper.getFilterProvider();
        PropertyFilter propertyFilter = objectSerializer.findPropertyFilter(mapper.getSerializerProvider(), "filter1", new Object());
        assertNull(propertyFilter);

        filterProvider.registerFilter(new SimpleFilterProvider().emptyFilterProvider().withFilter("filter1", filter));
        propertyFilter = objectSerializer.findPropertyFilter(mapper.getSerializerProvider(), "filter1", new Object());
        assertNotNull(propertyFilter);
        assertEquals(filter, propertyFilter);
    }

    @JsonSerializer(using = ToStringSerializer.class)
    static class MyCustomSerializer {}

    static class ToStringSerializer extends StdSerializer<String> {
        public ToStringSerializer() { super(String.class); }
        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.toString());
        }
    }

    static class ObjectFilter implements PropertyFilter {
        @Override
        public void serializeFilter(ObjectSerializerProvider SerializerProvider,
                                   PropertyWriter propertyWriter,
                                   Object value,
                                   TypeSerializer serializer) throws IOException {
            // empty implementation
        }

        @Override
        public void serializeFilter(ObjectSerializerProvider SerializerProvider,
                                   JsonGenerator jgen,
                                   Object value,
                                   TypeSerializer serializer) throws IOException {
            // empty implementation
        }
    }
}