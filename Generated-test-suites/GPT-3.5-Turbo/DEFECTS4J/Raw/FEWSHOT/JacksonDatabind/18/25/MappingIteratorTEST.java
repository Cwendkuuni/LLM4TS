package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MappingIteratorTest {

	private MappingIterator<String> mappingIterator;

	@Before
	public void setup() {
		String[] values = { "foo", "bar", "baz" };

		JsonParser parser = new JsonParser() {
			private int index = 0;

			@Override
			public ObjectCodec getCodec() {
				return null;
			}

			@Override
			public void setCodec(ObjectCodec c) {
			}

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException {
				if (index < values.length) {
					index++;
					return JsonToken.VALUE_STRING;
				} else {
					return null;
				}
			}

			@Override
			public JsonToken nextValue() throws IOException {
				return null;
			}

			@Override
			public JsonToken getCurrentToken() {
				return null;
			}

			@Override
			public JsonParser setCurrentValue(Object v) {
				return null;
			}

			@Override
			public void clearCurrentToken() {
			}

			@Override
			public JsonToken getLastClearedToken() {
				return null;
			}

			@Override
			public void overrideCurrentName(String name) {
			}

			@Override
			public String getCurrentName() throws IOException {
				return null;
			}

			@Override
			public String getText() throws IOException {
				return values[index-1];
			}

			@Override
			public char[] getTextCharacters() throws IOException {
				return null;
			}

			@Override
			public boolean hasTextCharacters() {
				return false;
			}

			@Override
			public int getTextLength() throws IOException {
				return 0;
			}

			@Override
			public int getTextOffset() throws IOException {
				return 0;
			}

			@Override
			public Number getNumberValue() throws IOException {
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public NumberType getNumberType() throws IOException {
				return null;
			}

			@Override
			public int getIntValue() throws IOException {
				return 0;
			}

			@Override
			public long getLongValue() throws IOException {
				return 0;
			}

			@Override
			public boolean getBooleanValue() throws IOException {
				return false;
			}

			@Override
			public double getDoubleValue() throws IOException {
				return 0;
			}

			@Override
			public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
				return null;
			}

			@Override
			public String getValueAsString() throws IOException {
				return null;
			}

			@Override
			public String getValueAsString(String def) throws IOException {
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object getEmbeddedObject() throws IOException {
				return null;
			}

			@Override
			public byte[] getBinaryValue() throws IOException {
				return null;
			}

			@Override
			public int readBinaryValue(Base64Variant arg0, OutputStream arg1) throws IOException {
				return 0;
			}

			@Override
			public JsonLocation getTokenLocation() {
				return null;
			}

			@Override
			public JsonLocation getCurrentLocation() {
				return null;
			}

		};

		JavaType type = null;
		DeserializationContext context = null;
		JsonDeserializer<String> deserializer = null;
		boolean managedParser = true;
		Object valueToUpdate = null;

		mappingIterator = new MappingIterator<>(type, parser, context, deserializer, managedParser, valueToUpdate);
	}

	@Test
	public void testHasNextValue() throws IOException {
		assertTrue(mappingIterator.hasNextValue());
		mappingIterator.next();
		assertTrue(mappingIterator.hasNextValue());
		mappingIterator.next();
		assertTrue(mappingIterator.hasNextValue());
		mappingIterator.next();
		assertFalse(mappingIterator.hasNextValue());
	}

	@Test
	public void testNextValue() throws IOException {
		assertEquals("foo", mappingIterator.nextValue());
		assertEquals("bar", mappingIterator.nextValue());
		assertEquals("baz", mappingIterator.nextValue());
		try {
			mappingIterator.nextValue();
			fail("Expected NoSuchElementException to be thrown");
		} catch (NoSuchElementException e) {
			// expected
		}
	}

	@Test
	public void testReadAll() throws IOException {
		List<String> expectedResult = Arrays.asList("foo", "bar", "baz");
		List<String> resultList = mappingIterator.readAll();
		assertEquals(expectedResult, resultList);
	}

	@Test
	public void testReadAllWithListArgument() throws IOException {
		List<String> expectedResult = Arrays.asList("foo", "bar", "baz");
		List<String> resultList = new ArrayList<>();
		resultList = mappingIterator.readAll(resultList);
		assertEquals(expectedResult, resultList);
	}

	@Test
	public void testReadAllWithCollectionArgument() throws IOException {
		List<String> expectedResult = Arrays.asList("foo", "bar", "baz");
		List<String> resultList = new ArrayList<>();
		resultList = mappingIterator.readAll(resultList);
		assertEquals(expectedResult, resultList);
	}

	@Test
	public void testGetParser() {
		assertNull(mappingIterator.getParser());
	}

	@Test
	public void testGetParserSchema() {
		assertNull(mappingIterator.getParserSchema());
	}

	@Test
	public void testGetCurrentLocation() {
		assertNull(mappingIterator.getCurrentLocation());
	}

	@Test
	public void testRemove() {
		try {
			mappingIterator.remove();
			fail("Expected UnsupportedOperationException to be thrown");
		} catch (UnsupportedOperationException e) {
			// expected
		}
	}

	@Test
	public void testClose() throws IOException {
		mappingIterator.close();
		// Verifying that close method does not throw any exceptions
	}

}