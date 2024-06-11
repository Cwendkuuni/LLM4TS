package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.JsonWriter;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonWriteContextTest {

 private static final JsonWriteContext createRootContext(DupDetector dd) {
 return JsonWriteContext.createRootContext(dd);
 }

 @Test
 public void testRootContextWriteValue() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testRootContextWriteValueDuplicateName() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(new DupDetector() {
 @Override
 public boolean isDup(String name) {
 return "name".equals(name);
 }
 });
 rootContext.writeFieldName("name");
 rootContext.writeValue();
 }

 @Test
 public void testRootContextWriteFieldNameAndValue() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 rootContext.writeFieldName("field1");
 rootContext.setCurrentValue("value1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, rootContext.writeValue());
 }

 @Test
 public void testChildArrayContextWriteValue() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 JsonWriteContext childContext = rootContext.createChildArrayContext();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.writeValue());
 }

 @Test
 public void testChildArrayContextWriteValueMultiple() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 JsonWriteContext childContext = rootContext.createChildArrayContext();
 childContext.writeValue();
 childContext.writeValue();
 childContext.writeValue();
 }

 @Test
 public void testChildObjectContextWriteValue() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 JsonWriteContext childContext = rootContext.createChildObjectContext();
 childContext.writeFieldName("field1");
 childContext.setCurrentValue("value1");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, childContext.writeValue());
 }

 @Test
 public void testChildObjectContextWriteFieldNameAndValue() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 JsonWriteContext childContext = rootContext.createChildObjectContext();
 childContext.writeFieldName("field1");
 childContext.setCurrentValue("value1");
 childContext.writeFieldName("field2");
 childContext.setCurrentValue("value2");
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, childContext.writeValue());
 }

 @Test
 public void testErrorWriteValueRootContext() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 rootContext.writeValue();
 rootContext.writeValue(); // should throw exception
 }

 @Test
 public void testErrorWriteValueChildContext() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 JsonWriteContext childContext = rootContext.createChildArrayContext();
 childContext.writeValue();
 childContext.writeValue(); // should throw exception
 }

 @Test
 public void testToString() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 rootContext.writeFieldName("rootField1");
 rootContext.setCurrentValue(new SerializedString("rootValue1"));
 JsonWriteContext childArrayContext = rootContext.createChildArrayContext();
 childArrayContext.writeValue();
 JsonWriteContext childObjectContext = childArrayContext.createChildObjectContext();
 childObjectContext.writeFieldName("childObjectField1");
 childObjectContext.setCurrentValue(new SerializedString("childObjectValue1"));
 childObjectContext.writeFieldName("childObjectField2");
 childObjectContext.setCurrentValue(new SerializedString("childObjectValue2"));

 String expected = "{\"rootField1\"}/[\n{\"childObjectField1\"}"
 + ",\n{\"childObjectField2\"}\n]";
 String actual = rootContext.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithPrettyPrinter() throws JsonProcessingException {
 JsonWriteContext rootContext = createRootContext(null);
 rootContext.writeFieldName("rootField1");
 rootContext.setCurrentValue(new SerializedString("rootValue1"));
 JsonWriteContext childArrayContext = rootContext.createChildArrayContext();
 childArrayContext.writeValue();
 JsonWriteContext childObjectContext = childArrayContext.createChildObjectContext();
 childObjectContext.writeFieldName("childObjectField1");
 childObjectContext.setCurrentValue(new SerializedString("childObjectValue1"));
 childObjectContext.writeFieldName("childObjectField2");
 childObjectContext.setCurrentValue(new SerializedString("childObjectValue2"));

 DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
 StringWriter sw = new StringWriter();
 JsonWriter writer = Json.createWriter(sw);
 writer.setWriter(writer);
 writer.writeValue(rootContext);
 writer.flush();
 String actual = sw.toString();
 String expected = "{\"rootField1\":null,\n" +
 "[\n{\"childObjectField1\":null,\n" +
 "\"childObjectField2\":null\n}\n]}\n";
 assertEquals(expected, actual);
 }
}