package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createTestContext() {
 return new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 }

 @Test
 public void testWriteFieldName() throws Exception {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("fieldName");
 assertFalse(context.isGotName());
 context.writeFieldName("anotherFieldName");
 assertTrue(context.isGotName());
 context.writeValue();
 context.reset();
 context.writeFieldName("fieldName");
 context.writeValue();
 context.reset();
 context.writeFieldName("fieldName");
 context.writeValue();
 context.writeValue();
 assertNotNull(context.getCurrentValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDupDetector() throws Exception {
 JsonWriteContext context = createTestContext();
 context.withDupDetector(new DupDetector() {
 @Override public boolean isDup(String name) {
 return name.equals("fieldName");
 }
 });
 context.writeFieldName("fieldName");
 context.writeFieldName("fieldName");
 }

 @Test
 public void testGetParent() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, null);
 assertEquals(parentContext, childContext.getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("name");
 assertEquals("name", context.getCurrentName());
 }

 @Test
 public void testWriteValue_RootContext() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test
 public void testWriteValue_ObjectContext() {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("name");
 context.writeValue();
 assertEquals("{\"name\"}", context.toString());
 }

 @Test
 public void testWriteValue_ArrayContext() {
 JsonWriteContext context = new JsonWriteContext().createChildArrayContext();
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals("[, ,]", context.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext1 = parentContext.createChildArrayContext();
 JsonWriteContext childContext2 = parentContext.createChildArrayContext();
 assertNotSame(childContext1, childContext2);
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext childContext1 = parentContext.createChildObjectContext();
 JsonWriteContext childContext2 = parentContext.createChildObjectContext();
 assertNotSame(childContext1, childContext2);
 }
}