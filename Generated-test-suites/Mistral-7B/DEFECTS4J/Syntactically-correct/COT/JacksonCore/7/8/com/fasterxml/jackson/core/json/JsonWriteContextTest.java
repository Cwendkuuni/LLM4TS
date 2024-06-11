package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

/**
 * Test class for JsonWriteContext.
 */
public class JsonWriteContextTest {

 private JsonWriteContext _rootContext;
 private JsonWriteContext _arrayContext;
 private JsonWriteContext _objectContext;

 @Test
 public void testJsonWriteContextConstructor() {
 _rootContext = JsonWriteContext.createRootContext();
 _arrayContext = JsonWriteContext.createRootContext(null).createChildArrayContext();
 _objectContext = JsonWriteContext.createRootContext(null).createChildObjectContext();

 assertEquals(_rootContext.getType(), JsonWriteContext.TYPE_ROOT);
 assertEquals(_arrayContext.getType(), JsonWriteContext.TYPE_ARRAY);
 assertEquals(_objectContext.getType(), JsonWriteContext.TYPE_OBJECT);
 }

 @Test
 public void testJsonWriteContextGetParent() {
 assertEquals(_rootContext.getParent(), null);
 assertEquals(_arrayContext.getParent(), _rootContext);
 assertEquals(_objectContext.getParent(), _arrayContext);
 }

 @Test
 public void testJsonWriteContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext contextWithDupDetector = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);

 assertEquals(contextWithDupDetector.getDupDetector(), dupDetector);
 }

 @Test
 public void testJsonWriteContextGetCurrentValue() {
 _rootContext.setCurrentValue("Value in root context");
 assertEquals(_rootContext.getCurrentValue(), "Value in root context");
 }

 @Test
 public void testJsonWriteContextSetCurrentValue() {
 _arrayContext.setCurrentValue(123);
 assertEquals(_arrayContext.getCurrentValue(), 123);
 }

 @Test
 public void testJsonWriteContextWriteFieldName() throws JsonProcessingException {
 String name = "name";
 _objectContext.writeFieldName(name);
 assertEquals(_objectContext.getCurrentName(), name);
 _objectContext.writeValue();
 assertEquals(_objectContext.getCurrentName(), null);
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonWriteContextWriteFieldNameWithGotNameTrue() throws JsonProcessingException {
 String name = "name";
 _objectContext.writeFieldName(name);
 _objectContext.writeFieldName(name);
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonWriteContextWriteFieldNameWithDupDetector() throws JsonProcessingException {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext contextWithDupDetector = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 contextWithDupDetector.writeFieldName("name");
 contextWithDupDetector.writeFieldName("name");
 }

 @Test
 public void testJsonWriteContextWriteValueAsRootContext() {
 _rootContext.writeValue();
 assertEquals(_rootContext.toString(), "/");
 }

 @Test
 public void testJsonWriteContextWriteValueAsArrayContext() {
 _arrayContext.writeValue();
 assertEquals(_arrayContext.toString(), "[0]");
 }

 @Test
 public void testJsonWriteContextWriteValueAsObjectContext() {
 _objectContext.writeFieldName("fieldName");
 _objectContext.writeValue();
 _objectContext.writeValue();
 assertEquals(_objectContext.toString(), "{\"fieldName\": null}");
 }

 @Test
 public void testJsonWriteContextCreateChildContext() {
 JsonWriteContext parentContext = _rootContext;
 JsonWriteContext childContext1 = parentContext.createChildArrayContext();
 JsonWriteContext childContext2 = parentContext.createChildObjectContext();

 assertEquals(childContext1.getParent(), parentContext);
 assertEquals(childContext2.getParent(), parentContext);
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonWriteContextCreateChildContextWithInvalidType() {
 _rootContext.createChildObjectContext();
 _rootContext.createChildArrayContext();
 }

 @Test
 public void testJsonWriteContextCreateChildContextWithSameParentTwice() {
 JsonWriteContext parentContext = _rootContext;
 JsonWriteContext childContext1 = parentContext.createChildArrayContext();
 JsonWriteContext childContext2 = parentContext.createChildArrayContext();

 assertEquals(childContext1.getParent(), parentContext);
 assertEquals(childContext2.getParent(), parentContext);
 assertEquals(childContext1, childContext2);
 }

 @Test
 public void testJsonWriteContextReset() {
 _objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 assertEquals(_objectContext.getType(), JsonWriteContext.TYPE_OBJECT);
 assertNull(_objectContext.getCurrentName());
 assertNull(_objectContext.getCurrentValue());
 }

 @Test
 public void testJsonWriteContextResetWithOtherType() {
 _objectContext.reset(JsonWriteContext.TYPE_ARRAY);
 assertEquals(_objectContext.getType(), JsonWriteContext.TYPE_ARRAY);
 assertNull(_objectContext.getCurrentName());
 assertNull(_objectContext.getCurrentValue());
 }
}