package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertNull(context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithoutGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertTrue(context.isGotName());
 assertEquals("name", context.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueWithoutGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 assertFalse(context.isGotName());
 }

 @Test
 public void testWriteValueWithGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 context.writeValue();
 assertTrue(context.isGotName());
 }

 @Test
 public void testAppendDesc() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 StringBuilder sb = new StringBuilder(64);
 context.appendDesc(sb);
 assertEquals("{\"}", sb.toString());
 }
}