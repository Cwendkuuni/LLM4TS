package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;

public class JsonWriteContextTest {

 private JsonFactory jsonFactory;
 private ObjectWriter objectWriter;

 @Before
 public void setup() throws JsonProcessingException {
 jsonFactory = new JsonFactory();
 objectWriter = jsonFactory.createObjectWriter(new StringWriter());
 }

 @Test
 public void testCreateRootContext() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertNull(rootContext.getParent());
 assertNull(rootContext.getCurrentName());
 }

 @Test
 public void testCreateChildArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = JsonWriteContext.createRootContext();
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 }

 @Test
 public void testCreateChildObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = JsonWriteContext.createRootContext();
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals(parentContext, childContext.getParent());
 assertNull(childContext.getCurrentName());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 rootContext.writeFieldName("field1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, rootContext.writeValue());
 rootContext.writeFieldName("field2");
 assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, rootContext.writeValue());
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteDuplicateFieldName() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 rootContext.withDupDetector(new DupDetector() {
 @Override public boolean isDup(String name) { return name.equals("field1"); }
 });
 rootContext.writeFieldName("field1");
 rootContext.writeFieldName("field1");
 }

 @Test
 public void testWriteValueForArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = JsonWriteContext.createRootContext();
 JsonWriteContext arrayContext = parentContext.createChildArrayContext();
 arrayContext.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
 }

 @Test
 public void testWriteValueForObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = JsonWriteContext.createRootContext();
 JsonWriteContext objectContext = parentContext.createChildObjectContext();
 ObjectNode rootNode = objectWriter.writeValueAsObject(parentContext);
 objectContext.writeFieldName("field1");
 objectContext.setCurrentValue(1);
 objectContext.writeValue();
 JsonNode node = objectWriter.writeValueAsTree(objectContext);
 JsonNode field1Node = node.get(0);
 assertEquals(1, field1Node.asInt());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueAfterFieldNameExpected() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 rootContext.writeFieldName("field1");
 assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, rootContext.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueAfterNameExpected() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 rootContext.writeFieldName("field1");
 rootContext.writeValue();
 rootContext.writeFieldName("field2");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueForRootContext() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 rootContext.writeValue();
 }

 @Test
 public void testToString() throws JsonProcessingException {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals("/", rootContext.toString());
 JsonWriteContext arrayContext = rootContext.createChildArrayContext();
 assertEquals("[", arrayContext.toString());
 JsonWriteContext objectContext = rootContext.createChildObjectContext();
 assertEquals("{\"field1\":", objectContext.toString());
 }
}