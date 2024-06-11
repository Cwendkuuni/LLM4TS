package com.fasterxml.jackson.core.json;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrePrePreParser;
import com.fasterxml.jackson.core.util.DefaultWriterFactory;
import com.fasterxml.jackson.core.util.DupDetector;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonWriteContextTest {

 private JsonWriteContext _rootContext;
 private JsonWriteContext _objectContext;
 private JsonWriteContext _arrayContext;
 private DupDetector _dupDetector;

 @Before
 public void setUp() {
 _rootContext = JsonWriteContext.createRootContext();
 _objectContext = _rootContext.createChildObjectContext();
 _arrayContext = _rootContext.createChildArrayContext();
 _dupDetector = new DefaultPrePrePreParser.DefaultDupDetector();
 }

 @After
 public void tearDown() {
 _objectContext = null;
 _arrayContext = null;
 _rootContext = null;
 _dupDetector = null;
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 // Test writing valid field names
 _objectContext.writeFieldName("field1");
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, _objectContext.getStatus());

 _objectContext.writeFieldName("field2");
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, _objectContext.getStatus());

 // Test writing duplicate field name
 _objectContext.reset();
 _objectContext.writeFieldName("field1");
 _objectContext.reset();
 _objectContext.writeFieldName("field1");
 Assert.assertFalse(_objectContext.getStatus() >= 0);
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 // Test writing valid values for root context
 _rootContext.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, _rootContext.getStatus());

 // Test writing valid values for object context
 _objectContext.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, _objectContext.getStatus());

 // Test writing valid values for array context
 _arrayContext.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, _arrayContext.getStatus());
 }

 @Test
 public void testSetCurrentValue() throws JsonProcessingException {
 // Test setting valid values for root context
 _rootContext.setCurrentValue("value1");
 Assert.assertEquals("value1", _rootContext.getCurrentValue());

 _rootContext.setCurrentValue(123);
 Assert.assertEquals(123, _rootContext.getCurrentValue());

 // Test setting valid values for object context
 _objectContext.setCurrentValue("value2");
 Assert.assertEquals("value2", _objectContext.getCurrentValue());

 _objectContext.setCurrentValue(new Object());
 Assert.assertNotNull(_objectContext.getCurrentValue());

 // Test setting valid values for array context
 _arrayContext.setCurrentValue(456);
 Assert.assertEquals(456, _arrayContext.getCurrentValue());

 _arrayContext.setCurrentValue(new int[] { 1, 2, 3 });
 Assert.assertArrayEquals(new int[] { 1, 2, 3 }, (int[]) _arrayContext.getCurrentValue());
 }

 @Test
 public void testWithDupDetector() {
 // Test passing valid DupDetector
 JsonWriteContext context = _rootContext.withDupDetector(_dupDetector);
 Assert.assertNotNull(context.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameDuplicateWithDetector() throws JsonProcessingException {
 // Test passing DupDetector that detects duplicate
 _objectContext.reset();
 _objectContext.writeFieldName("field1");
 _objectContext.withDupDetector(_dupDetector);
 _objectContext.writeFieldName("field1");
 }
}