import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private SettableBeanProperty settableBeanPropertyMock;
 private ObjectIdInfo objectIdInfoMock;
 private JsonDeserializer<?> jsonDeserializerMock;
 private NullValueProvider nullValueProviderMock;
 private AnnotatedMember annotatedMemberMock;
 private PropertyName propertyNameMock;

 @Before
 public void setUp() {
 settableBeanPropertyMock = mock(SettableBeanProperty.class);
 objectIdInfoMock = mock(ObjectIdInfo.class);
 jsonDeserializerMock = mock(JsonDeserializer.class);
 nullValueProviderMock = mock(NullValueProvider.class);
 annotatedMemberMock = Mockito.spy(new AnnotatedMember(Mockito.mock(AnnotatedClass.class), "testProperty"));
 propertyNameMock = Mockito.mock(PropertyName.class);

 objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
 }

 @Test
 public void testConstructorWithForward() {
 ObjectIdReferenceProperty objectIdReferencePropertyWithForward = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
 assertEquals(settableBeanPropertyMock, objectIdReferencePropertyWithForward._forward);
 }

 @Test
 public void testConstructorWithDeserializer() {
 ObjectIdReferenceProperty objectIdReferencePropertyWithDeserializer = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializerMock, nullValueProviderMock);
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithDeserializer);
 assertEquals(jsonDeserializerMock, objectIdReferencePropertyWithDeserializer._valueDeserializer);
 assertEquals(nullValueProviderMock, objectIdReferencePropertyWithDeserializer._nullProvider);
 }

 @Test
 public void testConstructorWithName() {
 ObjectIdReferenceProperty objectIdReferencePropertyWithName = new ObjectIdReferenceProperty(objectIdReferenceProperty, propertyNameMock);
 assertEquals(objectIdReferenceProperty, objectIdReferencePropertyWithName);
 assertEquals(propertyNameMock, objectIdReferencePropertyWithName._name);
 }

 @Test
 public void testGetAnnotation() {
 when(objectIdInfoMock.getValueAnnotation()).thenReturn(Mockito.mock(Annotation.class));
 assertEquals(Mockito.mock(Annotation.class), objectIdReferenceProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(annotatedMemberMock, objectIdReferenceProperty.getMember());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object testInstance = Mockito.mock(Object.class);
 JsonParser jsonParserMock = mock(JsonParser.class);
 Object deserializedValue = Mockito.mock(Object.class);
 when(jsonDeserializerMock.deserialize(jsonParserMock, JsonDeserializationContextMock.getInstance(), testInstance)).thenReturn(deserializedValue);

 when(settableBeanPropertyMock.deserializeAndSet(jsonParserMock, JsonDeserializationContextMock.getInstance(), testInstance)).thenCallRealMethod();

 objectIdReferenceProperty.deserializeAndSet(jsonParserMock, JsonDeserializationContextMock.getInstance(), testInstance);
 assertEquals(deserializedValue, settableBeanPropertyMock.getRawValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object testInstance = Mockito.mock(Object.class);
 JsonParser jsonParserMock = mock(JsonParser.class);
 Object deserializedValue = Mockito.mock(Object.class);
 when(jsonDeserializerMock.deserialize(jsonParserMock, JsonDeserializationContextMock.getInstance(), testInstance)).thenReturn(deserializedValue);
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParserMock, JsonDeserializationContextMock.getInstance(), testInstance);
 assertEquals(deserializedValue, result);
 }

 @Test
 public void testSet() throws IOException {
 Object testInstance = Mockito.mock(Object.class);
 Object value = Mockito.mock(Object.class);
 objectIdReferenceProperty.set(testInstance, value);
 assertEquals(value, settableBeanPropertyMock.getRawValue());
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object testInstance = Mockito.mock(Object.class);
 Object value = Mockito.mock(Object.class);
 Object result = objectIdReferenceProperty.setAndReturn(testInstance, value);
 assertEquals(value, result);
 }

 @Test
 public void testPropertyReferring() throws IOException {
 Object testInstance = Mockito.mock(Object.class);
 UnresolvedForwardReference unresolvedForwardReferenceMock = mock(UnresolvedForwardReference.class);
 Object id = Mockito.mock(Object.class);
 Object value = Mockito.mock(Object.class);
 Object type = Mockito.mock(Class.class);
 Object parent = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
 PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(parent, unresolvedForwardReferenceMock, type, testInstance);

 when(unresolvedForwardReferenceMock.getId()).thenReturn(id);
 when(unresolvedForwardReferenceMock.getValue()).thenReturn(value);
 when(jsonDeserializerMock.getObjectIdReader()).thenReturn(new StdSerializer<Object>(Object.class) {
 @Override public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return id;
 }
 });

 propertyReferring.handleResolvedForwardReference(id, value);
 assertEquals(value, settableBeanPropertyMock.getRawValue());
 }
}

class JsonDeserializationContextMock {
 private static volatile JsonDeserializationContext INSTANCE;

 public static JsonDeserializationContext getInstance() {
 if (INSTANCE == null) {
 INSTANCE = new JsonDeserializationContext() {
 // empty constructor
 };
 }
 return INSTANCE;
 }
}