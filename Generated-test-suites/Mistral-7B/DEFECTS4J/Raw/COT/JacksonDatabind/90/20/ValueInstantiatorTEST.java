package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.ser.std.ToStringBehavior;

import org.junit.Before;
import org.junit.Test;

public class ValueInstantiatorTest {

    private ValueInstantiator valueInstantiator;
    private ObjectMapper objectMapper;

    @Before
    public void init() throws IOException {
        valueInstantiator = new ValueInstantiator.Base(String.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() {
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        Object created = valueInstantiator.createUsingDefault(objectMapper.deserializationContext());
        assertNull(created);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        String testString = "test";

        Object[] args = {testString};
        SettableBeanProperty[] props = new SettableBeanProperty[] {
                new SettableBeanPropertyBuilder().setName("testProperty")
                        .setSetter(String.class.getMethod("setValue", String.class))
                        .build()
        };
        PropertyValueBuffer buffer = new PropertyValueBuffer();

        Object created = valueInstantiator.createFromObjectWith(objectMapper.deserializationContext(), props, buffer);
        assertEquals(testString, created);
    }

    @Test(expected = IOException.class)
    public void testCreateFromObjectWith_MissingArguments() throws IOException {
        SettableBeanProperty[] props = new SettableBeanProperty[0];
        Object created = valueInstantiator.createFromObjectWith(objectMapper.deserializationContext(), props, null);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        Object created = valueInstantiator.createUsingDelegate(objectMapper.deserializationContext(), null);
        assertNull(created);
    }

    @Test(expected = IOException.class)
    public void testCreateUsingArrayDelegate() throws IOException {
        Object created = valueInstantiator.createUsingArrayDelegate(objectMapper.deserializationContext(), null);
    }

    @Test
    public void testCreateFromString() throws IOException {
        String testString = "test";
        Object created = valueInstantiator.createFromString(objectMapper.deserializationContext(), testString);
        assertEquals(testString, created);
    }

    @Test
    public void testCanCreateFromInt() {
        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCreateFromInt() throws IOException {
        int testInt = 1;
        Object created = valueInstantiator.createFromInt(objectMapper.deserializationContext(), testInt);
        assertNull(created);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        long testLong = 1;
        Object created = valueInstantiator.createFromLong(objectMapper.deserializationContext(), testLong);
        assertNull(created);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        double testDouble = 1.0;
        Object created = valueInstantiator.createFromDouble(objectMapper.deserializationContext(), testDouble);
        assertNull(created);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        boolean testBoolean = true;
        Object created = valueInstantiator.createFromBoolean(objectMapper.deserializationContext(), testBoolean);
        assertNull(created);
    }

    @Test
    public void testGetDefaultCreator() {
        assertNull(valueInstantiator.getDefaultCreator());
    }

    @Test
    public void testGetDelegateCreator() {
        assertNull(valueInstantiator.getDelegateCreator());
    }

    @Test
    public void testGetArrayDelegateCreator() {
        assertNull(valueInstantiator.getArrayDelegateCreator());
    }

    @Test
    public void testGetWithArgsCreator() {
        assertNull(valueInstantiator.getWithArgsCreator());
    }

    @Test
    public void testGetIncompleteParameter() {
        assertNull(valueInstantiator.getIncompleteParameter());
    }

    static class TestClass {
        private String testString;

        @JsonCreator
        public TestClass(@JsonProperty("testProperty") String testProperty) {
            this.testString = testProperty;
        }

        public void setTestString(String testString) {
            this.testString = testString;
        }

        @JsonProperty
        public String getTestString() {
            return testString;
        }

        @Override
        public String toString() {
            return ToStringBehavior.DEFAULT.toString(this);
        }
    }

    @Test
    public void testCreateFromObjectWith_CustomCreator() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(getClass().getResource("/test-object.json"), ObjectNode.class);
        Object created = valueInstantiator.createFromObjectWith(objectMapper.deserializationContext(), node.toString(), TestClass.class);
        assertSame(new TestClass("test"), created);
    }

    @Test(expected = IOException.class)
    public void testCreateFromObjectWith_InvalidJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(getClass().getResource("/test-object-invalid.json"), ObjectNode.class);
        valueInstantiator.createFromObjectWith(objectMapper.deserializationContext(), node, TestClass.class);
    }
}