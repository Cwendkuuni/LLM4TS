package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.getDefaultInstance().findValueInstantiator(String.class);

        String jsonString = "\"Hello World\"";
        String value = instantiator.createUsingDefault(mapper.getDeserializationContext().readerFor(new StringType()));
        assertEquals("Hello World", value);
    }

    @Test
    public void testStringInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);

        String jsonString = "\"Hello World\"";
        String value = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{"prefix", "Hello World"});
        assertEquals("Hello World", value);
    }

    @Test(expected = RuntimeException.class)
    public void testMissingInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);

        String jsonString = "42";
        instantiator.createFromString(mapper.getDeserializationContext(), jsonString);
    }

    @Test
    public void testIntInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);

        String jsonString = "42";
        Integer value = instantiator.createFromInt(mapper.getDeserializationContext(), Integer.parseInt(jsonString));
        assertEquals(42, value.intValue());
    }

    @Test
    public void testLongInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);

        String jsonString = "42";
        Long value = instantiator.createFromLong(mapper.getDeserializationContext(), Long.parseLong(jsonString));
        assertEquals(42L, value.longValue());
    }

    @Test
    public void testDoubleInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);

        String jsonString = "42.0";
        Double value = instantiator.createFromDouble(mapper.getDeserializationContext(), Double.parseDouble(jsonString));
        assertEquals(42.0, value.doubleValue(), 0.0);
    }

    @Test
    public void testBooleanInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);

        String jsonStringTrue = "true";
        Boolean valueTrue = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertTrue(valueTrue);

        String jsonStringFalse = "false";
        Boolean valueFalse = instantiator.createFromBoolean(mapper.getDeserializationContext(), false);
        assertFalse(valueFalse);
    }

    @Test
    public void testDelegateInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyDelegateClass.class);

        Object jsonObject = mapper.readerFor(new TypeReference<Object>() {}.getType())
                .readValue(getClass().getResource("/testDelegate.json"));

        MyDelegateClass value = instantiator.createUsingDelegate(mapper.getDeserializationContext(), jsonObject);
        assertEquals("delegateValue", value.delegateValue);
    }

    @Test
    public void testArrayDelegateInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyArrayDelegateClass.class);

        Object jsonObject = mapper.readerFor(new TypeReference<Object[]>() {}.getType())
                .readValue(getClass().getResource("/testArrayDelegate.json"));

        MyArrayDelegateClass[] values = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), jsonObject);
        assertEquals(2, values.length);
        assertEquals("arrayDelegateValue1", values[0].arrayDelegateValue);
        assertEquals("arrayDelegateValue2", values[1].arrayDelegateValue);
    }
}

class MyDelegateClass {
    private String delegateValue;

    public MyDelegateClass(String delegateValue) {
        this.delegateValue = delegateValue;
    }

    public String getDelegateValue() {
        return delegateValue;
    }
}

class MyArrayDelegateClass {
    private String arrayDelegateValue;

    public MyArrayDelegateClass(String arrayDelegateValue) {
        this.arrayDelegateValue = arrayDelegateValue;
    }

    public String getArrayDelegateValue() {
        return arrayDelegateValue;
    }
}