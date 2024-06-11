package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson. core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;

/**
 * Test cases for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testBean;
 private JsonParser testParser;
 private DeserializationContext testContext;
 private PropertyBasedCreator testCreator;
 private PropertyValueBuffer testBuffer;

 @Before
 public void setUp() throws Exception {
 testParser = new TestJsonFactory().createParser("[]");
 testContext = new TestDeserializationContext();
 testCreator = new TestPropertyBasedCreator();
 testBuffer = new PropertyValueBuffer();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new TestProperty("p1"), new TestTypeDeserializer())
 .addExternal(new TestProperty("p2"), new TestTypeDeserializer())
 .build())
 .start();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String json = "{\"p1\":\"type1\"}";
 testParser.setCurrentLocation(new CurrentLocation());
 testParser.nextToken();
 testParser.nextToken(); // skip [
 testParser.nextToken(); // skip "p1"
 testParser.nextToken(); // skip :
 testParser.nextToken(); // read \"type1\"
 assertTrue(handler.handleTypePropertyValue(testParser, testContext, "p1", testBean));
 assertEquals("type1", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String json = "[\"type1\", null, 42]";
 testParser.setCurrentLocation(new CurrentLocation());
 testParser.nextToken(); // [
 testParser.nextToken(); // read "type1"
 testParser.nextToken(); // skip ,
 testParser.nextToken(); // read null
 testParser.nextToken(); // skip ,
 testParser.nextToken(); // read 42
 assertTrue(handler.handlePropertyValue(testParser, testContext, "p1", testBean));
 assertEquals(42, handler._properties[0].getProperty().get(testBean));
 }

 @Test
 public void testHandleTypePropertyValueMissingProperty() throws Exception {
 String json = "{\"missingProp\":\"type1\"}";
 testParser.setCurrentLocation(new CurrentLocation());
 testParser.nextToken();
 testParser.nextToken(); // skip [
 testParser.nextToken(); // skip \"missingProp\"
 testParser.nextToken(); // skip :
 testParser.nextToken(); // read \"type1\"
 assertFalse(handler.handleTypePropertyValue(testParser, testContext, "missingProp", testBean));
 }

 @Test
 public void testHandlePropertyValueMissingType() throws Exception {
 String json = "[null, null, 42]";
 testParser.setCurrentLocation(new CurrentLocation());
 testParser.nextToken(); // [
 testParser.nextToken(); // skip null
 testParser.nextToken(); // skip ,
 testParser.nextToken(); // skip null
 testParser.nextToken(); // read 42
 assertFalse(handler.handlePropertyValue(testParser, testContext, "p1", testBean));
 }

 @Test
 public void testComplete() throws Exception {
 String json = "[\"type1\", null, 42]";
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 handler.complete(testParser, testContext, testBean);
 assertEquals(42, handler._properties[0].getProperty().get(testBean));
 }

 @Test
 public void testCompleteMissingType() throws Exception {
 String json = "[null, null, 42]";
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteMissingProperty() throws Exception {
 String json = "{\"p1\":\"type1\", \"missingProp\":42}";
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreator() throws Exception {
 String json = "{\"p1\":{\"typeId\":\"type1\", \"value\":42}, \"p2\":43}";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2", testBean);
 Object result = handler.complete(testParser, testContext, testBean);
 assertEquals(42, handler._properties[0].getProperty().get(result));
 assertEquals(43, handler._properties[1].getProperty().get(result));
 }

 @Test
 public void testCompleteWithCreatorMissingType() throws Exception {
 String json = "{\"p1\":{\"value\":42}, \"p2\":43}";
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorMissingProperty() throws Exception {
 String json = "[{\"typeId\":\"type1\", \"value\":42}, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorPropertyMissing() throws Exception {
 String json = "[{\"typeId\":\"type1\"}, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorTypeMissing() throws Exception {
 String json = "[null, {\"value\":42}]";
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1.typeId", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorAndProperty() throws Exception {
 String json = "[{\"typeId\":\"type1\", \"value\":42}, 43]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2", testBean);
 Object result = handler.complete(testParser, testContext, testBean);
 assertEquals(42, handler._properties[0].getProperty().get(result));
 assertEquals(43, handler._properties[1].getProperty().get(result));
 }

 @Test
 public void testCompleteWithCreatorAndPropertyMissing() throws Exception {
 String json = "[{\"typeId\":\"type1\"}, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorAndPropertyTypeMissing() throws Exception {
 String json = "[null, {\"value\":42}]";
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorAndPropertyPropertyMissing() throws Exception {
 String json = "[{\"value\":42}, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithCreatorAndPropertyPropertyMissingAndCreatorMissing() throws Exception {
 String json = "[null, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBean));
 }

 @Test
 public void testCompleteWithPropertyValueBuffer() throws Exception {
 String json = "[{\"typeId\":\"type1\", \"value\":42}, {\"typeId\":\"type2\", \"value\":43}]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p2.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2.value", testBean);
 handler.complete(testParser, testContext, testBuffer, testCreator);
 Object result = testCreator.build(testContext, testBuffer);
 assertEquals(42, handler._properties[0].getProperty().get(result));
 assertEquals(43, handler._properties[1].getProperty().get(result));
 }

 @Test
 public void testCompleteWithPropertyValueBufferMissingType() throws Exception {
 String json = "[{\"value\":42}, {\"typeId\":\"type2\", \"value\":43}]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1.value", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p2.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2.value", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBuffer, testCreator));
 }

 @Test
 public void testCompleteWithPropertyValueBufferMissingProperty() throws Exception {
 String json = "[{\"typeId\":\"type1\", \"value\":42}, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p2.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2.value", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBuffer, testCreator));
 }

 @Test
 public void testCompleteWithPropertyValueBufferMissingTypeAndProperty() throws Exception {
 String json = "[null, {\"typeId\":\"type2\", \"value\":43}]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p2.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBuffer, testCreator));
 }

 @Test
 public void testCompleteWithPropertyValueBufferMissingPropertyAndType() throws Exception {
 String json = "[{\"value\":42}, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p2.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBuffer, testCreator));
 }

 @Test
 public void testCompleteWithPropertyValueBufferMissingAll() throws Exception {
 String json = "[null, null]";
 handler.handleTypePropertyValue(testParser, testContext, "p1.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p1", testBean);
 handler.handleTypePropertyValue(testParser, testContext, "p2.typeId", testBean);
 handler.handlePropertyValue(testParser, testContext, "p2", testBean);
 assertThrows(IOException.class, () -> handler.complete(testParser, testContext, testBuffer, testCreator));
 }
}