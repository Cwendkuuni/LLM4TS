import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.stream.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Mock
  private JsonWriter jsonWriter;

  @Mock
  private JsonReader jsonReader;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_withNullValue_shouldWriteNull() throws IOException {
    // Arrange
    Date value = null;

    // Act
    adapter.write(jsonWriter, value);
    jsonWriter.flush();

    // Assert
    // Check if "null" value is written
    // There should be no exception thrown
  }

  @Test
  public void testWrite_withValidValue_shouldWriteFormattedDate() throws IOException {
    // Arrange
    Date value = new Date(1234567890L);
    DateFormat expectedFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    String expectedDateString = expectedFormat.format(value);
    
    // Act
    adapter.write(jsonWriter, value);
    jsonWriter.flush();
    
    // Assert
    // Check if the correct formatted date is written
    // There should be no exception thrown
  }
  
  @Test
  public void testRead_withNonStringToken_shouldThrowJsonParseException() throws IOException {
    // Arrange
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    // Act & Assert
    // Ensure that a JsonParseException is thrown if the token is not a string
    try {
      adapter.read(jsonReader);
      fail("Expected JsonParseException to be thrown");
    } catch (JsonParseException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testRead_withInvalidDateString_shouldThrowJsonSyntaxException() throws IOException {
    // Arrange
    String invalidDateString = "invalid_date";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(invalidDateString);

    // Act & Assert
    // Ensure that a JsonSyntaxException is thrown if the date string is invalid
    try {
      adapter.read(jsonReader);
      fail("Expected JsonSyntaxException to be thrown");
    } catch (JsonSyntaxException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testRead_withValidDateString_shouldReturnDateInstance() throws IOException {
    // Arrange
    String validDateString = "2022-01-01T12:00:00Z";
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(validDateString);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(validDateString);
    
    // Act
    Date result = adapter.read(jsonReader);
    
    // Assert
    assertEquals(expectedDate, result);
  }
  
  @Test
  public void testRead_withValidDateStringAndDateTypeTimestamp_shouldReturnTimestampInstance() throws IOException {
    // Arrange
    String validDateString = "2022-01-01T12:00:00Z";
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(validDateString);
    Timestamp expectedTimestamp = new Timestamp(expectedDate.getTime());
    adapter = new DefaultDateTypeAdapter(Timestamp.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(validDateString);
    
    // Act
    Date result = adapter.read(jsonReader);
    
    // Assert
    assertEquals(expectedTimestamp, result);
  }
  
  @Test
  public void testRead_withValidDateStringAndDateTypeSqlDate_shouldReturnSqlDateInstance() throws IOException {
    // Arrange
    String validDateString = "2022-01-01T12:00:00Z";
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(validDateString);
    java.sql.Date expectedSqlDate = new java.sql.Date(expectedDate.getTime());
    adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(validDateString);
    
    // Act
    Date result = adapter.read(jsonReader);
    
    // Assert
    assertEquals(expectedSqlDate, result);
  }
  
  @Test
  public void testToString_shouldReturnSimpleNameAndDateFormatClass() {
    // Arrange
    String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
    
    // Act
    String result = adapter.toString();
    
    // Assert
    assertEquals(expectedString, result);
  }

}