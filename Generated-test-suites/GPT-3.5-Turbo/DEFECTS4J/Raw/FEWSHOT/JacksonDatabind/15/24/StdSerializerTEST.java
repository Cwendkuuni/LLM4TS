package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdSerializerTest {
  
  @Test
  public void testHandledType() {
    StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
      @Override
      public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {
      }
    };
    assertEquals(String.class, serializer.handledType());
  }
  
  @Test
  public void testGetSchema() throws JsonMappingException {
    StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
      @Override
      public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {
      }
    };
    JsonNode schema = serializer.getSchema(null, null);
    assertNotNull(schema);
    assertEquals("string", schema.get("type").asText());
  }
  
  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
      @Override
      public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {
      }
    };
    JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
    JavaType typeHint = Mockito.mock(JavaType.class);
    serializer.acceptJsonFormatVisitor(visitor, typeHint);
    Mockito.verify(visitor).expectAnyFormat(typeHint);
  }
  
  @Test
  public void testIsDefaultSerializer() {
    StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
      @Override
      public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {
      }
    };
    assertFalse(serializer.isDefaultSerializer(serializer));
  }
  
  @Test
  public void testFindConvertingContentSerializer() throws JsonMappingException {
    StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
      @Override
      public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {
      }
    };
    SerializerProvider provider = Mockito.mock(SerializerProvider.class);
    BeanProperty prop = Mockito.mock(BeanProperty.class);
    JsonSerializer<?> existingSerializer = Mockito.mock(JsonSerializer.class);
    Mockito.when(provider.getAnnotationIntrospector()).thenReturn(null);
    
    serializer.findConvertingContentSerializer(provider, prop, existingSerializer);
    
    Mockito.verify(provider, Mockito.times(2)).getAnnotationIntrospector();
  }
  
  @Test
  public void testFindPropertyFilter() throws JsonMappingException {
    StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
      @Override
      public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException, JsonGenerationException {
      }
    };
    SerializerProvider provider = Mockito.mock(SerializerProvider.class);
    Object filterId = new Object();
    Object valueToFilter = new Object();
    FilterProvider filters = Mockito.mock(FilterProvider.class);
    Mockito.when(provider.getFilterProvider()).thenReturn(filters);
    Mockito.when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(null);
    
    serializer.findPropertyFilter(provider, filterId, valueToFilter);
    
    Mockito.verify(provider).getFilterProvider();
    Mockito.verify(filters).findPropertyFilter(filterId, valueToFilter);
  }

}