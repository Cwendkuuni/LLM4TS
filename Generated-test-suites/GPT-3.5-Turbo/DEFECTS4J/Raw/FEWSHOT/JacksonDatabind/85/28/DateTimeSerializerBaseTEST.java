package com.fasterxml.jackson.databind.ser.std;

//import statements

public class DateTimeSerializerBaseTest {

  @Test
  public void testConstructor() {
    DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }
    };

    assertEquals(String.class, serializer.handledType());
    assertEquals(true, serializer._useTimestamp);
    assertNull(serializer._customFormat);
  }

  @Test
  public void testWithFormat() {
    DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }
    };

    DateTimeSerializerBase<String> newSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
    
    assertEquals(String.class, newSerializer.handledType());
    assertEquals(false, newSerializer._useTimestamp);
    assertNotNull(newSerializer._customFormat);
    assertEquals("yyyy-MM-dd", ((SimpleDateFormat) newSerializer._customFormat).toPattern());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithFormat_NullSerializerProvider() {
    DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
      @Override
      public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
      }

      @Override
      protected long _timestamp(String value) {
        return 0;
      }

      @Override
      public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      }
    };

    serializer._asTimestamp(null);
    fail("Should have thrown an IllegalArgumentException");
  }
  
  // Add tests for other methods in DateTimeSerializerBase
  
}