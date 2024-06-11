
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello World";
        int result = Utf8.encodedLength(sequence);
        assertEquals(sequence.length(), result);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        int offset = 0;
        int length = bytes.length;
        boolean result = Utf8.isWellFormed(bytes, offset, length);
        assertTrue(result);
    }
}
