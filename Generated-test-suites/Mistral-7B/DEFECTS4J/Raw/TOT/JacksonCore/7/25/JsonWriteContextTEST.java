package com.fasterxml.jackson.core.json.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonWriteContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext() {
 return JsonWriteContext.createRootContext();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 JsonWriteContext ctxt = createContext();
 ctxt.writeFieldName("field1");
 ctxt.writeValue();
 ctxt.writeValue(); // no error, should be ok after comma
 ctxt.writeFieldName("field2");
 ctxt.writeValue(); // no error, should be ok after colon

 assertEquals("{field1,field2}", ctxt.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 JsonWriteContext ctxt = createContext();
 ctxt.writeFieldName("field1");
 ctxt.writeValue();
 ctxt.withDupDetector(new DupDetector());
 ctxt.writeFieldName("field1");
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parent = createContext();
 JsonWriteContext child = parent.createChildObjectContext();
 child.writeFieldName("childField1");
 child.writeValue();
 child.writeValue();

 assertEquals("{childField1,}", child.toString());
 assertEquals("{childField1,}", parent.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parent = createContext();
 JsonWriteContext child = parent.createChildArrayContext();
 child.writeValue();
 child.writeValue();
 child.writeValue();

 assertEquals("[,]", child.toString());
 assertEquals("[,]", parent.toString());
 }
}