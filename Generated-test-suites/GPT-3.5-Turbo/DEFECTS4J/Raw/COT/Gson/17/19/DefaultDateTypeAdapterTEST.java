
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    @Before
    public void setup() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        jsonReader = mock(JsonReader.class);
        jsonWriter = mock(JsonWriter.class);
    }

    @Test
    public void write_NullDate_WritesNullValue() throws IOException {
        // Arrange
        Date date = null;

        // Act
        defaultDateTypeAdapter.write(jsonWriter, date);

        // Assert
        verify(jsonWriter).nullValue();
    }

    @Test
    public void write_ValidDate_WritesFormattedString() throws IOException {
        // Arrange
        Date date = new Date(2022, 1, 1);

        // Act
        defaultDateTypeAdapter.write(jsonWriter, date);

        // Assert
        verify(jsonWriter).value("Jan 1, 2022 12:00:00 AM");
    }

    @Test
    public void read_InvalidToken_ThrowsJsonParseException() throws IOException {
        // Arrange
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        // Act & Assert
        assertThrows(JsonParseException.class, () -> defaultDateTypeAdapter.read(jsonReader));
    }

    @Test
    public void read_ValidDate_ReturnsDateInstance() throws IOException {
        // Arrange
        String dateString = "Jan 1, 2022 12:00:00 AM";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(dateString);

        // Act
        Date date = defaultDateTypeAdapter.read(jsonReader);

        // Assert
        assertEquals(new Date(2022, 1, 1), date);
    }

    @Test
    public void read_ValidDateWithType_ReturnsDateInstance() throws IOException {
        // Arrange
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        String dateString = "Jan 1, 2022 12:00:00 AM";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(dateString);

        // Act
        Date date = defaultDateTypeAdapter.read(jsonReader);

        // Assert
        assertEquals(new Timestamp(new Date(2022, 1, 1).getTime()), date);
    }

    @Test
    public void read_ValidDateWithSqlDateType_ReturnsDateInstance() throws IOException {
        // Arrange
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        String dateString = "Jan 1, 2022 12:00:00 AM";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(dateString);

        // Act
        Date date = defaultDateTypeAdapter.read(jsonReader);

        // Assert
        assertEquals(new java.sql.Date(new Date(2022, 1, 1).getTime()), date);
    }

    @Test
    public void deserializeToDate_InvalidDateString_ThrowsJsonSyntaxException() {
        // Arrange
        String dateString = "Invalid Date String";

        // Act & Assert
        assertThrows(JsonSyntaxException.class, () -> defaultDateTypeAdapter.deserializeToDate(dateString));
    }

    @Test
    public void deserializeToDate_ValidDateString_ReturnsDateInstance() {
        // Arrange
        String dateString = "Jan 1, 2022 12:00:00 AM";

        // Act
        Date date = defaultDateTypeAdapter.deserializeToDate(dateString);

        // Assert
        assertEquals(new Date(2022, 1, 1), date);
    }

    @Test
    public void toString_ReturnsClassNameAndFormatName() {
        // Arrange
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";

        // Act
        String actualString = defaultDateTypeAdapter.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}
