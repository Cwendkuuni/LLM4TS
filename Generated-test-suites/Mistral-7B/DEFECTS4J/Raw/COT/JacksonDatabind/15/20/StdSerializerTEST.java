package com.fasterxml.jackson.databind.ser.std; // Import necessary packages

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.Wrapper;

import org.junit.*;

public class StdSerializerTest {

    

    @BeforeClass
    public static void setup() {
        // Setup class under test
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
    }

    @Test
    public void testHandleType() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new StdSerializer<String>(Object.class, true);
    }

    @Test
    public void testSerialize() throws Exception {
        String value = "Test";
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        stringSerializer.serialize(value, generator, new DefaultSerializerProvider());
        assertEquals("Test", generator.getOutputStr());
    }

    @Test
    public void testGetSchema() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode schema = stringSerializer.getSchema(new DefaultSerializerProvider(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonFormatVisitorWrapper(new ObjectMapper().getFactory());
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
        assertTrue(visitor.expectedAnyFormat(String.class));
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        SerializerProvider provider = new DefaultSerializerProvider();
        Throwable t = new IOException("");
        Object bean = new Object();
        try {
            stringSerializer.wrapAndThrow(provider, t, bean, "fieldName");
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            stringSerializer.wrapAndThrow(provider, t, bean, 0);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        t = new RuntimeException("");
        try {
            stringSerializer.wrapAndThrow(provider, t, bean, "fieldName");
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Expected
        }

        t = new IllegalArgumentException("");
        try {
            stringSerializer.wrapAndThrow(provider, t, bean, "fieldName");
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonSerializer<String> anotherSerializer = new MyCustomSerializer();
        assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(anotherSerializer));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        Object valueToFilter = new Object();

        // Test when no filter is provided
        StdSerializer<List<Integer>> listSerializer = new StdSerializer<List<Integer>>(List.class);
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        assertEquals(intSerializer, listSerializer.findConvertingContentSerializer(provider, null, valueToFilter));

        // Test when filter is provided
        @JsonSerialize(using = MyIntToListConverter.class)
        static class MyBean {
            int value;
        }

        @JsonDeserializer(using = MyListToIntConverter.class)
        static class MyDeserializer {
            @JsonProperty
            public int value;
        }

        Converter<Object,List<Integer>> converter = mapper.injectValues(MyBean.class, MyDeserializer.class);
        JsonSerializer<MyBean> beanSerializer = mapper.findValueSerializer(MyBean.class);
        assertEquals(new StdDelegatingSerializer(converter, List.class, intSerializer),
                beanSerializer.findConvertingContentSerializer(provider, null, valueToFilter));
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        Object valueToFilter = new Object();

        FilterProvider filters = new SimpleFilterProvider();
        filters.setFilterID("testFilter", new MyPropertyFilter());

        provider.setFilterProvider(filters);

        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        PropertyFilter filter = stringSerializer.findPropertyFilter(provider, "testFilter", valueToFilter);
        assertNotNull(filter);
    }

    static class MyIntToListConverter implements Converter<Object,List<Integer>> {
        @Override
        public List<Integer> convert(Object value, DeserializationContext ctx) throws IOException {
            return new ArrayList<Integer>() {{ add((Integer) value); }};
        }

        @Override
        public Object convertValue(Object value, Type descriptor) throws IOException {
            return ((List<Integer>) value).get(0);
        }

        @Override
        public Wrapper<Object> wrap(Object value, Type descriptor) {
            return null;
        }
    }

    static class MyListToIntConverter {
        @JsonProperty
        public int value;
    }

    static class MyPropertyFilter extends PropertyFilter {
        @Override
        public void serializeFilteredValue(PropertyWriter writer, Object value, Type typeHint, SerializerProvider provider)
            throws IOException {
            writer.writeString("filtered");
        }

        @Override
        public boolean appliesTo(SerializerProvider provider, BeanProperty property) {
            return true;
        }
    }

    static class MyCustomSerializer extends StdSerializer<String> {
    }

    
}