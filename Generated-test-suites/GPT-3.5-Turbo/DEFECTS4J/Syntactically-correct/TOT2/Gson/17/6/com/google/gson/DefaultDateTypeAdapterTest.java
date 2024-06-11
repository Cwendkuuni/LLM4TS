import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.sql.Timestamp;
import com.google.gson.DefaultDateTypeAdapter;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWriteNullDate() {
    // Test writing null date
    assertEquals("null date should result in null value in output", "null", adapter.write(null));
  }

  @Test
  public void testReadStringDate() {
    // Test reading string date
    Date date = new Date();
    assertEquals("reading string date should return valid Date object", date, adapter.read(date.toString()));
  }

  @Test
  public void testReadWrongDateFormat() {
    // Test reading date with wrong format
    assertNull("reading date with wrong format should return null", adapter.read("Invalid Date"));
  }

  @Test
  public void testReadTimestamp() {
    // Test reading Timestamp type
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    assertEquals("reading Timestamp type should return Timestamp object", timestamp, adapter.read(timestamp.toString()));
  }

  @Test
  public void testReadSQLDate() {
    // Test reading java.sql.Date type
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    assertEquals("reading java.sql.Date type should return java.sql.Date object", sqlDate, adapter.read(sqlDate.toString()));
  }

  @Test(expected = JsonParseException.class)
  public void testReadNonStringDate() {
    // Test reading non-string date
    adapter.read(123);
  }
}