import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() {
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);

    try {
      jsonTreeReader.beginArray();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testEndArray() {
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);

    try {
      jsonTreeReader.endArray();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testBeginObject() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      jsonTreeReader.beginObject();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testEndObject() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      jsonTreeReader.endObject();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testHasNext() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      boolean hasNext = jsonTreeReader.hasNext();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testPeek() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      JsonToken peekToken = jsonTreeReader.peek();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextName() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      String name = jsonTreeReader.nextName();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

    try {
      String value = jsonTreeReader.nextString();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

    try {
      boolean value = jsonTreeReader.nextBoolean();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextNull() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonNull);

    try {
      jsonTreeReader.nextNull();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextDouble() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(10.5);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

    try {
      double value = jsonTreeReader.nextDouble();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextLong() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(1000L);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

    try {
      long value = jsonTreeReader.nextLong();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testNextInt() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(100);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

    try {
      int value = jsonTreeReader.nextInt();
      // Add your assertions here
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testClose() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      jsonTreeReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testSkipValue() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      jsonTreeReader.skipValue();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testPromoteNameToValue() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    try {
      jsonTreeReader.promoteNameToValue();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add your assertions here
  }

  @Test
  public void testGetPath() {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

    String path = jsonTreeReader.getPath();
    // Add your assertions here

  }
}