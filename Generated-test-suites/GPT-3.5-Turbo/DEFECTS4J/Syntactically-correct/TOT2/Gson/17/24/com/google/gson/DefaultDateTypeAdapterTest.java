import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWriteNullDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        StringWriter writer = new StringWriter();
        adapter.write(new JsonWriter(writer), null);
        assertEquals("Null date should be written as null", "null", writer.toString());
    }

    @Test
    public void testReadInvalidDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        StringReader reader = new StringReader("invalidDate");
        try {
            adapter.read(new JsonReader(reader));
            fail("Reading invalid date should throw a JsonParseException");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testDeserializeToDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.deserializeToDate("2022-12-31T23:59:59");
        assertNotNull("Deserialized date should not be null", date);
    }

    @Test
    public void testWriteAndReadTimestamp() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Date date = new Date();
        StringWriter writer = new StringWriter();
        adapter.write(new JsonWriter(writer), date);
        StringReader reader = new StringReader(writer.toString());
        Date deserializedDate = adapter.read(new JsonReader(reader));
        assertEquals("Timestamp dates should be equal after write and read", date, deserializedDate);
    }

}