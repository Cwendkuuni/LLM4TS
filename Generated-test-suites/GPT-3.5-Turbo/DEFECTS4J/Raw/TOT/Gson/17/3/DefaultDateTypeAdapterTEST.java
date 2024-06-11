import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {
  
  private DefaultDateTypeAdapter defaultDateTypeAdapter;
  
  @Before
  public void setUp() {
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
  }
  
  @Test
  public void testWrite_NullValue_WritesNullValueInJsonWriter() {
    JsonWriter jsonWriter = new JsonWriter();
    try {
      defaultDateTypeAdapter.write(jsonWriter, null);
      assertEquals("null", jsonWriter.toString());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }
  
  @Test
  public void testWrite_ValidDate_WritesFormattedDateInJsonWriter() {
    JsonWriter jsonWriter = new JsonWriter();
    try {
      DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
      Date date = dateFormat.parse("2022-01-01 12:00:00");
      String expectedDate = dateFormat.format(date);
      
      defaultDateTypeAdapter.write(jsonWriter, date);
      assertEquals(expectedDate, jsonWriter.toString());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testRead_NonStringValue_ThrowsJsonParseException() {
    JsonReader jsonReader = new JsonReader();
    jsonReader.beginArray();
    jsonReader.endArray();
    
    try {
      defaultDateTypeAdapter.read(jsonReader);
      fail("JsonParseException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (JsonParseException e) {
      // Expected JsonParseException
    }
  }
  
  @Test
  public void testRead_ValidDateTimeString_ReturnsDateObject() {
    JsonReader jsonReader = new JsonReader();
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    String dateString = "2022-01-01 12:00:00";
    
    try {
      jsonReader.beginObject();
      jsonReader.name("date");
      jsonReader.value(dateString);
      jsonReader.endObject();
      
      Date expectedDate = dateFormat.parse(dateString);
      
      Date actualDate = defaultDateTypeAdapter.read(jsonReader);
      assertEquals(expectedDate, actualDate);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testRead_ValidDateTimeStringWithMilliseconds_ReturnsDateObject() {
    JsonReader jsonReader = new JsonReader();
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    String dateString = "2022-01-01 12:00:00.123";
    
    try {
      jsonReader.beginObject();
      jsonReader.name("date");
      jsonReader.value(dateString);
      jsonReader.endObject();
      
      Date expectedDate = dateFormat.parse(dateString);
      
      Date actualDate = defaultDateTypeAdapter.read(jsonReader);
      assertEquals(expectedDate, actualDate);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch (ParseException e) {
      fail("ParseException should not be thrown");
    }
  }
  
  @Test
  public void testRead_InvalidDateTimeString_ThrowsJsonSyntaxException() {
    JsonReader jsonReader = new JsonReader();
    String invalidDateString = "2022-01-01T12:00:00Z";
    
    try {
      jsonReader.beginObject();
      jsonReader.name("date");
      jsonReader.value(invalidDateString);
      jsonReader.endObject();
      
      defaultDateTypeAdapter.read(jsonReader);
      fail("JsonSyntaxException should be thrown");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    } catch(JsonSyntaxException e) {
      // Expected JsonSyntaxException
    }
  }
  
  @Test
  public void testConstructor_InvalidDateType_ThrowsIllegalArgumentException() {
    try {
      DefaultDateTypeAdapter invalidTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Expected IllegalArgumentException
    }
  }
  
  @Test
  public void testConstructor_ValidDateType_CreatesDateTypeAdapter() {
    DefaultDateTypeAdapter validTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    assertNotNull(validTypeAdapter);
  }
  
}