package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        SerializerProvider provider = new DefaultSerializerProvider();
        Object bean = new Object();
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class, false);
        try {
            objectSerializer.wrapAndThrow(provider, new RuntimeException("Test"), bean, "field");
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new JsonMappingException("Test"), bean, "field");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            objectSerializer.wrapAndThrow(provider, new Error(), bean, "field");
            fail("Expected Error");
        } catch (Error e) {
            // expected
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonSerializer<String> jsonSerializer = new StdSerializer<String>(String.class);
        assertNotNull(jsonSerializer);
        assertTrue(StdSerializer.class.isInstance(jsonSerializer));
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.isDefaultSerializer(jsonSerializer));
        assertFalse(StdSerializer.isDefaultSerializer(new CustomSerializer<>()));
    }

    @JsonSerialize(using = ToStringSerializer.class)
    static class TestClass {
        private int value;
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanDescription beanDesc = mapper.introspect(TestClass.class);
        PropertyDescriptor propDesc = beanDesc.findProperty("value");

        JsonSerializer<String> stringSerializer = provider.getSerializerProvider().findValueSerializer(String.class);
        JsonSerializer<Integer> intSerializer = provider.getSerializerProvider().findValueSerializer(Integer.class);

        StdSerializer<TestClass> testClassSerializer = new StdSerializer<TestClass>(TestClass.class);
        JsonSerializer<?> convertingSerializer = testClassSerializer.findConvertingContentSerializer(provider, propDesc, null);

        assertTrue(convertingSerializer instanceof StdDelegatingSerializer);
        assertTrue(convertingSerializer.getClass().isAssignableFrom(StringSerializer.class));
        assertTrue(convertingSerializer.getClass().isInstance(stringSerializer));
        assertTrue(stringSerializer.getClass().isInstance(convertingSerializer));

        JsonSerializer<Integer> resultSerializer = convertingSerializer;
        assertEquals(Integer.class, resultSerializer.handledType());
        assertEquals(intSerializer, resultSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        PropertyFilter filter = provider.getFilterProvider().findPropertyFilter("filterId", new Object());
        assertNull(filter);

        FilterProvider filterProvider = new FilterProvider() {
            @Override
            public PropertyFilter findPropertyFilter(String id, Object valueToFilter) {
                if (id.equals("filterId")) {
                    return new AlwaysIncludeFilter();
                }
                return null;
            }
        };
        provider.setFilterProvider(filterProvider);

        filter = provider.getFilterProvider().findPropertyFilter("filterId", new Object());
        assertNotNull(filter);
        assertTrue(filter instanceof AlwaysIncludeFilter);
    }
}

class CustomSerializer<T> extends StdSerializer<T> {
}

class ToStringSerializer extends StdSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}

class AlwaysIncludeFilter implements PropertyFilter {
    @Override
    public void serializeFilter(JsonGenerator jgen, SerializerProvider provider, Object bean, PropertyWriter propertyWriter) throws IOException {
        propertyWriter.writeValue(jgen, bean);
    }

    @Override
    public boolean shouldFilterProperty(String propName) {
        return false;
    }
}