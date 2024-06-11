package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 dupDetector = new DupDetector();
 context = JsonWriteContext.createRootContext(dupDetector);
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String name = "field1";
 int result = context.writeFieldName(name);
 assertEquals("Expected STATUS_OK_AFTER_COLON", JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 context.setCurrentValue("value1");
 name = "field2";
 result = context.writeFieldName(name);
 assertEquals("Expected STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameDuplicate() throws JsonProcessingException {
 String name = "field1";
 context.writeFieldName(name);
 context.writeFieldName(name);
 }

 @Test
 public void testWriteValueArray() throws JsonProcessingException {
 context = context.createChildArrayContext();
 context.writeValue();
 context.writeValue();
 int result = context.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test
 public void testWriteValueObject() throws JsonProcessingException {
 context = context.createChildObjectContext();
 context.writeFieldName("field1");
 context.setCurrentValue("value1");
 context.writeValue();
 context.writeFieldName("field2");
 context.setCurrentValue("value2");
 context.writeValue();
 int result = context.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COLON", JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test
 public void testWriteValueRootContext() throws JsonProcessingException {
 context = JsonWriteContext.createRootContext(dupDetector);
 context.writeValue();
 context.writeValue();
 int result = context.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_SPACE", JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
 }

 @Test
 public void testReset() throws JsonProcessingException {
 context = context.reset(JsonWriteContext.TYPE_OBJECT);
 context.writeFieldName("field1");
 context.setCurrentValue("value1");
 context.writeValue();
 int result = context.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COLON", JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 context.reset(JsonWriteContext.TYPE_ARRAY);
 context.writeValue();
 context.writeValue();
 result = context.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }
}