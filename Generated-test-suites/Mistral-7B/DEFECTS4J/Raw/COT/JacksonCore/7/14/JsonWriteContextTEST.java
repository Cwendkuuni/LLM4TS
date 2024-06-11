package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

/**
 * Unit tests for {@link JsonWriteContext} class
 */
public class JsonWriteContextTest {

 private JsonWriteContext createContext(int type, JsonWriteContext parent, DupDetector dups) {
        return new JsonWriteContext(type, parent, dups);
 }

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
        assertEquals(dupDetector, context.getDupDetector());
        assertNull(context.getParent());
 }

 @Test
 public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(parentContext, context.getParent());
        assertNull(context.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(parentContext, context.getParent());
        assertNull(context.getDupDetector());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWithNameAfterValue() {
        JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentValue("value");
        context.writeFieldName("name");
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameTwiceWithSameName() {
        JsonWriteContext context = createContext(JsonWriteContext.TYPE_OBJECT, null, new DupDetector());
        context.writeFieldName("name");
        context.writeFieldName("name");
 }

 @Test
 public void testWriteValueForRootContext() {
        JsonWriteContext context = createContext(JsonWriteContext.TYPE_ROOT, null, null);
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
 }

 @Test
 public void testWriteValueForArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
 }

 @Test
 public void testWriteValueForObjectContextWithName() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        context.writeFieldName("name");
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }

 @Test
 public void testWriteValueForObjectContextWithoutName() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.getStatus());
 }
}