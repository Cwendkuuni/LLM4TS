package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
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
 assertNull(context.getCurrentValue());
 assertEquals("", context.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertSame(dupDetector, context.getDupDetector());
 assertNull(context.getParent());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 assertEquals("", context.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertSame(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 assertEquals("[", childContext.toString());
 }

 @Test
 public void testCreateChildArrayContextWithDupDetector() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertSame(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 assertEquals("[", childContext.toString());
 assertSame(dupDetector, childContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertSame(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 assertEquals("{}", childContext.toString());
 }

 @Test
 public void testCreateChildObjectContextWithDupDetector() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertSame(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 assertEquals("{}", childContext.toString());
 assertSame(dupDetector, childContext.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithNameAlreadyUsed() {
 JerkerDupDetector dupDetector = new JerkerDupDetector(); // DupDetector implementation with known dup
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 context.writeFieldName("name");
 context.writeFieldName("name");
 }

 @Test
 public void testWriteValue() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 context.writeValue();
 assertEquals("{\"name\":}", context.toString());
 }

 @Test
 public void testWriteValueForArray() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeValue();
 context.writeValue();
 assertEquals("[,]", context.toString());
 }

 @Test
 public void testWriteValueForRootContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 context.writeValue();
 assertEquals("/,/", context.toString());
 }

}