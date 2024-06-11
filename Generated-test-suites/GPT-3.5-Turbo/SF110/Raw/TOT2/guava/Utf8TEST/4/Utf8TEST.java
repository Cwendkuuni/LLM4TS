import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "Hello";
        int expectedLength = 5;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);

        CharSequence emptySequence = "";
        expectedLength = 0;
        actualLength = Utf8.encodedLength(emptySequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "Test";
        int start = 0;
        int expectedLength = 4;
        int actualLength = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(expectedLength, actualLength);

        CharSequence emptySequence = "";
        start = 0;
        expectedLength = 0;
        actualLength = Utf8.encodedLengthGeneral(emptySequence, start);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {72, 101, 108, 108, 111};
        assertTrue(Utf8.isWellFormed(bytes));

        byte[] invalidBytes = {-128, 65, 66};
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }

    @Test
    public void testIsWellFormedWithOffset() {
        byte[] bytes = {72, 101, 108, 108, 111};
        int off = 0;
        int len = 5;
        assertTrue(Utf8.isWellFormed(bytes, off, len));

        int invalidOff = 1;
        int invalidLen = 3;
        assertFalse(Utf8.isWellFormed(bytes, invalidOff, invalidLen));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {72, 101, 108, 108, 111};
        int off = 0;
        int end = 5;
        assertFalse(Utf8.isWellFormedSlowPath(bytes, off, end));

        byte[] invalidBytes = {-128, -128, 65, 66};
        off = 0;
        end = 4;
        assertFalse(Utf8.isWellFormedSlowPath(invalidBytes, off, end));
    }
}