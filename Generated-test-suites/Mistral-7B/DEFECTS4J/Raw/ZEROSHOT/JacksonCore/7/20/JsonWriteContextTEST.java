package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.util.DefaultPreprendableDupDetector;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext() {
 return JsonWriteContext.createRootContext();
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext context = createContext();
 assertEquals("[]", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithoutGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeValue("test");
 context.writeFieldName("fieldName");
 context.writeValue("value");
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeFieldName("fieldName");
 context.writeValue("value");
 assertEquals("[\"fieldName\":\"value\"]", context.toString());
 }

 @Test
 public void testWriteValueArray() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeValue(1);
 context.writeValue(2);
 context.writeValue(3);
 assertEquals("[1,2,3]", context.toString());
 }

 @Test
 public void testWriteValueObject() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeFieldName("fieldName");
 context.writeValue("value");
 assertEquals("{\"fieldName\":\"value\"}", context.toString());
 }

 @Test
 public void testWriteValueObjectWithName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeFieldName("fieldName");
 context.writeValue("value");
 context.writeFieldName("fieldName2");
 context.writeValue("value2");
 assertEquals("{\"fieldName\":\"value\",\"fieldName2\":\"value2\"}", context.toString());
 }

 @Test
 public void testWriteValueRootContext() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.writeValue("test");
 assertEquals("/test", context.toString());
 }

 @Test
 public void testWriteValueChildArrayContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext();
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 childContext.writeValue(1);
 childContext.writeValue(2);
 childContext.writeValue(3);
 parentContext.writeValue(childContext);
 assertEquals("[1,2,3]", parentContext.toString());
 }

 @Test
 public void testWriteValueChildObjectContext() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext();
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 childContext.writeFieldName("fieldName");
 childContext.writeValue("value");
 parentContext.writeValue(childContext);
 assertEquals("{\"fieldName\":\"value\"}", parentContext.toString());
 }

 @Test
 public void testWithDupDetector() {
 JsonWriteContext context = createContext();
 DupDetector dupDetector = new DefaultPrependableDupDetector();
 JsonWriteContext contextWithDupDetector = context.withDupDetector(dupDetector);
 assertEquals(context, contextWithDupDetector.reset(JsonWriteContext.TYPE_OBJECT));
 assertSame(dupDetector, contextWithDupDetector.getDupDetector());
 }

 @Test(expected = JsonGenerationException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.withDupDetector(new DefaultPrependableDupDetector());
 context.writeFieldName("fieldName");
 context.writeFieldName("fieldName");
 context.writeValue("value");
 }
}