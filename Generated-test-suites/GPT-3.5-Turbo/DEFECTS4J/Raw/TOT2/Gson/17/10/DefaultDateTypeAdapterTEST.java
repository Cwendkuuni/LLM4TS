import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWriteNullDate() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        defaultDateTypeAdapter.write(jsonWriter, null);
        
        assertEquals("null", stringWriter.toString());
    }

    @Test
    public void testReadStringNotDate() throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader("\"This is not a date\""));

        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        assertThrows(JsonParseException.class, () -> {
            defaultDateTypeAdapter.read(jsonReader);
        });
    }

    @Test
    public void testDeserializeDateWithInvalidFormat() throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader("\"2022-15-40\""));

        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        assertThrows(JsonSyntaxException.class, () -> {
            defaultDateTypeAdapter.read(jsonReader);
        });
    }

    // Add more test cases for other methods as needed

}