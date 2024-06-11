import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        // Test writing a Date object
        Date date = new Date();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        
        try {
            adapter.write(jsonWriter, date);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testRead() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        // Test reading a valid date string
        String dateString = "2021-10-01 12:00:00";
        JsonReader jsonReader = new JsonReader(new StringReader("\"" + dateString + "\""));
        
        try {
            Date result = adapter.read(jsonReader);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        // Test reading an invalid date string
        String invalidDateString = "invalid";
        jsonReader = new JsonReader(new StringReader("\"" + invalidDateString + "\""));
        
        try {
            Date result = adapter.read(jsonReader);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
        assertEquals(expected, adapter.toString());
    }

    @Test
    public void testEdgeCases() {
        // Test for a null Date value
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        try {
            adapter.write(jsonWriter, null);
            assertEquals("null", stringWriter.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test(expected = JsonParseException.class)
    public void testExceptionHandling() {
        // Test for invalid date format causing JsonParseException
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String invalidDateString = "invalid";
        JsonReader jsonReader = new JsonReader(new StringReader("\"" + invalidDateString + "\""));
        
        try {
            adapter.read(jsonReader);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}