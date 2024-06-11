import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNullDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    MockJsonWriter out = new MockJsonWriter();
    adapter.write(out, null);
    
    assertEquals(JsonToken.NULL, out.peek());
  }

  @Test
  public void testWriteDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    MockJsonWriter out = new MockJsonWriter();
    Date date = new Date();
    adapter.write(out, date);
    
    assertEquals(date, adapter.deserializeToDate(out.peek()));
  }

  @Test(expected = JsonParseException.class)
  public void testReadNonStringDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    MockJsonReader in = new MockJsonReader(JsonToken.NUMBER);
    adapter.read(in);
  }

  @Test
  public void testReadDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    String dateString = "2022-01-01";
    MockJsonReader in = new MockJsonReader(JsonToken.STRING, dateString);
    Date date = adapter.deserializeToDate(dateString);
    
    assertEquals(date, adapter.read(in));
  }
  
  @Test
  public void testReadTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    
    String dateString = "2022-01-01";
    MockJsonReader in = new MockJsonReader(JsonToken.STRING, dateString);
    Timestamp timestamp = new Timestamp(adapter.deserializeToDate(dateString).getTime());
    
    assertEquals(timestamp, adapter.read(in));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeInvalidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    adapter.deserializeToDate("invalid date");
  }

}