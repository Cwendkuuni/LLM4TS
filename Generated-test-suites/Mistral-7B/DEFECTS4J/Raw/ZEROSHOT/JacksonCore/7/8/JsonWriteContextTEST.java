package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link JsonWriteContext}
 */
public class JsonWriteContextTest {

 private JsonWriteContext createRootContext() {
 return JsonWriteContext.createRootContext();
 }

 private JsonWriteContext createRootContext(DupDetector dupDetector) {
 return JsonWriteContext.createRootContext(dupDetector);
 }

 private JsonWriteContext createChildArrayContext(JsonWriteContext parentContext) {
 return parentContext.createChildArrayContext();
 }

 private JsonWriteContext createChildObjectContext(JsonWriteContext parentContext) {
 return parentContext.createChildObjectContext();
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = createRootContext();
 assertNull(context.getParent());
 assertTrue(context.isRootContext());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector() {};
 JsonWriteContext context = createRootContext(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }

 @Test
 public void testGetCurrentValue() {
 JsonWriteContext context = createRootContext();
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testSetCurrentValue() {
 JsonWriteContext context = createRootContext();
 context.setCurrentValue("value");
 assertEquals("value", context.getCurrentValue());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createRootContext();
 JsonWriteContext childContext = createChildArrayContext(parentContext);
 assertEquals(parentContext, childContext.getParent());
 assertFalse(childContext.isRootContext());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createRootContext();
 JsonWriteContext childContext = createChildObjectContext(parentContext);
 assertEquals(parentContext, childContext.getParent());
 assertFalse(childContext.isRootContext());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameNullName() throws JsonProcessingException {
 JsonWriteContext context = createRootContext();
 context.writeFieldName(null);
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createRootContext();
 int result = context.writeFieldName("fieldName");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 assertEquals("fieldName", context.getCurrentName());
 }

 @Test
 public void testWriteValueRootContext() throws JsonProcessingException {
 JsonWriteContext context = createRootContext();
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test
 public void testWriteValueArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createRootContext();
 JsonWriteContext childContext = createChildArrayContext(parentContext);
 int result = childContext.writeValue();
 assertEquals("[0]", childContext.toString());
 }

 @Test
 public void testWriteValueObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createRootContext();
 JsonWriteContext childContext = createChildObjectContext(parentContext);
 int result = childContext.writeFieldName("field1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 result = childContext.writeValue();
 assertEquals("{\"field1\":}", childContext.toString());
 }

 @Test
 public void testWriteValueObjectContextWithName() throws JsonProcessingException {
 JsonWriteContext parentContext = createRootContext();
 JsonWriteContext childContext = createChildObjectContext(parentContext);
 int result = childContext.writeFieldName("field1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 childContext.setCurrentValue("value1");
 result = childContext.writeValue();
 assertEquals("{\"field1\":\"value1\"}", childContext.toString());
 }

 @Test
 public void testWriteValueObjectContextWithNameAndParent() throws JsonProcessingException {
 JsonWriteContext parentContext = createRootContext();
 JsonWriteContext childContext = createChildObjectContext(parentContext);
 int result = childContext.writeFieldName("field1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 JsonWriteContext grandChildContext = createChildObjectContext(childContext);
 grandChildContext.writeFieldName("field2");
 grandChildContext.setCurrentValue("value2");
 result = grandChildContext.writeValue();
 assertEquals("{\"field1\":{\"field2\":\"value2\"}}", childContext.toString());
 }
}