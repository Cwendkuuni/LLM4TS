package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createTestContext() {
 return new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 }

 @Test
 public void testCreateRootContext() {
 assertEquals("{},", JsonWriteContext.createRootContext().toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector duplicates = new DupDetector();
 JsonWriteContext rootContext = JsonWriteContext.createRootContext(duplicates);
 assertEquals("{},", rootContext.toString());
 assertNotNull(rootContext.getDupDetector());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createTestContext();
 JsonWriteContext arrayContext = parentContext.createChildArrayContext();
 assertEquals("[]", arrayContext.toString());
 assertNull(arrayContext.getCurrentName());
 assertNull(arrayContext.getCurrentValue());
 assertEquals(parentContext, arrayContext.getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createTestContext();
 JsonWriteContext objectContext = parentContext.createChildObjectContext();
 assertEquals("{}", objectContext.toString());
 assertNull(objectContext.getCurrentValue());
 assertNull(objectContext.getCurrentName());
 assertEquals(parentContext, objectContext.getParent());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithGotNameSet() {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("name");
 context.writeFieldName("value");
 fail("writeFieldName should throw JsonProcessingException when _gotName is true.");
 }

 @Test
 public void testWriteFieldName() {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("name");
 assertEquals("{\"name\":}", context.toString());
 context.writeFieldName("value");
 assertEquals("{\"name\":\"value\"}", context.toString());
 }

 @Test
 public void testWriteValueInRootContext() {
 JsonWriteContext context = createTestContext();
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test
 public void testWriteValueInObjectContext() {
 JsonWriteContext context = createTestContext();
 context.writeFieldName("name");
 context.writeValue();
 assertEquals("{\"name\":}", context.toString());
 context.writeValue();
 assertEquals("{\"name\":\"\"}", context.toString());
 }

 @Test
 public void testWriteValueInArrayContext() {
 JsonWriteContext context = createTestContext();
 context.writeValue(); // create array context
 context.writeValue(); // write first value
 context.writeValue(); // write second value
 assertEquals("[,]", context.toString());
 context.writeValue(); // write third value
 assertEquals("[,,\"\"]", context.toString());
 }
}