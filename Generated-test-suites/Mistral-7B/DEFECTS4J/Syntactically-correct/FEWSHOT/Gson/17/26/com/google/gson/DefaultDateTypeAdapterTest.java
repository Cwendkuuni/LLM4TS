package com.google.gson.internal.bind.util; // Import ISO8601Utils for testing deserialization with ISO8601 format

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    private Class<? extends Date> dateType;
    private DateFormat enUsFormat;
    private DateFormat localFormat;
    private JsonSerializationContext serializationContext;
    private JsonReader reader;
    private JsonWriter writer;
    private Gson gson;

    @Before
    public void setUp() {
        dateType = Date.class;
        enUsFormat = mock(DateFormat.class);
        localFormat = mock(DateFormat.class);
        serializationContext = new GsonBuilder().setPrettyPrinting().create();
        reader = serializationContext.newJsonReader("{}");
        writer = new JsonWriter(new StringBuilder());
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(dateType, enUsFormat, localFormat)).create();
    }

    @Test
    public void testDefaultConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType);
        assertEquals(Date.class, adapter.dateType);
        assertNotNull(adapter.enUsFormat);
        assertNotNull(adapter.localFormat);
    }

    @Test
    public void testStringConstructor() {
        String datePattern = "dd-MM-yyyy HH:mm:ss z";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateType, datePattern);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(new SimpleDateFormat(datePattern, Locale.US), adapter.enUsFormat);
        assertEquals(new SimpleDateFormat(datePattern), adapter.localFormat);
    }

    @Test
    public void testDateStyleConstructor() {
        int dateStyle = DateFormat.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, DateFormat.SHORT);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateInstance(dateStyle, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateInstance(dateStyle), adapter.localFormat);
    }

    @Test
    public void testDateTimeInstanceConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.LONG, DateFormat.LONG);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG), adapter.localFormat);
    }

    @Test
    public void testWrite() throws IOException {
        Date date = new Date();
        String expected = enUsFormat.format(date);
        writer.beginObject();
        adapter.write(writer, date);
        writer.endObject();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testReadDate() throws IOException {
        String dateString = "2022-12-31T23:59:59Z";
        Date date = adapter.read(reader);
        assertNotNull(date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString), date);
    }

    @Test
    public void testReadTimestamp() throws IOException {
        String timestampString = "2022-12-31T23:59:59.123Z";
        Timestamp timestamp = adapter.read(reader);
        assertNotNull(timestamp);
        assertEquals(new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(timestampString).getTime()), timestamp);
    }

    @Test
    public void testReadSqlDate() throws IOException {
        String sqlDateString = "2022-12-31";
        java.sql.Date sqlDate = adapter.read(reader);
        assertNotNull(sqlDate);
        assertEquals(Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(sqlDateString)), sqlDate);
    }

    @Test(expected = AssertionError.class)
    public void testUnsupportedType() throws IOException {
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextNull();
        adapter.read(reader); // Should not reach here
    }

    @Test
    public void testDeserializeToDateWithLocalFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, localFormat);
        Date date = adapter.deserializetoDate("2022-12-31");
        assertNotNull(date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-31"), date);
    }

    @Test
    public void testDeserializeToDateWithEnUsFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat);
        Date date = adapter.deserializetoDate("2022-12-31");
        assertNotNull(date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-31"), date);
    }

    @Test
    public void testDeserializeToDateWithIsoFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.deserializetoDate("2022-12-31T00:00:00Z");
        assertNotNull(date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'").parse("2022-12-31T00:00:00Z"), date);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDateWithInvalidFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.deserializetoDate("InvalidFormat");
    }

    @Test
    public void testSerialization() {
        Date date = new Date();
        JsonElement jsonElement = gson.toJsonTree(date, new TypeToken<Date>() {}.getType());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals("\"2022-12-31 15:45:23.234\"", jsonObject.get("$").getAsString());
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, localFormat);
        assertEquals("DefaultDateTypeAdapter(java.text.SimpleDateFormat[@types=[class java.text.DateFormat, class java.text.ParsePosition], defaultRaisesException=false, serialNum=-1627027399])", adapter.toString());
    }
}