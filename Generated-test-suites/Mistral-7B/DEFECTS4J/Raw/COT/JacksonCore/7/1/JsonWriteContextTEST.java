package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.*;

public class JsonWriteContextTest {

 private JsonWriteContext _context;

 @Before
 public void setUp() {
 _context = JsonWriteContext.createRootContext();
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext actual = JsonWriteContext.createRootContext();
 assertNotNull(actual);
 assertEquals(_context.getType(), JsonWriteContext.TYPE_ROOT);
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext actual = _context.createChildArrayContext();
 assertNotNull(actual);
 assertEquals(JsonWriteContext.TYPE_ARRAY, actual.getType());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext actual = _context.createChildObjectContext();
 assertNotNull(actual);
 assertEquals(JsonWriteContext.TYPE_OBJECT, actual.getType());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWhenGotNameIsTrue() {
 _context.writeFieldName("name");
 _context.writeFieldName("value"); // should throw an exception
 }

 @Test
 public void testWriteValue_RootContext() {
 _context.writeValue();
 int actual = _context.getIndex();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, _context.getStatus());
 assertEquals(0, actual);
 }

 @Test
 public void testWriteValue_ObjectContext() {
 _context = JsonWriteContext.createRootContext().createChildObjectContext();
 _context.writeFieldName("name");
 _context.writeValue();
 int actual = _context.getIndex();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, _context.getStatus());
 assertEquals(1, actual);
 }

 @Test
 public void testWriteValue_ArrayContext() {
 _context = JsonWriteContext.createRootContext().createChildArrayContext();
 _context.writeValue();
 _context.writeValue();
 int actual = _context.getIndex();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, _context.getStatus());
 assertEquals(1, actual);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueWhenGotNameIsTrue() {
 _context = JsonWriteContext.createRootContext().createChildObjectContext();
 _context.writeFieldName("name");
 _context.writeValue(); // should throw an exception
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteFieldNameWithDupDetector() {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext(dupDetector);
 jsonWriteContext.writeFieldName("name");
 jsonWriteContext.writeFieldName("name");
 }

}