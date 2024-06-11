package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private JsonWriteContext parentContext;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 dupDetector = new DupDetector();
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, parentContext, dupDetector);
 }

 @Test
 public void testCreateRootContext() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals("\"/\"", rootContext.toString());
 }

 @Test
 public void testCreateRootContextWithDupDetector() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
 assertEquals("\"/\"", rootContext.toString());
 }

 @Test
 public void testCreateChildArrayContext() throws JsonProcessingException {
 JsonWriteContext arrayContext = context.createChildArrayContext();
 assertEquals("[0]", arrayContext.toString());
 }

 @Test
 public void testCreateChildObjectContext() throws JsonProcessingException {
 JsonWriteContext objectContext = context.createChildObjectContext();
 assertEquals("{\"\":0}", objectContext.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithEmptyName() throws JsonProcessingException {
 context.writeFieldName("");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithNullName() throws JsonProcessingException {
 context.writeFieldName(null);
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 context.writeFieldName("fieldName");
 assertEquals("{\"fieldName\":0}", context.toString());
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 context.writeValue();
 assertEquals("{\"fieldName\":}", context.toString());
 }

 @Test
 public void testWriteValueInArrayContext() throws JsonProcessingException {
 JsonWriteContext arrayContext = context.createChildArrayContext();
 arrayContext.writeValue();
 assertEquals("[0]", arrayContext.toString());
 arrayContext.writeValue();
 assertEquals("[1]", arrayContext.toString());
 }

 @Test
 public void testGetParent() {
 assertNull(context.getParent());
 assertSame(parentContext, context.getParent());
 }

 @Test
 public void testGetCurrentName() {
 assertNull(context.getCurrentName());
 context.writeFieldName("fieldName");
 assertEquals("fieldName", context.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 assertSame(dupDetector, context.getDupDetector());
 }

 @Test
 public void testWithDupDetector() throws JsonProcessingException {
 JsonWriteContext newContext = context.withDupDetector(new DupDetector());
 assertSame(dupDetector, context.getDupDetector());
 assertNotSame(dupDetector, newContext.getDupDetector());
 }

 @Test
 public void testReset() throws JsonProcessingException {
 context = context.reset(JsonWriteContext.TYPE_OBJECT);
 assertEquals("{\"\":0}", context.toString());
 }
}