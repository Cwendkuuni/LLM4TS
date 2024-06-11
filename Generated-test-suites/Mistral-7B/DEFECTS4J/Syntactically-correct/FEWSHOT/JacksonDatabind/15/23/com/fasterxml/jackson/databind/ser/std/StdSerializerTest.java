package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
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
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isInstance(stringSerializer.isDefaultSerializer(stringSerializer)));

        MyCustomSerializer<Integer> customSerializer = new MyCustomSerializer<>();
        assertFalse(StdSerializer.class.isInstance(customSerializer));
        assertFalse(StdSerializer.class.isInstance(stringSerializer.isDefaultSerializer(customSerializer)));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String[].class, new StringArraySerializer());
        mapper.registerModule(module);

        StdSerializer<Object> handler = new StdSerializer<Object>(Object.class);
        Object value = new Object[]{"foo", "bar"};

        JsonSerializer<Object> serializer = handler.findConvertingContentSerializer(mapper.getSerializerProvider(), null, value);
        assertTrue(serializer instanceof StdSerializer);
        assertTrue(serializer.getClass().equals(StringArraySerializer.class));

        serializer = handler.findConvertingContentSerializer(mapper.getSerializerProvider(), null, value);
        assertEquals(serializer, handler);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.setFilterId("filter1", new MyPropertyFilter());

        StdSerializer<Object> handler = new StdSerializer<Object>(Object.class);
        Object value = "test";

        PropertyFilter filter = handler.findPropertyFilter(mapper.getSerializerProvider(), "filter1", value);

        assertNotNull(filter);
        assertTrue(filter instanceof MyPropertyFilter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullFilterProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> handler = new StdSerializer<Object>(Object.class);
        Object value = "test";

        handler.findPropertyFilter(null, "filter1", value);
    }
}

class MyCustomSerializer<T> extends StdSerializer<T> {
    public MyCustomSerializer() {
        this(String.class);
    }

    public MyCustomSerializer(Class<T> t) {
        super(t);
    }

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        // Custom serialization logic
    }
}

class MyPropertyFilter implements PropertyFilter {
    @Override
    public void serializeAsSchema(JsonFormatVisitorWrapper visitor, SerializerProvider provider, Object el) throws JsonMappingException {
        // Filtering logic
    }

    @Override
    public void serializeAsValue(SerializerProvider provider, JsonGenerator jgen, Object el) throws IOException, JsonProcessingException {
        // Filtering logic
    }
}

class StringArraySerializer extends StdSerializer<Object> {
    public StringArraySerializer() {
        this(Object[].class);
    }

    public StringArraySerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        ObjectNode node = provider.getJsonFactory().createObjectNode();
        node.put("value", TextNode.valueOf(java.util.Arrays.toString((Object[]) value)));
        jgen.writeTree(node);
    }
}

class SimpleFilterProvider extends FilterProvider {
    private final Map<String, PropertyFilter> filters = new HashMap<>();

    public PropertyFilter findFilterById(String id) {
        return filters.get(id);
    }

    public void setFilter(String id, PropertyFilter filter) {
        filters.put(id, filter);
    }
}