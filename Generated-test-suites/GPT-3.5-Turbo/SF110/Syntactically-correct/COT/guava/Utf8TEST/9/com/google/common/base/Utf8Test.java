import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_noSpecialCharacters() {
        String sequence = "Hello World!";
        int expectedLength = 13;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLength_withSpecialCharacters() {
        String sequence = "Привет, мир!";
        int expectedLength = 25;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_invalidLength() {
        String sequence = "Hello World!";
        sequence += sequence += sequence += sequence += sequence;
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_validBytes() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertTrue(isWellFormed);
    }

    @Test
    public void testIsWellFormed_invalidBytes() {
        byte[] bytes = {72, -32};
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);
    }

    @Test
    public void testIsWellFormed_invalidRange() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        boolean isWellFormed = Utf8.isWellFormed(bytes, 5, 8);
        assertFalse(isWellFormed);
    }

    @Test
    public void testIsWellFormed_noInvalidBytes() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        boolean isWellFormed = Utf8.isWellFormed(bytes, 0, 12);
        assertTrue(isWellFormed);
    }

    @Test
    public void testIsWellFormedSlowPath_validBytes() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        boolean isWellFormedSlowPath = Utf8.isWellFormedSlowPath(bytes, 0, 12);
        assertTrue(isWellFormedSlowPath);
    }

    @Test
    public void testIsWellFormedSlowPath_invalidBytes() {
        byte[] bytes = {72, -32};
        boolean isWellFormedSlowPath = Utf8.isWellFormedSlowPath(bytes, 0, 2);
        assertFalse(isWellFormedSlowPath);
    }

    @Test
    public void testIsWellFormedSlowPath_invalidRange() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        boolean isWellFormedSlowPath = Utf8.isWellFormedSlowPath(bytes, 5, 8);
        assertFalse(isWellFormedSlowPath);
    }
    
    @Test
    public void testIsWellFormedSlowPath_noInvalidBytes() {
        byte[] bytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};
        boolean isWellFormedSlowPath = Utf8.isWellFormedSlowPath(bytes, 0, 12);
        assertTrue(isWellFormedSlowPath);
    }

}