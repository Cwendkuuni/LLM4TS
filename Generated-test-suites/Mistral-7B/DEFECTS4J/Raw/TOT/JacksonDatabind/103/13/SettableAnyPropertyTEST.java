package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private ObjectNode objectNode;
    private Map<Object, Object> map;
    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        objectNode = mapper.createObjectNode();
        map = new HashMap<>();
        settableAnyProperty = mapper.findSpecialValueDeserializer(SettableAnyProperty.class, Object.class);
    }

    @Test
    public void testDeserializeAndSet_ValueIsNull() throws IOException {
        Object pojo = new Object();
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
        Assert.assertNull(map.get("property"));
    }

    @Test
    public void testDeserializeAndSet_ValueIsString() throws IOException {
        Object pojo = new Object();
        Object value = "Test Value";
        objectNode.put("property", value);
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
        Assert.assertEquals(value, map.get("property"));
    }

    @Test
    public void testDeserializeAndSet_ValueIsNumber() throws IOException {
        Object pojo = new Object();
        Object value = 123;
        objectNode.put("property", value);
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
        Assert.assertEquals(value, map.get("property"));
    }

    @Test
    public void testDeserializeAndSet_ValueIsBoolean() throws IOException {
        Object pojo = new Object();
        Object value = true;
        objectNode.put("property", value);
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
        Assert.assertEquals(value, map.get("property"));
    }

    @Test
    public void testDeserializeAndSet_ValueIsList() throws IOException {
        Object pojo = new Object();
        ObjectNode listNode = mapper.createObjectNode();
        listNode.put("0", 1);
        listNode.put("1", 2);
        objectNode.put("property", listNode);
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
        Assert.assertEquals(listNode.toString(), map.get("property").toString());
    }

    @Test
    public void testDeserializeAndSet_ValueIsMap() throws IOException {
        Object pojo = new Object();
        ObjectNode mapNode = mapper.createObjectNode();
        mapNode.put("key1", 1);
        mapNode.put("key2", 2);
        objectNode.put("property", mapNode);
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
        Assert.assertEquals(mapNode.toString(), map.get("property").toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_InvalidType() throws IOException {
        Object pojo = new Object();
        objectNode.put("property", new Object());
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_MissingSetter() throws IOException {
        Object pojo = new Object();
        AnnotatedMember setter = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);
        settableAnyProperty.deserializeAndSet(objectNode.asParser(), null, pojo, "property");
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_IOException() throws IOException {
        Object pojo = new Object();
        settableAnyProperty.deserializeAndSet(null, null, pojo, "property");
    }
}