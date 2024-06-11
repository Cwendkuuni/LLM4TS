import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement result = writer.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThrows(IllegalStateException.class, () -> writer.endArray());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThrows(IllegalStateException.class, () -> writer.endObject());
    }

    @Test
    public void testValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThrows(IOException.class, () -> writer.value(null));
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertDoesNotThrow(() -> writer.flush());
    }

    @Test
    public void testClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThrows(IOException.class, () -> writer.close());
    }
}