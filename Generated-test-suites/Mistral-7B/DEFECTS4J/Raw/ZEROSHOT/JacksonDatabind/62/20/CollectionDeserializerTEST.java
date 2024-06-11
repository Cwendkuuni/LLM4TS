package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test class for CollectionDeserializer.
 */
public class CollectionDeserializerTest {

    @JsonDeserialize(using = CollectionDeserializer.class)
    static class TestCollection extends ArrayList<String> {
    }

    /**
     * Test constructor with no arguments.
     * @throws Exception
     */
    @Test
    public void testConstructorNoArgs() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertEquals(Collection.class, deserializer.getType());
        assertEquals(valueDeserializer, deserializer.getValueDeserializer());
        assertEquals(valueTypeDeserializer, deserializer.getValueTypeDeserializer());
        assertEquals(valueInstantiator, deserializer.getValueInstantiator());
        assertEquals(delegateDeserializer, deserializer.getDelegateDeserializer());
        assertEquals(unwrapSingle, deserializer.getUnwrapSingle());
    }

    /**
     * Test constructor with arguments.
     * @throws Exception
     */
    @Test
    public void testConstructorWithArgs() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = newStdTypeDeserializer(String[].class);
        ValueInstantiator valueInstantiator = newStdValueInstantiator(ArrayList.class);
        JsonDeserializer<Object> delegateDeserializer = newStdDeserializer(Integer.class);
        Boolean unwrapSingle = Boolean.TRUE;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertEquals(Collection.class, deserializer.getType());
        assertEquals(valueDeserializer, deserializer.getValueDeserializer());
        assertEquals(valueTypeDeserializer, deserializer.getValueTypeDeserializer());
        assertEquals(valueInstantiator, deserializer.getValueInstantiator());
        assertEquals(delegateDeserializer, deserializer.getDelegateDeserializer());
        assertEquals(unwrapSingle, deserializer.getUnwrapSingle());
    }

    /**
     * Test isCachable method.
     * @throws Exception
     */
    @Test
    public void testIsCachable() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertFalse(deserializer.isCachable());

        valueDeserializer = null;
        deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertFalse(deserializer.isCachable());

        valueDeserializer = newStdDeserializer(String.class);
        valueTypeDeserializer = newStdTypeDeserializer(String[].class);
        deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertFalse(deserializer.isCachable());

        valueDeserializer = newStdDeserializer(String.class);
        valueTypeDeserializer = null;
        deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertFalse(deserializer.isCachable());

        valueDeserializer = null;
        valueTypeDeserializer = newStdTypeDeserializer(String[].class);
        deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertFalse(deserializer.isCachable());

        valueDeserializer = null;
        valueTypeDeserializer = null;
        deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertTrue(deserializer.isCachable());
    }

    /**
     * Test createContextual method.
     * @throws Exception
     */
    @Test
    public void testCreateContextual() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = newStdValueInstantiator(ArrayList.class);
        JsonDeserializer<Object> delegateDeserializer = newStdDeserializer(Integer.class);
        Boolean unwrapSingle = null;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getId()).thenReturn("testProperty");
        when(property.getName()).thenReturn("testProperty");

        CollectionDeserializer result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(String[].class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(TestCollection.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(ArrayList.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(Map.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        when(property.getName()).thenReturn("testProperty2");
        valueDeserializer = newStdDeserializer(String.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(TestCollection.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(ArrayList.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(Map.class);
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);

        valueDeserializer = newStdDeserializer(Map.class);
        when(property.getType()).thenReturn(SimpleType.constructUnboundedType(Map.class));
        result = deserializer.createContextual(ctxt, property);
        assertNotNull(result);
        assertNotSame(deserializer, result);
        assertTrue(result instanceof MapDeserializer);
    }

    /**
     * Test deserialize method with no arguments.
     * @throws Exception
     */
    @Test
    public void testDeserializeNoArgs() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        JsonParser parser = createParser("[\"test1\", \"test2\", \"test3\"]");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("test1", result.toString().substring(0, 5));
        assertEquals("test2", result.toString().substring(6, 11));
        assertEquals("test3", result.toString().substring(12));
    }

    /**
     * Test deserialize method with arguments.
     * @throws Exception
     */
    @Test
    public void testDeserializeWithArgs() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = newStdTypeDeserializer(String[].class);
        ValueInstantiator valueInstantiator = newStdValueInstantiator(ArrayList.class);
        JsonDeserializer<Object> delegateDeserializer = newStdDeserializer(Integer.class);
        Boolean unwrapSingle = Boolean.TRUE;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        JsonParser parser = createParser("[1, 2, 3]");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, ((List<Integer>) result).get(0));
        assertEquals(2, ((List<Integer>) result).get(1));
        assertEquals(3, ((List<Integer>) result).get(2));
    }

    /**
     * Test deserializeWithType method.
     * @throws Exception
     */
    @Test
    public void testDeserializeWithType() throws Exception {
        JavaType collectionType = SimpleType.constructUnboundedType(Collection.class);
        JsonDeserializer<Object> valueDeserializer = newStdDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = newStdTypeDeserializer(String[].class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, null, null, null);
        JsonParser parser = createParser("[\"test1\", \"test2\", \"test3\"]");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(parser, ctxt)).thenReturn(new String[]{"test1", "test2", "test3"});
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("test1", result[0]);
        assertEquals("test2", result[1]);
        assertEquals("test3", result[2]);
    }
}