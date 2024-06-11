package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testValueInstantiatorDefault() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(String.class);
        assertEquals("Default test for String instantiator", instantiator.createUsingDefault(objectMapper.getDeserializationContext()));
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromObjectWithNullArgs() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object.class);
        instantiator.createFromObjectWith(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testValueInstantiatorFromObjectWithValidArgs() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Map.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("key1", 1);
        inputMap.put("key2", 2);

        ObjectWriter writer = mapper.writer().withTypeValidator(null);
        String json = writer.writeValueAsString(inputMap);

        Object inputObject = objectMapper.readValue(json, Map.class);

        Object outputObject = instantiator.createFromObjectWith(objectMapper.getDeserializationContext(), inputObject.getClass().getDeclaredComponents()[0]);

        Map<String, Integer> expectedOutputMap = new HashMap<>();
        expectedOutputMap.put("key1", 1);
        expectedOutputMap.put("key2", 2);

        assertEquals("Test for Map instantiator from ObjectWith", expectedOutputMap, outputObject);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromStringNull() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object.class);
        instantiator.createFromString(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testValueInstantiatorFromStringValid() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(StringBuilder.class);

        assertEquals("Test for StringBuilder instantiator from String", "TestStringBuilder", instantiator.createFromString(objectMapper.getDeserializationContext(), "TestStringBuilder"));
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromIntNull() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object.class);
        instantiator.createFromInt(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testValueInstantiatorFromIntValid() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Integer.class);

        assertEquals("Test for Integer instantiator from Int", 42, instantiator.createFromInt(objectMapper.getDeserializationContext(), 42));
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromLongNull() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object.class);
        instantiator.createFromLong(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testValueInstantiatorFromLongValid() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Long.class);

        assertEquals("Test for Long instantiator from Long", 42L, instantiator.createFromLong(objectMapper.getDeserializationContext(), 42L));
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromDoubleNull() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object.class);
        instantiator.createFromDouble(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testValueInstantiatorFromDoubleValid() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Double.class);

        assertEquals("Test for Double instantiator from Double", 42.0D, instantiator.createFromDouble(objectMapper.getDeserializationContext(), 42.0D), 0.001);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromBooleanNull() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object.class);
        instantiator.createFromBoolean(objectMapper.getDeserializationContext(), null);
    }

    @Test
    public void testValueInstantiatorFromBooleanValid() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Boolean.class);

        assertEquals("Test for Boolean instantiator from Boolean", true, instantiator.createFromBoolean(objectMapper.getDeserializationContext(), true));
        assertEquals("Test for Boolean instantiator from Boolean", false, instantiator.createFromBoolean(objectMapper.getDeserializationContext(), false));
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromObjectWithEmptyList() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(Object[].class);
        instantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[0]);
    }

    @Test
    public void testValueInstantiatorFromObjectWithValidArguments() throws IOException {
        ValueInstantiator instantiator = objectMapper.findValueInstantiator(ArrayValueInstantiatorTest.Person[].class);

        ObjectMapper mapper = new ObjectMapper();
        ArrayValueInstantiatorTest.Person[] inputArray = {new ArrayValueInstantiatorTest.Person("John", 30), new ArrayValueInstantiatorTest.Person("Mike", 28)};

        ObjectWriter writer = mapper.writer().withTypeValidator(null);
        String json = writer.writeValueAsString(inputArray);

        Object inputObject = objectMapper.readValue(json, ArrayValueInstantiatorTest.Person[].class);

        Object outputObject = instantiator.createFromObjectWith(objectMapper.getDeserializationContext(), inputObject.getClass().getDeclaredComponents()[0]);

        ArrayValueInstantiatorTest.Person[] expectedOutputArray = {new ArrayValueInstantiatorTest.Person("John", 30), new ArrayValueInstantiatorTest.Person("Mike", 28)};

        assertEquals("Test for array instantiator from ObjectWith", expectedOutputArray, outputObject);
    }
}

class ArrayValueInstantiatorTest {

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}