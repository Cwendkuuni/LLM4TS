package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSettableAnyProperty() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);

        BeanProperty property = new BeanPropertyBuilder()
                .withName("testProperty")
                .withType(javaType)
                .build();

        AnnotatedMember setter = new AnnotatedField(SettableAnyPropertyTest.class.getDeclaredField("testMap"));
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\": {\"key1\": 1, \"key2\": \"two\"}}";
        Map<String, Object> testMap = new HashMap<>();
        Object testInstance = new Object();

        settableAnyProperty.deserializeAndSet(createParser(json), new DeserializationContext(mapper, createContextElements()), testInstance, "testProperty");

        assertEquals(2, testMap.size());
        assertEquals(1, testMap.get("key1"));
        assertEquals("two", testMap.get("key2"));

        // Basic test case for getProperty()
        assertEquals(property, settableAnyProperty.getProperty());

        // Basic test case for hasValueDeserializer()
        assertFalse(settableAnyProperty.hasValueDeserializer());

        // Basic test case for getType()
        assertEquals(javaType, settableAnyProperty.getType());

        // Test for deserializeAndSet() method with null value
        json = "{\"testProperty\": null}";
        testMap = new HashMap<>();
        testInstance = new Object();
        settableAnyProperty.deserializeAndSet(createParser(json), new DeserializationContext(mapper, createContextElements()), testInstance, "testProperty");
        assertTrue(testMap.isEmpty());

        // Test for deserializeAndSet() method with non-null value
        json = "{\"testProperty\": {\"key1\": 1, \"key2\": \"two\"}}";
        testMap = new HashMap<>();
        testInstance = new Object();
        settableAnyProperty.deserializeAndSet(createParser(json), new DeserializationContext(mapper, createContextElements()), testInstance, "testProperty");
        assertEquals(2, testMap.size());

        // Test for deserialize() method with null value
        JsonParser parser = createParser("null");
        Object deserializedValue = settableAnyProperty.deserialize(parser, new DeserializationContext(mapper, createContextElements()));
        assertNull(deserializedValue);

        // Test for deserialize() method with non-null value
        parser = createParser("1");
        deserializedValue = settableAnyProperty.deserialize(parser, new DeserializationContext(mapper, createContextElements()));
        assertEquals(1, deserializedValue);

        // Test for deserialize() method with annotated type
        parser = createParser("\"two\"");
        deserializedValue = settableAnyProperty.deserialize(parser, new DeserializationContext(mapper, createContextElements()));
        assertEquals("two", deserializedValue);

        // Test for set() method with null value
        testMap = new HashMap<>();
        testInstance = new Object();
        settableAnyProperty.set(testInstance, "key1", null);
        assertTrue(testMap.isEmpty());

        // Test for set() method with non-null value
        testMap = new HashMap<>();
        testInstance = new Object();
        settableAnyProperty.set(testInstance, "key1", new Integer(1));
        assertEquals(1, testMap.get("key1"));

        // Test for set() method with annotated fields
        testMap = new HashMap<>();
        testInstance = new TestMap();
        settableAnyProperty.set(testInstance, "testMap", new Integer(1));
        assertEquals(1, testMap.get("testMap").get(0));

        // Test for set() method with annotated methods
        testMap = new HashMap<>();
        testInstance = new TestClass();
        settableAnyProperty.set(testInstance, "setTestInt", new Integer(1));
        assertEquals(1, testInstance.getTestInt());

        // Test for fixAccess() method
        settableAnyProperty.fixAccess(new DeserializationConfig(mapper, createContextElements()));
        parser = createParser("{\"testProperty\": 1}");
        deserializedValue = settableAnyProperty.deserialize(parser, new DeserializationContext(mapper, createContextElements()));
        assertEquals(1, deserializedValue);

        // Test for readResolve() method
        SettableAnyProperty deserializedSettableAnyProperty = (SettableAnyProperty) mapper.readValue(json, SettableAnyProperty.class);
        assertEquals(property, deserializedSettableAnyProperty.getProperty());
        assertEquals(_property, deserializedSettableAnyProperty._property);

        // Test for toString() method
        assertEquals("[any property on class com.fasterxml.jackson.databind.deser.test.SettableAnyPropertyTest]", settableAnyProperty.toString());

        // Test for AnySetterReferring class
        Object roid = new Object();
        UnresolvedForwardReference reference = new UnresolvedForwardReference(roid, "testKey");
        AnySetterReferring referring = new AnySetterReferring(settableAnyProperty, reference, Object.class, testInstance, "testProperty");
        referring.handleResolvedForwardReference("testKey", new Integer(1));
        assertEquals(1, testMap.get("testProperty"));
    }
}