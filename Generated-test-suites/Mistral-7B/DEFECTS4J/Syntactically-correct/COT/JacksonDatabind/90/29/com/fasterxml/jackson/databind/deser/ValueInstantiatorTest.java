package com.example.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        valueInstantiator = new ValueInstantiator.Base(String.class);
    }

    @Test
    public void testGetValueTypeDesc() {
        String expected = "java.lang.String";
        String result = valueInstantiator.getValueTypeDesc();
        assertEquals(expected, result);
    }

    @Test
    public void testGetValueClass() {
        Class<?> expected = String.class;
        Class<?> result = valueInstantiator.getValueClass();
        assertEquals(expected, result);
    }

    @Test
    public void testCanInstantiate_false() {
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateFromString_false() {
        assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt_false() {
        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong_false() {
        assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble_false() {
        assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean_false() {
        assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault_false() {
        assertFalse(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate_false() {
        assertFalse(valueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith_false() {
        assertFalse(valueInstantiator.canCreateFromObjectWith());
    }

    @Test(expected = AssertionError.class)
    public void testCreateUsingDefault() throws IOException {
        Object result = valueInstantiator.createUsingDefault(null);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromObjectWith_emptyArguments() throws IOException {
        Object[] arguments = new Object[0];
        Object result = valueInstantiator.createFromObjectWith(null, arguments);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromObjectWith_wrongType() throws IOException {
        Object[] arguments = new Object[]{new Integer(1)};
        Object result = valueInstantiator.createFromObjectWith(null, arguments);
        assertNull(result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        String input = "Hello World";
        Object result = valueInstantiator.createFromString(objectMapper.getDeserializationContext(), input);
        assertEquals(input, result.toString());
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromInt() throws IOException {
        int input = 1;
        Object result = valueInstantiator.createFromInt(objectMapper.getDeserializationContext(), input);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromLong() throws IOException {
        long input = 1L;
        Object result = valueInstantiator.createFromLong(objectMapper.getDeserializationContext(), input);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromDouble() throws IOException {
        double input = 1.0;
        Object result = valueInstantiator.createFromDouble(objectMapper.getDeserializationContext(), input);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromBoolean() throws IOException {
        boolean input = true;
        Object result = valueInstantiator.createFromBoolean(objectMapper.getDeserializationContext(), input);
        assertNull(result);
    }

}