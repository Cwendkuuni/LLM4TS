package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ValueInstantiatorTest {

    private static final String VALUE_TYPE = "com.example.MyValue";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private ValueInstantiator instantiator;

    @Before
    public void setup() throws Exception {
        instantiator = new ValueInstantiator.Base(Class.forName(VALUE_TYPE));
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals(VALUE_TYPE, instantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        assertEquals(Class.forName(VALUE_TYPE), instantiator.getValueClass());
    }

    @Test
    public void testCanInstantiate_False() {
        assertFalse(instantiator.canInstantiate());
    }

    @Test
    public void testCanCreateUsingDefault_False() throws Exception {
        assertFalse(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext().withFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object created = instantiator.createUsingDefault(ctxt);
        assertNull(created);
    }

    @Test
    public void testCanCreateUsingDelegate_False() throws Exception {
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext().withFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object delegate = new Object();
        Object created = instantiator.createUsingDelegate(ctxt, delegate);
        assertNull(created);
    }

    @Test
    public void testCanCreateFromObjectWith_False() throws Exception {
        assertFalse(instantiator.canCreateFromObjectWith());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        SettableBeanProperty[] props = new SettableBeanProperty[]{new SettableBeanProperty("prop", MAPPER.getTypeFactory().constructType(String.class))};
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJO("prop", "test");
        Object created = instantiator.createFromObjectWith(ctxt, props, new PropertyValueBuffer());
        assertNull(created);
    }

    @Test
    public void testCanCreateFromObjectWith_Parameters() throws Exception {
        Class<?> parameterType = String.class;
        Method createFromObjectWithMethod = instantiator.getClass().getMethod("createFromObjectWith", DeserializationContext.class, SettableBeanProperty[].class, PropertyValueBuffer.class);
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJO("prop", "test");
        SettableBeanProperty[] props = new SettableBeanProperty[]{new SettableBeanProperty("prop", MAPPER.getTypeFactory().constructType(parameterType))};
        Object created = (Object) createFromObjectWithMethod.invoke(instantiator, new Object[]{ctxt, props, new PropertyValueBuffer()}, new Object[0]);
        assertNull(created);
    }

    @Test
    public void testCanCreateFromString_False() throws Exception {
        assertFalse(instantiator.canCreateFromString());
    }

    @Test
    public void testCreateFromString() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String value = "test";
        Object created = instantiator.createFromString(ctxt, value);
        assertNull(created);
    }

    @Test
    public void testCanCreateFromInt_False() throws Exception {
        assertFalse(instantiator.canCreateFromInt());
    }

    @Test
    public void testCreateFromInt() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        int value = 1;
        Object created = instantiator.createFromInt(ctxt, value);
        assertNull(created);
    }

    @Test
    public void testCanCreateFromLong_False() throws Exception {
        assertFalse(instantiator.canCreateFromLong());
    }

    @Test
    public void testCreateFromLong() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        long value = 1;
        Object created = instantiator.createFromLong(ctxt, value);
        assertNull(created);
    }

    @Test
    public void testCanCreateFromDouble_False() throws Exception {
        assertFalse(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        double value = 1.0;
        Object created = instantiator.createFromDouble(ctxt, value);
        assertNull(created);
    }

    @Test
    public void testCanCreateFromBoolean_False() throws Exception {
        assertFalse(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        boolean value = true;
        Object created = instantiator.createFromBoolean(ctxt, value);
        assertNull(created);
    }

    @Test
    public void testGetDelegateType_Null() throws Exception {
        assertNull(instantiator.getDelegateType(MAPPER.getDeserializationContext()));
    }

    @Test
    public void testGetArrayDelegateType_Null() throws Exception {
        assertNull(instantiator.getArrayDelegateType(MAPPER.getDeserializationContext()));
    }

    @Test
    public void testCreateUsingDefault_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext().withFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory factory = MAPPER.getFactory();
        JsonParser parser = factory.createParser("{}");
        parser.nextToken();
        try {
            instantiator.createUsingDefault(ctxt);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no default no-arguments constructor found", e.getMessage());
        }
    }

    @Test
    public void testCreateFromObjectWith_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        SettableBeanProperty[] props = new SettableBeanProperty[]{new SettableBeanProperty("prop", MAPPER.getTypeFactory().constructType(String.class))};
        ObjectNode node = MAPPER.createObjectNode();
        Object created = null;
        try {
            created = instantiator.createFromObjectWith(ctxt, props, new PropertyValueBuffer());
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no creator with arguments specified", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateUsingDelegate_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object delegate = new Object();
        Object created = null;
        try {
            created = instantiator.createUsingDelegate(ctxt, delegate);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no delegate creator specified", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateUsingArrayDelegate_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object delegate = new Object[0];
        Object created = null;
        try {
            created = instantiator.createUsingArrayDelegate(ctxt, delegate);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no array delegate creator specified", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateFromString_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String value = "test";
        Object created = null;
        try {
            created = instantiator.createFromString(ctxt, value);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no String-argument constructor/factory method to deserialize from String value ('test')", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateFromInt_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        int value = 1;
        Object created = null;
        try {
            created = instantiator.createFromInt(ctxt, value);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no int/Int-argument constructor/factory method to deserialize from Number value (1)", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateFromLong_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        long value = 1;
        Object created = null;
        try {
            created = instantiator.createFromLong(ctxt, value);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no long/Long-argument constructor/factory method to deserialize from Number value (1)", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateFromDouble_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        double value = 1.0;
        Object created = null;
        try {
            created = instantiator.createFromDouble(ctxt, value);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no double/Double-argument constructor/factory method to deserialize from Number value (1.0)", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateFromBoolean_Exception() throws Exception {
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        boolean value = true;
        Object created = null;
        try {
            created = instantiator.createFromBoolean(ctxt, value);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("no boolean/Boolean-argument constructor/factory method to deserialize from boolean value (true)", e.getMessage());
        }
        assertNull(created);
    }

    @Test
    public void testCreateFromObjectWith_Valid() throws Exception {
        JsonFactory factory = MAPPER.getFactory();
        JsonParser parser = factory.createParser("{\"prop\":\"test\"}");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        SettableBeanProperty[] props = new SettableBeanProperty[]{new SettableBeanProperty("prop", MAPPER.getTypeFactory().constructType(String.class))};
        ObjectNode node = MAPPER.readValue(parser, ObjectNode.class);
        Object created = instantiator.createFromObjectWith(ctxt, props, new PropertyValueBuffer());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(ValueInstantiator.class.getDeclaredField("delegateCreator").get(instantiator), created);
    }

    @Test
    public void testCreateUsingDelegate_Valid() throws Exception {
        JsonFactory factory = MAPPER.getFactory();
        JsonParser parser = factory.createParser("{}");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object creator = new Object();
        Object created = instantiator.createUsingDelegate(ctxt, creator);
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(StdValueInstantiator.class.getField("creator").get(creator), created);
    }

    @Test
    public void testCreateUsingArrayDelegate_Valid() throws Exception {
        JsonFactory factory = MAPPER.getFactory();
        JsonParser parser = factory.createParser("{\"arr\":[1,2,3]}");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object creator = new ArrayList[0];
        Class<?> arrayType = Class.forName("java.util.ArrayList");
        JavaType javaType = MAPPER.getTypeFactory().constructType(arrayType.getComponentType());
        Object created = instantiator.createUsingArrayDelegate(ctxt, creator);
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(javaType, mapper.getTypeFactory().constructType(javaType.getRawClass().getName() + "<" + arrayType.getName() + ">"));
    }

    @Test
    public void testGetDefaultCreator_Null() throws Exception {
        assertNull(instantiator.getDefaultCreator());
    }

    @Test
    public void testGetDelegateCreator_Null() throws Exception {
        assertNull(instantiator.getDelegateCreator());
    }

    @Test
    public void testGetArrayDelegateCreator_Null() throws Exception {
        assertNull(instantiator.getArrayDelegateCreator());
    }

    @Test
    public void testGetWithArgsCreator_Null() throws Exception {
        assertNull(instantiator.getWithArgsCreator());
    }

    @Test
    public void testGetIncompleteParameter_Null() throws Exception {
        assertNull(instantiator.getIncompleteParameter());
    }
}