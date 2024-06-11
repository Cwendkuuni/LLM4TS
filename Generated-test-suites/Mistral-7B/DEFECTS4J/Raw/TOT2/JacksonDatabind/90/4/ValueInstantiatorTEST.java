package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.StdValueInstantiator;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorDefault() throws IOException {
        Class<ValueInstantiator> valueInstantiatorClass = ValueInstantiator.class;
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(valueInstantiatorClass);
        assertNotNull(instantiator);
        Object instantiatedObject = instantiator.createUsingDefault(mapper.getDeserializationContext().getDefaultParser());
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Object);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorMissingCreatorString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(ValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        String value = "test";
        instantiator.createFromString(mapper.getDeserializationContext(), value);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorMissingCreatorInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(ValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        int value = 1;
        instantiator.createFromInt(mapper.getDeserializationContext(), value);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorMissingCreatorLong() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(ValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        long value = 1L;
        instantiator.createFromLong(mapper.getDeserializationContext(), value);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorMissingCreatorDouble() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(ValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        double value = 1.0;
        instantiator.createFromDouble(mapper.getDeserializationContext(), value);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorMissingCreatorBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(ValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        boolean value = true;
        instantiator.createFromBoolean(mapper.getDeserializationContext(), value);
    }

    @Test
    public void testValueInstantiatorCanCreateUsingDefault() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(Object.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        Object instantiatedObject = instantiator.createUsingDefault(mapper.getDeserializationContext().getDefaultParser());
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Object);
    }

    @Test
    public void testValueInstantiatorCanCreateFromObjectWith() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(Map.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        Map<String, Object> map = Map.of("key", "value");
        Object instantiatedObject = instantiator.createFromObjectWith(mapper.getDeserializationContext(), map.entrySet().toArray(new Map.Entry[0]));
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Map);
        assertEquals(map, instantiatedObject);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorCannotCreateFromObjectWithInvalidArgs() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(List.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        List<String> list = List.of("value1", "value2");
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { list });
    }

    @Test
    public void testValueInstantiatorStringCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(String.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromString());
        String value = "value";
        Object instantiatedObject = instantiator.createFromString(mapper.getDeserializationContext(), value);
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof String);
        assertEquals(value, instantiatedObject);
    }

    @Test
    public void testValueInstantiatorIntCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(Integer.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromInt());
        int value = 1;
        Object instantiatedObject = instantiator.createFromInt(mapper.getDeserializationContext(), value);
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Integer);
        assertEquals(value, instantiatedObject);
    }

    @Test
    public void testValueInstantiatorLongCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(Long.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromLong());
        long value = 1L;
        Object instantiatedObject = instantiator.createFromLong(mapper.getDeserializationContext(), value);
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Long);
        assertEquals(value, instantiatedObject);
    }

    @Test
    public void testValueInstantiatorDoubleCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(Double.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromDouble());
        double value = 1.0;
        Object instantiatedObject = instantiator.createFromDouble(mapper.getDeserializationContext(), value);
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Double);
        assertEquals(value, instantiatedObject);
    }

    @Test
    public void testValueInstantiatorBooleanCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(Boolean.class);
        JavaType javaType = mapper.getTypeFactory().constructType(StdValueInstantiator.class);
        ValueInstantiator instantiator = mapper.findValueInstantiator(javaType);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromBoolean());
        boolean value = true;
        Object instantiatedObject = instantiator.createFromBoolean(mapper.getDeserializationContext(), value);
        assertNotNull(instantiatedObject);
        assertTrue(instantiatedObject instanceof Boolean);
        assertEquals(value, instantiatedObject);
    }
}