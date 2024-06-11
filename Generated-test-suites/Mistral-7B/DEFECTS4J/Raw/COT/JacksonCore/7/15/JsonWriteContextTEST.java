package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DupDetector;

import org.junit.Test;

/**
 * Test case for JsonWriteContext class
 */
public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, DupDetector dupDetector) {
 return new JsonWriteContext(type, null, dupDetector);
 }

 @Test
 public void testCreateRootContext() throws JsonProcessingException {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertNull(context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, new DupDetector() {
 @Override public boolean isDup(String name) { return name.equals("test"); }
 });
 context.writeFieldName("test");
 }

 @Test
 public void testWriteValue_RootContext() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null);
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
 }

 @Test
 public void testWriteValue_ArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 childContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.getStatus());
 }

 @Test
 public void testWriteValue_ObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 childContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.getStatus());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithoutGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null);
 context.writeValue();
 context.writeFieldName("test");
 }

 @Test
 public void testWriteFieldNameAndValue() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null);
 context.writeFieldName("test");
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }
}