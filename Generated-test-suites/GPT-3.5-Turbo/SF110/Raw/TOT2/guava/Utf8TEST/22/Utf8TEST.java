
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(5, Utf8.encodedLength("hello"));
        assertEquals(6, Utf8.encodedLength("你好"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(5, Utf8.encodedLengthGeneral("hello", 0));
        assertEquals(9, Utf8.encodedLengthGeneral("😊hello", 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] validBytes = {65, 66, 67};
        assertTrue(Utf8.isWellFormed(validBytes));

        byte[] invalidBytes = {65, -128, 67};
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] validBytes = {65, 66, 67, 68, -62, -96};
        assertTrue(Utf8.isWellFormed(validBytes, 0, validBytes.length));

        byte[] invalidBytes = {65, 66, -128, -96};
        assertFalse(Utf8.isWellFormed(invalidBytes, 0, invalidBytes.length));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] validBytes = {65, 66, -32, -96};
        assertTrue(Utf8.isWellFormedSlowPath(validBytes, 0, validBytes.length));

        byte[] invalidBytes = {65, -32, -95};
        assertFalse(Utf8.isWellFormedSlowPath(invalidBytes, 0, invalidBytes.length));
    }
}

