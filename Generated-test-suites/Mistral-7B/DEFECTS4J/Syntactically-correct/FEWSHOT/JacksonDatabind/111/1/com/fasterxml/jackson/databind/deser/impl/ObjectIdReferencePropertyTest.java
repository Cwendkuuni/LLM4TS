package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferenceProperty() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "bar";
            }
        };
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward", deserializer);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property.getForward());
        assertEquals(objectIdInfo, property.getObjectIdInfo());
    }

    @Test
    public void testObjectIdReferencePropertyWithValueDeserializer() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "bar";
            }
        };
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward", deserializer);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo, deserializer);
        assertEquals(forward, property.getForward());
        assertEquals(objectIdInfo, property.getObjectIdInfo());
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testObjectIdReferencePropertyWithNewName() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty newProperty = property.withName(PropertyName.construct("newName"));
        assertEquals(forward, newProperty.getForward());
        assertEquals(objectIdInfo, newProperty.getObjectIdInfo());
        assertEquals("newName", newProperty.getName());
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "bar";
            }
        };
        ObjectIdReferenceProperty newProperty = new ObjectIdReferenceProperty(property, deserializer);
        property.fixAccess(new DeserializationConfig());
        newProperty.fixAccess(new DeserializationConfig());
        assertEquals(property.getForward(), newProperty.getForward());
        assertEquals(property.getObjectIdInfo(), newProperty.getObjectIdInfo());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonId idAnnotation = AnnotationIntrospector.findAnnotation(forward.getMember(), JsonId.class);
        assertEquals(idAnnotation, property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward.getMember(), property.getMember());
    }

    @Test
    public void testGetCreatorIndex() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode instance = ObjectMapper.getDefaultInstance().readValue(new ObjectMapper().writeValueAsString(new ObjectNode()), ObjectNode.class);
        JsonParser p = new ObjectMapper().getFactory().createParser(new ObjectMapper().writeValueAsString(new ObjectNode()));
        property.deserializeAndSet(p, new DeserializationContext(), instance);
        assertEquals("bar", instance.get("forward").asText());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode instance = ObjectMapper.getDefaultInstance().readValue(new ObjectMapper().writeValueAsString(new ObjectNode()), ObjectNode.class);
        JsonParser p = new ObjectMapper().getFactory().createParser(new ObjectMapper().writeValueAsString(new ObjectNode()));
        assertEquals("bar", property.deserializeSetAndReturn(p, new DeserializationContext(), instance));
        assertEquals("bar", instance.get("forward").asText());
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode instance = ObjectMapper.getDefaultInstance().readValue(new ObjectMapper().writeValueAsString(new ObjectNode()), ObjectNode.class);
        property.set(instance, "bar");
        assertEquals("bar", instance.get("forward").asText());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode instance = ObjectMapper.getDefaultInstance().readValue(new ObjectMapper().writeValueAsString(new ObjectNode()), ObjectNode.class);
        assertEquals("bar", property.setAndReturn(instance, "bar"));
        assertEquals("bar", instance.get("forward").asText());
    }

    @Test
    public void testUnresolvedForwardReference() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(AnnotatedMember.Std.simplePojo("foo").getAnnotated());
        SettableBeanProperty forward = new SettableBeanProperty(ObjectNode.class, "forward");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode instance = ObjectMapper.getDefaultInstance().readValue(new ObjectMapper().writeValueAsString(new ObjectNode()), ObjectNode.class);
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        property.deserializeSetAndReturn(new JsonParser(new StringReader("")), new DeserializationContext(), instance);
        assertEquals(null, instance.get("forward"));
        reference.getRoid().appendReferring(property.new PropertyReferring(property, reference, ObjectNode.class, instance));
        property.set(instance, "bar");
        assertEquals("bar", instance.get("forward").asText());
    }
}