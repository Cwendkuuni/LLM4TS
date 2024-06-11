package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;

public class JsonWriteContextTest {

 private JsonWriteContext _context;

 @Before
 public void setUp() {
 _context = JsonWriteContext.createRootContext();
 }

 @Test
 public void testWriteFieldName_NullName() {
 try {
 _context.writeFieldName(null);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @Test
 public void testWriteFieldName_ValidName() {
 String name = "testField";
 int result = _context.writeFieldName(name);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldName_DuplicateName() throws JsonProcessingException {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext contextWithDupDetector = JsonWriteContext.createRootContext(dupDetector);
 String name = "testField";
 contextWithDupDetector.writeFieldName(name);
 contextWithDupDetector.writeFieldName(name);
 }

 @Test
 public void testWriteValue_DefaultContext() {
 int result = _context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
 }

 @Test
 public void testWriteValue_ObjectContext() {
 _context = _context.createChildObjectContext();
 int result = _context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 }

 @Test
 public void testWriteValue_ArrayContext() {
 _context = _context.createChildArrayContext();
 int result = _context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteValue_NonArrayOrObjectContext() {
 _context = _context.reset(JsonWriteContext.TYPE_ROOT);
 int result = _context.writeValue();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testToString() {
 assertEquals("/", _context.toString());
 JsonWriteContext objectContext = _context.createChildObjectContext();
 assertEquals("{\\}", objectContext.toString());
 JsonWriteContext arrayContext = objectContext.createChildArrayContext();
 assertEquals("[0]", arrayContext.toString());
 }

}