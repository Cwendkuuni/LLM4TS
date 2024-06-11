package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferenceProperty() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setObjectIdGenerator(ObjectIdGenerators.IntObjectIdGenerator.instance());

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property.getBackRef());
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithDeserializer() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setObjectIdGenerator(ObjectIdGenerators.IntObjectIdGenerator.instance());

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        assertEquals(forward, property.getBackRef());
        assertEquals(objectIdInfo, property._objectIdInfo);
        assertEquals(deserializer, property._valueDeserializer);
    }

    @Test
    public void testObjectIdReferencePropertyWithName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setObjectIdGenerator(ObjectIdGenerators.IntObjectIdGenerator.instance());

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("oldName", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        ObjectIdReferenceProperty propertyWithName = property.withName(new PropertyName("newName"));

        assertEquals(propertyWithName.getName(), "newName");
        assertEquals(property, propertyWithName.getBackRef());
        assertEquals(objectIdInfo, propertyWithName._objectIdInfo);
        assertEquals(deserializer, propertyWithName._valueDeserializer);
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", "nameValue");

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        Object dummyInstance = new Object() {};

        property.fixAccess(new DeserializationConfig());
        property.deserializeAndSet(null, null, dummyInstance);
        assertEquals("nameValue", dummyInstance.getClass().getField("name").get(dummyInstance));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setObjectIdGenerator(ObjectIdGenerators.IntObjectIdGenerator.instance());

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        assertEquals(JsonIdentityInfo.class, property.getAnnotation(JsonIdentityInfo.class).annotationType());
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setObjectIdGenerator(ObjectIdGenerators.IntObjectIdGenerator.instance());

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        AnnotatedMember member = property.getMember();
        assertEquals("name", member.getName());
        assertEquals(Object.class, member.getMember().getType());
    }

    @Test
    public void testGetCreatorIndex() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", "nameValue");

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        Object dummyInstance = new Object() {};
        AnnotationIntrospector introspector = new AnnotationIntrospector();

        int index = property.getCreatorIndex();
        assertEquals(index, introspector.findCreatorIndex(dummyInstance.getClass(), "name"));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", 1);

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        Object dummyInstance = new Object() {};

        property.deserializeAndSet(null, new DeserializationContext(), dummyInstance);
        assertEquals(1, dummyInstance.getClass().getField("name").get(dummyInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", 1);

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        Object dummyInstance = new Object() {};

        Object value = property.deserializeSetAndReturn(null, new DeserializationContext(), dummyInstance);
        assertEquals(1, value.getClass().getField("name").get(value));
    }

    @Test
    public void testHandleResolvedForwardReference() throws Exception {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectNode objectNode1 = new ObjectMapper().createObjectNode();
        objectNode1.put("id", 1);
        ObjectNode objectNode2 = new ObjectMapper().createObjectNode();
        objectNode2.put("name", "nameValue");

        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty forward = new AnnotatedClassProperty.Std("name", Object.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, deserializer, null);

        Object dummyInstance1 = new Object() {};
        Object dummyInstance2 = new Object() {};

        PropertyReferring ref = new ObjectIdReferenceProperty.PropertyReferring(property, null, Object.class, dummyInstance1) {
            @Override
            public void handleResolvedForwardReference(Object id, Object value) throws IOException {
                assertEquals(1, id.hashCode());
                assertEquals(dummyInstance2, value);
                super.handleResolvedForwardReference(id, value);
            }
        };

        property.deserializeAndSet(null, new DeserializationContext(), dummyInstance1);
        property.set(dummyInstance1, dummyInstance2);
    }
}