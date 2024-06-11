package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

/**
 * Test class for JsonWriteContext.
 */
public class JsonWriteContextTest {

 private JsonFactory jsonFactory;
 private JsonWriterFactory writerFactory;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 writerFactory = jsonFactory.getSerializerProvider().getWriterFactory();
 }

 // ------------------ TESTS FOR CONSTRUCTORS ------------------

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
 assertNull(context.getParent());
 assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateRootContextWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
 assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
 assertNull(context.getParent());
 assertEquals(dupDetector, context.getDupDetector());
 }

 // ------------------ TESTS FOR getters ------------------

 @Test
 public void testGetParent() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getParent().getParent());
 }

 @Test
 public void testGetCurrentName() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertEquals("name", context.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testGetCurrentNameAfterWriteValue() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeValue();
 context.writeFieldName("name");
 context.writeValue();
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 DupDetector dupDetector = new DupDetector();
 context = context.withDupDetector(dupDetector);
 assertEquals(dupDetector, context.getDupDetector());
 }

 // ------------------ TESTS FOR writeFieldName() ------------------

 @Test
 public void testWriteFieldNameNullName() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 assertFalse(context.writeFieldName(null));
 }

 @Test
 public void testWriteFieldNameEmptyName() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 assertFalse(context.writeFieldName(""));
 }

 @Test
 public void testWriteFieldNameSingleCharacterName() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("a");
 assertTrue(context.writeValue());
 }

 @Test
 public void testWriteFieldNameMultipleCharacterName() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertTrue(context.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameDupName() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 context.writeFieldName("name");
 }

 // ------------------ TESTS FOR writeValue() ------------------

 @Test
 public void testWriteValueRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test
 public void testWriteValueArrayContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext context = parentContext.createChildArrayContext();
 context.writeValue();
 assertEquals("[", context.toString());
 context.writeValue();
 assertEquals("[,", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 }

 @Test
 public void testWriteValueObjectContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
 JsonWriteContext context = parentContext.createChildObjectContext();
 context.writeFieldName("name");
 context.writeValue();
 context.writeFieldName("age");
 context.writeValue();
 assertEquals("{\"name\":", context.toString());
 assertEquals("{\"name\":1,", context.writeValue(1));
 assertEquals("{\"name\":1,\"age\":}", context.toString());
 }

 // ------------------ TESTS FOR appendDesc() ------------------

 @Test
 public void testAppendDescRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 StringBuilder sb = new StringBuilder(64);
 context.appendDesc(sb);
 assertEquals("/", sb.toString());
 }

 @Test
 public void testAppendDescArrayContext() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
 StringBuilder sb = new StringBuilder(64);
 context.appendDesc(sb);
 assertEquals("[", sb.toString());
 }

 @Test
 public void testAppendDescObjectContext() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 StringBuilder sb = new StringBuilder(64);
 context.appendDesc(sb);
 assertEquals("{", sb.toString());
 context.writeFieldName("name");
 context.appendDesc(sb);
 assertEquals("{\"name\":", sb.toString());
 }

 // ------------------ TESTS FOR toString() ------------------

 @Test
 public void testToStringRootContext() {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals("/", context.toString());
 }

 @Test
 public void testToStringArrayContext() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
 assertEquals("[]", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 }

 @Test
 public void testToStringObjectContext() {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 assertEquals("{\"name\":}", context.toString());
 context.writeValue();
 assertEquals("{\"name\":1}", context.toString());
 context.writeFieldName("age");
 assertEquals("{\"name\":1,\"age\":}", context.toString());
 }

}