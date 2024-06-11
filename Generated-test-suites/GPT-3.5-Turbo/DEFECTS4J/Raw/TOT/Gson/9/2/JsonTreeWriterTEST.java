import org.junit.Test;

public class JsonTreeWriterTest {
  
  @Test
  public void testGet() {
    // Test the get() method to ensure it returns the top level object produced by the writer
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertNull(element);
    
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    element = writer.get();
    assertTrue(element.isJsonObject());
    JsonObject object = (JsonObject) element;
    assertEquals("John Doe", object.get("name").getAsString());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGet_IncompleteDocument() {
    // Test the get() method when the document is incomplete and an IllegalStateException is expected
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    // The document is not complete, so an exception should be thrown
    writer.get();
  }
  
  @Test
  public void testBeginArray() {
    // Test the beginArray() method to ensure it starts an array and adds it to the stack
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.peek();
    assertFalse(element.isJsonNull());
    assertTrue(element.isJsonArray());
    assertEquals(1, writer.stack.size());
  }
  
  @Test
  public void testEndArray() {
    // Test the endArray() method to ensure it ends an array and removes it from the stack
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.peek();
    assertTrue(element.isJsonObject());
    JsonObject object = (JsonObject) element;
    assertTrue(object.entrySet().isEmpty());
    assertTrue(writer.stack.isEmpty());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArray_EmptyStack() {
    // Test the endArray() method when the stack is empty and an IllegalStateException is expected
    JsonTreeWriter writer = new JsonTreeWriter();
    // The stack is empty, so an exception should be thrown
    writer.endArray();
  }
  
  @Test
  public void testBeginObject() {
    // Test the beginObject() method to ensure it starts an object and adds it to the stack
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement element = writer.peek();
    assertFalse(element.isJsonNull());
    assertTrue(element.isJsonObject());
    assertEquals(1, writer.stack.size());
  }
  
  @Test
  public void testEndObject() {
    // Test the endObject() method to ensure it ends an object and removes it from the stack
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.peek();
    assertTrue(element.isJsonObject());
    JsonObject object = (JsonObject) element;
    assertTrue(object.entrySet().isEmpty());
    assertTrue(writer.stack.isEmpty());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndObject_EmptyStack() {
    // Test the endObject() method when the stack is empty and an IllegalStateException is expected
    JsonTreeWriter writer = new JsonTreeWriter();
    // The stack is empty, so an exception should be thrown
    writer.endObject();
  }
  
  @Test
  public void testName() {
    // Test the name() method to ensure it sets the pending name for the next JSON object value
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertEquals("John Doe", object.get("name").getAsString());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testName_EmptyStack() {
    // Test the name() method when the stack is empty and an IllegalStateException is expected
    JsonTreeWriter writer = new JsonTreeWriter();
    // The stack is empty, so an exception should be thrown
    writer.name("name");
  }
  
  @Test
  public void testValue_String() {
    // Test the value() method for a string value to ensure it adds the value to the JSON object
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertEquals("John Doe", object.get("name").getAsString());
  }
  
  @Test
  public void testValue_Null() {
    // Test the value() method for a null value to ensure it adds a JsonNull element to the JSON object
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertTrue(object.get("name").isJsonNull());
  }
  
  @Test
  public void testValue_Boolean() {
    // Test the value() method for a boolean value to ensure it adds the value to the JSON object
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("isAdult");
    writer.value(true);
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertTrue(object.get("isAdult").getAsBoolean());
  }
  
  @Test
  public void testValue_Double() {
    // Test the value() method for a double value to ensure it adds the value to the JSON object
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("score");
    writer.value(85.5);
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertEquals(85.5, object.get("score").getAsDouble(), 0.0001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValue_Double_Invalid() {
    // Test the value() method for an invalid double value and ensure an IllegalArgumentException is thrown
    JsonTreeWriter writer = new JsonTreeWriter();
    // NaN is not allowed in JSON, so an exception should be thrown
    writer.value(Double.NaN);
  }
  
  @Test
  public void testValue_Long() {
    // Test the value() method for a long value to ensure it adds the value to the JSON object
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("count");
    writer.value(100L);
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertEquals(100L, object.get("count").getAsLong());
  }
  
  @Test
  public void testValue_Number() {
    // Test the value() method for a Number value to ensure it adds the value to the JSON object
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("value");
    writer.value(10);
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    assertEquals(10, object.get("value").getAsInt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValue_Number_Invalid() {
    // Test the value() method for an invalid Number value and ensure an IllegalArgumentException is thrown
    JsonTreeWriter writer = new JsonTreeWriter();
    // NaN is not allowed in JSON, so an exception should be thrown
    writer.value(Double.NaN);
  }
  
  @Test
  public void testFlush() {
    // Test the flush() method to ensure it does not throw any exceptions
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
  }
  
  @Test(expected = IOException.class)
  public void testClose_IncompleteDocument() throws IOException {
    // Test the close() method when the document is incomplete and an IOException is expected
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("name").value("John Doe");
    // The document is not complete, so an exception should be thrown
    writer.close();
  }
  
  @Test
  public void testClose_CompleteDocument() throws IOException {
    // Test the close() method when the document is complete and there are no errors
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    writer.close();
    // No exceptions should be thrown
  }
  
}