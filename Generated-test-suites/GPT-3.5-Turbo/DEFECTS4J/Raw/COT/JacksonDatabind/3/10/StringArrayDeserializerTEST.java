
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {
  
  @Test
  public void testDeserialize() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
  
    when(jp.isExpectedStartArrayToken()).thenReturn(true);
    when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
    when(jp.getText()).thenReturn("test");
  
    String[] result = deserializer.deserialize(jp, ctxt);
  
    Assert.assertEquals(1, result.length);
    Assert.assertEquals("test", result[0]);
  }
  
  @Test
  public void testDeserializeCustom() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
    JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
  
    when(jp.isExpectedStartArrayToken()).thenReturn(true);
    when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
    when(jp.getText()).thenReturn("test");
    when(deserializer._elementDeserializer).thenReturn(elementDeserializer);
    when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("customTest");
  
    String[] result = deserializer.deserialize(jp, ctxt);
  
    Assert.assertEquals(1, result.length);
    Assert.assertEquals("customTest", result[0]);
  }
  
  @Test(expected = IOException.class)
  public void testDeserializeNonArray() throws IOException {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
  
    when(jp.isExpectedStartArrayToken()).thenReturn(false);
  
    deserializer.deserialize(jp, ctxt);
  }
  
  @Test
  public void testDeserializeNullValue() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
  
    when(jp.isExpectedStartArrayToken()).thenReturn(true);
    when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
  
    String[] result = deserializer.deserialize(jp, ctxt);
  
    Assert.assertEquals(1, result.length);
    Assert.assertNull(result[0]);
  }
  
  @Test
  public void testDeserializeEmptyStringAsNullObject() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
  
    when(jp.isExpectedStartArrayToken()).thenReturn(true);
    when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
    when(jp.getText()).thenReturn("");
    when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
  
    String[] result = deserializer.deserialize(jp, ctxt);
  
    Assert.assertNull(result);
  }
  
  @Test(expected = IOException.class)
  public void testDeserializeNonArrayNoAcceptSingleValueAsArray() throws IOException {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
  
    when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
    when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
  
    deserializer.deserialize(jp, ctxt);
  }
  
  @Test
  public void testDeserializeNonArrayWithAcceptSingleValueAsArray() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
  
    when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
    when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
    when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
  
    String[] result = deserializer.deserialize(jp, ctxt);
  
    Assert.assertEquals(1, result.length);
    Assert.assertNull(result[0]);
  }
  
  @Test
  public void testDeserializeWithType() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    JsonParser jp = mock(JsonParser.class);
    DeserializationContext ctxt = mock(DeserializationContext.class);
    TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
  
    Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
  
    Assert.assertNull(result);
  }
  
  @Test
  public void testCreateContextualWithValueDeserializer() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    DeserializationContext ctxt = mock(DeserializationContext.class);
    BeanProperty property = mock(BeanProperty.class);
    JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
  
    when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(valueDeserializer);
    when(deserializer.isDefaultDeserializer(valueDeserializer)).thenReturn(false);
  
    JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
  
    Assert.assertTrue(result instanceof StringArrayDeserializer);
    Assert.assertNotEquals(deserializer, result);
    Assert.assertEquals(valueDeserializer, ((StringArrayDeserializer)result)._elementDeserializer);
  }
  
  @Test
  public void testCreateContextualWithDefaultValueDeserializer() {
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    DeserializationContext ctxt = mock(DeserializationContext.class);
    BeanProperty property = mock(BeanProperty.class);
    JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
  
    when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(valueDeserializer);
    when(deserializer.isDefaultDeserializer(valueDeserializer)).thenReturn(true);
  
    JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
  
    Assert.assertEquals(deserializer, result);
    Assert.assertEquals(valueDeserializer, ((StringArrayDeserializer)result)._elementDeserializer);
  }
}
