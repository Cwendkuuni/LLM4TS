
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonToken;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter_WriteAndReadValidDate() {
        // ... (Expert 1's test case)
    }

    @Test
    public void testDefaultDateTypeAdapter_WriteAndReadInvalidDate() {
        // ... (Expert 2's test case)
    }

    @Test
    public void testDefaultDateTypeAdapter_DeserializeValidDate() {
        // ... (Expert 1's test case)
    }

    @Test
    public void testDefaultDateTypeAdapter_DeserializeInvalidDate() {
        // ... (Expert 3's test case)
    }

    @Test
    public void testDefaultDateTypeAdapter_DeserializeEmptyString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        try {
            adapter.deserializeToDate("");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Text '' is marked as unparseable"));
        }
    }

    @Test
    public void testDefaultDateTypeAdapter_DeserializeNullString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        try {
            adapter.deserializeToDate(null);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Text 'null' is marked as unparseable"));
        }
    }
}
