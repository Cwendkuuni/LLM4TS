package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

	@Test
	public void testCreateFlattened() {
		JsonParser first = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParser second = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
		assertEquals(2, sequence.containedParsersCount());
	}
	
	@Test
	public void testContainedParsersCount() {
		JsonParser parser1 = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParser parser2 = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {parser1, parser2});
		assertEquals(2, sequence.containedParsersCount());
	}
	
	@Test
	public void testClose() throws IOException {
		JsonParser parser1 = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParser parser2 = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {parser1, parser2});
		sequence.close();
	}
	
	@Test
	public void testNextToken() throws IOException, JsonParseException {
		JsonParser parser1 = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParser parser2 = new JsonParser() {

			@Override
			public void close() throws IOException {
			}

			@Override
			public JsonToken nextToken() throws IOException, JsonParseException {
				return null;
			}
			
		};
		
		JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {parser1, parser2});
		assertNull(sequence.nextToken());
	}
}