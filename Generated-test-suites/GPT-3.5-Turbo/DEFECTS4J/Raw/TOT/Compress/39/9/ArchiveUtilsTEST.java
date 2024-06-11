import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    // Test case for toString() method
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry() {
            @Override
            public long getSize() {
                return 2000;
            }

            @Override
            public boolean isDirectory() {
                return false;
            }

            @Override
            public String getName() {
                return "main.c";
            }
        };

        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    // Test case for matchAsciiBuffer() method with valid input
    @Test
    public void testMatchAsciiBuffer_validInput() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    // Test case for matchAsciiBuffer() method with invalid input
    @Test
    public void testMatchAsciiBuffer_invalidInput() {
        String expected = "Hello";
        byte[] buffer = "World".getBytes();

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    // Test case for toAsciiBytes() method
    @Test
    public void testToAsciiBytes() {
        String input = "Hello World";
        byte[] expected = input.getBytes();

        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(input));
    }

    // Test case for toAsciiString() method
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "Hello World".getBytes();
        String expected = "Hello World";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    // Test case for isEqual() method with equal buffers
    @Test
    public void testIsEqual_equalBuffers() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    // Test case for isEqual() method with unequal buffers
    @Test
    public void testIsEqual_unequalBuffers() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "World".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    // Test case for isArrayZero() method with all zeros
    @Test
    public void testIsArrayZero_allZeros() {
        byte[] array = new byte[5];

        assertTrue(ArchiveUtils.isArrayZero(array, 5));
    }

    // Test case for isArrayZero() method with non-zero elements
    @Test
    public void testIsArrayZero_nonZeroElements() {
        byte[] array = {1, 2, 3, 4, 5};

        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }

    // Test case for sanitize() method with printable characters
    @Test
    public void testSanitize_printableCharacters() {
        String input = "Hello World";
        String expected = "Hello World";

        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    // Test case for sanitize() method with non-printable characters
    @Test
    public void testSanitize_nonPrintableCharacters() {
        String input = "\u0000Hello\u001FWorld";
        String expected = "?Hello?World";

        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

}