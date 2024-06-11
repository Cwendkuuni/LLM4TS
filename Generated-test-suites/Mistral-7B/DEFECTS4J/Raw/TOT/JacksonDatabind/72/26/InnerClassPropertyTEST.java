package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.*;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

 @BeforeClass
 public static void setup() {
 // Add any setup code here, e.g. creating test data
 }

 @AfterClass
 public static void teardown() {
 // Add any teardown code here, e.g. closing resources
 }

 @Test
 public void testDefaultConstructor() throws Exception {
 InnerClassProperty property = new InnerClassProperty(new StdSerializer<>() {}, null);
 assertNull(property._delegate);
 assertNull(property._annotated);
 assertNull(property._creator);
 }

 @Test
 public void testSettableBeanPropertyConstructor() throws Exception {
 SettableBeanProperty delegate = new StdDelegateProperty();
 Constructor<InnerClassType> constructor = InnerClassType.class.getDeclaredConstructor();
 InnerClassProperty property = new InnerClassProperty(delegate, constructor);
 assertEquals(delegate, property._delegate);
 assertNotNull(property._annotated);
 assertNotNull(property._creator);
 }

 @Test
 public void testJdkSerializationConstructor() throws Exception {
 SettableBeanProperty srcProperty = new StdDelegateProperty();
 InnerClassProperty property = new InnerClassProperty(srcProperty, new AnnotatedConstructor(null, srcProperty._creator, null, null));
 assertEquals(srcProperty, property);
 assertNull(property._delegate);
 assertNotNull(property._annotated);
 assertNotNull(property._creator);
 }

 @Test
 public void testJsonDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClass\":[]}";
 InnerClassContainer container = mapper.readValue(json, InnerClassContainer.class);
 assertEquals(0, container.getInnerClasses().size());

 JsonParser jp = mapper.getFactory().createParser(json);
 InnerClassProperty property = mapper.findDeserializer(InnerClassProperty.class);
 Object readValue = property.deserializeAndSet(jp, new TestContext(), new Object());
 assertEquals(new InnerClassType(), readValue);

 assertEquals(1, container.getInnerClasses().size());
 assertEquals(new InnerClassType(), container.getInnerClasses().get(0));
 }

 @Test
 public void testNullValueDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClass\":null}";
 InnerClassContainer container = mapper.readValue(json, InnerClassContainer.class);
 assertEquals(0, container.getInnerClasses().size());

 JsonParser jp = mapper.getFactory().createParser(json);
 InnerClassProperty property = mapper.findDeserializer(InnerClassProperty.class);
 Object readValue = property.deserializeAndSet(jp, new TestContext(), new Object());
 assertNull(readValue);

 assertEquals(0, container.getInnerClasses().size());
 }

 @Test
 public void testTypeDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"innerClass\":{\"@type\":\"com.fasterxml.jackson.databind.deser.impl.InnerClassType\"}}";
 InnerClassContainer container = mapper.readValue(json, InnerClassContainer.class);
 assertEquals(0, container.getInnerClasses().size());

 JsonParser jp = mapper.getFactory().createParser(json);
 InnerClassProperty property = mapper.findDeserializer(InnerClassProperty.class);
 property.setValueDeserializer(new TypeDeserializer());
 Object readValue = property.deserializeAndSet(jp, new TestContext(), new Object());
 assertEquals(new InnerClassType(), readValue);

 assertEquals(1, container.getInnerClasses().size());
 assertEquals(new InnerClassType(), container.getInnerClasses().get(0));
 }

 @Test
 public void testExceptionDuringConstruction() throws Exception {
 SettableBeanProperty delegate = new StdDelegateProperty();
 Constructor<InnerClassType> constructor = InnerClassType.class.getDeclaredConstructor();
 constructor.setAccessible(true);
 InnerClassProperty property = new InnerClassProperty(delegate, constructor);
 assertNotNull(property);

 try {
 constructor.newInstance(new Object());
 fail("Should have thrown an exception");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testNullInputDuringDeserialization() throws Exception {
 SettableBeanProperty property = new InnerClassProperty(new StdDelegateProperty(), null);
 ObjectMapper mapper = new ObjectMapper();
 String json = "{}";
 try {
 property.deserializeAndSet(mapper.getFactory().createParser(json), new TestContext(), new Object());
 fail("Should have thrown an exception");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }
}

class InnerClassContainer {
 private List<InnerClassType> innerClasses = new ArrayList<>();

 public List<InnerClassType> getInnerClasses() {
 return innerClasses;
 }
}

class InnerClassType {
 // Empty for test purposes
}

class StdDelegateProperty extends SettableBeanProperty {
 // Empty for test purposes
}

class TestContext extends DeserializationContext {
 // Empty for test purposes
}

class TypeDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return new Object();
 }
}