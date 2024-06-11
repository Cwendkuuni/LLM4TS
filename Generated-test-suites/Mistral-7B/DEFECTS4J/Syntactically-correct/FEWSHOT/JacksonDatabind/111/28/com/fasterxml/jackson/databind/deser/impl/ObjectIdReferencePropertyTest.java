package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializedForm;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo.DefaultIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdReferencePropertyTest {

 private SettableBeanProperty _forward;
 private ObjectIdReferenceProperty _property;
 private Object _pojo;
 private JsonParser _parser;
 private DeserializationContext _ctxt;
 private ObjectIdInfo _objectIdInfo;
 private PropertyName _name;
 private JsonDeserializer<Object> _deserializer;
 private NullValueProvider _nullProvider;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 _forward = mock(SettableBeanProperty.class);
 _parser = mock(JsonParser.class);
 _ctxt = mock(DeserializationContext.class);
 _objectIdInfo = mock(ObjectIdInfo.class);
 _name = mock(PropertyName.class);
 _deserializer = mock(JsonDeserializer.class);
 _nullProvider = mock(NullValueProvider.class);
 }

 @Test
 public void testConstructorWithForward() {
 _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
 assertSame(_forward, _property._forward);
 assertSame(_objectIdInfo, _property._objectIdInfo);
 }

 @Test
 public void testConstructorWithDeserializer() {
 _property = new ObjectIdReferenceProperty(_forward, _deserializer, _nullProvider);
 assertSame(_forward, _property._forward);
 assertSame(_deserializer, _property._valueDeserializer);
 assertSame(_nullProvider, _property._nullProvider);
 }

 @Test
 public void testConstructorWithName() {
 _property = new ObjectIdReferenceProperty(_forward, _name);
 assertSame(_forward, _property);
 assertSame(_name, _property.getName());
 }

 @Test
 public void testGetName() {
 _property = new ObjectIdReferenceProperty(_forward, _name);
 assertSame(_name, _property.getName());
 }

 @Test
 public void testWithName() {
 _property = new ObjectIdReferenceProperty(_forward, _name);
 _property = _property.withName(new PropertyName("newName"));
 assertNotSame(_name, _property.getName());
 assertSame("newName", _property.getName().asPrefix());
 }

 @Test
 public void testWithValueDeserializer() {
 _property = new ObjectIdReferenceProperty(_forward, _deserializer, _nullProvider);
 _property = _property.withValueDeserializer(mock(JsonDeserializer.class));
 assertNotSame(_deserializer, _property._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() {
 _property = new ObjectIdReferenceProperty(_forward, _deserializer, _nullProvider);
 _property = _property.withNullProvider(mock(NullValueProvider.class));
 assertNotSame(_nullProvider, _property._nullProvider);
 }

 @Test
 public void testFixAccess() throws IOException {
 _property = new ObjectIdReferenceProperty(_forward, _name);
 _property.fixAccess(_ctxt);
 _forward.fixAccess(_ctxt);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Annotation annotation = mock(Annotation.class);
 when(_forward.getAnnotation(Annotation.class)).thenReturn(annotation);
 assertSame(annotation, _property.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() throws Exception {
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(_forward.getMember()).thenReturn(member);
 assertSame(member, _property.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws Exception {
 int creatorIndex = 0;
 when(_forward.getCreatorIndex()).thenReturn(creatorIndex);
 assertEquals(creatorIndex, _property.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = new Object();
 when(_parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(_parser.readValueAs(Object.class)).thenReturn(id);
 when(_ctxt.findBeanProperty(_name)).thenReturn(_property);
 _property.deserializeAndSet(_parser, _ctxt, _pojo);
 assertEquals(id, _pojo);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = new Object();
 when(_parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(_parser.readValueAs(Object.class)).thenReturn(id);
 when(_ctxt.findBeanProperty(_name)).thenReturn(_property);
 when(_property.deserialize(null, _ctxt)).thenReturn(id);
 Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _pojo);
 assertEquals(id, result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = new Object();
 _property.set(_pojo, id);
 assertEquals(id, _pojo);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = new Object();
 Object result = _property.setAndReturn(_pojo, id);
 assertEquals(id, result);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 Object id = new Object();
 _property = new ObjectIdReferenceProperty(_forward, null) {
 @Override
 public void handleResolvedForwardReference(Object idObject, Object value) throws IOException {
 super.handleResolvedForwardReference(idObject, value);
 }
 };
 Object result = _property.deserializeSetAndReturn(_parser, _ctxt, _pojo);
 assertNull(result);

 when(_parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(_parser.readValueAs(Object.class)).thenReturn(id);
 when(_ctxt.findBeanProperty(_name)).thenReturn(_property);
 when(_property.deserialize(null, _ctxt)).thenReturn(null);
 when(_property.getObjectIdReader()).thenReturn(new DefaultIdResolver());
 when(_property.getObjectIdInfo()).thenReturn(_objectIdInfo);
 _property.handleResolvedForwardReference(id, _pojo);
 assertSame(_pojo, _property._pojo);
 }

 @Test
 public void testIdentityInfo() throws Exception {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo();
 objectIdInfo.setIdGenerator(ObjectIdGenerators.IntIdGenerator.instance());
 BeanPropertyDefinition def = mock(BeanPropertyDefinition.class);
 when(def.getName()).thenReturn(_name);
 when(def.getAnnotationValues()).thenReturn(new SerializedForm.SerializerMethodAnnotation(ObjectIdGenerators.Id.class, null));
 when(_objectIdInfo.getDefinition()).thenReturn(def);
 when(_objectIdInfo.getObjectIdReader()).thenReturn(new DefaultIdResolver());
 when(_objectIdInfo.getIdValue()).thenReturn("123");
 _property = new ObjectIdReferenceProperty(_forward, _deserializer, _nullProvider, _objectIdInfo);
 assertEquals("123", _property.getObjectIdInfo().getIdValue());
 }
}