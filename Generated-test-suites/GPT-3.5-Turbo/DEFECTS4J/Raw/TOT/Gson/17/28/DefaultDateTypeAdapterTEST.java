import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter defaultDateTypeAdapter;
  
  @Before
  public void setUp() {
    defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullDate_JsonNull() {
    JsonWriter writer = new JsonWriter();
    defaultDateTypeAdapter.write(writer, null);
    
    assertEquals(JsonToken.NULL, writer.peek());
  }
  
  @Test
  public void testWrite_ValidDate_JsonStringWithFormattedDate() {
    JsonWriter writer = new JsonWriter();
    Date date = new Date(1220227200000L); // September 1st, 2008 00:00:00 UTC
    defaultDateTypeAdapter.write(writer, date);
    
    assertEquals(JsonToken.STRING, writer.peek());
    assertEquals("Sep 1, 2008 12:00:00 AM", writer.nextString());
  }
  
  @Test
  public void testRead_InvalidJson_ThrowsJsonParseException() {
    JsonReader reader = new JsonReader();
    reader.json("[invalid json]");
    
    assertThrows(JsonParseException.class, () -> {
      defaultDateTypeAdapter.read(reader);
    });
  }
  
  @Test
  public void testRead_NullJson_ThrowsJsonParseException() {
    JsonReader reader = new JsonReader();
    reader.json("null");
    
    assertThrows(JsonParseException.class, () -> {
      defaultDateTypeAdapter.read(reader);
    });
  }
  
  @Test
  public void testRead_ValidJsonWithDateString_ReturnsDateObject() {
    JsonReader reader = new JsonReader();
    reader.json("\"Sep 1, 2008 12:00:00 AM\"");
    
    Date date = defaultDateTypeAdapter.read(reader);
    
    assertEquals(1220227200000L, date.getTime());
  }
  
  @Test
  public void testRead_ValidJsonWithTimeString_ReturnsDateObject() {
    JsonReader reader = new JsonReader();
    reader.json("\"12:00:00 AM\"");
    
    Date date = defaultDateTypeAdapter.read(reader);
    
    assertEquals(0L, date.getTime());
  }

}