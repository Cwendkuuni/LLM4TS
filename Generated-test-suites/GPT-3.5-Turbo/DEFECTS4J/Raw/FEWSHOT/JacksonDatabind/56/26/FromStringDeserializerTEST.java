package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {
	@Mock
	JsonParser jsonParser;

	@Mock
	DeserializationContext deserializationContext;

	@Test
	public void testDeserialize() throws Exception {
		String value = "\"test\"";
		FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer.Std(Object.class,
				FromStringDeserializer.Std.STD_LOCALE);

		MockitoAnnotations.initMocks(this);
		Mockito.when(jsonParser.getValueAsString()).thenReturn(value);
		Object result = fromStringDeserializer.deserialize(jsonParser, deserializationContext);

		assertEquals("test", result);
	}

	@Test(expected = JsonMappingException.class)
	public void testDeserialize_EmptyString() throws Exception {
		String value = "\"\"";
		FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer.Std(Object.class,
				FromStringDeserializer.Std.STD_LOCALE);

		MockitoAnnotations.initMocks(this);
		Mockito.when(jsonParser.getValueAsString()).thenReturn(value);
		Object result = fromStringDeserializer.deserialize(jsonParser, deserializationContext);
		assertNull(result);
	}

	@Test
	public void testDeserialize_EmptyString_WithURI() throws Exception {
		String value = "\"\"";
		FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer.Std(Object.class,
				FromStringDeserializer.Std.STD_URI);

		MockitoAnnotations.initMocks(this);
		Mockito.when(jsonParser.getValueAsString()).thenReturn(value);
		Object result = fromStringDeserializer.deserialize(jsonParser, deserializationContext);
		assertEquals(URI.create(""), result);
	}

	@Test
	public void testDeserialize_StringRepresentation() throws Exception {
		String value = "\"test\"";
		FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer.Std(Object.class,
				FromStringDeserializer.Std.STD_PATTERN);

		MockitoAnnotations.initMocks(this);
		Mockito.when(jsonParser.getValueAsString()).thenReturn(value);
		Object result = fromStringDeserializer.deserialize(jsonParser, deserializationContext);

		assertEquals(Pattern.compile("test"), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeserialize_InvalidType() throws Exception {
		String value = "\"test\"";
		FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer.Std(Object.class,
				FromStringDeserializer.Std.STD_FILE);

		MockitoAnnotations.initMocks(this);
		Mockito.when(jsonParser.getValueAsString()).thenReturn(value);
		Object result = fromStringDeserializer.deserialize(jsonParser, deserializationContext);
		assertEquals(new File("test"), result);
	}

}