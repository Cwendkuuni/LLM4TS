package com.google.gson.internal.bind.test.util; // Assuming a package named com.google.gson.internal.bind.test.util is created for testing purposes

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapterConstruction() {
        DefaultDateTypeAdapter dateTypeAdapter1 = new DefaultDateTypeAdapter(Date.class);
        DefaultDateTypeAdapter dateTypeAdapter2 = new DefaultDateTypeAdapter("dd/MM/yyyy HH:mm:ss");
        DefaultDateTypeAdapter dateTypeAdapter3 = new DefaultDateTypeAdapter(Date.class, "dd/MM/yyyy HH:mm:ss");
        DefaultDateTypeAdapter dateTypeAdapter4 = new DefaultDateTypeAdapter(Date.class, Date.LENGTH);
        DefaultDateTypeAdapter dateTypeAdapter5 = new DefaultDateTypeAdapter(Date.class, Date.LENGTH, Date.LENGTH);
        DefaultDateTypeAdapter dateTypeAdapter6 = new DefaultDateTypeAdapter(Timestamp.class);
        DefaultDateTypeAdapter dateTypeAdapter7 = new DefaultDateTypeAdapter(java.sql.Date.class);

        assertEquals(Date.class, dateTypeAdapter1.dateType);
        assertEquals("dd/MM/yyyy HH:mm:ss", dateTypeAdapter2.enUsFormat.toPattern());
        assertEquals("dd/MM/yyyy HH:mm:ss", dateTypeAdapter2.localFormat.toPattern());
        assertEquals(Date.class, dateTypeAdapter3.dateType);
        assertEquals("dd/MM/yyyy HH:mm:ss", dateTypeAdapter3.enUsFormat.toPattern());
        assertEquals("dd/MM/yyyy HH:mm:ss", dateTypeAdapter3.localFormat.toPattern());
        assertEquals(Timestamp.class, dateTypeAdapter6.dateType);
        assertEquals(java.sql.Date.class, dateTypeAdapter7.dateType);
    }

    @Test
    public void testDefaultDateTypeAdapterWrite() throws IOException {
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(System.out);
        Date date = new Date();
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        gson.setLenient(true);
        gson.registerTypeAdapter(Date.class, dateTypeAdapter);
        gson.toJson(date, writer);
        writer.flush();

        String jsonDate = "\"2023-03-17 15:30:45.123\"".replaceAll("\"", "\\");
        JsonElement jsonElement = JsonParser.parseString(jsonDate);
        Date readDate = dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(jsonDate))).getAsDate();

        assertEquals(date.getTime(), readDate.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterReadDate() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        Date date = new Date();
        String jsonDate = "\"2023-03-17 15:30:45.123\"";
        JsonElement jsonElement = JsonParser.parseString(jsonDate);

        Date readDate = dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(jsonDate)));
        assertEquals(date.getTime(), readDate.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterReadTimestamp() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String jsonTimestamp = "\"2023-03-17 15:30:45.123Z\"";
        JsonElement jsonElement = JsonParser.parseString(jsonTimestamp);

        Timestamp readTimestamp = dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(jsonTimestamp)));
        assertEquals(timestamp.getTime(), readTimestamp.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterReadSqlDate() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String jsonSqlDate = "\"2023-03-17\"";
        JsonElement jsonElement = JsonParser.parseString(jsonSqlDate);

        java.sql.Date readSqlDate = dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(jsonSqlDate)));
        assertEquals(sqlDate.getTime(), readSqlDate.getTime());
    }

    @Test(expected = JsonParseException.class)
    public void testDefaultDateTypeAdapterReadInvalidJsonDate() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        String invalidJsonDate = "\"this is not a date\"";
        JsonElement jsonElement = JsonParser.parseString(invalidJsonDate);

        dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(invalidJsonDate)));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDefaultDateTypeAdapterReadInvalidDateFormat() throws ParseException {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss");

        String invalidDateFormat = "\"2023-03-17 15:30:45.123InvalidFormat\"";
        JsonElement jsonElement = JsonParser.parseString(invalidDateFormat);

        dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(invalidDateFormat)));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDefaultDateTypeAdapterReadISO8601Date() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        String iso8601Date = "\"2023-03-17T15:30:45.123Z\"";
        JsonElement jsonElement = JsonParser.parseString(iso8601Date);

        dateTypeAdapter.read(gson.newJsonReader(new JsonParser().parse(iso8601Date)));
    }

    @Test
    public void testDefaultDateTypeAdapterSerializationWithCustomFormat() {
        Gson gson = new GsonBuilder()
                .setLenient(true)
                .registerTypeAdapter(Date.class, new DefaultDateTypeAdapter("dd/MM/yyyy"))
                .create();

        Date date = new Date();
        JsonWriter writer = new JsonWriter(System.out);
        gson.toJson(date, writer);
        writer.flush();

        String jsonDate = "\"29/03/2023\"".replaceAll("\"", "\\");
        JsonElement jsonElement = JsonParser.parseString(jsonDate);
        Date readDate = new DefaultDateTypeAdapter("dd/MM/yyyy").read(gson.newJsonReader(new JsonParser().parse(jsonDate))).getAsDate();

        assertEquals(date.getTime(), readDate.getTime());
    }

    @BeforeClass
    public static void setUp() {
        Gson gson = new Gson();
        gson.registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class));
    }
}