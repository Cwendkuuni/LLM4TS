import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    DefaultDateTypeAdapter adapter;

    @Before
    public void setup() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWriteNullValue() {
        // Arrange
        JsonWriter writer = mock(JsonWriter.class);

        // Act
        adapter.write(writer, null);

        // Assert
        verify(writer).nullValue();
    }

    @Test
    public void testWriteDateValue() throws IOException {
        // Arrange
        JsonWriter writer = mock(JsonWriter.class);
        Date date = new Date();
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        String formattedDate = format.format(date);

        // Act
        adapter.write(writer, date);

        // Assert
        verify(writer).value(formattedDate);
    }

    @Test
    public void testReadStringDateValue() throws IOException {
        // Arrange
        JsonReader reader = mock(JsonReader.class);
        String dateStr = "2021-05-25T12:00:00";
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(dateStr);
        SimpleDateFormat sdf = new SimpleDateFormat(DefaultDateTypeAdapter.ISO_DATE_PATTERN);

        // Act
        Date result = adapter.read(reader);

        // Assert
        assertEquals(sdf.parse(dateStr), result);
    }

    @Test(expected = JsonParseException.class)
    public void testReadNonStringDateValue() throws IOException {
        // Arrange
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NUMBER);

        // Act
        adapter.read(reader);

        // Assert
        // Expects JsonParseException to be thrown
    }

    @Test
    public void testDeserializeToDateWithValidFormat() {
        // Arrange
        String dateStr = "2021-05-25T12:00:00";

        // Act
        Date result = adapter.deserializeToDate(dateStr);
        SimpleDateFormat sdf = new SimpleDateFormat(DefaultDateTypeAdapter.ISO_DATE_PATTERN);

        // Assert
        assertEquals(sdf.parse(dateStr), result);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDateWithInvalidFormat() {
        // Arrange
        String invalidDateStr = "2021-13-50T12:00:00";

        // Act
        adapter.deserializeToDate(invalidDateStr);

        // Assert
        // Expects JsonSyntaxException to be thrown
    }

    @Test
    public void testToString() {
        // Arrange
        String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";

        // Act
        String result = adapter.toString();

        // Assert
        assertEquals(expected, result);
    }
}