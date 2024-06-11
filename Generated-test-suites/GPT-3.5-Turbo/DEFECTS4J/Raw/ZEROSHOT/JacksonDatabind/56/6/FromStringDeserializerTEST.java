package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
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

public class FromStringDeserializerTest {

  @Test
  public void testDeserialize_file() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
    DeserializationContext context = new DeserializationContext(null, null);
    File result = (File) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_url() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
    DeserializationContext context = new DeserializationContext(null, null);
    URL result = (URL) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_uri() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
    DeserializationContext context = new DeserializationContext(null, null);
    URI result = (URI) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_class() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
    DeserializationContext context = new DeserializationContext(null, null);
    Class<?> result = (Class<?>) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_javaType() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
    DeserializationContext context = new DeserializationContext(null, null);
    JavaType result = (JavaType) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_currency() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
    DeserializationContext context = new DeserializationContext(null, null);
    Currency result = (Currency) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_pattern() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
    DeserializationContext context = new DeserializationContext(null, null);
    Pattern result = (Pattern) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_locale() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
    DeserializationContext context = new DeserializationContext(null, null);
    Locale result = (Locale) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_charset() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
    DeserializationContext context = new DeserializationContext(null, null);
    Charset result = (Charset) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_timeZone() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
    DeserializationContext context = new DeserializationContext(null, null);
    TimeZone result = (TimeZone) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_inetAddress() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
    DeserializationContext context = new DeserializationContext(null, null);
    InetAddress result = (InetAddress) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test
  public void testDeserialize_inetSocketAddress() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
    DeserializationContext context = new DeserializationContext(null, null);
    InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(null, context);
    assertNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeserialize_invalidInput() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
    DeserializationContext context = new DeserializationContext(null, null);
    deserializer.deserialize(null, context);
  }

  @Test
  public void test_deserialize_fromEmptyString() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
    Object result = deserializer._deserializeFromEmptyString();
    assertNull(result);
  }

  @Test
  public void test_deserializeEmbedded_emptyString() throws IOException {
    FromStringDeserializer deserializer = new FromStringDeserializer() {
      @Override
      protected Object _deserialize(String value, DeserializationContext ctxt)
          throws IOException {
        return null;
      }

      @Override
      protected Object _deserializeEmbedded(Object ob, DeserializationContext ctxt)
          throws IOException {
        return super._deserializeEmbedded(ob, ctxt);
      }
    };
    Object ob = new Object();
    DeserializationContext context = new DeserializationContext(null, null);
    Object result = deserializer._deserializeEmbedded(ob, context);
    assertNull(result);
  }

  @Test(expected = JsonMappingException.class)
  public void test_deserializeEmbedded_exception() throws IOException {
    FromStringDeserializer deserializer = new FromStringDeserializer() {
      @Override
      protected Object _deserialize(String value, DeserializationContext ctxt)
          throws IOException {
        return null;
      }

      @Override
      protected Object _deserializeEmbedded(Object ob, DeserializationContext ctxt)
          throws IOException {
        throw new IOException("Embedded object exception");
      }
    };
    Object ob = new Object();
    DeserializationContext context = new DeserializationContext(null, null);
    deserializer._deserializeEmbedded(ob, context);
  }

  @Test
  public void test_deserializeFromEmptyString_uri() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
    Object result = deserializer._deserializeFromEmptyString();
    assertTrue(result instanceof URI);
    assertEquals("", ((URI) result).toString());
  }

  @Test
  public void test_deserializeFromEmptyString_locale() throws IOException {
    FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
    Object result = deserializer._deserializeFromEmptyString();
    assertTrue(result instanceof Locale);
    assertEquals(Locale.ROOT, result);
  }

}