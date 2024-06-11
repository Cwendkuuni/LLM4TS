import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithDateTypeAndPattern() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd/MM/yyyy");
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithDateTypeAndStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT);
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(DateFormat.SHORT, DateFormat.MEDIUM);
    assertNotNull(adapter);
  }
  
  @Test
  public void testConstructorWithDateTypeDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.MEDIUM);
    assertNotNull(adapter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithInvalidDateTypeThrowsIllegalArgumentException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(String.class);
  }
  
  @Test
  public void testWriteMethodWithNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter out = new JsonWriter(null);
    adapter.write(out, null);
    assertEquals(JsonToken.NULL, out.peek());
  }
  
  @Test
  public void testWriteMethodWithValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter out = new JsonWriter(null);
    Date date = new Date();
    adapter.write(out, date);
    assertEquals(JsonToken.STRING, out.peek());
  }
  
  @Test
  public void testReadMethodWithInvalidTokenThrowsJsonParseException() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader(null);
    in.beginObject();
    try {
      adapter.read(in);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
    in.endObject();
  }
  
  @Test
  public void testReadMethodWithValidStringToken() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader(null);
    in.beginObject();
    in.nextName();
    in.value("2022-01-01");
    in.endObject();
    Date date = adapter.read(in);
    assertNotNull(date);
  }
  
  @Test
  public void testToStringMethod() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
  
  private class JsonParseException extends RuntimeException {
    public JsonParseException(String message) {
      super(message);
    }
  }
  
  private class JsonWriter {
    private JsonToken token;
    
    public JsonWriter(JsonToken token) {
      this.token = token;
    }
    
    public void nullValue() {
      this.token = JsonToken.NULL;
    }
    
    public void value(String value) {
      this.token = JsonToken.STRING;
    }
    
    public void beginObject() {}
    
    public void nextName() {}
    
    public void endObject() {}
    
    public JsonToken peek() {
      return token;
    }
  }
  
  private class JsonReader {
    private JsonToken token;
    
    public JsonReader(JsonToken token) {
      this.token = token;
    }
    
    public void beginObject() {}
    
    public void nextName() {}
    
    public void value(String value) {}
    
    public void endObject() {}
    
    public JsonToken peek() {
      return token;
    }
  }
}