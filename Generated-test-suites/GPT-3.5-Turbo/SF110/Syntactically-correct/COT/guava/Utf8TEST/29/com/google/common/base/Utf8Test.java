

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test basic functionality
        assertEquals(6, Utf8.encodedLength("Hello!"));
        assertEquals(4, Utf8.encodedLength("你好"));
        assertEquals(3, Utf8.encodedLength("€"));
        assertEquals(8, Utf8.encodedLength("🌍"));

        // Test empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test exception handling with maximum UTF-8 length
        try {
            Utf8.encodedLength("你好世界");
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("UTF-8 length does not fit in int: 6442450946", e.getMessage());
        }

        // Test exception handling with unpaired surrogate
        try {
            Utf8.encodedLength("Hello\uD83DTest");
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 9", e.getMessage());
        }
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed sequences
        assertTrue(Utf8.isWellFormed("Hello!".getBytes()));
        assertTrue(Utf8.isWellFormed("你好".getBytes()));
        assertTrue(Utf8.isWellFormed("€".getBytes()));
        assertTrue(Utf8.isWellFormed("🌍".getBytes()));

        // Test empty sequence
        assertTrue(Utf8.isWellFormed("".getBytes()));

        // Test malformed sequences
        assertFalse(Utf8.isWellFormed("Hello!\uFFFD".getBytes()));
        assertFalse(Utf8.isWellFormed("Hello\uFFFDTest".getBytes()));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test well-formed sequences
        byte[] bytes1 = "Hello!".getBytes();
        assertTrue(Utf8.isWellFormed(bytes1, 0, 6));
        byte[] bytes2 = "你好".getBytes();
        assertTrue(Utf8.isWellFormed(bytes2, 0, 4));
        byte[] bytes3 = "€".getBytes();
        assertTrue(Utf8.isWellFormed(bytes3, 0, 3));
        byte[] bytes4 = "🌍".getBytes();
        assertTrue(Utf8.isWellFormed(bytes4, 0, 8));

        // Test empty sequence
        byte[] bytes5 = "".getBytes();
        assertTrue(Utf8.isWellFormed(bytes5, 0, 0));

        // Test malformed sequences
        byte[] bytes6 = "Hello!\uFFFD".getBytes();
        assertFalse(Utf8.isWellFormed(bytes6, 0, 7));
        byte[] bytes7 = "Hello\uFFFDTest".getBytes();
        assertFalse(Utf8.isWellFormed(bytes7, 0, 13));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = "Hello!".getBytes();
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, 6));
        byte[] bytes2 = "你好".getBytes();
        assertTrue(Utf8.isWellFormedSlowPath(bytes2, 0, 4));
        byte[] bytes3 = "€".getBytes();
        assertTrue(Utf8.isWellFormedSlowPath(bytes3, 0, 3));
        byte[] bytes4 = "🌍".getBytes();
        assertTrue(Utf8.isWellFormedSlowPath(bytes4, 0, 8));

        byte[] bytes5 = "".getBytes();
        assertTrue(Utf8.isWellFormedSlowPath(bytes5, 0, 0));

        byte[] bytes6 = "Hello!\uFFFD".getBytes();
        assertFalse(Utf8.isWellFormedSlowPath(bytes6, 0, 7));
        byte[] bytes7 = "Hello\uFFFDTest".getBytes();
        assertFalse(Utf8.isWellFormedSlowPath(bytes7, 0, 13));
    }

}

