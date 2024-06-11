import org.junit.Assert;
import org.junit.Test;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

  // Test cases for write() method
  @Test
  public void testWrite_NullDate() {
    try {
      DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
      JsonWriter out = new JsonWriter();
      adapter.write(out, null);
      Assert.assertEquals("null", out.toString());
    } catch (IOException e) {
      Assert.fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWrite_ValidDate() {
    try {
      DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
      JsonWriter out = new JsonWriter();
      Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
      adapter.write(out, date);
      Assert.assertEquals("\"2021-01-01\"", out.toString());
    } catch (IOException | ParseException e) {
      Assert.fail("Exception should not be thrown");
    }
  }
  
  // Test cases for read() method
  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDataType() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader();
    in.beginObject();
    adapter.read(in);
  }

  @Test
  public void testRead_ValidDate() {
    try {
      DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
      JsonReader in = new JsonReader();
      in.beginObject();
      in.nextString("2021-01-01");
      Date date = adapter.read(in);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      Assert.assertEquals(2021, calendar.get(Calendar.YEAR));
      Assert.assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      Assert.assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    } catch (IOException e) {
      Assert.fail("IOException should not be thrown");
    }
  }

  // Test cases for toString() method
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Assert.assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}