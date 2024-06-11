import static org.junit.Assert.*;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.sql.Timestamp;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter jsonWriter = new JsonWriter(); // create your own JsonWriter instance
        try {
            adapter.write(jsonWriter, null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        // Assert that the JsonWriter has written null value
        assertEquals(JsonToken.NULL, jsonWriter.peek());
    }

    @Test
    public void testRead_NonStringValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader jsonReader = new JsonReader(); // create your own JsonReader instance
        try {
            adapter.read(jsonReader);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
           // Assert that the correct exception is thrown
           assertTrue(e.getMessage().contains("The date should be a string value"));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testRead_InvalidDateString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader jsonReader = new JsonReader(); // create your own JsonReader instance
        jsonReader.beginObject();
        jsonReader.name("date").value("invalid_date");
        jsonReader.endObject();
        try {
            adapter.read(jsonReader);
            fail("Expected JsonSyntaxException");
        } catch (JsonSyntaxException e) {
           // Assert that the correct exception is thrown
           assertTrue(e.getMessage().contains("invalid_date"));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testRead_DeserializeToDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String validDateString = "2022-01-01T00:00:00Z"; // valid date string
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(validDateString); // expected date object
        JsonReader jsonReader = new JsonReader(); // create your own JsonReader instance
        jsonReader.value(validDateString);
        try {
            Date actualDate = adapter.read(jsonReader);
            // Assert that the actual date object matches the expected date object
            assertEquals(expectedDate, actualDate);
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }
}