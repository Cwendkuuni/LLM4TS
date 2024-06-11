package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
 return new JsonWriteContext(type, parent, dups);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertNull(rootContext.getParent());
 assertNull(rootContext.getCurrentName());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext arrayContext = parentContext.createChildArrayContext();
 assertEquals(parentContext, arrayContext.getParent());
 assertNull(arrayContext.getCurrentName());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 JsonWriteContext objectContext = parentContext.createChildObjectContext();
 assertEquals(parentContext, objectContext.getParent());
 assertNull(objectContext.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithGotName() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeFieldName("age");
 context.writeValue(); // reset _gotName
 context.writeFieldName("name");
}

 @Test
 public void testWriteValue_Array() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ARRAY, null, null);
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals(3, context.getCurrentIndex());
}

 @Test
 public void testWriteValue_Object() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals(1, context.getCurrentIndex());
}

 @Test
 public void testWriteValue_Root() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
 context.writeValue();
 context.writeValue();
 context.writeValue();
 assertEquals(3, context.getCurrentIndex());
}

 @Test
 public void testToString() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.writeValue("John");
 context.writeValue(42);
 assertEquals("{\"name\":42}", context.toString());
}

 @Test
 public void testWithDupDetector() {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext contextWithDupDetector = context.withDupDetector(dupDetector);
 assertEquals(context, contextWithDupDetector.getParent());
 assertEquals(dupDetector, contextWithDupDetector.getDupDetector());
}

 @Test(expected = JsonProcessingException.class)
 public void testCheckDup_DuplicateField() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector());
 context.writeFieldName("name");
 context.writeFieldName("name");
}

 @Test(expected = JsonProcessingException.class)
 public void testCheckDup_NullDupDetector() throws JsonProcessingException {
 JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
 context.writeFieldName("name");
 context.withDupDetector(null).writeFieldName("name");
}
}