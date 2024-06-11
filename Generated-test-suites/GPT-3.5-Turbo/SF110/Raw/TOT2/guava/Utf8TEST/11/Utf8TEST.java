import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Test";
        int result = Utf8.encodedLength(sequence);
        assertEquals(4, result);
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "Test";
        int result = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(4, result);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{0, 1, 2, 3};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{-1, -2, -3, -4};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 4);
        assertFalse(result);
    }
}