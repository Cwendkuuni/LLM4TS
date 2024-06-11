package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

/**
 * Test class for JsonWriteContext
 */
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
 assertSame(dupDetector, context.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getDupDetector());
 }

 @Test
 public void testWriteFieldNameNullName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 try {
 context.writeFieldName(null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
}

 @Test
 public void testWriteFieldNameDuplicateName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector() {
 @Override public boolean isDup(String name) {
 return name.equals("test");
 }
 });
 try {
 context.writeFieldName("test");
 context.writeFieldName("test");
 fail("Expected JsonGenerationException");
 } catch (JsonGenerationException e) {
 // Expected
 }
 }

 @Test
 public void testWriteValueRootContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
 }

 @Test
 public void testWriteValueObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext context = parentContext.createChildObjectContext();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
 }

 @Test
 public void testWriteValueArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext context = parentContext.createChildArrayContext();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
 }
}