package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationTest;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test case for {@link ValueInstantiator} abstract class.
 */
public class ValueInstantiatorTest extends DeserializationTest {

    @Test
    public void testDefaultInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Default instantiator should create instances of Object class
        assertEquals(Integer.class, mapper.getTypeFactory().constructType(ValueInstantiator.class));
        assertEquals(Object.class, new ValueInstantiator().getValueClass());

        // Test default creator (no arguments)
        ValueInstantiator instantiator = new ValueInstantiator() {
            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        Object result = mapper.readValue("{}", instantiator.getType());
        assertInstanceOf(Object.class, result);

        // Test invalid JSON
        assertThrows(JsonMappingException.class, () -> mapper.readValue("{}", instantiator.getType()));
        assertThrows(IOException.class, () -> mapper.readValue("{ \"foo\": 1 }", instantiator.getType()));

        // Test null value
        result = mapper.readValue(null, instantiator.getType());
        assertNull(result);
    }

    @Test
    public void testCustomInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Custom instantiator for Integer class
        class MyIntInstantiator extends ValueInstantiator {
            @Override
            public Class<?> getValueClass() {
                return Integer.class;
            }

            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Integer createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value * 2;
            }
        }

        MyIntInstantiator instantiator = new MyIntInstantiator();
        Object result = mapper.readValue("3", instantiator.getType());
        assertEquals(6, result);

        // Test invalid JSON
        assertThrows(JsonMappingException.class, () -> mapper.readValue("{}", instantiator.getType()));
        assertThrows(IOException.class, () -> mapper.readValue("{ \"foo\": 1 }", instantiator.getType()));

        // Test null value
        result = mapper.readValue(null, instantiator.getType());
        assertNull(result);
    }
}