package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPreprendableDupDetector;
import com.fasterxml.jackson.core.util.DupDetector;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.JsonWriteContext.TYPE_OBJECT;
import com.fasterxml.jackson.core.JsonWriteContext.TYPE_ARRAY;

/**
 * tests for JsonWriteContext
 */
public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext arrayContext;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 rootContext = JsonWriteContext.createRootContext();
 dupDetector = new DefaultPrependableDupDetector();
 objectContext = JsonWriteContext.createRootContext(dupDetector).createChildObjectContext();
 arrayContext = JsonWriteContext.createRootContext(dupDetector).createChildArrayContext();
 }

 /**
 * Test method for {@link JsonWriteContext#writeFieldName(String)}
 */
 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 // Test writing valid field names
 objectContext.writeFieldName("field1");
 Assert.assertEquals(TYPE_OBJECT, objectContext.getParent().getType());
 Assert.assertEquals("field1", objectContext.getCurrentName());
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());

 // Test writing duplicate field names
 dupDetector.add("field1");
 objectContext.writeFieldName("field1");
 Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, objectContext.writeValue());
 Assert.assertThrows(JsonGenerationException.class, () -> objectContext.writeFieldName("field1"));
 }

 /**
 * Test method for {@link JsonWriteContext#writeValue()}
 */
 @Test
 public void testWriteValue() {
 // Test writing value in root context
 rootContext.setCurrentValue("value1");
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
 Assert.assertEquals("value1", rootContext.getCurrentValue());

 // Test writing value in object context
 objectContext.writeFieldName("field1");
 objectContext.setCurrentValue("value2");
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
 Assert.assertEquals("field1", objectContext.getCurrentName());
 Assert.assertEquals("value2", objectContext.getCurrentValue());

 // Test writing value in array context
 arrayContext.writeValue();
 Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, arrayContext.writeValue());
 }

 /**
 * Test method for {@link JsonWriteContext#getParent()}
 */
 @Test
 public void testGetParent() {
 // Test getting parent context in root context
 Assert.assertNull(rootContext.getParent());

 // Test getting parent context in object context
 Assert.assertEquals(rootContext, objectContext.getParent());
 }

 /**
 * Test method for {@link JsonWriteContext#getCurrentName()}
 */
 @Test
 public void testgetCurrentName() {
 // Test getting current name in root context
 Assert.assertNull(rootContext.getCurrentName());

 // Test getting current name in object context
 objectContext.writeFieldName("field1");
 Assert.assertEquals("field1", objectContext.getCurrentName());
 }

 /**
 * Test method for {@link JsonWriteContext#getDupDetector()}
 */
 @Test
 public void testGetDupDetector() {
 // Test getting dup detector in root context
 Assert.assertNull(rootContext.getDupDetector());

 // Test getting dup detector in object context
 Assert.assertEquals(dupDetector, objectContext.getDupDetector());
 }

 /**
 * Test method for {@link JsonWriteContext#reset(int)}
 */
 @Test
 public void testReset() {
 // Test resetting context to root context
 rootContext = rootContext.reset(JsonWriteContext.TYPE_ROOT);
 Assert.assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
 Assert.assertNull(rootContext.getCurrentName());
 Assert.assertNull(rootContext.getCurrentValue());
 Assert.assertNull(rootContext.getDupDetector());

 // Test resetting context to object context
 objectContext = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
 Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
 Assert.assertNull(objectContext.getCurrentName());
 Assert.assertNull(objectContext.getCurrentValue());
 Assert.assertEquals(dupDetector, objectContext.getDupDetector());
 }
}