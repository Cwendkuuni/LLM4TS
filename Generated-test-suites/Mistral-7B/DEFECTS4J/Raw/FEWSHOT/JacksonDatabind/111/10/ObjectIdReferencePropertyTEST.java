package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstruction() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(property);
        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyCopy() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(property, null);
        assertNotNull(copy);
        assertEquals(property, copy);
        assertEquals(property._forward, copy._forward);
        assertEquals(property._objectIdInfo, copy._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyRename() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectIdReferenceProperty renamed = new ObjectIdReferenceProperty(property, new PropertyName("newName"));
        assertNotNull(renamed);
        assertEquals(property, renamed);
        assertEquals(property._forward, renamed._forward);
        assertEquals(property._objectIdInfo, renamed._objectIdInfo);
        assertEquals("newName", renamed.getName());
    }

    @Test
    public void testObjectIdReferencePropertyValueDeserializer() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        ObjectIdReferenceProperty propertyWithDeserializer = new ObjectIdReferenceProperty(property, deserializer, null);
        assertEquals(property, propertyWithDeserializer);
        assertEquals(property._forward, propertyWithDeserializer._forward);
        assertEquals(property._objectIdInfo, propertyWithDeserializer._objectIdInfo);
        assertEquals(deserializer, propertyWithDeserializer._valueDeserializer);
    }

    @Test
    public void testObjectIdReferencePropertyNullValueProvider() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        NullValueProvider nva = new StandardNullValueProvider();
        ObjectIdReferenceProperty propertyWithNullProvider = new ObjectIdReferenceProperty(property, null, nva);
        assertEquals(property, propertyWithNullProvider);
        assertEquals(property._forward, propertyWithNullProvider._forward);
        assertEquals(property._objectIdInfo, propertyWithNullProvider._objectIdInfo);
        assertEquals(nva, propertyWithNullProvider._nullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        DeserializationConfig config = new DeserializationConfig();
        property.fixAccess(config);
        assertNotNull(config);
        assertNotNull(config.getConfigValue(DeserializationFeature.FAIL_ON_EMPTY_BEANS));
        assertNull(config.getConfigValue(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES));
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Assert.assertNotNull(property.getAnnotation(Annotation.class));
        Assert.assertEquals(objectIdInfo, property.getAnnotation(ObjectIdInfo.class));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        AnnotatedMember member = property.getMember();
        assertNotNull(member);
        assertEquals(forward.getMember(), member);
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        int creatorIndex = property.getCreatorIndex();
        assertEquals(-1, creatorIndex);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = mapper.readValue(ObjectNode.class.getDeclaredField("fields"), Object.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "1");
        Object newPojo = mapper.readValue(node, Object.class);
        property.deserializeAndSet(mapper.getFactory().getParserForObject(node), null, pojo);
        assertEquals(newPojo, pojo);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = mapper.readValue(ObjectNode.class.getDeclaredField("fields"), Object.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "1");
        Object newPojo = mapper.readValue(node, Object.class);
        Object result = property.deserializeSetAndReturn(mapper.getFactory().getParserForObject(node), null, pojo);
        assertEquals(newPojo, result);
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = mapper.readValue(ObjectNode.class.getDeclaredField("fields"), Object.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "1");
        property.set(pojo, mapper.readValue(node, Object.class));
        assertEquals(mapper.readValue(node, Object.class), pojo.getClass().getDeclaredField("id").get(pojo));
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = mapper.readValue(ObjectNode.class.getDeclaredField("fields"), Object.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "1");
        Object newPojo = mapper.readValue(node, Object.class);
        Object result = property.setAndReturn(pojo, newPojo);
        assertEquals(newPojo, result);
        assertEquals(newPojo, pojo.getClass().getDeclaredField("id").get(pojo));
    }

    @Test
    public void testUnresolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Object.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedField("id", Object.class), null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = new Object();
        JsonParser parser = new MockParser();
        UnresolvedForwardReference reference = new UnresolvedForwardReference(parser, Object.class, null, null);
        PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, reference, Object.class, pojo);
        property.deserializeSetAndReturn(parser, null, pojo);
        assertEquals(pojo, parser.getValueAsToken().getValueAsString());
    }
}