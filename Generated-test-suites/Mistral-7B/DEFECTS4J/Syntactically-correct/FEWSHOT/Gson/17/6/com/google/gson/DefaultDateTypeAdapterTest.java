package com.google.gson.internal.bind.util; // Added for ISO8601Utils test

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonToken;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReaderInternalAccess;
import com.google.gson.stream.JsonWriterInternalAccess;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertEquals(Date.class, adapter.dateType);
        assertSame(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.enUsFormat);
        assertSame(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.localFormat);
    }

    @Test
    public void testCustomDatePatternConstructor() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z");
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("dd-MM-yyyy hh:mm:ss a z", dateFormat, dateFormat);
        assertEquals(Date.class, adapter.dateType);
        assertSame(dateFormat, adapter.enUsFormat);
        assertSame(dateFormat, adapter.localFormat);
    }

    @Test
    public void testCustomDateStylesConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT);
        assertEquals(Date.class, adapter.dateType);
        assertSame(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US), adapter.enUsFormat);
        assertSame(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US), adapter.localFormat);
    }

    @Test
    public void testThrowsIllegalArgumentExceptionForInvalidDateTypes() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter(Integer.class));
        assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter(String.class));
    }

    @Test
    public void testWriteJson() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        Date date = new Date(1611638888000L); // 2021-02-20 15:24:48
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        adapter.write(gson.newJsonWriter(writer), date);
        writer.close();

        String expectedJson = "\"2021-02-20T15:24:48.000Z\"";
        String json = gson.toJson(date);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testReadJsonDate() throws IOException {
        String json = "\"2021-02-20T15:24:48.000Z\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = adapter.read(reader);
        assertEquals(new Date(1611638888000L), date);
    }

    @Test
    public void testReadJsonTimestamp() throws IOException {
        String json = "\"2021-02-20T15:24:48.123Z\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Date date = adapter.read(reader);
        assertEquals(new Timestamp(1611638888123L), date);
    }

    @Test
    public void testReadJsonSqlDate() throws IOException {
        String json = "\"2021-02-20\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        Date date = adapter.read(reader);
        assertEquals(java.sql.Date.valueOf("2021-02-20"), date);
    }

    @Test
    public void testNullValue() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("null"));
        reader.setLenient(true);

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = adapter.read(reader);
        assertNull(date);
    }

    @Test
    public void testReadJsonInvalidString() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("abcd"));
        reader.setLenient(false); // If set to true, JsonParseException won't be thrown

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    }

    @Test
    public void testReadJsonInvalidTypePattern() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("{\"type\":\"invalid\", \"value\":\"2021-02-20T15:24:48.000Z\"}"));
        reader.setLenient(false); // If set to true, JsonParseException won't be thrown

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertThrows(JsonParseException.class, () -> adapter.read(reader));
    }

    @Test
    public void testReadJsonWithJsonReaderInternalAccess() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"2021-02-20T15:24:48.000Z\"")) {
            @Override
            public JsonElement peek() throws IOException {
                return super.peek();
            }
        };
        reader.setLenient(true);

        JsonReaderInternalAccess jsonReaderInternalAccess = reader.getReaderForClass(JsonElement.class);
        JsonElement jsonElement = jsonReaderInternalAccess.peek();

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = adapter.read(reader);
        assertEquals(new Date(1611638888000L), date);
    }

    @Test
    public void testReadJsonWithJsonWriterInternalAccess() throws IOException {
        JsonWriter writer = new JsonWriter(new StringWriter()) {
            @Override
            public JsonWriter name(String name) throws IOException {
                this.name = name;
                return this;
            }
        };
        writer.setLenient(true);

        JsonWriterInternalAccess jsonWriterInternalAccess = writer.getWriterForClass(JsonObject.class);
        JsonObject jsonObject = jsonWriterInternalAccess.getAsJsonObject();
        jsonObject.addProperty("name", "value");
        jsonObject.add("type", new JsonPrimitive("date"));
        jsonObject.add("value", new JsonPrimitive("\"2021-02-20T15:24:48.000Z\""));

        writer.flush();
        String json = writer.toString();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = adapter.read(reader);
        assertEquals(new Date(1611638888000L), date);
    }
}