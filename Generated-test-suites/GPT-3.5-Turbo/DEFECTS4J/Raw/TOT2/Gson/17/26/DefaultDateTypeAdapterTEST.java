import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullDate() {
        // Given
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter();

        // When
        adapter.write(writer, null);

        // Then
        assertNull(writer.toString());
    }

    @Test
    public void testRead_ValidDateString() {
        // Given
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader();

        // When
        Date date = adapter.read(reader);

        // Then
        assertNotNull(date);
    }

    @Test
    public void testDeserializeToDate_InvalidDateString() {
        // Given
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String invalidDate = "Invalid Date String";

        // When
        Date date = adapter.deserializeToDate(invalidDate);

        // Then
        assertNull(date);
    }

    @Test
    public void testToString() {
        // Given
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // When
        String result = adapter.toString();

        // Then
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
    }
}