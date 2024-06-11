package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Test class for JsonWriteContext
 */
public class JsonWriteContextTest {

 private ObjectMapper objectMapper;
 private JsonSerializer<Object> ser;
 private SerializerProvider provider;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 provider = new DefaultSerializerProvider();
 ser = objectMapper.getSerializer(Object.class);
 }

 @Test
 public void testCreateRootContext() throws JsonProcessingException {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 assertEquals("[]", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameNullName() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName(null);
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameDuplicateName() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("fieldName");
 context.writeFieldName("fieldName");
 fail("Expected JsonProcessingException");
 }

 @Test
 public void testWriteFieldNameValid() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("fieldName");
 assertEquals("{\"fieldName\":}", context.toString());
 }

 @Test
 public void testWriteValueNullContext() throws JsonProcessingException {
 JsonWriteContext context = null;
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_EXPECT_CONTEXT, result);
 }

 @Test
 public void testWriteValueRootContext() throws JsonProcessingException {
 JsonWriteContext context = JsonWriteContext.createRootContext();
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
 }

 @Test
 public void testWriteValueArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
 JsonWriteContext context = parentContext.createChildArrayContext();
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test
 public void testWriteValueObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext context = parentContext.createChildObjectContext();
 context.writeFieldName("fieldName");
 context.writeValue();
 int result = context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test
 public void testToString() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("fieldName");
 context.writeValue();
 assertEquals("{\"fieldName\":}", context.toString());

 JsonWriteContext arrayContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
 arrayContext.writeValue();
 arrayContext.writeValue();
 assertEquals("[,]", arrayContext.toString());
 }

 @Test
 public void testCreateChildArrayContextWithParent() throws JsonProcessingException {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
 JsonWriteContext context = parentContext.createChildArrayContext();
 assertEquals(parentContext, context.getParent());
 }

 @Test
 public void testCreateChildObjectContextWithParent() throws JsonProcessingException {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext context = parentContext.createChildObjectContext();
 assertEquals(parentContext, context.getParent());
 }

 @Test
 public void testToStringWithDupDetector() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector());
 context.writeFieldName("fieldName");
 context.writeValue();
 assertEquals("{\"fieldName\":}", context.toString());
 context.writeFieldName("fieldName");
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonGenerationException.class)
 public void testCheckDupDuplicateField() throws JsonProcessingException {
 JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector());
 context.writeFieldName("fieldName");
 context._dups.addDuplicate("fieldName");
 context.writeFieldName("fieldName");
 }
}