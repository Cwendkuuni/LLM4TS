package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonArrayBase;
import com.fasterxml.jackson.core.base.JsonString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = new TypeReference<AtomicReference<Object>>() {}.getType();
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);

        Assert.assertNotNull(serializer);
        Assert.assertEquals(fullType, serializer.getTargetType());
        Assert.assertNull(serializer.getValueInstantiator());
        Assert.assertNull(serializer.getTypeDeserializer());
        Assert.assertNull(serializer.getDeserializer());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;
        AtomicReference<Object> nullValue = new AtomicReference<>();

        Assert.assertEquals(nullValue, AtomicReferenceDeserializer.getNullValue(ctxt));
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        AtomicReference<Object> emptyValue = new AtomicReference<>();

        Assert.assertEquals(emptyValue, AtomicReferenceDeserializer.getEmptyValue(ctxt));
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();

        AtomicReference<Object> result = AtomicReferenceDeserializer.referenceValue(contents);

        Assert.assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object referenced = reference.get();

        Assert.assertEquals(referenced, AtomicReferenceDeserializer.getReferenced(reference));
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object newValue = new Object();

        AtomicReference<Object> result = AtomicReferenceDeserializer.updateReference(reference, newValue);

        Assert.assertEquals(newValue, result.get());
        Assert.assertEquals(reference, result);
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        Assert.assertTrue(AtomicReferenceDeserializer.supportsUpdate(config));
    }

    @Test
    public void testWithResolved() {
        JavaType fullType = new TypeReference<AtomicReference<Object>>() {}.getType();
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        AtomicReferenceDeserializer original = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        TypeDeserializer typeDeser2 = null;
        JsonDeserializer<?> deser2 = null;

        AtomicReferenceDeserializer newSerializer = original.withResolved(typeDeser2, deser2);

        Assert.assertNotSame(original, newSerializer);
        Assert.assertEquals(fullType, newSerializer.getTargetType());
        Assert.assertNull(newSerializer.getValueInstantiator());
        Assert.assertNull(newSerializer.getTypeDeserializer());
        Assert.assertNull(newSerializer.getDeserializer());
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("value", new JsonString("initialValue"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        AtomicReference<Object> result = AtomicReferenceDeserializer.deserialize(parser, ctxt, new TypeReference<AtomicReference<Object>>() {}.getValueType());
        Assert.assertEquals("initialValue", result.get());

        Object newValue = new Object();
        parser.setValue(node);
        result = AtomicReferenceDeserializer.deserialize(parser, ctxt, new TypeReference<AtomicReference<Object>>() {}.getValueType());
        result.set(newValue);
        Assert.assertEquals(newValue, result.get());

        ObjectNode rootNode = factory.createObjectNode();
        rootNode.setAll(node);
        JsonNode node2 = rootNode.get("value");
        Assert.assertTrue(node2 instanceof JsonString);
        JsonString jsonString = (JsonString) node2;
        node2 = jsonString.asText();
        Assert.assertEquals("initialValue", node2);

        node2 = node.put("value", node2);
        node.put("newValue", new JsonString("newValue"));

        result = AtomicReferenceDeserializer.deserialize(parser, ctxt, new TypeReference<AtomicReference<Object>>() {}.getValueType());
        Assert.assertEquals("newValue", node2.asText());
        Assert.assertEquals(newValue, result.get());
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.getParser();

        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        JsonNode node = factory.createArrayNode();
        node.add(JsonFactory.valueWrapper(new JsonString("initialValue1")));
        node.add(JsonFactory.valueWrapper(new JsonString("initialValue2")));
        node.add(JsonFactory.valueWrapper(new JsonString("initialValue3")));

        AtomicReference<Object>[] results = new AtomicReference[3];
        for (int i = 0; i < 3; i++) {
            results[i] = AtomicReferenceDeserializer.deserialize(parser, ctxt, new TypeReference<AtomicReference<Object>>[]{}.length, new TypeReference<AtomicReference<Object>>[] { new TypeReference<AtomicReference<Object>>() {}.getType() });
        }

        Object initialValue1 = results[0].get();
        Object initialValue2 = results[1].get();
        Object initialValue3 = results[2].get();

        Assert.assertNotSame(initialValue1, initialValue2);
        Assert.assertNotSame(initialValue1, initialValue3);
        Assert.assertNotSame(initialValue2, initialValue3);

        node = factory.createArrayNode();
        node.add(JsonFactory.valueWrapper(new JsonString("newValue1")));
        node.add(JsonFactory.valueWrapper(new JsonString("newValue2")));
        node.add(JsonFactory.valueWrapper(new JsonString("newValue3")));

        for (int i = 0; i < 3; i++) {
            results[i].set(new Object());
        }

        for (int i = 0; i < 3; i++) {
            AtomicReference<Object> result = AtomicReferenceDeserializer.deserialize(parser, ctxt, new TypeReference<AtomicReference<Object>>[]{}.length, new TypeReference<AtomicReference<Object>>[] { new TypeReference<AtomicReference<Object>>() {}.getType() });
            Object newValue = results[i].get();
            Assert.assertEquals(newValue, result.get());
        }

        ObjectNode rootNode = factory.createObjectNode();
        JsonNode node2 = node.get(0);
        Assert.assertTrue(node2 instanceof JsonString);
        JsonString jsonString = (JsonString) node2;
        node2 = jsonString.asText();
        Assert.assertEquals("newValue1", node2);

        node2 = node.get(1);
        Assert.assertTrue(node2 instanceof JsonString);
        jsonString = (JsonString) node2;
        node2 = jsonString.asText();
        Assert.assertEquals("newValue2", node2);

        node2 = node.get(2);
        Assert.assertTrue(node2 instanceof JsonString);
        jsonString = (JsonString) node2;
        node2 = jsonString.asText();
        Assert.assertEquals("newValue3", node2);

        node = factory.createObjectNode();
        node.setAll(node);

        for (int i = 0; i < 3; i++) {
            results[i].set(new Object());
        }

        for (int i = 0; i < 3; i++) {
            JsonNode node3 = node.get("value" + i);
            Assert.assertTrue(node3 instanceof JsonArray);

            JsonNode node4 = ((JsonArray) node3).get(0);
            Assert.assertTrue(node4 instanceof JsonString);
            jsonString = (JsonString) node4;
            node4 = jsonString.asText();
            Assert.assertEquals("newValue" + i, node4);

            results[i].set(results[i].get());
        }

        for (int i = 0; i < 3; i++) {
            Object newValue = new Object();
            results[i].set(newValue);
        }

        for (int i = 0; i < 3; i++) {
            String name = "value" + i;
            ObjectNode expectedNode = factory.createObjectNode();
            expectedNode.put("value", JsonFactory.valueWrapper(new JsonString("newValue" + i)));
            JsonNode node5 = node.get(name);
            JsonNode node6 = AtomicReferenceDeserializer.deserialize(parser, ctxt, new TypeReference<AtomicReference<Object>>() {}.getValueType());
            Assert.assertEquals(expectedNode, node5);
            Assert.assertEquals(newValue, node6.get());
        }
    }
}