
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for FromStringDeserializer class
 */
public class FromStringDeserializerTest {

  private DeserializationContext ctxt;

  @Before
  public void setUp() {
    ctxt = new DeserializationContext(null, null, null);
  }

  @Test
  public void testDeserialize_File() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
    File result = (File) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (File) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (File) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_URL() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
    URL result = (URL) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (URL) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (URL) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_URI() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
    URI result = (URI) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (URI) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (URI) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_Class() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
    Class<?> result = (Class<?>) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (Class<?>) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (Class<?>) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_JavaType() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
    JavaType result = (JavaType) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (JavaType) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (JavaType) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_Currency() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
    Currency result = (Currency) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (Currency) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (Currency) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_Pattern() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
    Pattern result = (Pattern) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (Pattern) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (Pattern) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_Locale() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
    Locale result = (Locale) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (Locale) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (Locale) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_Charset() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
    Charset result = (Charset) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (Charset) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (Charset) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_TimeZone() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
    TimeZone result = (TimeZone) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (TimeZone) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (TimeZone) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_InetAddress() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
    InetAddress result = (InetAddress) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (InetAddress) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (InetAddress) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testDeserialize_InetSocketAddress() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
    InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(null, ctxt);
    assertNull(result);

    result = (InetSocketAddress) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);

    result = (InetSocketAddress) deserializer.deserialize(ctxt.getParser(), ctxt);
    assertNull(result);
  }

  @Test
  public void testStdConstructor() {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
    assertNotNull(deserializer);
    assertEquals(File.class, deserializer._valueClass);
    assertEquals(FromStringDeserializer.Std.STD_FILE, deserializer._kind);
  }
}
