package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdReferenceProperty_SimpleDeserialization() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SimpleProperty("forward"), null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        ObjectIdReferenceProperty testProperty = new ObjectIdReferenceProperty(property, null, objectIdInfo);

        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.put("id", 1L);

        Object result = MAPPER.readValue(input, testProperty.getType());

        assertEquals(1L, result);
        assertSame(property, testProperty.getBackRef());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdReferenceProperty_NullValueDeserialization() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SimpleProperty("forward"), null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        ObjectIdReferenceProperty testProperty = new ObjectIdReferenceProperty(property, new NullValueDeserializer(), objectIdInfo);

        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.putNull("id");

        Object result = MAPPER.readValue(input, testProperty.getType());

        assertNull(result);
        assertSame(property, testProperty.getBackRef());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdReferenceProperty_ValueDeserialization() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SimpleProperty("forward"), null, null);
        JsonDeserializer<String> valueDeserializer = new StandardDeserializer<>(String.class);
        ObjectIdReferenceProperty testProperty = new ObjectIdReferenceProperty(property, valueDeserializer, null);

        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.put("id", 1L);
        input.put("name", "Test");

        Object result = MAPPER.readValue(input, testProperty.getType());

        assertEquals("Test", result);
        assertSame(property, testProperty.getBackRef());
        assertTrue(valueDeserializer.isCachedInstance(result));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdReferenceProperty_ForwardReference_SameInstance() throws IOException {
        ObjectIdReferenceProperty property1 = new ObjectIdReferenceProperty(new SimpleProperty("forward1"), null, null);
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(Object.class, "id1");
        ObjectIdReferenceProperty testProperty1 = new ObjectIdReferenceProperty(property1, null, objectIdInfo1);

        ObjectIdReferenceProperty property2 = new ObjectIdReferenceProperty(new SimpleProperty("forward2"), null, null);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(Object.class, "id2");
        ObjectIdReferenceProperty testProperty2 = new ObjectIdReferenceProperty(property2, null, objectIdInfo2);

        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.put("id1", testProperty1);
        input.put("id2", testProperty2);

        Object result = MAPPER.readValue(input, Object[].class)[0];

        ObjectIdReferenceProperty ref1 = (ObjectIdReferenceProperty) result;
        ObjectIdReferenceProperty ref2 = (ObjectIdReferenceProperty) result;

        assertSame(property1, ref1.getBackRef());
        assertSame(property2, ref2.getBackRef());
        assertSame(ref1, ref2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdReferenceProperty_ForwardReference_DifferentInstances() throws IOException {
        ObjectIdReferenceProperty property1 = new ObjectIdReferenceProperty(new SimpleProperty("forward1"), null, null);
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(Object.class, "id1");
        ObjectIdReferenceProperty testProperty1 = new ObjectIdReferenceProperty(property1, null, objectIdInfo1);

        ObjectIdReferenceProperty property2 = new ObjectIdReferenceProperty(new SimpleProperty("forward2"), null, null);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(Object.class, "id2");
        ObjectIdReferenceProperty testProperty2 = new ObjectIdReferenceProperty(property2, null, objectIdInfo2);

        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.put("id1", testProperty1);
        input.put("id2", testProperty2);

        Object[] results = MAPPER.readValue(input, Object[].class);

        ObjectIdReferenceProperty ref1 = (ObjectIdReferenceProperty) results[0];
        ObjectIdReferenceProperty ref2 = (ObjectIdReferenceProperty) results[1];

        assertSame(property1, ref1.getBackRef());
        assertSame(property2, ref2.getBackRef());
        assertNotSame(ref1, ref2);
    }

    @Test
    public void testObjectIdReferenceProperty_NullValueIdResolution() throws IOException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(new SimpleProperty("forward"), null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        ObjectIdReferenceProperty testProperty = new ObjectIdReferenceProperty(property, null, objectIdInfo);

        // No id provided for forward reference
        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.put("id", testProperty);

        try {
            Object result = MAPPER.readValue(input, Object.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unresolved forward reference but no identity info present: [class com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty], path: [toObject]", e.getMessage());
        }
    }

    @Test
    public void testObjectIdReferenceProperty_CircularReference() throws IOException {
        ObjectIdReferenceProperty property1 = new ObjectIdReferenceProperty(new SimpleProperty("forward1"), null, null);
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(Object.class, "id1");
        ObjectIdReferenceProperty testProperty1 = new ObjectIdReferenceProperty(property1, null, objectIdInfo1);

        ObjectIdReferenceProperty property2 = new ObjectIdReferenceProperty(new SimpleProperty("forward2"), null, null);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(Object.class, "id2");
        ObjectIdReferenceProperty testProperty2 = new ObjectIdReferenceProperty(property2, null, objectIdInfo2);

        testProperty1.setBackRef(testProperty2);
        testProperty2.setBackRef(testProperty1);

        JsonNode input = new ObjectNode(JsonProcessor.getStandardParser());
        input.put("id1", testProperty1);
        input.put("id2", testProperty2);

        try {
            Object result = MAPPER.readValue(input, Object[].class)[0];
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference reference) {
            // Expected
        }
    }
}