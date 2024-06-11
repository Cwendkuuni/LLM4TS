
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.stream.JsonParseException;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertNull(adapter.write(null));
    }

    @Test
    public void testWrite_DateValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date date = new Date();
        String formattedDate = dateFormatter.format(date);
        assertEquals(formattedDate, adapter.write(date));
    }

    @Test
    public void testWrite_TimestampValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String formattedTimestamp = dateFormatter.format(timestamp);
        assertEquals(formattedTimestamp, adapter.write(timestamp));
    }

    @Test
    public void testWrite_SqlDateValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String formattedSqlDate = dateFormatter.format(sqlDate);
        assertEquals(formattedSqlDate, adapter.write(sqlDate));
    }

    @Test
    public void testRead_NullToken() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertNull(adapter.read(null));
    }

    @Test(expected = JsonParseException.class)
    public void testRead_NonStringToken() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(JsonToken.BOOLEAN);
    }

    @Test(expected = JsonParseException.class)
    public void testRead_InvalidDateTimeFormat() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read("2021-13-33 25:10:00");
    }

    @Test
    public void testRead_DateValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date date = new Date();
        String formattedDate = dateFormatter.format(date);
        assertEquals(date, adapter.read(formattedDate));
    }

    @Test
    public void testRead_TimestampValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String formattedTimestamp = dateFormatter.format(timestamp);
        assertEquals(timestamp, adapter.read(formattedTimestamp));
    }

    @Test
    public void testRead_SqlDateValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String formattedSqlDate = dateFormatter.format(sqlDate);
        assertEquals(sqlDate, adapter.read(formattedSqlDate));
    }
}
