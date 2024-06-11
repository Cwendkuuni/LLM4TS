package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class FromStringDeserializerTest {

  // Define a mock JsonParser and DeserializationContext for testing
  private JsonParser mockParser;
  private DeserializationContext mockContext;

  @Test
  public void testDeserialize_file() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"file.json\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.FileDeserializer deserializer = new FromStringDeserializer.FileDeserializer();
    File result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("file.json", result.getPath());
  }

  @Test
  public void testDeserialize_url() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"http://www.example.com\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.URLDeserializer deserializer = new FromStringDeserializer.URLDeserializer();
    URL result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("http://www.example.com", result.toString());
  }

  @Test
  public void testDeserialize_uri() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"http://www.example.com\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.URIDeserializer deserializer = new FromStringDeserializer.URIDeserializer();
    URI result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("http://www.example.com", result.toString());
  }

  @Test
  public void testDeserialize_class() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"java.lang.String\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.ClassDeserializer deserializer = new FromStringDeserializer.ClassDeserializer();
    Class<?> result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals(String.class, result);
  }

  @Test
  public void testDeserialize_timezone() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"GMT\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.TimeZoneDeserializer deserializer = new FromStringDeserializer.TimeZoneDeserializer();
    TimeZone result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("GMT", result.getID());
  }

  @Test
  public void testDeserialize_currency() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"USD\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.CurrencyDeserializer deserializer = new FromStringDeserializer.CurrencyDeserializer();
    Currency result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("USD", result.getCurrencyCode());
  }

  @Test
  public void testDeserialize_pattern() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"[a-z]+\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.PatternDeserializer deserializer = new FromStringDeserializer.PatternDeserializer();
    Pattern result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("[a-z]+", result.pattern());
  }

  @Test
  public void testDeserialize_locale() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"en_US\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.LocaleDeserializer deserializer = new FromStringDeserializer.LocaleDeserializer();
    Locale result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("en", result.getLanguage());
    assertEquals("US", result.getCountry());
  }

  @Test
  public void testDeserialize_charset() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"UTF-8\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.CharsetDeserializer deserializer = new FromStringDeserializer.CharsetDeserializer();
    Charset result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("UTF-8", result.name());
  }

  @Test
  public void testDeserialize_inetAddress() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"127.0.0.1\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.InetAddressDeserializer deserializer = new FromStringDeserializer.InetAddressDeserializer();
    InetAddress result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("127.0.0.1", result.getHostAddress());
  }

  @Test
  public void testDeserialize_inetSocketAddress() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"localhost:8080\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.InetSocketAddressDeserializer deserializer = new FromStringDeserializer.InetSocketAddressDeserializer();
    InetSocketAddress result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNotNull(result);
    assertEquals("localhost", result.getHostString());
    assertEquals(8080, result.getPort());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserialize_invalid() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"invalid\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.FileDeserializer deserializer = new FromStringDeserializer.FileDeserializer();
    deserializer.deserialize(mockParser, mockContext);
  }

  @Test(expected = JsonMappingException.class)
  public void testDeserialize_null() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "null";
    mockParser.setCurrentToken(JsonToken.VALUE_NULL);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.FileDeserializer deserializer = new FromStringDeserializer.FileDeserializer();
    deserializer.deserialize(mockParser, mockContext);
  }

  @Test
  public void testDeserialize_emptyString() throws Exception {
    // Create a mock JSON string and set it as the current token of the mock parser
    String json = "\"\"";
    mockParser.setCurrentToken(JsonToken.VALUE_STRING);
    mockParser.setText(json);

    // Create a FromStringDeserializer instance and call the deserialize method
    FromStringDeserializer.FileDeserializer deserializer = new FromStringDeserializer.FileDeserializer();
    File result = deserializer.deserialize(mockParser, mockContext);

    // Verify the result
    assertNull(result);
  }

}