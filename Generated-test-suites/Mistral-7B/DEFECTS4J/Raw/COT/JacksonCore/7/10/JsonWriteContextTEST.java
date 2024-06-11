package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertNotNull(context);
 assertNull(context.getParent());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertNotNull(context);
 assertNull(context.getParent());
 assertNotNull(context.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();

 assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
 assertNotNull(childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildObjectContext();

 assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
 assertNotNull(childContext.getParent());
 assertNull(childContext.getCurrentName());
 assertNull(childContext.getCurrentValue());
 }

 @Test
 public void testWriteFieldName_ValidName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");

 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 assertEquals("name", context.getCurrentName());
 }

 @Test
 public void testWriteFieldName_InvalidName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 assertThrows(JsonProcessingException.class, () -> context.writeFieldName("\"name\""));
 }

 @Test
 public void testWriteValue_ArrayContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeValue();

 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
 }

 @Test
 public void testWriteValue_ObjectContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();

 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }

 @Test
 public void testWriteValue_RootContext() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();

 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValue_GotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 }
}