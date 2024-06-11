package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.*;

public class JsonWriteContextTest {

 private JsonWriteContext _context;
 private JsonFactory _factory;
 private DupDetector _dupDetector;

 @Before
 public void setUp() {
 _factory = new JsonFactory();
 _dupDetector = new SimpleDupDetector();
 _context = JsonWriteContext.createRootContext(_dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
 assertNull(rootContext.getParent());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext arrayContext = _context.createChildArrayContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
 assertEquals(_context, arrayContext.getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext objContext = _context.createChildObjectContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, objContext.getType());
 assertEquals(_context, objContext.getParent());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String name = "testName";
 _context = _context.createChildObjectContext();
 int result = _context.writeFieldName(name);
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
 assertEquals(name, _context.getCurrentName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDuplicateFieldName() throws JsonProcessingException {
 String name = "testName";
 _dupDetector.addDuplicate(name);
 _context = _context.createChildObjectContext();
 _context.writeFieldName(name);
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 _context = _context.createChildArrayContext();
 _context.writeValue();
 assertEquals(0, _context.getCurrentIndex());
 _context.writeValue();
 assertEquals(1, _context.getCurrentIndex());
 }

 @Test
 public void testWriteValueObjectContext() throws JsonProcessingException {
 _context = _context.createChildObjectContext();
 _context.writeValue();
 assertEquals(-1, _context.getCurrentIndex());
 _context.writeValue();
 assertEquals(0, _context.getCurrentIndex());
 }

 @Test
 public void testWriteValueRootContext() throws JsonProcessingException {
 _context = JsonWriteContext.createRootContext(_dupDetector);
 _context.writeValue();
 assertEquals(0, _context.getCurrentIndex());
 _context.writeValue();
 assertEquals(1, _context.getCurrentIndex());
 }

 @Test
 public void testToString() {
 _context = _context.createChildObjectContext();
 _context.writeFieldName("testName");
 _context.writeValue("testValue");
 String result = _context.toString();
 String expected = "{testName}";
 assertEquals(expected, result);
 }
}