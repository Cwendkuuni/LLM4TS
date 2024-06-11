package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPreprendableDupDetector;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext objectContextWithName;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 rootContext = JsonWriteContext.createRootContext();
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 dupDetector = new DefaultPrependableDupDetector();
 }

 @Test
 public void testWriteFieldName_NullName() throws JsonProcessingException {
 JsonWriteContext context = objectContextWithName.reset(JsonWriteContext.TYPE_OBJECT);
 Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeFieldName(null));
 }

 @Test
 public void testWriteFieldName_EmptyName() throws JsonProcessingException {
 JsonWriteContext context = objectContextWithName.reset(JsonWriteContext.TYPE_OBJECT);
 Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeFieldName(""));
 }

 @Test
 public void testWriteFieldName_DuplicateName() throws JsonProcessingException {
 JsonWriteContext context = objectContextWithName.reset(JsonWriteContext.TYPE_OBJECT);
 context.withDupDetector(dupDetector);
 Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("test"));
 dupDetector.addDup("test");
 Assert.assertThrows(JsonProcessingException.class, () -> context.writeFieldName("test"));
 }

 @Test
 public void testWriteValue_RootContext() throws JsonProcessingException {
 rootContext = rootContext.reset(JsonWriteContext.TYPE_ROOT);
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
 }

 @Test
 public void testWriteValue_ArrayContext() throws JsonProcessingException {
 arrayContext = arrayContext.reset(JsonWriteContext.TYPE_ARRAY);
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
 }

 @Test
 public void testWriteValue_ObjectContext() throws JsonProcessingException {
 objectContext = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_ExpectName() throws JsonProcessingException {
 objectContextWithName = objectContextWithName.reset(JsonWriteContext.TYPE_OBJECT);
 objectContextWithName.writeFieldName("test");
 Assert.assertFalse(objectContextWithName.gotName());
 objectContextWithName.writeValue();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_ExpectValue() throws JsonProcessingException {
 objectContextWithName = objectContextWithName.reset(JsonWriteContext.TYPE_OBJECT);
 objectContextWithName.writeFieldName("test");
 objectContextWithName.gotName(true);
 Assert.assertFalse(objectContextWithName.writeValue() == JsonWriteContext.STATUS_EXPECT_VALUE);
 }

 @Test
 public void testToString() throws JsonProcessingException {
 Assert.assertEquals("{}", rootContext.toString());
 Assert.assertEquals("[]", arrayContext.toString());
 Assert.assertEquals("{\"test\":}", objectContextWithName.toString());
 }
}