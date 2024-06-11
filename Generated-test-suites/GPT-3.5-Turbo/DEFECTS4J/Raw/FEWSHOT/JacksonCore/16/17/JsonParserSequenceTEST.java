
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class JsonParserSequenceTest {

	private JsonParserSequence parserSequence;
	private JsonParser[] parsers;

	@Before
	public void setup() {
		parsers = new JsonParser[] { new JsonParserMock(), new JsonParserMock() };
		parserSequence = new JsonParserSequence(parsers);
	}

	@Test
	public void testClose() throws IOException {
		parserSequence.close();
	}

	@Test
	public void testNextToken() throws IOException {
		JsonToken token = parserSequence.nextToken();
		assertTrue(token instanceof JsonTokenMock);
		assertFalse(parserSequence.switchToNext());
	}

	@Test
	public void testContainedParsersCount() {
		int count = parserSequence.containedParsersCount();
		assertEquals(2, count);
	}
}

class JsonParserMock extends JsonParser {

	@Override
	public JsonToken nextToken() throws IOException {
		return new JsonTokenMock();
	}

	@Override
	public void close() throws IOException {

	}
}

class JsonTokenMock extends JsonToken {

}
