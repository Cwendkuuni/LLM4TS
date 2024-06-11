import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private ObjectIdReferenceProperty objectIdReferenceProperty2;
 private ObjectIdReferenceProperty objectIdReferenceProperty3;
 private ObjectNode testRoot;
 private JsonDeserializer<String> stringDeserializer;
 private JsonParser jsonParser;
 private ObjectIdInfo objectIdInfo;
 private AnnotatedMember annotatedMember;
 private AnnotatedClass annotatedClass;
 private SettableBeanProperty settableBeanProperty;
 private NullValueProvider nullValueProvider;
 private Object pojo;

 @Before
 public void setUp() throws Exception {
 objectIdInfo = new ObjectIdInfo(Object.class, "id");
 annotatedMember = new AnnotatedMember(new Field("id"), new JsonDeserialize(String.class));
 annotatedClass = new AnnotatedClass(Object.class);
 settableBeanProperty = new SettableBeanProperty(new ObjectIdReferenceProperty(null, objectIdInfo), nullValueProvider);
 nullValueProvider = new NullValueProvider();
 pojo = new Object();
 stringDeserializer = new StringDeserializer();
 jsonParser = new JsonParser();
 testRoot = new ObjectMapper().createObjectNode();
 }

 @Test
 public void testObjectIdReferencePropertyWithNullValueProvider() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 assertNull(objectIdReferenceProperty.getValueDeserializer());
 assertNull(objectIdReferenceProperty.getNullProvider());
 }

 @Test
 public void testObjectIdReferencePropertyWithValueDeserializer() {
 objectIdReferenceProperty2 = new ObjectIdReferenceProperty(objectIdReferenceProperty, stringDeserializer, nullValueProvider);
 assertSame(stringDeserializer, objectIdReferenceProperty2.getValueDeserializer());
 assertSame(nullValueProvider, objectIdReferenceProperty2.getNullProvider());
 }

 @Test
 public void testObjectIdReferencePropertyWithNewName() {
 objectIdReferenceProperty3 = new ObjectIdReferenceProperty(objectIdReferenceProperty2, new PropertyName("newName"));
 assertEquals("newName", objectIdReferenceProperty3.getName());
 assertSame(stringDeserializer, objectIdReferenceProperty3.getValueDeserializer());
 assertSame(nullValueProvider, objectIdReferenceProperty3.getNullProvider());
 }

 @Test
 public void testSettableBeanPropertyWithName() {
 SettableBeanProperty settableBeanPropertyWithName = objectIdReferenceProperty3.withName(new PropertyName("newName2"));
 assertEquals("newName2", settableBeanPropertyWithName.getName());
 assertSame(stringDeserializer, settableBeanPropertyWithName.getValueDeserializer());
 assertSame(nullValueProvider, settableBeanPropertyWithName.getNullProvider());
 }

 @Test
 public void testObjectIdReferencePropertyWithValueDeserializerAndNullValueProvider() {
 objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 objectIdReferenceProperty2 = new ObjectIdReferenceProperty(objectIdReferenceProperty, stringDeserializer, nullValueProvider);
 assertSame(stringDeserializer, objectIdReferenceProperty2.getValueDeserializer());
 assertSame(nullValueProvider, objectIdReferenceProperty2.getNullProvider());
 }

 @Test
 public void testFixAccess() throws IOException {
 testRoot.put("object", pojo);
 testRoot.put("id", "123");
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader objectReader = objectMapper.readerForTree(TestClass.class);
 Object testClass = objectReader.readValue(testRoot);
 ObjectIdReferenceProperty testObjectIdReferenceProperty = findObjectIdReferenceProperty(testClass, "id");
 testObjectIdReferenceProperty.fixAccess(objectMapper.getConfig());
 assertNotNull(testObjectIdReferenceProperty.getValueDeserializer());
 assertNotNull(testObjectIdReferenceProperty.getNullProvider());
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetNullValue() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 testObjectIdReferenceProperty.deserializeAndSet(jsonParser, null, pojo);
 }

 @Test
 public void testDeserializeAndSetNullValueWithIdentityInfo() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference();
 unresolvedForwardReference.setReferenceId("123");
 testObjectIdReferenceProperty.deserializeAndSet(jsonParser, null, pojo, unresolvedForwardReference);
 assertNull(pojo.getClass().getDeclaredField("id").get(pojo));
 }

 @Test
 public void testDeserializeAndSetValue() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 testObjectIdReferenceProperty.deserializeAndSet(jsonParser, null, pojo, "testValue");
 assertEquals("testValue", pojo.getClass().getDeclaredField("id").get(pojo));
 }

 @Test
 public void testDeserializeSetAndReturnNullValue() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 Object testValue = testObjectIdReferenceProperty.deserializeSetAndReturn(jsonParser, null, pojo);
 assertNull(testValue);
 }

 @Test
 public void testSet() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 testObjectIdReferenceProperty.set(pojo, "testValue");
 assertEquals("testValue", pojo.getClass().getDeclaredField("id").get(pojo));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 Object testValue = testObjectIdReferenceProperty.setAndReturn(pojo, "testValue");
 assertEquals("testValue", testValue);
 }

 @Test
 public void testCreatePropertyReferring() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 ObjectIdReferenceProperty.PropertyReferring propertyReferring = testObjectIdReferenceProperty.createPropertyReferring(pojo, "123");
 assertNotNull(propertyReferring);
 }

 @Test
 public void testHandleResolvedForwardReference() throws IOException {
 testObjectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
 ObjectIdReferenceProperty.PropertyReferring propertyReferring = testObjectIdReferenceProperty.createPropertyReferring(pojo, "123");
 Object testValue = new Object();
 objectIdReferenceProperty.handleResolvedForwardReference(123, testValue);
 assertEquals(testValue, pojo.getClass().getDeclaredField("id").get(pojo));
 }

 private ObjectIdReferenceProperty findObjectIdReferenceProperty(Object testClass, String fieldName) {
 ObjectIdReferenceProperty objectIdReferenceProperty = null;
 AnnotatedMember annotatedMember = annotatedClass.findMember(fieldName);
 for (BeanPropertyPropertyDescriptor propertyDescriptor : BeanPropertyFactory.instance.findProperties(testClass, null)) {
 if (propertyDescriptor.getName().equals(fieldName)) {
 SettableBeanProperty settableBeanProperty = propertyDescriptor.getWriteMethod();
 if (settableBeanProperty instanceof ObjectIdReferenceProperty) {
 objectIdReferenceProperty = (ObjectIdReferenceProperty) settableBeanProperty;
 break;
 }
 }
 }
 return objectIdReferenceProperty;
 }

 @JsonDeserialize(using = StringDeserializer.class)
 static class TestClass {
 String id;
 }

 static class StringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
 return jsonParser.getValueAsString();
 }
 }
}