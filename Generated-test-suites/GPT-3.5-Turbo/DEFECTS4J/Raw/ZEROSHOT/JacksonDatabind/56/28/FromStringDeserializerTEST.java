package com.fasterxml.jackson.databind.deser.std;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {
	@Test
	public void testDeserialize_withStringValue_returnsDeserializedValue() throws IOException {
		// Arrange
		FromStringDeserializer<String> deserializer = new FromStringDeserializer<>("java.lang.String") {
			@Override
			protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
				return "Deserialized: " + value;
			}
		};
		JsonParser p = new JsonFactory().createParser("\"Test\"");
		DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new DefaultDeserializationContext.Impl(new ObjectMapper(), null));
		
		// Act
		String result = deserializer.deserialize(p, ctxt);
		
		// Assert
		Assert.assertEquals("Deserialized: Test", result);
	}
	
	@Test
	public void testDeserialize_withEmptyStringValue_returnsNull() throws IOException {
		// Arrange
		FromStringDeserializer<String> deserializer = new FromStringDeserializer<>("java.lang.String") {
			@Override
			protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
				return value.isEmpty() ? null : "Deserialized: " + value;
			}
			
			@Override
			protected String _deserializeFromEmptyString() throws IOException {
				return null;
			}
		};
		JsonParser p = new JsonFactory().createParser("\"\"");
		DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new DefaultDeserializationContext.Impl(new ObjectMapper(), null));
		
		// Act
		String result = deserializer.deserialize(p, ctxt);
		
		// Assert
		Assert.assertNull(result);
	}
}