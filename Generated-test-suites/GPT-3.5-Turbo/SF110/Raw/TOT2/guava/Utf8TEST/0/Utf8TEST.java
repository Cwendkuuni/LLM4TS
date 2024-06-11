import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(5, Utf8.encodedLength("hello"));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[]{65, 66, 67}));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{65, -66, 67}, 1, 1));
    }
}