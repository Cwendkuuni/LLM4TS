package com.fasterxml.jackson.core.json; // imports omitted for brevity

import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

 private DupDetector noDups = new NoOpDupDetector(); // for positive cases
 private DupDetector withDups = new DupDetector() { // for negative cases
  @Override public boolean isDup(String name) {
   return name != null && name.equals("field1");
  }
 };

 @Test
 public void testRootContext() {
  JsonWriteContext rootCtx = JsonWriteContext.createRootContext(noDups);
  rootCtx.writeValue();
  Assert.assertEquals("/", rootCtx.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
  JsonWriteContext parentCtx = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, noDups);
  JsonWriteContext arrayCtx = parentCtx.createChildArrayContext();
  arrayCtx.writeValue();
  Assert.assertEquals("[", arrayCtx.toString());

  int index = arrayCtx.getCurrentIndex();
  arrayCtx.writeValue();
  Assert.assertEquals("[,]", arrayCtx.toString());
  Assert.assertEquals(1, index);
 }

 @Test
 public void testCreateChildObjectContext() {
  JsonWriteContext parentCtx = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, noDups);
  JsonWriteContext objCtx = parentCtx.createChildObjectContext();

  objCtx.writeFieldName("field1");
  objCtx.writeValue(); // no value yet
  Assert.assertEquals("{\"field1\":}", objCtx.toString());

  objCtx.setCurrentValue("value1");
  objCtx.writeValue();
  Assert.assertEquals("{\"field1\":\"value1\"}", objCtx.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwice() {
  JsonWriteContext objCtx = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, noDups);
  objCtx.writeFieldName("field1");
  objCtx.writeFieldName("field1");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithDuplicateName() {
  JsonWriteContext objCtx = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, withDups);
  objCtx.writeFieldName("field1");
  objCtx.writeValue();
  objCtx.writeFieldName("field1");
 }

 @Test
 public void testGetParent() {
  JsonWriteContext parentCtx = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, noDups);
  JsonWriteContext childArrayCtx = parentCtx.createChildArrayContext();
  JsonWriteContext childObjCtx = childArrayCtx.createChildObjectContext();

  Assert.assertEquals(parentCtx, childArrayCtx.getParent());
  Assert.assertEquals(childArrayCtx, childObjCtx.getParent());
  Assert.assertNull(childObjCtx.getParent().getParent());
  Assert.assertEquals(parentCtx, childObjCtx.getParent());
 }

 @Test
 public void testGetDupDetector() {
  JsonWriteContext objCtx = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, noDups);
  DupDetector dupDetector = objCtx.getDupDetector();
  Assert.assertSame(noDups, dupDetector);
 }
}

class NoOpDupDetector implements DupDetector {
 @Override public boolean isDup(String name) {
  return false;
 }
}