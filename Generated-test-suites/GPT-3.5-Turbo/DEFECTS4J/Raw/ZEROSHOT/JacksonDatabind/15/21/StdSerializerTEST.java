package com.fasterxml.jackson.databind.ser.std;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTest;

public class StdSerializerTest {

  private StdSerializer<StdSerializerTest> stdSerializer;

  @Before
  public void setUp() {
    stdSerializer = new StdSerializer<StdSerializerTest>(StdSerializerTest.class) {
      @Override
      public void serialize(StdSerializerTest value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        // custom serialize implementation
      }

      @Override
      public JsonNode getSchema(SerializerProvider provider, Type typeHint)
            throws JsonMappingException {
        // custom getSchema implementation
        return null;
      }

      @Override
      public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
            throws JsonMappingException {
        // custom acceptJsonFormatVisitor implementation
      }
    };
  }

  @Test
  public void testHandledType() {
    Class<StdSerializerTest> expected = StdSerializerTest.class;
    Class<StdSerializerTest> actual = stdSerializer.handledType();
    assertEquals(expected, actual);
  }
}