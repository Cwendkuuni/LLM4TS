import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  DefaultDateTypeAdapter defaultDateTypeAdapter;

  @Before
  public void setUp() {
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void write_NullValue_WritesNullValue() {
    // Arrange
    JsonWriterStub writerStub = new JsonWriterStub();

    // Act
    defaultDateTypeAdapter.write(writerStub, null);

    // Assert
    assertEquals("null", writerStub.getWrittenValue());
  }

  @Test
  public void write_ValidDate_WritesFormattedDate() {
    // Arrange
    JsonWriterStub writerStub = new JsonWriterStub();
    Date date = new Date(2021, 6, 15);

    // Act
    defaultDateTypeAdapter.write(writerStub, date);

    // Assert
    assertEquals("July 15, 3921 12:00:00 AM", writerStub.getWrittenValue());
  }

  @Test(expected = JsonParseException.class)
  public void read_NonStringValue_ThrowsJsonParseException() {
    // Arrange
    JsonReaderStub readerStub = new JsonReaderStub();
    readerStub.setNextToken(JsonToken.NULL);

    // Act
    defaultDateTypeAdapter.read(readerStub);
  }

  @Test
  public void read_ValidDateString_ReturnsDateObject() {
    // Arrange
    JsonReaderStub readerStub = new JsonReaderStub();
    readerStub.setNextToken(JsonToken.STRING);
    readerStub.setNextValue("July 15, 3921 12:00:00 AM");

    // Act
    Date result = defaultDateTypeAdapter.read(readerStub);

    // Assert
    assertNotNull(result);
    assertEquals(new Date(2021, 6, 15), result);
  }

  @Test
  public void read_NullDateString_ReturnsNull() {
    // Arrange
    JsonReaderStub readerStub = new JsonReaderStub();
    readerStub.setNextToken(JsonToken.STRING);
    readerStub.setNextValue("null");

    // Act
    Date result = defaultDateTypeAdapter.read(readerStub);

    // Assert
    assertNull(result);
  }

  @Test
  public void read_InvalidDateString_ThrowsJsonSyntaxException() {
    // Arrange
    JsonReaderStub readerStub = new JsonReaderStub();
    readerStub.setNextToken(JsonToken.STRING);
    readerStub.setNextValue("InvalidDate");

    // Act
    try {
      defaultDateTypeAdapter.read(readerStub);
      fail("Expected JsonSyntaxException to be thrown");
    } catch (JsonSyntaxException e) {
      // Assert
      assertEquals("InvalidDate", e.getJson());
    }
  }

  @Test
  public void toString_ReturnsClassName() {
    // Act
    String result = defaultDateTypeAdapter.toString();

    // Assert
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
  }

  // Helper stubs for JsonReader and JsonWriter

  private class JsonReaderStub extends JsonReader {

    private JsonToken nextToken;
    private String nextValue;

    public JsonReaderStub() {
      super(null);
    }

    public void setNextToken(JsonToken nextToken) {
      this.nextToken = nextToken;
    }

    public void setNextValue(String nextValue) {
      this.nextValue = nextValue;
    }

    @Override
    public JsonToken peek() {
      return nextToken;
    }

    @Override
    public String nextString() {
      return nextValue;
    }
  }

  private class JsonWriterStub extends JsonWriter {

    private String writtenValue;

    public JsonWriterStub() {
      super(null);
    }

    @Override
    public void nullValue() {
      writtenValue = "null";
    }

    @Override
    public void value(String value) {
      writtenValue = value;
    }

    public String getWrittenValue() {
      return writtenValue;
    }
  }

}