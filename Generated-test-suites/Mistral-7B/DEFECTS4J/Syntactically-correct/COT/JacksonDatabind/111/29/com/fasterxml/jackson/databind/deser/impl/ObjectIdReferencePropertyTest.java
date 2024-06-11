package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty _property;
 private ObjectIdInfo _objectIdInfo;
 private SettableBeanProperty _forward;
 private JsonParser _parser;
 private DeserializationContext _context;
 private SerializerProvider _serializerProvider;
 private Object _instance;
 private JsonDeserializer<?> _deserializer;
 private NullValueProvider _nullProvider;
 private AnnotationIntrospector _introspector;
 private ObjectIdGenerator _objectIdGenerator;
 private JsonFactory _jsonFactory;
 private AnnotatedMember _member;
 private PropertyName _newName;
 private ObjectIdReader _objectIdReader;
 private JsonDeserializer<?> _valueDeserializer;
 private UnresolvedForwardReference _reference;
 private Object _referencedInstance;
 private Class<?> _type;
 private Object _value;
 private Object _resolvedId;
 private Object _resolvedValue;

 @Before
 public void setUp() throws Exception {
 _objectIdGenerator = new ObjectIdGenerator();
 _jsonFactory = new JsonFactory();
 _parser = _jsonFactory.createParser("{}");
 _context = new TestContext();
 _serializerProvider = new DefaultSerializerProvider();
 _instance = new Object();
 _deserializer = new StubJsonDeserializer();
 _nullProvider = new StubNullValueProvider();
 _forward = new SettableBeanProperty(new StubSettableBeanProperty(), _objectIdGenerator);
 _introspector = new AnnotationIntrospector(new StandardAnnotationIntrospector());
 _objectIdInfo = new ObjectIdInfo(_introspector, _objectIdGenerator, _jsonFactory);
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 _type = _instance.getClass();
 }

 @Test
 public void testConstructorWithForward() {
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 assertSame(_forward, _property._forward);
 assertSame(_objectIdInfo, _property._objectIdInfo);
 }

 @Test
 public void testConstructorWithDeserializer() {
 _property = new ObjectIdReferenceProperty(_property, _deserializer, _nullProvider);
 assertSame(_property, _property._forward);
 assertSame(_deserializer, _property._valueDeserializer);
 assertSame(_nullProvider, _property._nullProvider);
 }

 @Test
 public void testConstructorWithName() {
 _newName = PropertyName.construct("newName");
 _property = new ObjectIdReferenceProperty(_property, _newName);
 assertSame(_property, _property._forward);
 assertSame(_newName, _property.getName());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 _member = new AnnotatedMember(new StubAnnotatedClass(), "member");
 _property.setMember(_member);
 _introspector.findAnnotation(_member, JsonIdentityInfo.class);
 _objectIdInfo.setObjectIdReader(_objectIdReader);
 _objectIdInfo.setIdGenerator(ObjectIdGenerators.PropertyBased.class);
 _property.getAnnotation(JsonIdentityInfo.class);
 assertTrue(_objectIdInfo.isEnabled());
 }

 @Test
 public void testGetMember() {
 _property.setMember(_member);
 assertSame(_member, _property.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 _property.setCreatorIndex(1);
 assertEquals(1, _property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 _value = "value";
 _reference = new UnresolvedForwardReference(_objectIdReader, _type, _value);
 _objectIdInfo.setObjectIdReader(_objectIdReader);
 _property.deserializeAndSet(_parser, _context, _instance);
 assertEquals(_value, _instance);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithUnresolvedReference() throws Exception {
 _value = "value";
 _objectIdInfo.setObjectIdReader(null);
 _property.deserializeAndSet(_parser, _context, _instance);
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws Exception {
 _value = "value";
 _objectIdInfo.setObjectIdReader(_objectIdReader);
 _reference = new UnresolvedForwardReference(_objectIdReader, _type, _value);
 _property.deserializeSetAndReturn(
 _parser, _context, _instance);
 assertNull(_instance);
 }

 @Test
 public void testDeserializeSetAndReturnValue() throws Exception {
 _value = "value";
 _objectIdInfo.setObjectIdReader(_objectIdReader);
 _property.deserializeSetAndReturn(
 _parser, _context, _instance);
 assertEquals(_value, _instance);
 }

 @Test(expected = IOException.class)
 public void testSetWithUnresolvedReference() throws Exception {
 _objectIdInfo.setObjectIdReader(null);
 _property.set(_instance, _value);
 }

 @Test
 public void testSetAndReturnNull() throws Exception {
 _property.setAndReturn(_instance, null);
 assertNull(_instance);
 }

 @Test
 public void testSetAndReturnValue() throws Exception {
 _property.setAndReturn(_instance, _value);
 assertEquals(_value, _instance);
 }

 @Test
 public void testHandleResolvedForwardReference() throws Exception {
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 _reference = new UnresolvedForwardReference(_objectIdReader, _type, _referencedInstance);
 _property.getReferring().handleResolvedForwardReference(_resolvedId, _resolvedValue);
 assertEquals(_resolvedValue, _referencedInstance);
 }

 @Test
 public void testWithName() {
 _newName = PropertyName.construct("newName");
 _property = _property.withName(_newName);
 assertSame(_property, _property._forward);
 assertSame(_newName, _property.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 _valueDeserializer = new StubJsonDeserializer();
 _property = _property.withValueDeserializer(_valueDeserializer);
 assertSame(_valueDeserializer, _property._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 _nullProvider = new StubNullValueProvider();
 _property = _property.withNullProvider(_nullProvider);
 assertSame(_nullProvider, _property._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 _property.fixAccess(_context);
 _forward.fixAccess(_context);
 }

 @Test
 public void testToString() {
 String expected = "ObjectIdReferenceProperty[name='name', property=Property[name='name'], valueDeserializer=class com.example.StubJsonDeserializer]";
 assertEquals(expected, _property.toString());
 }
}

 class StubJsonDeserializer implements JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
}

 class StubNullValueProvider implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return null;
 }
}

 class StubSettableBeanProperty extends SettableBeanProperty {
 @Override
 public void set(Object instance, Object value) throws IOException {
 }

 @Override
 public Object setAndReturn(Object instance, Object value) throws IOException {
 return null;
 }
}

 class StubAnnotatedClass {
 }

 class ObjectIdGenerator {
 }

 class JsonDeserializer<T> {
 private JsonObjectIdReader _objectIdReader;

 public JsonObjectIdReader getObjectIdReader() {
 return _objectIdReader;
 }

 public void setObjectIdReader(JsonObjectIdReader objectIdReader) {
 _objectIdReader = objectIdReader;
 }
}

 class PropertyReferring extends Referring {
 private ObjectIdReferenceProperty _parent;
 private Object _pojo;

 public PropertyReferring(ObjectIdReferenceProperty parent, UnresolvedForwardReference ref,
 Class<?> type, Object ob) {
 super(ref, type);
 _parent = parent;
 _pojo = ob;
 }

 @Override
 public void handleResolvedForwardReference(Object id, Object value) throws IOException {
 _parent.set(_pojo, value);
 }
}