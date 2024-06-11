package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 context = JsonWriteContext.createRootContext(dupDetector = new EmptyDupDetector());
 }

 @Test
 public void testRootContextWriteValue() throws JsonProcessingException {
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testRootContextWriteFieldNameBeforeValue() throws JsonProcessingException {
 context.writeFieldName("name");
 context.writeValue();
 }

 @Test
 public void testRootContextWriteFieldNameAndValue() throws JsonProcessingException {
 context.writeFieldName("name");
 context.setCurrentValue("value");
 context.writeValue();
 assertEquals("{\"name\":\"value\"}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testRootContextWriteFieldNameTwice() throws JsonProcessingException {
 context.writeFieldName("name");
 context.writeFieldName("name");
 }

 @Test
 public void testChildArrayContextWriteValue() throws JsonProcessingException {
 context = context.createChildArrayContext();
 context.writeValue();
 assertEquals("[", context.toString());
 context.writeValue();
 assertEquals("[,", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 }

 @Test
 public void testChildArrayContextWriteFieldNameAndValue() throws JsonProcessingException {
 context = context.createChildArrayContext();
 context.writeFieldName("name");
 context.setCurrentValue("value");
 context.writeValue();
 assertEquals("[,]", context.toString());
 context.writeValue();
 assertEquals("[,]", context.toString());
 context.writeValue();
 assertEquals("[,\"name\":\"value\"]", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testChildArrayContextWriteFieldNameBeforeValue() throws JsonProcessingException {
 context = context.createChildArrayContext();
 context.writeFieldName("name");
 context.writeValue();
 }

 @Test
 public void testChildObjectContextWriteValue() throws JsonProcessingException {
 context = context.createChildObjectContext();
 context.writeValue();
 assertEquals("{\"}", context.toString());
 }

 @Test
 public void testChildObjectContextWriteFieldNameAndValue() throws JsonProcessingException {
 context = context.createChildObjectContext();
 context.writeFieldName("name");
 context.setCurrentValue("value");
 context.writeValue();
 assertEquals("{\"name\":\"value\"}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testChildObjectContextWriteFieldNameBeforeValue() throws JsonProcessingException {
 context = context.createChildObjectContext();
 context.writeFieldName("name");
 context.writeValue();
 }
}

class EmptyDupDetector implements DupDetector {
 @Override public boolean isDup(String name) {
 return false;
 }

 @Override public DupDetector child() {
 return this;
 }

 @Override public void reset() {
 // no-op
 }
}