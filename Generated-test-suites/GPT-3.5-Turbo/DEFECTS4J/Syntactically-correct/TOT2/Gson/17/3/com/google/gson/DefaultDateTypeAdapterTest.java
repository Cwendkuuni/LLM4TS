import static org.junit.Assert.*;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;


public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        try {
            adapter.write(jsonWriter, null);
            assertEquals("null", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWrite_ValidDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        Date date = new Date();
        
        try {
            adapter.write(jsonWriter, date);
            assertEquals("Date format as expected", adapter.enUsFormat.format(date), writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testRead_ValidDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateStr = "2021-12-01T12:00:00";
        JsonReader jsonReader = new JsonReader(new StringReader("\"" + dateStr + "\""));

        try {
            Date date = adapter.read(jsonReader);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date expectedDate = sdf.parse(dateStr);
            assertEquals(expectedDate, date);
        } catch (IOException | ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testRead_InvalidDateFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String invalidDateStr = "2021-32-01T12:00:00";
        JsonReader jsonReader = new JsonReader(new StringReader("\"" + invalidDateStr + "\""));

        try {
            Date date = adapter.read(jsonReader);
            assertNull(date); // Expecting null as date format is invalid
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testRead_InvalidDateString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String invalidDateStr = "invalid_date";
        JsonReader jsonReader = new JsonReader(new StringReader("\"" + invalidDateStr + "\""));

        assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    }

}