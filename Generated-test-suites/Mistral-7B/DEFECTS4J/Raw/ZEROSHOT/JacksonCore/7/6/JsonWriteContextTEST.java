package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 JsonWriteContext rootContext;
 JsonWriteContext arrayContext;
 JsonWriteContext objectContext;
 JsonWriteContext objectContextWithName;
 JsonWriteContext objectContextWithValue;
 DupDetector noDupDetector;
 DupDetector withDupDetector;

 @Before
 public void setup() {
 noDupDetector = new DupDetector();
 withDupDetector = new DupDetector();
 withDupDetector.add("test", "value");
 rootContext = JsonWriteContext.createRootContext(noDupDetector);
 arrayContext = rootContext.createChildArrayContext();
 objectContext = rootContext.createChildObjectContext();
 objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_OBJECT).writeFieldName("test");
 objectContextWithValue = objectContextWithName.setCurrentValue("value");
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals("", context.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext context = arrayContext;
 assertEquals("[", context.toString());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext context = objectContext;
 assertEquals("{\\", context.toString());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testWriteFieldName() {
 JsonWriteContext context = objectContextWithName;
 int status = context.writeFieldName("test");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
 assertEquals("{\"test\":", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDuplicateFieldName() {
 JsonWriteContext context = objectContextWithName;
 context.withDupDetector(withDupDetector);
 context.writeFieldName("test");
 }

 @Test
 public void testWriteValueArrayContext() {
 JsonWriteContext context = arrayContext;
 int status = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
 assertEquals("[", rootContext.toString());
 }

 @Test
 public void testWriteValueObjectContext() {
 JsonWriteContext context = objectContextWithValue;
 int status = context.writeValue();
 assertEquals("{\"test\":value}", rootContext.toString());
 }

 @Test
 public void testWriteValueObjectContextWithNameAndValue() {
 JsonWriteContext context = objectContextWithValue;
 int status = context.writeValue();
 assertEquals("{\"test\":value}", context.toString());
 }

 @Test
 public void testReset() {
 JsonWriteContext context = objectContextWithValue.reset(JsonWriteContext.TYPE_ARRAY);
 assertEquals("[", context.toString());
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testSetDupDetector() {
 JsonWriteContext context = objectContext;
 context.withDupDetector(withDupDetector);
 }

 @Test
 public void testGetParent() {
 JsonWriteContext context = objectContextWithValue;
 assertEquals(rootContext, context.getParent());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext context = objectContextWithValue;
 DupDetector dupDetector = context.getDupDetector();
 assertEquals(noDupDetector, dupDetector);
 }
}