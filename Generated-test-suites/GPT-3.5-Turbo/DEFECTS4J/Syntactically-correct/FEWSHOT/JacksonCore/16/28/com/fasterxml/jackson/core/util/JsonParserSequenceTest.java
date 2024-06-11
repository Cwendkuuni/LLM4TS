package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
	
	@Test
	public void testClose() throws IOException {
		JsonParser parser1 = mock(JsonParser.class);
		JsonParser parser2 = mock(JsonParser.class);
		JsonParser[] parsers = new JsonParser[] { parser1, parser2 };
		
		JsonParserSequence sequence = new JsonParserSequence(parsers);
		sequence.close();
		
		// Verify that close is called on all parsers
		Mockito.verify(parser1).close();
		Mockito.verify(parser2).close();
	}
	
	@Test
	public void testNextToken() throws IOException, JsonParseException {
		JsonToken expectedToken1 = JsonToken.START_OBJECT;
		JsonToken expectedToken2 = JsonToken.END_OBJECT;
		
		JsonParser parser1 = mock(JsonParser.class);
		when(parser1.nextToken()).thenReturn(expectedToken1);
		
		JsonParser parser2 = mock(JsonParser.class);
		when(parser2.nextToken()).thenReturn(expectedToken2);
		
		JsonParser[] parsers = new JsonParser[] { parser1, parser2 };
		
		JsonParserSequence sequence = new JsonParserSequence(parsers);
		
		// First call to nextToken should return token from parser1
		JsonToken token1 = sequence.nextToken();
		assertEquals(expectedToken1, token1);
		
		// Second call to nextToken should return token from parser2
		JsonToken token2 = sequence.nextToken();
		assertEquals(expectedToken2, token2);
	}
	
	@Test
	public void testContainedParsersCount() {
		JsonParser parser1 = mock(JsonParser.class);
		JsonParser parser2 = mock(JsonParser.class);
		JsonParser[] parsers = new JsonParser[] { parser1, parser2 };
		
		JsonParserSequence sequence = new JsonParserSequence(parsers);
		int count = sequence.containedParsersCount();
		
		// Verify that the count is correct
		assertEquals(2, count);
	}
}