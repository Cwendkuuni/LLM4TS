import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Test
    public void testWriteNullDate() throws IOException {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        // Test writing null Date
        Date date = null;
        JsonWriter out = new JsonWriter();
        defaultDateTypeAdapter.write(out, date);
        assertEquals("Should write null for null Date", "null", out.toString());
    }

    @Test
    public void testWriteDate() throws IOException {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        // Test writing Date
        Date date = new Date();
        JsonWriter out = new JsonWriter();
        defaultDateTypeAdapter.write(out, date);
        assertNotNull("Should write Date object", out.toString());
    }

    @Test
    public void testReadInvalidDate() throws IOException {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        // Test reading invalid Date format
        JsonReader in = new JsonReader(JsonToken.STRING, "invaliddate");
        try {
            defaultDateTypeAdapter.read(in);
        } catch (JsonParseException e) {
            assertNotNull("Should throw JsonParseException for invalid Date format", e);
        }
    }

    @Test
    public void testDeserializeToDate() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, "MM/dd/yyyy");

        // Test deserializing Date with custom date format
        Date date = defaultDateTypeAdapter.deserializeToDate("12/25/2022");
        assertNotNull("Should deserialize Date with custom date format", date);
    }

    @Test
    public void testToString() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        // Test toString method
        String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
        assertEquals("Should return correct string representation", expected, defaultDateTypeAdapter.toString());
    }
}