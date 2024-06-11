import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.DateTypeAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import junit.framework.TestCase;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

    @org.junit.Test
    public void testDefaultDateTypeAdapterInstance() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        TestCase.assertNotNull(adapter);
        TestCase.assertEquals(Date.class, adapter.getTType());
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterInstanceStringPattern() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        TestCase.assertNotNull(adapter);
        TestCase.assertEquals(Date.class, adapter.getTType());
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterInstanceDateStyles() {
        DefaultDateTypeAdapter adapterDateStyle = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
        TestCase.assertNotNull(adapterDateStyle);
        TestCase.assertEquals(Date.class, adapterDateStyle.getTType());

        DefaultDateTypeAdapter adapterDateStyleShort = new DefaultDateTypeAdapter(int.class, Calendar.SHORT);
        TestCase.assertNotNull(adapterDateStyleShort);
        TestCase.assertEquals(int.class, adapterDateStyleShort.getTType());

        DefaultDateTypeAdapter adapterDateStyleLong = new DefaultDateTypeAdapter(int.class, Calendar.LONG);
        TestCase.assertNotNull(adapterDateStyleLong);
        TestCase.assertEquals(int.class, adapterDateStyleLong.getTType());
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterWrite() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date();
        String expected = "Wed Aug 24 13:36:03 EDT 2022";
        String result;

        JsonWriter jsonWriter = new JsonWriter(System.out);
        adapter.write(jsonWriter, date);
        jsonWriter.flush();
        result = System.out.getOutputStream().toString();

        TestCase.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterRead() throws IOException {
        String jsonString = "\"2022-08-24T13:36:03.000-04:00\"";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        JsonElement element = JsonParser.parseString(jsonString).getAsJsonObject();
        Date date = adapter.read(new JsonReader(new JsonElementInputStream(element)));

        TestCase.assertNotNull(date);
        TestCase.assertEquals(getExpectedDate("2022-08-24T13:36:03.000-04:00"), date);
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterReadInvalidFormat() throws IOException {
        String jsonString = "\"InvalidFormat\"";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        try {
            JsonElement element = JsonParser.parseString(jsonString).getAsJsonObject();
            adapter.read(new JsonReader(new JsonElementInputStream(element)));
            TestCase.fail("Expected JsonSyntaxException");
        } catch (JsonSyntaxException e) {
            TestCase.assertEquals("InvalidFormat: The date \"InvalidFormat\" is malformed at \"InvalidFormat\"", e.getMessage());
        }
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterReadNull() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        JsonReader jsonReader = new JsonReader(new StringReader("null"));
        try {
            adapter.read(jsonReader);
            TestCase.fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            TestCase.assertEquals("The date should not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
        String jsonString = "\"2022-08-24T13:36:03.000-04:00\"";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

        JsonElement element = JsonParser.parseString(jsonString).getAsJsonObject();
        Timestamp timestamp = (Timestamp) adapter.read(new JsonReader(new JsonElementInputStream(element)));

        TestCase.assertNotNull(timestamp);
        TestCase.assertEquals(getExpectedTimestamp("2022-08-24T13:36:03.000-04:00"), timestamp);
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterReadSqlDate() throws IOException {
        String jsonString = "\"2022-08-24\"";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        JsonElement element = JsonParser.parseString(jsonString).getAsJsonObject();
        java.sql.Date sqlDate = (java.sql.Date) adapter.read(new JsonReader(new JsonElementInputStream(element)));

        TestCase.assertNotNull(sqlDate);
        TestCase.assertEquals(getExpectedSqlDate("2022-08-24"), sqlDate);
    }

    @org.junit.Test
    public void testDefaultDateTypeAdapterToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        TestCase.assertEquals(SIMPLE_NAME + "(Date)", adapter.toString());
    }

    private static Date getExpectedDate(String dateString) {
        try {
            return new SimpleDateFormat("E MMM d HH:mm:ss z yyyy").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static Timestamp getExpectedTimestamp(String timestampString) {
        try {
            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(timestampString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static java.sql.Date getExpectedSqlDate(String sqlDateString) {
        try {
            return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(sqlDateString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static class JsonElementInputStream extends java.io.InputStreamReader {
        private JsonElement jsonElement;

        public JsonElementInputStream(JsonElement jsonElement) {
            super(new StringReader(""));
            this.jsonElement = jsonElement;
        }

        @Override
        public int read() throws IOException {
            return jsonElement.getAsString().codePointAt(jsonReaderIndex++);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (jsonElement.isJsonNull()) {
                throw new IOException("Expected a JSON object or array but got null");
            }

            StringBuilder stringBuilder = new StringBuilder();
            int read = 0;
            while (jsonReaderIndex < jsonElement.getAsString().length()) {
                int codePoint = jsonElement.getAsString().codePointAt(jsonReaderIndex++);
                if (codePoint == '\u0000') {
                    break;
                }
                stringBuilder.appendCodePoint(codePoint);
                if (off + read >= b.length) {
                    break;
                }
                b[off + read++] = (byte) codePoint;
            }

            if (read > 0) {
                return read;
            }

            throw new IoException("Unexpected end of JSON input");
        }
    }
}