package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void test_createRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals("Root context", context.toString(), "/");
 }

 @Test
 public void test_createRootContextWithDuplicator() {
 DupDetector duplicator = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(duplicator);
 assertEquals("Root context with dup detector", context.toString(), "/");
 }

 @Test
 public void test_createChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals("Array context", childContext.toString(), "[0]");
 }

 @Test(expected = JsonProcessingException.class)
 public void test_createChildArrayContextWithDuplicator() {
 DupDetector duplicator = new DupDetector();
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, duplicator);
 parentContext.createChildArrayContext();
 parentContext.createChildArrayContext();
 fail("Expected JsonProcessingException");
 }

 @Test
 public void test_createChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals("Object context", childContext.toString(), "{0}");
 }

 @Test(expected = JsonProcessingException.class)
 public void test_createChildObjectContextWithDuplicator() {
 DupDetector duplicator = new DupDetector();
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, duplicator);
 parentContext.createChildObjectContext();
 parentContext.createChildObjectContext();
 fail("Expected JsonProcessingException");
 }

 @Test
 public void test_writeFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertEquals("Field name 'name'", context.toString(), "{name}");
 }

 @Test(expected = JsonProcessingException.class)
 public void test_writeFieldNameTwice() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeFieldName("name");
 fail("Expected JsonProcessingException");
 }

 @Test
 public void test_writeValue() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();
 assertEquals("Object context with value", context.toString(), "{0}");
 }

 @Test
 public void test_writeValueForRootContext() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 assertEquals("Root context with value", context.toString(), "/");
 }

 @Test
 public void test_writeValueForArrayContext() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeValue();
 assertEquals("Array context with value", context.toString(), "[0]");
 }

 @Test
 public void test_reset() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();
 context.reset(JsonWriteContext.TYPE_ARRAY);
 assertEquals("Array context after reset", context.toString(), "[0]");
 }

 @Test
 public void test_getParent() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals("Parent context", parentContext.getParent(), null);
 assertEquals("Child context", childContext.getParent(), parentContext);
 }

 @Test
 public void test_getDupDetector() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 assertNull("No dup detector for root context", context.getDupDetector());
 }

 @Test
 public void test_setAndGetCurrentValue() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.setCurrentValue(new Object());
 assertEquals("Current value is set to new Object()", context.getCurrentValue(), new Object());
 }
}