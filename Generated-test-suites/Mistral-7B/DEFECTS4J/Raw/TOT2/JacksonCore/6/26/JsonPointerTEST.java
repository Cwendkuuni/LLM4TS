import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    @Test
    public void testMatchesEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testMatchesNonEmptyPointer() {
        JsonPointer jsonPointer = new JsonPointer("/test/property", null, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
    }
}