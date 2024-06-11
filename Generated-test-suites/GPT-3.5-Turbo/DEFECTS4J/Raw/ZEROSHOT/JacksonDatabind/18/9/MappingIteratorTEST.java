package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

public class MappingIteratorTest {

	@Test
	public void testHasNextValue() throws IOException {
		MappingIterator<Object> iterator = new MappingIterator<>(null, null, null, null, false, null);
		JsonParser parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return true; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void close() throws IOException { }
		};
		iterator._parser = parser;
		assertTrue(iterator.hasNextValue());
		
		iterator._parser = null;
		assertFalse(iterator.hasNextValue());
	}
	
	@Test
	public void testNextValue() throws IOException {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		JsonParser parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void clearCurrentToken() throws IOException { }
		};
		iterator._parser = parser;
		
		TestObject expected = new TestObject();
		JsonDeserializer<TestObject> deserializer = new JsonDeserializer<TestObject>() {
			@Override
			public TestObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return expected;
			}
		};
		iterator._deserializer = deserializer;
		assertEquals(expected, iterator.nextValue());
		
		iterator._updatedValue = new TestObject();
		deserializer = new JsonDeserializer<TestObject>() {
			@Override
			public void deserialize(JsonParser p, DeserializationContext ctxt, TestObject intoValue) throws IOException {
				// do nothing
			}
		};
		iterator._deserializer = deserializer;
		assertEquals(iterator._updatedValue, iterator.nextValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void testNextValueWithIOException() throws IOException {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		JsonParser parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void clearCurrentToken() throws IOException { }
		};
		iterator._parser = parser;
		
		JsonDeserializer<TestObject> deserializer = new JsonDeserializer<TestObject>() {
			@Override
			public TestObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				throw new IOException();
			}
		};
		iterator._deserializer = deserializer;
		iterator.nextValue();
	}

	@Test
	public void testReadAll() throws IOException {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		iterator._parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void clearCurrentToken() throws IOException { }
		};
		
		JsonDeserializer<TestObject> deserializer = new JsonDeserializer<TestObject>() {
			@Override
			public TestObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return new TestObject();
			}
		};
		iterator._deserializer = deserializer;
		
		List<TestObject> resultList = new ArrayList<>();
		assertEquals(resultList, iterator.readAll(resultList));
		
		List<TestObject> results = new ArrayList<>();
		results.add(new TestObject());
		iterator._deserializer = new JsonDeserializer<TestObject>() {
			@Override
			public TestObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				results.add(new TestObject());
				return null;
			}
		};
		assertEquals(results, iterator.readAll());
	}
	
	@Test(expected = JsonParseException.class)
	public void testReadAllWithIOException() throws IOException {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		iterator._parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException {
				throw new IOException();
			}
			@Override
			public void clearCurrentToken() throws IOException { }
		};
		
		JsonDeserializer<TestObject> deserializer = new JsonDeserializer<TestObject>() {
			@Override
			public TestObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return new TestObject();
			}
		};
		iterator._deserializer = deserializer;
		
		List<TestObject> resultList = new ArrayList<>();
		iterator.readAll(resultList);
	}
	
	@Test
	public void testGetParser() {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		JsonParser parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void clearCurrentToken() throws IOException { }
		};
		iterator._parser = parser;
		assertEquals(parser, iterator.getParser());
	}
	
	@Test
	public void testGetParserSchema() {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		JsonParser parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void clearCurrentToken() throws IOException { }
			@Override
			public FormatSchema getSchema() { return null; }
		};
		iterator._parser = parser;
		assertEquals(null, iterator.getParserSchema());
	}
	
	@Test
	public void testGetCurrentLocation() {
		MappingIterator<TestObject> iterator = new MappingIterator<>(null, null, null, null, false, null);
		JsonParser parser = new JsonParser() {
			@Override
			public boolean isExpectedStartArrayToken() throws IOException { return false; }
			@Override
			public JsonToken getCurrentToken() { return null; }
			@Override
			public JsonToken nextToken() throws IOException { return null; }
			@Override
			public void clearCurrentToken() throws IOException { }
			@Override
		    public JsonLocation getCurrentLocation() { return null; }
		};
		iterator._parser = parser;
		assertEquals(null, iterator.getCurrentLocation());
	}
    
    private static class TestObject { }
}