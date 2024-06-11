import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;
    private DateFormat enUsFormat;
    private DateFormat localFormat;

    @Before
    public void setup() {
        enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
    }

    @Test
    public void testWrite() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        Date date = new Date();

        adapter.write(out, date);

        verify(out).value(enUsFormat.format(date));
    }

    @Test
    public void testWriteWithNullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);

        adapter.write(out, null);

        verify(out).nullValue();
    }

    @Test
    public void testRead() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String dateString = "2020-01-01T12:00:00Z";
        Date expectedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        when(in.nextString()).thenReturn(dateString);

        Date date = adapter.read(in);

        assertEquals(expectedDate, date);
    }

    @Test(expected = JsonParseException.class)
    public void testReadWithNonStringToken() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

        adapter.read(in);
    }

    @Test
    public void testReadWithNullToken() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Date date = adapter.read(in);

        assertNull(date);
    }

    @Test
    public void testDeserializeToDate() {
        String dateString = "2020-01-01T12:00:00Z";
        Date expectedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));

        Date date = adapter.deserializeToDate(dateString);

        assertEquals(expectedDate, date);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDateWithInvalidString() {
        String dateString = "2020-01-01";
        adapter.deserializeToDate(dateString);
    }

}