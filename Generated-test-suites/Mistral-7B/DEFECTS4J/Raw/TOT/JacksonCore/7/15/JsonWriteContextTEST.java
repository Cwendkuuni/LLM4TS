package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrePrecedenceDupDetector;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext arrayContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext nestedObjectContext;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 rootContext = JsonWriteContext.createRootContext(new DefaultPrePrecedenceDupDetector());
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 nestedObjectContext = objectContext.createChildObjectContext();
 dupDetector = new DefaultPrePrecedenceDupDetector();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String name = "name1";
 int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COMMA;
 JsonWriteContext context = objectContext;
 context.writeFieldName(name);
 Assert.assertEquals(expectedStatus, context.getStatus());

 name = "name2";
 context.reset();
 context = objectContext;
 context.writeFieldName(name);
 context.writeValue();
 context.writeFieldName(name);
 expectedStatus = JsonWriteContext.STATUS_EXPECT_VALUE;
 Assert.assertEquals(expectedStatus, context.getStatus());
 }

 @Test(expected = JsonGenerationException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 String name = "name";
 JsonWriteContext context = objectContext;
 context.withDupDetector(dupDetector);
 context.writeFieldName(name);
 context.writeValue();
 context.writeFieldName(name);
 }

 @Test
 public void testWriteValueArray() throws JsonProcessingException {
 int expectedIndex = 0;
 JsonWriteContext context = arrayContext;
 context.writeValue();
 context.setCurrentValue(1);
 context.writeValue();
 context.writeValue();
 context.writeValue();
 Assert.assertEquals(expectedIndex, context.getCurrentIndex());
 }

 @Test
 public void testWriteValueObject() throws JsonProcessingException {
 String name1 = "name1";
 String name2 = "name2";
 JsonWriteContext context = objectContext;
 context.writeFieldName(name1);
 context.writeValue();
 context.setCurrentValue("value1");
 context.writeValue();
 context.writeFieldName(name2);
 context.writeValue();
 context.setCurrentValue("value2");
 context.writeValue();
 context.writeFieldName(name1);
 context.writeValue();
 context.setCurrentValue("value3");
 context.writeValue();

 String expectedJson = "{\"name1\":\"value3\",\"name2\":\"value2\"}";
 Assert.assertEquals(expectedJson, context.toString());
 }

 @Test
 public void testWriteValueNestedObject() throws JsonProcessingException {
 String name1 = "name1";
 String name2 = "name2";
 JsonWriteContext context = nestedObjectContext;
 context.writeFieldName(name1);
 context.writeValue();
 context.setCurrentValue(objectContext);
 context.writeFieldName(name2);
 context.writeValue();
 context.setCurrentValue(new Object());
 context.writeValue();
 context.writeFieldName(name1);
 context.writeValue();
 context.setCurrentValue("value3");
 context.writeValue();
 context.writeFieldName(name2);
 context.writeValue();
 context.setCurrentValue("value4");
 context.writeValue();

 String expectedJson = "{\"name1\":{\"name2\":\"value4\",\"\":\"value3\"}}";
 Assert.assertEquals(expectedJson, context.toString());
 }

 @Test
 public void testReset() throws JsonProcessingException {
 JsonWriteContext context = arrayContext;
 context.writeValue();
 context.reset();
 int expectedIndex = -1;
 Assert.assertEquals(expectedIndex, context.getCurrentIndex());
 }

 @Test
 public void testGetParent() {
 JsonWriteContext context = arrayContext;
 Assert.assertNull(context.getParent());
 context = objectContext;
 Assert.assertEquals(rootContext, context.getParent());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext context = objectContext;
 Assert.assertNull(context.getDupDetector());
 context = objectContext.withDupDetector(dupDetector);
 Assert.assertEquals(dupDetector, context.getDupDetector());
 }

 @Test
 public void testToString() {
 JsonWriteContext context = rootContext;
 Assert.assertEquals("/", context.toString());
 context = arrayContext;
 Assert.assertEquals("[", context.toString());
 context = objectContext;
 Assert.assertEquals("{\"", context.toString());
 context = nestedObjectContext;
 Assert.assertEquals("{\"{\"", context.toString());
 }
}