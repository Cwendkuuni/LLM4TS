import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {
    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void write_NullValue_JsonWriterNullValue() throws Exception {
        JsonWriter jsonWriter = new JsonWriter();
        defaultDateTypeAdapter.write(jsonWriter, null);
        assertEquals(JsonToken.NULL, jsonWriter.peek());
    }

    @Test
    public void write_DateValue_JsonWriterValue() throws Exception {
        JsonWriter jsonWriter = new JsonWriter();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
        defaultDateTypeAdapter.write(jsonWriter, date);
        assertEquals(JsonToken.STRING, jsonWriter.peek());
        assertEquals("2021-01-01", jsonWriter.nextString());
    }

    @Test
    public void read_NonStringJsonToken_JsonParseException() throws Exception {
        JsonReader jsonReader = new JsonReader();
        jsonReader.beginObject();
        assertThrows(JsonParseException.class, () -> defaultDateTypeAdapter.read(jsonReader));
    }

    @Test
    public void read_ValidDateString_ReturnsDate() throws Exception {
        JsonReader jsonReader = new JsonReader();
        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.nextString("2021-01-01");
        jsonReader.endObject();

        Date date = defaultDateTypeAdapter.read(jsonReader);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date expectedDate = sdf.parse("2021-01-01");

        assertEquals(expectedDate, date);
    }

    @Test
    public void read_ValidDateStringWithType_ReturnsCorrespondingDateType() throws Exception {
        DefaultDateTypeAdapter timestampTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);

        JsonReader jsonReader = new JsonReader();
        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.nextString("2021-01-01");
        jsonReader.endObject();

        Date date = timestampTypeAdapter.read(jsonReader);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Timestamp expectedTimestamp = new Timestamp(sdf.parse("2021-01-01").getTime());

        assertEquals(expectedTimestamp, date);
    }

    @Test
    public void read_InvalidDateString_JsonSyntaxException() throws Exception {
        JsonReader jsonReader = new JsonReader();
        jsonReader.beginObject();
        jsonReader.nextName();
        jsonReader.nextString("2021-01-01T00:00:00Z");
        jsonReader.endObject();

        assertThrows(JsonSyntaxException.class, () -> defaultDateTypeAdapter.read(jsonReader));
    }

    @Test
    public void toString_ReturnsExpectedString() {
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        assertEquals(expectedString, defaultDateTypeAdapter.toString());
    }
}