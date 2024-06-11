import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter() {
        // Test DefaultDateTypeAdapter with default constructor
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, defaultDateTypeAdapter).create();

        // Test writing a Date object to a JsonWriter
        Date date = new Date();
        String json = "\"2022-12-31 12:00:00\""; // A valid date string
        JsonWriter jsonWriter = new JsonWriter(System.out);
        gson.toJson(date, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals(json, jsonWriter.getString());

        // Test reading a Date object from a JsonReader
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Date readDate = defaultDateTypeAdapter.read(jsonReader);
        Assert.assertEquals(date.getTime(), readDate.getTime());

        // Test writing a Timestamp object to a JsonWriter
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String jsonTimestamp = "\"2022-12-31 12:00:00.123\""; // A valid timestamp string
        jsonWriter = new JsonWriter(System.out);
        gson.toJson(timestamp, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals(jsonTimestamp, jsonWriter.getString());

        // Test reading a Timestamp object from a JsonReader
        JsonElement jsonElement = new JsonParser().parse(jsonTimestamp);
        JsonReader jsonReaderTimestamp = new JsonReader(new StringReader(jsonElement.getAsString()));
        Timestamp readTimestamp = defaultDateTypeAdapter.read(jsonReaderTimestamp);
        Assert.assertEquals(timestamp.getTime(), readTimestamp.getTime());

        // Test writing a java.sql.Date object to a JsonWriter
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String jsonSqlDate = "\"2022-12-31\""; // A valid java.sql.Date string
        jsonWriter = new JsonWriter(System.out);
        gson.toJson(sqlDate, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals(jsonSqlDate, jsonWriter.getString());

        // Test reading a java.sql.Date object from a JsonReader
        JsonReader jsonReaderSqlDate = new JsonReader(new StringReader(jsonSqlDate));
        java.sql.Date readSqlDate = defaultDateTypeAdapter.read(jsonReaderSqlDate);
        Assert.assertEquals(sqlDate.getTime(), readSqlDate.getTime());

        // Test writing a Date object with an invalid format to a JsonWriter
        Date invalidDate = new Date();
        json = "\"invalid date\""; // An invalid date string
        jsonWriter = new JsonWriter(System.out);
        try {
            gson.toJson(invalidDate, jsonWriter);
            jsonWriter.flush();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The date 'invalid date' should be a valid date string", e.getMessage());
        }

        // Test reading a Date object with an invalid format from a JsonReader
        jsonElement = new JsonParser().parse(json);
        jsonReader = new JsonReader(new StringReader(jsonElement.getAsString()));
        try {
            defaultDateTypeAdapter.read(jsonReader);
            Assert.fail();
        } catch (JsonParseException e) {
            Assert.assertEquals("The date 'invalid date' should be a valid date string", e.getMessage());
        }

        // Test writing a null Date object to a JsonWriter
        jsonWriter = new JsonWriter(System.out);
        gson.toJson(null, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals("null", jsonWriter.getString());

        // Test reading a null Date object from a JsonReader
        jsonReader = new JsonReader(new StringReader("null"));
        Assert.assertNull(defaultDateTypeAdapter.read(jsonReader));
    }

    @Test
    public void testDefaultDateTypeAdapterWithStringDatePattern() {
        // Test DefaultDateTypeAdapter with a custom constructor using a specific date pattern
        String datePattern = "dd-MM-yyyy HH:mm:ss.SSS";
        DefaultDateTypeAdapter customDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, customDateTypeAdapter).create();

        // Test writing a Date object to a JsonWriter
        Date date = new Date();
        String json = "\"31-12-2022 12:00:00.123\""; // A valid date string with the custom pattern
        JsonWriter jsonWriter = new JsonWriter(System.out);
        gson.toJson(date, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals(json, jsonWriter.getString());

        // Test reading a Date object from a JsonReader
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Date readDate = customDateTypeAdapter.read(jsonReader);
        Assert.assertEquals(date.getTime(), readDate.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterWithCustomDateStyles() {
        // Test DefaultDateTypeAdapter with custom date and time styles
        DefaultDateTypeAdapter customDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, customDateTypeAdapter).create();

        // Test writing a Date object to a JsonWriter
        Date date = new Date();
        String json = "\"31-12-2022\""; // A valid date string with the custom date style
        JsonWriter jsonWriter = new JsonWriter(System.out);
        gson.toJson(date, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals(json, jsonWriter.getString());

        // Test reading a Date object from a JsonReader
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Date readDate = customDateTypeAdapter.read(jsonReader);
        Assert.assertEquals(date.getTime(), readDate.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterWithLocale() {
        // Test DefaultDateTypeAdapter with a custom constructor using a specific locale
        Locale locale = new Locale("fr_FR");
        DefaultDateTypeAdapter customDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, null, null, locale);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, customDateTypeAdapter).create();

        // Test writing a Date object to a JsonWriter
        Date date = new Date();
        String json = "\"31-12-2022 12:00:00\""; // A valid date string in French format
        JsonWriter jsonWriter = new JsonWriter(System.out);
        gson.toJson(date, jsonWriter);
        jsonWriter.flush();

        Assert.assertEquals(json, jsonWriter.getString());

        // Test reading a Date object from a JsonReader
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Date readDate = customDateTypeAdapter.read(jsonReader);
        Assert.assertEquals(date.getTime(), readDate.getTime());
    }
}