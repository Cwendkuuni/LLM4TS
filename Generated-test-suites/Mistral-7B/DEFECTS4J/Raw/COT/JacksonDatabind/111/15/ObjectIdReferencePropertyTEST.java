package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forward;
 private ObjectIdReferenceProperty property;
 private JsonDeserializer<?> valueDeserializer;
 private Object pojo;
 private Class<?> type;
 private Object id;
 private Object value;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private NullValueProvider nullValueProvider;

 @ org.junit.Before
 public void setUp() throws Exception {
 objectIdInfo = new ObjectIdInfo(SimpleBeanPropertyFilter.FilterFlag.NONE, "test");
 forward = new SettableBeanProperty(null, null, null);
 valueDeserializer = new StdDeserializer<>();
 type = Object.class;
 pojo = new Object();
 jsonParser = new Mapper().getFactory().createParser("{}");
 deserializationContext = new DeserializationContext(MapperFactory.DEFAULT_INSTANCE, jsonParser);
 nullValueProvider = new StandardNullValueProvider();
 property = new ObjectIdReferenceProperty(forward, objectIdInfo);
 }

 @ org.junit.Test
 public void testConstructorWithForwardAndObjectIdInfo() {
 // Basic functionality test
 ObjectIdReferenceProperty result = new ObjectIdReferenceProperty(forward, objectIdInfo);
 assertEquals(forward, result._forward);
 assertEquals(objectIdInfo, result._objectIdInfo);
 }

 @ org.junit.Test
 public void testConstructorWithSrcDeserializerAndNullValueProvider() {
 // Basic functionality test
 ObjectIdReferenceProperty result = new ObjectIdReferenceProperty(property, valueDeserializer, nullValueProvider);
 assertEquals(property, result._forward);
 assertEquals(objectIdInfo, result._objectIdInfo);
 }

 @ org.junit.Test
 public void testConstructorWithSrcPropertyName() {
 // Basic functionality test
 ObjectIdReferenceProperty result = new ObjectIdReferenceProperty(property, new PropertyName("test"));
 assertEquals(property, result._forward);
 assertEquals(objectIdInfo, result._objectIdInfo);
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testWithNameWithNullPropertyName() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, new PropertyName("test"));
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testWithValueDeserializerWithNullJsonDeserializer() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(this.property, null);
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testWithNullProviderWithNullNullValueProvider() {
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(this.property, null);
 }

 @ org.junit.Test
 public void testFixAccess() {
 assertNull(property._forward.getMember());
 property.fixAccess(deserializationContext);
 assertNotNull(property._forward.getMember());
 }

 @ org.junit.Test
 public void testGetAnnotation() {
 Annotation result = property.getAnnotation(Annotation.class);
 assertNull(result);
 }

 @ org.junit.Test
 public void testGetMember() {
 AnnotatedMember result = property.getMember();
 assertNotNull(result);
 }

 @ org.junit.Test
 public void testGetCreatorIndex() {
 int result = property.getCreatorIndex();
 assertEquals(-1, result);
 }

 @ org.junit.Test
 public void testDeserializeAndSet() throws IOException {
 Object result = property.deserializeAndSet(jsonParser, deserializationContext, pojo);
 assertNull(result);
 }

 @ org.junit.Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 assertNull(result);
 }

 @ org.junit.Test
 public void testDeserializeSetAndReturnValue() throws IOException {
 ObjectNode node = new ObjectMapper().createObjectNode();
 node.put("id", id);
 node.put("value", value);
 Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 assertEquals(value, result);
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testDeserializeSetAndReturnWithNullId() throws IOException {
 Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 }

 @ org.junit.Test(expected = UnresolvedForwardReference.class)
 public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 property.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 reference.getRoid().appendReferring(new Referring(property, reference, type, pojo));
 }

 @ org.junit.Test
 public void testSet() throws IOException {
 property.set(pojo, value);
 assertEquals(value, property.get(pojo));
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testSetWithNullValue() {
 property.set(pojo, null);
 }

 @ org.junit.Test
 public void testSetAndReturnNull() throws IOException {
 Object result = property.setAndReturn(pojo, null);
 assertNull(result);
 }

 @ org.junit.Test
 public void testSetAndReturnValue() throws IOException {
 Object result = property.setAndReturn(pojo, value);
 assertEquals(value, result);
 }

 @ org.junit.Test
 public void testPropertyReferringHandlerResolvedForwardReference() throws IOException {
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 property.deserializeSetAndReturn(jsonParser, deserializationContext, pojo);
 property.getParent().handleResolvedForwardReference(id, value);
 assertEquals(value, property.get(pojo));
 }
}