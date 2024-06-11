import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNullDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriterMock out = new JsonWriterMock();
    Date date = null;
    try {
      adapter.write(out, date);
      assertEquals(out.toString(), "null");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testWriteValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriterMock out = new JsonWriterMock();
    Date date = new Date();
    try {
      adapter.write(out, date);
      assertEquals(out.toString(), date.toString());
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testReadValidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReaderMock in = new JsonReaderMock("\"2022-07-15T12:00:00\"");
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2022-07-15T12:00:00");
    try {
      Date actualDate = adapter.read(in);
      assertEquals(expectedDate, actualDate);
    } catch (IOException | ParseException e) {
      fail("Exception should not be thrown");
    }
  }
  
  @Test
  public void testReadInvalidDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReaderMock in = new JsonReaderMock("invalidDate");
    try {
      Date actualDate = adapter.read(in);
      assertNull(actualDate);
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  // Add more test cases for other methods if needed

}