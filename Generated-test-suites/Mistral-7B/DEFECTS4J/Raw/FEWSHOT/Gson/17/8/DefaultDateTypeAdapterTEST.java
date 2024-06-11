package com.google.gson.internal.adapter;  // Assuming the test class is in this package for better organization

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapterInstance() {
        // Test default constructor
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, adapter.getType());
        assertNull(adapter.getEnUsFormat());
        assertNull(adapter.getLocalFormat());

        // Test constructor with DateType
        adapter = new DefaultDateTypeAdapter(Timestamp.class);
        assertEquals(Timestamp.class, adapter.getType());
        assertNull(adapter.getEnUsFormat());
        assertNull(adapter.getLocalFormat());

        // Test constructor with DatePattern
        adapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss.SSS z");
        assertEquals(Date.class, adapter.getType());
        assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", adapter.getEnUsFormat().toPattern());
        assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", adapter.getLocalFormat().toPattern());

        // Test constructor with dateStyle and timeStyle
        adapter = new DefaultDateTypeAdapter(Locale.US, DateFormat.SHORT, DateFormat.SHORT);
        assertEquals(Date.class, adapter.getType());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US), adapter.getEnUsFormat());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), adapter.getLocalFormat());
    }

    @Test
    public void testWrite() throws IOException {
        // Test writing a null value
        StringWriter out = new StringWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(new JsonWriter(new BufferedWriter(out)), null);
        assertEquals("null", out.toString());

        // Test writing a valid date
        Date date = new Date();
        out = new StringWriter();
        adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(new JsonWriter(new BufferedWriter(out)), date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        assertEquals(dateFormat.format(date), out.toString());
    }

    @Test
    public void testRead() throws IOException {
        // Test reading a null value
        String jsonString = "\"null\"";
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(new JsonReader(new StringReader(jsonString)));
        assertNull(date);

        // Test reading an invalid date format
        jsonString = "\"2023-02-31T12:34:56.123Z\"";  // Invalid date format as it contains 31st of February
        jsonElement = new JsonParser().parse(jsonString);
        adapter = new DefaultDateTypeAdapter(Date.class);
        try {
            date = adapter.read(new JsonReader(new StringReader(jsonString)));
            fail();
        } catch (JsonParseException e) {
            assertEquals("Unexpected token(TYPE_STRING)", e.getMessage());
        }

        // Test reading a valid date
        jsonString = "\"2023-02-28T12:34:56.123Z\"";
        jsonElement = new JsonParser().parse(jsonString);
        adapter = new DefaultDateTypeAdapter(Date.class);
        date = adapter.read(new JsonReader(new StringReader(jsonString)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        assertEquals(dateFormat.parse(jsonString), date);

        // Test reading a valid date with custom format
        jsonString = "\"12-Mar-23 12:34:56 PM\"";
        jsonElement = new JsonParser().parse(jsonString);
        adapter = new DefaultDateTypeAdapter("d-MMM-yy h:mm:ss a", Date.class);
        date = adapter.read(new JsonReader(new StringReader(jsonString)));
        assertEquals(new SimpleDateFormat("d-MMM-yy h:mm:ss a", Locale.US).parse(jsonString), date);
    }

    @Test
    public void testReadWithDifferentDateClasses() throws IOException {
        String jsonString = "\"2023-02-28T12:34:56.123Z\"";
        JsonElement jsonElement = new JsonParser().parse(jsonString);

        // Test reading against Date class
        DefaultDateTypeAdapter adapterDate = new DefaultDateTypeAdapter(Date.class);
        Date date = adapterDate.read(new JsonReader(new StringReader(jsonString)));
        assertEquals(new Date(), date);

        // Test reading against Timestamp class
        DefaultDateTypeAdapter adapterTimestamp = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = (Timestamp) adapterTimestamp.read(new JsonReader(new StringReader(jsonString)));
        assertEquals(new Timestamp(date.getTime()), timestamp);

        // Test reading against java.sql.Date class
        DefaultDateTypeAdapter adapterSqlDate = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = (java.sql.Date) adapterSqlDate.read(new JsonReader(new StringReader(jsonString)));
        assertEquals(new java.sql.Date(date.getTime()), sqlDate);
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals("DefaultDateTypeAdapter(java.text.SimpleDateFormat)", adapter.toString());
    }
}