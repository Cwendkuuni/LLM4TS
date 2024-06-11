package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonSerializer<String> customSerializer = new CustomStringSerializer();

        assertInstanceOf(JacksonStdImpl.class, stringSerializer);
        assertFalse(stringSerializer.isDefaultSerializer(customSerializer));
        assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("foo", "bar");

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new CustomStringSerializer());
        mapper.registerModule(module);

        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonSerializer<ObjectNode> existingSerializer = mapper.findValueSerializer(ObjectNode.class);

        JsonSerializer<String> convertingContentSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, node);

        assertNull(convertingContentSerializer);

        JsonSerializer<String> expectedSerializer = mapper.findValueSerializer(String.class);
        assertInstanceOf(StdSerializer.class, expectedSerializer);
        assertInstanceOf(StdSerializer.class, convertingContentSerializer);
        assertEquals(expectedSerializer.getClass(), convertingContentSerializer.getClass());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filters = mapper.getFilterProvider();
        filters.setFilterIdPrefix("custom.");

        StdSerializer<String> serializer = new StdSerializer<String>(String.class);

        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "custom.stringFilter", "test");

        assertNull(filter);

        PropertyFilter expectedFilter = filters.findPropertyFilter("stringFilter", "test");
        assertInstanceOf(PropertyFilter.FilterProviderDefault.class, expectedFilter);
        assertInstanceOf(PropertyFilter.FilterProviderDefault.class, filter);
        assertEquals(expectedFilter.getClass(), filter.getClass());
    }
}

class CustomStringSerializer extends StdSerializer<String> {
    CustomStringSerializer() {
        this(String.class);
    }

    CustomStringSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString("CUSTOM_" + value);
    }
}