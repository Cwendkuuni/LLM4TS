
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  DefaultDateTypeAdapter adapter;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWriteNullDate() {
    // Arrange
    JsonWriter writer = mock(JsonWriter.class);

    // Act
    adapter.write(writer, null);

    // Assert
    verify(writer).nullValue();
  }

  @Test
  public void testWriteDate() {
    // Arrange
    JsonWriter writer = mock(JsonWriter.class);
    Date date = new Date();

    // Act
    adapter.write(writer, date);

    // Assert
    verify(writer).value(anyString());
  }

  @Test(expected = JsonParseException.class)
  public void testReadNonStringDate() throws IOException {
    // Arrange
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BOOLEAN);

    // Act
    adapter.read(reader);
  }

  @Test
  public void testReadValidDate() throws IOException {
    // Arrange
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("2020-01-01");

    // Act
    Date date = adapter.read(reader);

    // Assert
    assertNotNull(date);
  }

  @Test
  public void testToString() {
    // Arrange
    String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";

    // Act
    String result = adapter.toString();

    // Assert
    assertEquals(expectedString, result);
  }

}
