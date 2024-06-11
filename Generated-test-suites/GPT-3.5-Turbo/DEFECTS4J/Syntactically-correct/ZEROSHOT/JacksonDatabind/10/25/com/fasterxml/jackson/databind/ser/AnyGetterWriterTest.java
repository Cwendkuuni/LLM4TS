package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AnyGetterWriterTest {

	@Test
	public void testGetAndSerialize() throws Exception {
		// Create test data
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		// Create test objects
		BeanProperty property = new BeanProperty();
		AnnotatedMember accessor = new AnnotatedMember();
		MapSerializer serializer = new MapSerializer();

		AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

		JsonGenerator gen = new JsonGenerator();
		gen.setMap(map);

		SerializerProvider provider = new SerializerProvider();

		// Test getAndSerialize method
		writer.getAndSerialize(gen, provider);

		// Check if fields are serialized correctly
		assertNotNull(gen.getMap());
		assertEquals(2, gen.getMap().size());
		assertTrue(gen.getMap().containsKey("key1"));
		assertTrue(gen.getMap().containsKey("key2"));
		assertEquals("value1", gen.getMap().get("key1"));
		assertEquals("value2", gen.getMap().get("key2"));
	}

	@Test
	public void testGetAndFilter() throws Exception {
		// Create test data
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		// Create test objects
		BeanProperty property = new BeanProperty();
		AnnotatedMember accessor = new AnnotatedMember();
		MapSerializer serializer = new MapSerializer();
		PropertyFilter filter = new PropertyFilter();

		AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

		JsonGenerator gen = new JsonGenerator();
		gen.setMap(map);

		SerializerProvider provider = new SerializerProvider();

		// Test getAndFilter method
		writer.getAndFilter(gen, provider, filter);

		// Check if fields are filtered correctly
		assertNotNull(gen.getMap());
		assertEquals(2, gen.getMap().size());
		assertTrue(gen.getMap().containsKey("key1"));
		assertTrue(gen.getMap().containsKey("key2"));
		assertEquals("value1", gen.getMap().get("key1"));
		assertEquals("value2", gen.getMap().get("key2"));
	}

	@Test
	public void testResolve() throws JsonMappingException {
		// Create test objects
		BeanProperty property = new BeanProperty();
		AnnotatedMember accessor = new AnnotatedMember();
		MapSerializer serializer = new MapSerializer();

		AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

		SerializerProvider provider = new SerializerProvider();

		// Test resolve method
		writer.resolve(provider);

		// Check if serializer is resolved correctly
		assertNotNull(writer._mapSerializer);
		assertTrue(writer._mapSerializer instanceof MapSerializer);
	}

}