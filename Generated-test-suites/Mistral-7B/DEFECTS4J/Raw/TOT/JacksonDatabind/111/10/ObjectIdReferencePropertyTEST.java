package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReferenceProperty property;
 private ObjectIdReferencePojo pojo;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 AnnotatedClass ac = mapper.getTypeFactory().constructType(ObjectIdReferencePojo.class);
 ObjectReader reader = mapper.readerFor(ac);
 property = reader.findProperty("objectIdReferenceProperty");
 pojo = mapper.readValue(new ObjectMapper().createParser("{}"), ObjectIdReferencePojo.class);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonNode node = mapper.readTree("{\"objectIdReferenceProperty\": \"testValue\"}");
 ObjectNode objNode = (ObjectNode) node;
 Object newValue = "newValue";
 pojo = mapper.readerFor(ObjectIdReferencePojo.class).readValue(objNode);
 property.setValue(pojo, newValue);
 assertEquals(newValue, pojo.getObjectIdReferenceProperty());
 }

 @Test
 public void testDeserializeAndReturnNull() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"objectIdReferenceProperty\": null}");
 assertNull(property.deserializeSetAndReturn(p, null, pojo));
 }

 @Test
 public void testDeserializeAndReturnUnresolvedForwardReference() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"objectIdReferenceProperty\": {}}");
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 property.deserializeSetAndReturn(p, null, pojo, reference);
 assertEquals(pojo.getClass(), reference.getReferring().getType());
 assertEquals(pojo, reference.getReferring().getInstance());
 }

 @Test
 public void testDeserializeAndReturnObjectWithIdentityInfo() throws Exception {
 JsonNode node = mapper.readTree("{\"objectIdReferenceProperty\": null}");
 ObjectNode objNode = (ObjectNode) node;
 JsonDeserialize annotation = AnnotationIntrospector.findAnnotation(ObjectIdReferencePojo.class, ObjectIdReferenceProperty.class);
 ObjectIdInfo objectIdInfo = annotation.getValue();
 pojo = mapper.readerFor(ObjectIdReferencePojo.class).readValue(objNode);
 Object newValue = new Object();
 pojo = mapper.readerFor(ObjectIdReferencePojo.class).readValue(objNode);
 property.setValue(pojo, newValue);
 ObjectIdReferencePropertyTest.PropertyReferring referencing = (ObjectIdReferencePropertyTest.PropertyReferring) property.getMember().getAnnotated();
 referencing.handleResolvedForwardReference(pojo.getId(), newValue);
 assertEquals(newValue, pojo.getObjectIdReferenceProperty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleResolvedForwardReferenceWithUnresolvedReference() throws Exception {
 ObjectIdReferencePropertyTest.PropertyReferring referencing = new ObjectIdReferencePropertyTest.PropertyReferring(property, null, Object.class, pojo);
 referencing.handleResolvedForwardReference(null, null);
 }

}

class ObjectIdReferencePojo {
 private String objectIdReferenceProperty;
 private Long id;

 public String getObjectIdReferenceProperty() {
 return objectIdReferenceProperty;
 }

 public void setObjectIdReferenceProperty(String objectIdReferenceProperty) {
 this.objectIdReferenceProperty = objectIdReferenceProperty;
 }

 public Long getId() {
 return id;
 }

 public void setId(Long id) {
 this.id = id;
 }
}

class ObjectIdReferencePropertyTest {
 static class PropertyReferring extends Referring {
 private final ObjectIdReferenceProperty _parent;

 public PropertyReferring(ObjectIdReferenceProperty parent) {
 super(parent);
 _parent = parent;
 }

 @Override
 public void handleResolvedForwardReference(Object id, Object value) throws IOException {
 _parent.setId((Long) id);
 }
 }
}