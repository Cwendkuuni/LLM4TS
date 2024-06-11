
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWriteNullDate() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // act
        JsonWriter out = new JsonWriter();
        adapter.write(out, null);

        // assert
        assertEquals("null", out.toString());
    }

    @Test
    public void testWriteDate() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date();

        // act
        JsonWriter out = new JsonWriter();
        adapter.write(out, date);

        // assert
        assertEquals(date.toString(), out.toString());
    }

    @Test
    public void testReadInvalidJsonValue() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String jsonValue = "invalid";

        // act/assert
        try {
            JsonReader in = new JsonReader(jsonValue);
            adapter.read(in);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // success
        }
    }

    @Test
    public void testReadValidJsonValue() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String jsonValue = "2022-01-01T00:00:00Z";

        // act
        JsonReader in = new JsonReader(jsonValue);
        Date date = adapter.read(in);

        // assert
        assertEquals(new Date(1640995200000L), date);
    }

    @Test
    public void testDeserializeToDate_invalidFormat() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateStr = "2022-01-01X00:00:00Z";

        // act/assert
        try {
            adapter.deserializeToDate(dateStr);
            fail("Expected JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            // success
        }
    }

    @Test
    public void testDeserializeToDate_enUsFormat() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateStr = "Sat, 01 Jan 2022 00:00:00 GMT";

        // act
        Date date = adapter.deserializeToDate(dateStr);

        // assert
        assertEquals(new Date(1640995200000L), date);
    }

    @Test
    public void testToString() {
        // arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // act
        String result = adapter.toString();

        // assert
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
    }
}

