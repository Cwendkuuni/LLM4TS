import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(7, Utf8.encodedLength("Hello"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(4, Utf8.encodedLengthGeneral("Test", 0));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[]{0, 1, 2}));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{-32, 65, -67}, 0, 3));
    }
}