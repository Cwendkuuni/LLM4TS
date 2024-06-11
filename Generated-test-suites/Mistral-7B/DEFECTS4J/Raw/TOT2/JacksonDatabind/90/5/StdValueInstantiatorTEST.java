package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.*;

/**
 * Test cases for {@link StdValueInstantiator} class.
 */
public class StdValueInstantiatorTest {

    private StdValueInstantiator instantiator;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        instantiator = mapper.getValueInstantiatorFinder().findValueInstantiator(mapper, String.class);
    }

    // Test default constructor
    @Test
    public void testCreateUsingDefault() throws IOException {
        Object value = instantiator.createUsingDefault(mapper.getDeserializationContext(), null);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test with-args constructor
    @Test
    public void testCreateFromObjectWith() throws IOException {
        Object[] args = new Object[] { new Object(), new Object(), new Object() };
        Object value = instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test delegate constructor
    @Test
    public void testCreateUsingDelegate() throws IOException {
        String delegateClass = "com.example.MyDelegateClass";
        Object delegate = mapper.readValue(new ObjectMapper().readTree("{\"delegateClass\":\"" + delegateClass + "\"}"), Object.class);
        Object value = instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);

        // Test failure case: no delegate constructor
        instantiator = mapper.getValueInstantiatorFinder().findValueInstantiator(mapper, Integer.class);
        try {
            instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
            fail("Expected InstantiationException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to instantiate value of type [simple type, class java.lang.Integer]: no String-argument constructor/factory method", e.getMessage());
        }
    }

    // Test array delegate constructor
    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        Object[] args = new Object[] { new Object(), new Object(), new Object() };
        String[] delegateArgs = new String[] {"arg1", "arg2", "arg3"};
        Object delegate = mapper.readValue(new ObjectMapper().readTree("{\"delegateClass\":\"com.example.MyDelegateClass\", \"delegateArguments\":[" + ObjectMapper.getDefaultInstance().writeValueAsString(delegateArgs) + "]}"), Object.class);
        Object value = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), delegate);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);

        // Test failure case: no array delegate constructor
        instantiator = mapper.getValueInstantiatorFinder().findValueInstantiator(mapper, int[].class);
        try {
            instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), delegate);
            fail("Expected InstantiationException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to instantiate value of type [simple type, class [I]: no suitable constructor/factory method found", e.getMessage());
        }
    }

    // Test string constructor
    @Test
    public void testCreateFromString() throws IOException {
        Object value = instantiator.createFromString(mapper.getDeserializationContext(), "");
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test int constructor
    @Test
    public void testCreateFromInt() throws IOException {
        Object value = instantiator.createFromInt(mapper.getDeserializationContext(), 0);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test long constructor
    @Test
    public void testCreateFromLong() throws IOException {
        Object value = instantiator.createFromLong(mapper.getDeserializationContext(), 0L);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test double constructor
    @Test
    public void testCreateFromDouble() throws IOException {
        Object value = instantiator.createFromDouble(mapper.getDeserializationContext(), 0.0);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test boolean constructor
    @Test
    public void testCreateFromBoolean() throws IOException {
        Object value = instantiator.createFromBoolean(mapper.getDeserializationContext(), false);
        assertNotNull(value);
        assertInstanceOf(String.class, value);
        assertEquals("", (String) value);
    }

    // Test failure cases: null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingDefaultWithNullContext() throws IOException {
        instantiator.createUsingDefault(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromObjectWithWithNullContext() throws IOException {
        Object[] args = new Object[] { new Object(), new Object(), null };
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingDelegateWithNullContext() throws IOException {
        Object delegate = mapper.readValue(new ObjectMapper().readTree("{\"delegateClass\":null}"), Object.class);
        instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUsingArrayDelegateWithNullContext() throws IOException {
        Object[] args = new Object[] { new Object(), null, new Object() };
        String[] delegateArgs = new String[] {"arg1", null, "arg3"};
        Object delegate = mapper.readValue(new ObjectMapper().readTree("{\"delegateClass\":\"com.example.MyDelegateClass\", \"delegateArguments\":[" + ObjectMapper.getDefaultInstance().writeValueAsString(delegateArgs) + "]}"), Object.class);
        instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), delegate);
    }

    // Test failure cases: unsupported types
    @Test(expected = JsonMappingException.class)
    public void testCreateFromObjectWithUnsupportedType() throws IOException {
        Object[] args = new Object[] { new Object(), new Object(), new Object(), new Object() };
        instantiator = mapper.getValueInstantiatorFinder().findValueInstantiator(mapper, Object[].class);
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingDelegateUnsupportedType() throws IOException {
        String delegateClass = "com.example.MyDelegateClass";
        Object delegate = mapper.readValue(new ObjectMapper().readTree("{\"delegateClass\":\"" + delegateClass + "\"}"), Object.class);
        instantiator = mapper.getValueInstantiatorFinder().findValueInstantiator(mapper, Object[].class);
        try {
            instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
            fail("Expected InstantiationException");
        } catch (JsonMappingException e) {
            assertEquals("No suitable constructor found for type [simple type, class java.lang.Object[]]: can not instantiate value of type [simple type, class java.lang.Object[]]", e.getMessage());
        }
    }

    // Test failure cases: missing injectables
    @Test(expected = JsonMappingException.class)
    public void testCreateFromObjectWithMissingInjectable() throws IOException {
        Object[] args = new Object[] { new Object(), null, new Object() };
        instantiator = mapper.getValueInstantiatorFinder().findValueInstantiator(mapper, String.class);
        try {
            instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
            fail("Expected InstantiationException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to instantiate value of type [simple type, class java.lang.String]: no String-argument constructor/factory method", e.getMessage());
        }
    }
}