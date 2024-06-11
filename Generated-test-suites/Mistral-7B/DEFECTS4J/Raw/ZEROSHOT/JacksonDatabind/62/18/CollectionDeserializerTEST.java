package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

/**
 * Test class for {@link CollectionDeserializer}.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    /**
     * Test constructor with no arguments.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testConstructorNoArguments() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, null);
        assertEquals(Collection.class, deserializer.getType());
        assertNull(deserializer.getValueDeserializer());
        assertNull(deserializer.getValueTypeDeserializer());
        assertNull(deserializer.getValueInstantiator());
        assertNull(deserializer.getDelegateDeserializer());
        assertNull(deserializer.getUnwrapSingle());
    }

    /**
     * Test constructor with arguments.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testConstructorWithArguments() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory()
                .constructParametricType(Collection.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = mapper.createDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory()
                .findValueDeserializer(Integer.class, null);
        ValueInstantiator valueInstantiator = mapper.getFactory()
                .findValueInstantiator(Integer.class);
        JsonDeserializer<Object> delegateDeserializer = mapper.createDeserializer(String.class);
        Boolean unwrapSingle = Boolean.TRUE;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType,
                valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        assertEquals(collectionType, deserializer.getType());
        assertEquals(valueDeserializer, deserializer.getValueDeserializer());
        assertEquals(valueTypeDeserializer, deserializer.getValueTypeDeserializer());
        assertEquals(valueInstantiator, deserializer.getValueInstantiator());
        assertEquals(delegateDeserializer, deserializer.getDelegateDeserializer());
        assertEquals(unwrapSingle, deserializer.getUnwrapSingle());
    }

    /**
     * Test isCachable method.
     */
    @Test
    public void testIsCachable() {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, null);
        assertFalse(deserializer.isCachable());
        JsonDeserializer<Object> valueDeserializer = mapper.createDeserializer(String.class);
        deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, valueDeserializer.getClass());
        assertFalse(deserializer.isCachable());
        deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, valueDeserializer.getClass(), null);
        assertFalse(deserializer.isCachable());
        deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, valueDeserializer.getClass(), null, null);
        assertTrue(deserializer.isCachable());
    }

    /**
     * Test createContextual method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory()
                .constructParametricType(Collection.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = mapper.createDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory()
                .findValueDeserializer(Integer.class, null);
        ValueInstantiator valueInstantiator = mapper.getFactory()
                .findValueInstantiator(Integer.class);
        JsonDeserializer<Object> delegateDeserializer = mapper.createDeserializer(String.class);
        Boolean unwrapSingle = Boolean.TRUE;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType,
                valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        BeanProperty property = new BeanProperty("list", collectionType);
        deserializer = deserializer.createContextual(mapper.getDeserializationContext(), property);
        assertEquals(deserializer.getValueDeserializer(), mapper.createDeserializer(Integer.class));
        assertEquals(deserializer.getValueTypeDeserializer(), valueTypeDeserializer);
        assertEquals(deserializer.getValueInstantiator(), valueInstantiator);
        assertEquals(deserializer.getDelegateDeserializer(), null);
        assertEquals(deserializer.getUnwrapSingle(), unwrapSingle);
    }

    /**
     * Test deserialize method with no arguments.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeNoArguments() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        CollectionDeserializer deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertTrue(result.isEmpty());
    }

    /**
     * Test deserialize method with arguments.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeWithArguments() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        JavaType collectionType = mapper.getTypeFactory()
                .constructParametricType(Collection.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = mapper.createDeserializer(Integer.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory()
                .findValueDeserializer(Integer.class, null);
        Collection<Object> result = new ArrayList<Object>();
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType,
                valueDeserializer, valueTypeDeserializer, null, null, null);
        deserializer.deserialize(parser, mapper.getDeserializationContext(), result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }

    /**
     * Test deserializeWithType method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        TypeDeserializer typeDeserializer = mapper.getFactory()
                .findValueDeserializer(List.class, null);
        List<Integer> result = typeDeserializer.deserializeTypedFromArray(parser, mapper.getDeserializationContext());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }

    /**
     * Test handleNonArray method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testHandleNonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1");
        CollectionDeserializer deserializer = mapper.getFactory()
                .findValueDeserializer(Collection.class, null);
        Collection<Object> result = new ArrayList<Object>();
        deserializer.handleNonArray(parser, mapper.getDeserializationContext(), result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }
}