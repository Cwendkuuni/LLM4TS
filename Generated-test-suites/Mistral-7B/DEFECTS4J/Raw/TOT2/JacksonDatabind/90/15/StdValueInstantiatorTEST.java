package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    // Test for default constructor
    @Test
    public void testCreateUsingDefault() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        Object value = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNotNull(value);
        assertEquals("DEFAULT_INSTANCE", value.toString());
    }

    // Test for constructor with arguments
    @Test
    public void testCreateFromObjectWith() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        Object[] args = new Object[1];
        args[0] = "TEST";
        Object value = instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
        assertNotNull(value);
        assertEquals("TEST", value.toString());
    }

    // Test for delegate constructor
    @Test
    public void testCreateUsingDelegate() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromObjectSettings(null, new AnnotatedClass.StandardImpl(AnnotatedWithParams.class), null, null, null, null);
        Object delegate = mapper.readValue( "[{\"delegate\":\"DELEGATE\"}]", AnnotatedWithParams.class );
        Object value = instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        assertNotNull(value);
        assertEquals("DELEGATE", value.toString());
    }

    // Test for array delegate constructor
    @Test
    public void testCreateUsingArrayDelegate() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromArraySettings(null, new AnnotatedClass.StandardImpl(AnnotatedWithParams.class), null);
        Object[] args = new Object[1];
        args[0] = "DELEGATE";
        Object delegate = mapper.readValue( "[{\"delegate\":[\"DELEGATE\"]}]", AnnotatedWithParams[].class );
        Object value = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), delegate);
        assertNotNull(value);
        assertEquals("DELEGATE", value.toString());
    }

    // Test for string creator
    @Test
    public void testCreateFromString() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromStringCreator(new AnnotatedClass.StandardImpl(AnnotatedWithParams.class));
        Object value = instantiator.createFromString(mapper.getDeserializationContext(), "\"TEST\"");
        assertNotNull(value);
        assertEquals("TEST", value.toString());
    }

    // Test for int creator
    @Test
    public void testCreateFromInt() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromIntCreator(new AnnotatedClass.StandardImpl(AnnotatedWithParams.class));
        Object value = instantiator.createFromInt(mapper.getDeserializationContext(), 1);
        assertNotNull(value);
        assertEquals(1, value.hashCode());
        assertEquals("1", value.toString());
    }

    // Test for long creator
    @Test
    public void testCreateFromLong() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromLongCreator(new AnnotatedClass.StandardImpl(AnnotatedWithParams.class));
        Object value = instantiator.createFromLong(mapper.getDeserializationContext(), 1L);
        assertNotNull(value);
        assertEquals(1L, value.hashCode());
        assertEquals("1", value.toString());
    }

    // Test for double creator
    @Test
    public void testCreateFromDouble() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromDoubleCreator(new AnnotatedClass.StandardImpl(AnnotatedWithParams.class));
        Object value = instantiator.createFromDouble(mapper.getDeserializationContext(), 1.0);
        assertNotNull(value);
        assertEquals(1.0, value.hashCode(), 0.001);
        assertEquals("1.0", value.toString());
    }

    // Test for boolean creator
    @Test
    public void testCreateFromBoolean() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureFromBooleanCreator(new AnnotatedClass.StandardImpl(AnnotatedWithParams.class));
        Object value = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertNotNull(value);
        assertEquals(true, value.hashCode());
        assertEquals("true", value.toString());
    }

    // Test for incomplete creator
    @Test(expected = JsonMappingException.class)
    public void testCreateFromIncompleteParameter() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        instantiator.configureIncompleteParameter(new AnnotatedParameter(null, null, null));
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]);
    }

    // Test for null value
    @Test
    public void testCreateFromNullValue() throws JsonProcessingException, JsonMappingException {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), StdValueInstantiator.class);
        Object value = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNull(value);
    }
}