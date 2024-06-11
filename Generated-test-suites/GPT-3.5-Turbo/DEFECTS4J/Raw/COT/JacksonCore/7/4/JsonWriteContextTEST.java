package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
	
	@Test
	public void testWriteFieldName() throws Exception {
		JsonWriteContext context = JsonWriteContext.createRootContext();
		int result = context.writeFieldName("field");
		assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
		
		// Edge case: Empty field name
		result = context.writeFieldName("");
		assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
	}
	
	@Test
	public void testWriteValue() {
		JsonWriteContext context = JsonWriteContext.createRootContext();
		int result = context.writeValue();
		assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
		
		// Edge case: Array context
		context = context.createChildArrayContext();
		result = context.writeValue();
		assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
		
		// Edge case: Object context
		context = context.createChildObjectContext();
		result = context.writeValue();
		assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
	}
	
	@Test
	public void testToString() {
		JsonWriteContext context = JsonWriteContext.createRootContext();
		String result = context.toString();
		assertEquals("/[0]", result);
		
		// Edge case: Object with field name
		context = context.createChildObjectContext();
		context.writeFieldName("field");
		result = context.toString();
		assertEquals("/{\"field\":0}", result);
		
		// Edge case: Empty field name
		context.writeFieldName("");
		result = context.toString();
		assertEquals("/{?:0}", result);
	}
}