package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

 private ObjectNode node;
 private JsonParser parser;
 private ObjectMapper mapper;
 private InnerClassProperty property;
 private MyInnerClass innerClass;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 node = mapper.createObjectNode();
 parser = mapper.getFactory().createParser(node);
 innerClass = new MyInnerClass();
 property = new InnerClassProperty(new TestBeanProperty(), MyInnerClass.InnerClassConstructor.class);
 }

 @Test
 public void testDeserialize() throws Exception {
 // Given
 node.put("name", "innerClassPropertyName");
 node.put("innerClass", mapper.writeValueAsString(new Object()));

 // When
 property.deserializeAndSet(parser, new TestContext(), innerClass);

 // Then
 Assert.assertNotNull(innerClass.innerClassProperty);
 }

 @Test
 public void testNullValue() throws Exception {
 // Given
 node.put("name", "innerClassPropertyName");
 node.putNull("innerClass");

 // When
 property.deserializeAndSet(parser, new TestContext(), innerClass);

 // Then
 Assert.assertNull(innerClass.innerClassProperty);
 }

 @Test
 public void testTypeDeserializer() throws Exception {
 // Given
 node.put("name", "innerClassPropertyName");
 node.put("innerClass", mapper.writeValueAsString(new TypeClass()));

 JsonDeserializer<TypeClass> typeDeserializer = mock(JsonDeserializer.class);
 when(typeDeserializer.deserialize(parser, new TestContext(), eq(TypeClass.class)))
 .thenReturn(new TypeClass());
 property = new InnerClassProperty(new TestBeanProperty(), null, typeDeserializer);

 // When
 property.deserializeAndSet(parser, new TestContext(), innerClass);

 // Then
 Assert.assertEquals(new TypeClass(), innerClass.innerClassProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws Exception {
 // Given
 property = new InnerClassProperty(new TestBeanProperty(), null);

 // When
 property.deserializeAndSet(parser, new TestContext(), innerClass);

 // Then
 // This exception should be thrown automatically during test execution
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 // Given
 doThrow(new IOException("Test IOException")).when(parser).nextToken();

 // When
 property.deserializeAndSet(parser, new TestContext(), innerClass);

 // Then
 // This exception should be thrown automatically during test execution
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJdkSerialization() throws Exception {
 // Given
 node.put("name", "innerClassPropertyName");
 node.put("innerClass", mapper.writeValueAsString(innerClass));

 // When
 Object readResolve = innerClass.readResolve();
 InnerClassProperty innerClassProperty = (InnerClassProperty) readResolve;
 Object value = innerClassProperty.deserialize(parser, new TestContext());

 // Then
 Assert.assertNotNull(value);
 }

 @Test
 public void testJdkSerializationWriteReplace() throws Exception {
 // Given
 Object writeReplace = innerClass.writeReplace();
 InnerClassProperty innerClassProperty = (InnerClassProperty) writeReplace;
 Object value = innerClassProperty.deserialize(parser, new TestContext());

 // When
 // No need to assert anything in this test case, as it just checks if the writeReplace method returns a valid InnerClassProperty instance
 }
}

class TestBean {
 private InnerClassProperty innerClassProperty;
}

class TestBeanProperty extends SettableBeanProperty {
 public TestBeanProperty() {
 super(InnerClassProperty.class);
 }
}

class MyInnerClass {
 private InnerClassProperty innerClassProperty;

 static class InnerClassConstructor {
 static final Constructor<MyInnerClass> INSTANCE = MyInnerClass.class.getDeclaredConstructor();
 }

 public MyInnerClass() {
 }
}

class TypeClass {
}

class TestContext extends DeserializationContext {
}