package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionDeserializerTest {
    
	@Test
	public void testDeserialize() throws Exception {
		CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null, null, null);
		assertEquals(null, deserializer.deserialize(null, null));
	}
	
	@Test
	public void testDeserializeWithType() throws Exception {
		CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null, null, null);
		assertEquals(null, deserializer.deserializeWithType(null, null, null));
	}
}