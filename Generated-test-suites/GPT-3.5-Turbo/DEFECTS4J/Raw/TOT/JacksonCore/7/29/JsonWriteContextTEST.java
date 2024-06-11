package com.fasterxml.jackson.core.json;

import org.junit.Test;
import org.junit.Assert;

public class JsonWriteContextTest {

	  @Test
	  public void testWriteFieldName() {
	    JsonWriteContext context = JsonWriteContext.createRootContext();
	    
	    // Test initial status is as expected
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name1"));
	    
	    // Test writing a field name after a comma
	    context.writeValue();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeFieldName("name2"));
	    
	    // Test writing a field name after a colon
	    context.writeValue();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("name3"));
	    
	    // Test writing a field name after a space (in root context)
	    context.writeValue();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeFieldName("name4"));
	    
	    // Test writing a field name when a value is expected
	    context.writeFieldName("name5");
	    Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("name6"));
	    
	    // Test writing a field name when a name is expected
	    Assert.assertEquals(JsonWriteContext.STATUS_EXPECT_NAME, context.writeFieldName("name7"));
	  }
	  
	  @Test
	  public void testWriteValue() {
	    JsonWriteContext context = JsonWriteContext.createRootContext();
	    
	    // Test writing value in object context
	    context.createChildObjectContext();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
	    context.writeValue();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
	    
	    // Test writing value in array context
	    context.reset(JsonWriteContext.TYPE_ARRAY);
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
	    context.writeValue();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
	    
	    // Test writing value in root context
	    context.reset(JsonWriteContext.TYPE_ROOT);
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
	    context.writeValue();
	    Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
	  }

	  @Test
	  public void testGetCurrentName() {
	    JsonWriteContext context = JsonWriteContext.createRootContext();
	    
	    // Test current name is null
	    Assert.assertNull(context.getCurrentName());
	    
	    // Test current name is set after writing a field name
	    context.writeFieldName("name1");
	    Assert.assertEquals("name1", context.getCurrentName());
	    
	    // Test current name is reset after writing a value
	    context.writeValue();
	    Assert.assertNull(context.getCurrentName());
	  }

	  @Test
	  public void testGetDupDetector() {
	    // Test root context
	    JsonWriteContext context = JsonWriteContext.createRootContext();
	    Assert.assertNull(context.getDupDetector());
	    
	    // Test child object context
	    context.createChildObjectContext();
	    Assert.assertNotNull(context.getDupDetector());
	    
	    // Test child array context
	    context.reset(JsonWriteContext.TYPE_ARRAY);
	    Assert.assertNotNull(context.getDupDetector());
	  }

	  @Test
	  public void testGetCurrentValue() {
	    JsonWriteContext context = JsonWriteContext.createRootContext();
	    
	    // Test current value is null
	    Assert.assertNull(context.getCurrentValue());
	    
	    // Test current value is set after setCurrentValue() is called
	    String value = "value1";
	    context.setCurrentValue(value);
	    Assert.assertEquals(value, context.getCurrentValue());
	  }

	  @Test
	  public void testToString() {
	    JsonWriteContext context = JsonWriteContext.createRootContext();
	    
	    // Test toString() representation in object context
	    context.createChildObjectContext();
	    String expectedObjectString = "{\"?\"}";
	    Assert.assertEquals(expectedObjectString, context.toString());
	    
	    // Test toString() representation in array context
	    context.reset(JsonWriteContext.TYPE_ARRAY);
	    String expectedArrayString = "[0]";
	    Assert.assertEquals(expectedArrayString, context.toString());
	    
	    // Test toString() representation in root context
	    context.reset(JsonWriteContext.TYPE_ROOT);
	    String expectedRootString = "/";
	    Assert.assertEquals(expectedRootString, context.toString());
	  }
	}