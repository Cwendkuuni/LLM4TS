package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.TestUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    private JavaType _fullType;
    private AtomicReferenceDeserializer _deserializer;
    private ObjectNode _testNode;

    @Before
    public void setUp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        _fullType = mapper.typeFactory().constructType(AtomicReference.class, Object.class);
        _deserializer = new AtomicReferenceDeserializer(_fullType, null, null, null);
        _testNode = mapper.createObjectNode();
    }

    @After
    public void tearDown() {
        _testNode = null;
        _deserializer = null;
    }

    @Test
    public void testGetNullValue() {
        AtomicReference<Object> result = _deserializer.getNullValue(null);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getClass(), AtomicReference.class);
        Assert.assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        Object emptyValue = _deserializer.getEmptyValue(null);
        Assert.assertEquals(emptyValue.getClass(), AtomicReference.class);
        Assert.assertNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> result = _deserializer.referenceValue(contents);
        Assert.assertEquals(result.getClass(), AtomicReference.class);
        Assert.assertEquals(result.get(), contents);
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = _deserializer.getReferenced(reference);
        Assert.assertEquals(result, contents);
    }

    @Test
    public void testUpdateReference() throws IOException {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        _testNode.put("value", "newValue");

        AtomicReference<Object> updatedReference = _deserializer.updateReference(reference, "newValue");
        Assert.assertEquals(updatedReference.getClass(), AtomicReference.class);
        Assert.assertEquals(updatedReference.get(), "newValue");

        Object newContents = new Object();
        _testNode.put("value", newContents);

        updatedReference = _deserializer.updateReference(reference, _testNode);
        Assert.assertEquals(updatedReference.getClass(), AtomicReference.class);
        Assert.assertEquals(updatedReference.get(), newContents);
    }

    @Test
    public void testSupportsUpdate() {
        Assert.assertTrue(_deserializer.supportsUpdate(null));
    }
}