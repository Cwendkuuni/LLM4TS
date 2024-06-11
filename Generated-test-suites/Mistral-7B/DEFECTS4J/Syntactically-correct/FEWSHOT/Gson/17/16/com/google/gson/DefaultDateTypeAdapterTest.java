package com.google.gson.internal.serializer.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter() {
        // Test empty constructor
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(null);
        assertEquals(Date.class, defaultDateTypeAdapter.dateType);
        assertNull(defaultDateTypeAdapter.enUsFormat);
        assertNull(defaultDateTypeAdapter.localFormat);

        // Test Date constructor
        DefaultDateTypeAdapter dateDefaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, dateDefaultDateTypeAdapter.dateType);
        assertNotNull(dateDefaultDateTypeAdapter.enUsFormat);
        assertNotNull(dateDefaultDateTypeAdapter.localFormat);

        // Test Timestamp constructor
        DefaultDateTypeAdapter timestampDefaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        assertEquals(Timestamp.class, timestampDefaultDateTypeAdapter.dateType);
        assertNotNull(timestampDefaultDateTypeAdapter.enUsFormat);
        assertNotNull(timestampDefaultDateTypeAdapter.localFormat);

        // Test java.sql.Date constructor
        DefaultDateTypeAdapter sqlDateDefaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        assertEquals(java.sql.Date.class, sqlDateDefaultTypeAdapter.dateType);
        assertNotNull(sqlDateDefaultDateTypeAdapter.enUsFormat);
        assertNotNull(sqlDateDefaultDateTypeAdapter.localFormat);

        // Test custom constructor with DatePattern
        DefaultDateTypeAdapter customDateDefaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd HH:mm:ss");
        assertEquals(Date.class, customDateDefaultDateTypeAdapter.dateType);
        assertNotNull(customDateDefaultDateTypeAdapter.enUsFormat);
        assertNotNull(customDateDefaultDateTypeAdapter.localFormat);

        // Test custom constructor with dateStyle and timeStyle
        DefaultDateTypeAdapter customDateTimeDefaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
        assertEquals(Date.class, customDateTimeDefaultDateTypeAdapter.dateType);
        assertNotNull(customDateTimeDefaultDateTypeAdapter.enUsFormat);
        assertNotNull(customDateTimeDefaultDateTypeAdapter.localFormat);
    }

    @Test
    public void testWrite() throws IOException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        Date date = new Date();
        String json = writeToJson(defaultDateTypeAdapter, date);
        Date deserializedDate = readFromJson(json, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testRead() throws IOException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        String jsonDate = "\"2022-12-31 11:59:59\"";
        JsonElement jsonElement = JsonParser.parseString(jsonDate);
        Date deserializedDate = defaultDateTypeAdapter.read(JsonReader.ofJsonTree(jsonElement));

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expectedDate = expectedFormat.parse(jsonDate);
        assertEquals(expectedDate, deserializedDate);

        jsonDate = "null";
        jsonElement = JsonParser.parseString(jsonDate);
        assertNull(defaultDateTypeAdapter.read(JsonReader.ofJsonTree(jsonElement)));

        jsonDate = "\"Invalid date\"";
        jsonElement = JsonParser.parseString(jsonDate);
        assertThrows(JsonSyntaxException.class, () -> {
            defaultDateTypeAdapter.read(JsonReader.ofJsonTree(jsonElement));
        });
    }

    private String writeToJson(DefaultDateTypeAdapter adapter, Date date) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();
        String json = gson.toJson(date);
        return json;
    }

    private <T> T readFromJson(String json, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(json);
        JsonElement jsonTree = JsonParser.parseJsonElement(jsonElement.getAsString(), new TypeToken<JsonElement>() {}.getType());
        T result = gson.fromJson(jsonTree, clazz);
        return result;
    }
}