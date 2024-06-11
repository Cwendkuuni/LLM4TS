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
 assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 assertNull(context.getParent());
 assertNull(context.getCurrentName());
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
 public void testWriteFieldNameWithGotName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeFieldName("age");
 assertFalse(context.isGotName());
 context.writeValue(); // this should set gotName to false
 context.writeFieldName("name"); // should throw exception
 }

 @Test
 public void testWriteValueWithObject() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 assertEquals("{\"name\"}", context.toString());
 }

 @Test
 public void testWriteValueWithArray() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeValue();
 assertEquals("[", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 context.writeValue();
 assertEquals("[, ,]", context.toString());
 }

 @Test
 public void testWriteValueWithRoot() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 assertEquals("/", context.toString());
 context.writeValue();
 assertEquals("/ ", context.toString());
 }

 @Test
 public void testWriteFieldNameWithNullName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName(null);
 assertFalse(context.isGotName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithEmptyName() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithEmptyArray() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeFieldName("");
 }

 @Test
 public void testWriteValueWithNullValue() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue(null);
 assertEquals("{\"name\":null}", context.toString());
 }
}