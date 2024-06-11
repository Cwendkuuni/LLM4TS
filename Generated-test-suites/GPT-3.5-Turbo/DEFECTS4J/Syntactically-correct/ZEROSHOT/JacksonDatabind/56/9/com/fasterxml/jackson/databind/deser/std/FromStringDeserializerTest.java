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

import org.junit.*;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {
  
  @Test
  public void testDeserialize() throws IOException {
    // Prepare
    JsonParser parser = new JsonFactory().createParser("{\"key\":\"value\"}");
    DeserializationContext context = new DeserializationContext(new ObjectMapper(), null, null);
    
    // Test
    FromStringDeserializer deserializer = new FromStringDeserializerStub(Object.class);
    Object result = deserializer.deserialize(parser, context);

    // Verify
    assertNull(result);
  }
  
  private class FromStringDeserializerStub extends FromStringDeserializer {
    public FromStringDeserializerStub(Class valueType) {
      super(valueType);
    }
    
    @Override
    protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
      return null;
    }
  }

}