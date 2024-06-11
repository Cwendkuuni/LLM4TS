package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext() {
 return JsonWriteContext.createRootContext(null);
 }

 @Test
 public void testWriteFieldNameAndValue() throws JsonProcessingException {
 JsonWriteContext context = createContext();

 context.writeFieldName("name1");
 context.setCurrentValue("value1");
 context.writeValue();

 assertEquals("{\"name1\":\"value1\"}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwice() throws JsonProcessingException {
 JsonWriteContext context = createContext();

 context.writeFieldName("name1");
 context.writeFieldName("name1");
 context.setCurrentValue("value1");
 context.writeValue();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueWithoutFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.setCurrentValue("value1");
 context.writeValue();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameAndValueWithNull() throws JsonProcessingException {
 JsonWriteContext context = createContext();

 context.writeFieldName("name1");
 context.setCurrentValue(null);
 context.writeValue();
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext();
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(JsonStreamContext.TYPE_ARRAY, childContext.getType());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext();
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals(JsonStreamContext.TYPE_OBJECT, childContext.getType());
 }

 @Test
 public void testGetParentAndCurrentName() {
 JsonWriteContext context = createContext();
 context.writeFieldName("name1");
 context.setCurrentValue("value1");
 context.writeValue();
 JsonWriteContext parentContext = context.getParent();
 assertEquals(null, parentContext);
 assertEquals("name1", context.getCurrentName());
 }

 @Test
 public void testGetDupDetector() {
 JsonWriteContext context = createContext();
 context.withDupDetector(new DupDetector() {
 @Override public boolean isDup(String name) {
 return name.equals("name1");
 }
 });
 assertEquals(context.getDupDetector(), context.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDupDetector() throws JsonProcessingException {
 JsonWriteContext context = createContext();
 context.withDupDetector(new DupDetector() {
 @Override public boolean isDup(String name) {
 return name.equals("name1");
 }
 });
 context.writeFieldName("name1");
 context.writeFieldName("name1");
 context.setCurrentValue("value1");
 context.writeValue();
 }

}