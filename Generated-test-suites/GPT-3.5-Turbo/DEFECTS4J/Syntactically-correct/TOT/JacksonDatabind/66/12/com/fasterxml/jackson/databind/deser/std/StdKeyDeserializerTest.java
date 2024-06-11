package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

	@Test
	public void testDeserializeKey() throws Exception {
		
		DeserializationContext ctxt = null;
		
		StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
		
		Object result = deserializer.deserializeKey("123", ctxt);
		assertEquals(123, result);
		
		result = deserializer.deserializeKey("-10", ctxt);
		assertEquals(-10, result);
		
		result = deserializer.deserializeKey("0", ctxt);
		assertEquals(0, result);
		
		StdKeyDeserializer deserializer2 = new StdKeyDeserializer(TYPE_STRING, String.class);
		
		result = deserializer2.deserializeKey("hello", ctxt);
		assertEquals("hello", result);
		
		result = deserializer2.deserializeKey("!@#$%^&*()_+?><:{}][", ctxt);
		assertEquals("!@#$%^&*()_+?><:{}][", result);
		
		StdKeyDeserializer deserializer3 = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
		
		result = deserializer3.deserializeKey("true", ctxt);
		assertEquals(true, result);
		
		result = deserializer3.deserializeKey("false", ctxt);
		assertEquals(false, result);
		
		result = deserializer3.deserializeKey("TRUE", ctxt);
		assertNull(result);
		
		result = deserializer3.deserializeKey("FALSE", ctxt);
		assertNull(result);
		
	}
	
	@Test
	public void testDeserializeKeyError() throws Exception {
		
		DeserializationContext ctxt = null;
		
		StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
		
		Object result = deserializer.deserializeKey("1234qwer", ctxt);
		assertNull(result);
		
		result = deserializer.deserializeKey("QWERTYUIOP", ctxt);
		assertNull(result);
		
		StdKeyDeserializer deserializer2 = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
		
		result = deserializer2.deserializeKey("123", ctxt);
		assertNull(result);
		
		result = deserializer2.deserializeKey("TRUEFALSE", ctxt);
		assertNull(result);
		
	}
	
	@Test
	public void testGetKeyClass() {
		
		StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
		Class<?> result = deserializer.getKeyClass();
		assertEquals(Integer.class, result);
		
		deserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
		result = deserializer.getKeyClass();
		assertEquals(String.class, result);
		
		deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
		result = deserializer.getKeyClass();
		assertEquals(Boolean.class, result);
		
	}

}