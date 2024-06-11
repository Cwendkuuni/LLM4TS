package com.google.gson.internal.bind.util; // for ISO8601Utils

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
    @Test
    public void testWrite() throws IOException {
        Gson gson = new Gson();
        Date date = new Date();
        JsonWriter writer = new JsonWriter(System.out);
        gson.toJson(date, writer);
        writer.flush();
        assertEquals("2022-11-17 15:17:04.028", reader().readTree().getAsJsonObject().get("").getAsString());

        Date date2 = new Date(0);
        writer = new JsonWriter(System.out);
        gson.toJson(date2, writer);
        writer.flush();
        assertEquals("1970-01-01 00:00:00.000", reader().readTree().getAsJsonObject().get("").getAsString());

        gson = new GsonBuilder().setLenient().create();
        writer = new JsonWriter(System.out);
        gson.toJson(date2, writer);
        writer.flush();
        assertEquals("null", reader().readTree().getAsJsonObject().get("").getAsString());
    }

    @Test
    public void testRead_Date() throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = reader().readTree();
        Date date = gson.fromJson(jsonElement, Date.class);
        assertEquals("2022-11-17 15:17:04.028", enUsFormat().format(date));

        jsonElement = reader().readTree().getAsJsonObject().get("date2").getAsJsonPrimitive().getAsString();
        date = gson.fromJson(jsonElement, Date.class);
        assertEquals("1970-01-01 00:00:00.000", enUsFormat().format(date));

        jsonElement = reader().readTree().getAsJsonObject().get("date3").getAsJsonPrimitive().getAsString();
        assertThrows(JsonParseException.class, () -> gson.fromJson(jsonElement, Date.class));
    }

    @Test
    public void testRead_Timestamp() throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = reader().readTree().getAsJsonObject().get("timestamp");
        Timestamp timestamp = gson.fromJson(jsonElement, Timestamp.class);
        assertEquals(1668619424000L, timestamp.getTime());

        jsonElement = reader().readTree().getAsJsonObject().get("timestamp2");
        assertThrows(JsonParseException.class, () -> gson.fromJson(jsonElement, Timestamp.class));
    }

    @Test
    public void testRead_SQLDate() throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = reader().readTree().getAsJsonObject().get("sqlDate");
        Date date = gson.fromJson(jsonElement, java.sql.Date.class);
        assertEquals("1970-01-01", sqlFormat().format(date));

        jsonElement = reader().readTree().getAsJsonObject().get("sqlDate2");
        assertThrows(JsonParseException.class, () -> gson.fromJson(jsonElement, java.sql.Date.class));
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.LONG, DateFormat.LONG);
        assertEquals("DefaultDateTypeAdapter(java.text.SimpleDateFormat)", adapter.toString());
    }

    @Test
    public void testDefaultConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertEquals("DefaultDateTypeAdapter(java.text.SimpleDateFormat)", adapter.toString());
    }

    @Test
    public void testDateStyleLong() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(DateFormat.LONG, DateFormat.LONG);
        assertEquals("Thu Nov 17 15:17:04 PST 2022", enUsFormat().format(adapter.read(reader())).toString());
        assertEquals("Thu Nov 17 15:17:04 PST 2022", enUsFormat().format(adapter.read(reader("2022-11-17 15:17:04 PST"))).toString());
    }

    @Test
    public void testDateStyleShort() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(DateFormat.SHORT, DateFormat.SHORT);
        assertEquals("11/17/22", enUsFormat().format(adapter.read(reader())).toString());
        assertEquals("11/17/22", enUsFormat().format(adapter.read(reader("2022-11-17"))).toString());
    }

    @Test
    public void testDateStyleMedium() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(DateFormat.MEDIUM, DateFormat.MEDIUM);
        assertEquals("Nov 17, 2022", enUsFormat().format(adapter.read(reader())).toString());
        assertEquals("Nov 17, 2022", enUsFormat().format(adapter.read(reader("2022-11-17"))).toString());
    }

    @Test
    public void testDateStyleFull() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(DateFormat.FULL, DateFormat.FULL);
        assertEquals("Thursday, November 17, 2022", enUsFormat().format(adapter.read(reader())).toString());
        assertEquals("Thursday, November 17, 2022", enUsFormat().format(adapter.read(reader("2022-11-17"))).toString());
    }

    @Test
    public void testDateStyleDefault() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertEquals("Thu Nov 17 15:17:04 PST 2022", enUsFormat().format(adapter.read(reader())).toString());
        assertEquals("Thu Nov 17 15:17:04 PST 2022", enUsFormat().format(adapter.read(reader("2022-11-17 15:17:04 PST"))).toString());
    }

    @Test
    public void testDateStyleCustom() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd HH:mm:ss z", DateFormat.DEFAULT);
        assertEquals("2022-11-17 15:17:04 PST", enUsFormat().format(adapter.read(reader())).toString());
        assertEquals("2022-11-17 15:17:04 PST", enUsFormat().format(adapter.read(reader("2022-11-17 15:17:04 PST"))).toString());
    }

    @Test
    public void testReadNull() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertNull(adapter.read(reader(null)));
    }

    @Test
    public void testWriteNull() throws IOException {
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(System.out);
        gson.toJson(null, writer);
        writer.flush();
        assertNull(reader().readTree().getAsJsonObject().get(""));
    }

    @Test
    public void testReadInvalidFormat() throws IOException {
        assertThrows(JsonSyntaxException.class, () -> new DefaultDateTypeAdapter(Date.class).read(reader("invalid format")));
    }

    @Test
    public void testReadIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultDateTypeAdapter("java.util.Date", null));
    }

    private JsonReader reader() {
        return new JsonReader(new StringReader("{\"\":\"2022-11-17 15:17:04 PST\",\"date2\":\"1970-01-01\",\"date3\":\"invalid format\",\"timestamp\":1668619424000,\"sqlDate\":\"1970-01-01\",\"timestamp2\":\"2022-11-17T15:17:04Z\"}"));
    }

    private JsonReader reader(String json) {
        return new JsonReader(new StringReader(json));
    }

    private DateFormat enUsFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    }

    private DateFormat sqlFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    private static class StringReader extends java.io.Reader {
        private final String str;

        public StringReader(String str) {
            this.str = str;
        }

        @Override
        public int read() throws IOException {
            if (str == null || str.length() == 0) {
                throw new IOException();
            }
            int i = 0;
            while (str.charAt(i) != '\u0000' && str.charAt(i) != '\n') {
                i++;
            }
            return i >= 0 ? str.charAt(i) : -1;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int index = off;
            int len2 = len;

            if (str == null || str.length() == 0) {
                throw new IOException();
            }

            while (len2 > 0) {
                int c = read();
                if (c < 0) {
                    break;
                }
                cbuf[index++] = (char) c;
                len2--;
            }
            if (index > off) {
                return index - off;
            }
            throw new IOException();
        }

        @Override
        public void close() throws IOException {
            // no-op
        }

        @Override
        public boolean markSupported(boolean readAhead) {
            return false;
        }

        @Override
        public void mark(int readAheadLimit) {
            // no-op
        }

        @Override
        public long position() throws IOException {
            throw new IOException();
        }

        @Override
        public long length() throws IOException {
            throw new IOException();
        }
    }
}