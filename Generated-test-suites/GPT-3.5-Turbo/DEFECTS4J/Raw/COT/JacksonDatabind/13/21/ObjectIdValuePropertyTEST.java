
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdValuePropertyTest {
	
	@Test
	public void testConstructor() {
		ObjectIdReader objectIdReader = new ObjectIdReader();
		PropertyMetadata metadata = new PropertyMetadata();
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
		
		assertEquals(objectIdReader.propertyName, property.getName());
		assertEquals(objectIdReader.getIdType(), property.getType());
		assertEquals(metadata, property.getMetadata());
	}
	
	@Test
	public void testWithDeser() {
		ObjectIdValueProperty src = new ObjectIdValueProperty();
		JsonDeserializer<?> deser = new JsonDeserializer<>();
		ObjectIdValueProperty property = src.withValueDeserializer(deser);
		
		assertEquals(src, property);
		assertEquals(deser, property.getValueDeserializer());
	}
	
	@Test
	public void testWithName() {
		ObjectIdValueProperty src = new ObjectIdValueProperty();
		PropertyName newName = new PropertyName("new");
		ObjectIdValueProperty property = src.withName(newName);
		
		assertEquals(src, property);
		assertEquals(newName, property.getName());
	}
	
	@Test
	public void testDeserializeAndSet() {
		// TODO: Test method functionality
	}
	
	@Test
	public void testDeserializeSetAndReturn() {
		// TODO: Test method functionality
	}
	
	@Test
	public void testSet() {
		// TODO: Test method functionality
	}
	
	@Test
	public void testSetAndReturn() {
		// TODO: Test method functionality
	}

}
