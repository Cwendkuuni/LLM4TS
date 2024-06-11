package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, DupDetector dupDetector) {
 return new JsonWriteContext(type, null, dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext ctx = JsonWriteContext.createRootContext();
 assertEquals("[]", ctx.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext ctx = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("[]", ctx.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parent = createContext(JsonWriteContext.TYPE_OBJECT, null);
 JsonWriteContext child = parent.createChildArrayContext();
 assertEquals("[", child.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testCreateChildArrayContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext parent = createContext(JsonWriteContext.TYPE_OBJECT, dupDetector);
 JsonWriteContext child = parent.createChildArrayContext();
 parent.writeFieldName("field1");
 child.writeValue();
 child.writeValue();
}

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parent = createContext(JsonWriteContext.TYPE_OBJECT, null);
 JsonWriteContext child = parent.createChildObjectContext();
 assertEquals("{\"}:", child.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testCreateChildObjectContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext parent = createContext(JsonWriteContext.TYPE_OBJECT, dupDetector);
 JsonWriteContext child = parent.createChildObjectContext();
 parent.writeFieldName("field1");
 parent.writeFieldName("field1");
 child.writeValue();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext ctx = createContext(JsonWriteContext.TYPE_OBJECT, null);
 ctx.writeFieldName("field1");
 ctx.writeValue();
 assertEquals("{\"field1\":}", ctx.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameMultipleTimes() throws JsonProcessingException {
 JsonWriteContext ctx = createContext(JsonWriteContext.TYPE_OBJECT, null);
 ctx.writeFieldName("field1");
 ctx.writeFieldName("field2");
 ctx.writeValue();
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 JsonWriteContext ctx = createContext(JsonWriteContext.TYPE_ARRAY, null);
 ctx.writeValue();
 assertEquals("[", ctx.toString());
 ctx.writeValue();
 assertEquals("[,]", ctx.toString());
 ctx.writeValue();
 assertEquals("[, ,]", ctx.toString());
 }

 @Test
 public void testWriteValueObject() throws JsonProcessingException {
 JsonWriteContext ctx = createContext(JsonWriteContext.TYPE_OBJECT, null);
 ctx.writeFieldName("field1");
 ctx.writeValue("value1");
 assertEquals("{\"field1\":\"value1\"}", ctx.toString());
 }

 @Test
 public void testWriteValueArray() throws JsonProcessingException {
 JsonWriteContext ctx = createContext(JsonWriteContext.TYPE_ARRAY, null);
 ctx.writeValue(1);
 ctx.writeValue(2);
 ctx.writeValue(3);
 assertEquals("[1, 2, 3]", ctx.toString());
 }
}