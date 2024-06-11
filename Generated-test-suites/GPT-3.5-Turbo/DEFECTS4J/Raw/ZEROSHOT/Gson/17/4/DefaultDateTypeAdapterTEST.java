import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
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
    public void testWrite_WithNullValue_ShouldWriteNullValue() throws IOException {
        JsonWriter jsonWriter = new JsonWriter();
        defaultDateTypeAdapter.write(jsonWriter, null);
        assertEquals(JsonToken.NULL, jsonWriter.peek());
    }

    @Test
    public void testWrite_WithNonNullValue_ShouldWriteFormattedValue() throws IOException {
        Date date = new Date();
        JsonWriter jsonWriter = new JsonWriter();
        defaultDateTypeAdapter.write(jsonWriter, date);
        assertEquals(date.toString(), jsonWriter.peek());
    }

    @Test(expected = JsonParseException.class)
    public void testRead_WithNonStringValue_ShouldThrowException() throws IOException {
        JsonReader jsonReader = new JsonReader();
        jsonReader.setToken(JsonToken.NULL);
        defaultDateTypeAdapter.read(jsonReader);
    }

    @Test
    public void testRead_WithValidDateString_ShouldReturnCorrectDate() throws IOException, NoSuchFieldException, IllegalAccessException {
        String dateString = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        Field localFormatField = DefaultDateTypeAdapter.class.getDeclaredField("localFormat");
        localFormatField.setAccessible(true);
        DateFormat localFormat = (DateFormat) localFormatField.get(defaultDateTypeAdapter);
        JsonReader jsonReader = new JsonReader();
        jsonReader.setToken(JsonToken.STRING, dateString);
        Date actualDate = defaultDateTypeAdapter.read(jsonReader);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testDeserializeToDate_WithValidDateString_ShouldReturnCorrectDate() {
        String dateString = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        Date actualDate = defaultDateTypeAdapter.deserializeToDate(dateString);
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDate_WithInvalidDateString_ShouldThrowException() {
        String dateString = "01-01-2022";
        defaultDateTypeAdapter.deserializeToDate(dateString);
    }

    @Test
    public void testToString_ShouldReturnCorrectStringRepresentation() {
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        String actualString = defaultDateTypeAdapter.toString();
        assertEquals(expectedString, actualString);
    }

}