import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test the functionality of the toString method
        // Create a dummy ArchiveEntry object
        ArchiveEntry entry = new ArchiveEntry() {
            @Override
            public String getName() {
                return "main.c";
            }

            @Override
            public boolean isDirectory() {
                return false;
            }

            @Override
            public long getSize() {
                return 2000;
            }
        };

        // Expected result
        String expectedResult = "-    2000 main.c";

        // Compare the actual result with the expected result
        assertEquals(expectedResult, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Test the matchAsciiBuffer method with expected string "test" and buffer [116, 101, 115, 116]
        String expected = "test";
        byte[] buffer = {116, 101, 115, 116};

        // Check if buffer matches expected string, should return true
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        // Test the toAsciiBytes method with input string "test"
        String input = "test";

        // Expected result
        byte[] expectedResult = {116, 101, 115, 116};

        // Compare the actual result with the expected result
        assertArrayEquals(expectedResult, ArchiveUtils.toAsciiBytes(input));
    }

    @Test
    public void testToAsciiString() {
        // Test the toAsciiString method with input bytes [116, 101, 115, 116]
        byte[] input = {116, 101, 115, 116};

        // Expected result
        String expectedResult = "test";

        // Compare the actual result with the expected result
        assertEquals(expectedResult, ArchiveUtils.toAsciiString(input));
    }

    @Test
    public void testIsEqual() {
        // Test the isEqual method with two equal byte arrays [116, 101, 115, 116]
        byte[] buffer1 = {116, 101, 115, 116};
        byte[] buffer2 = {116, 101, 115, 116};

        // Compare buffer1 and buffer2, should return true
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithNull() {
        // Test the isEqualWithNull method with two equal byte arrays [116, 101, 115, 116]
        byte[] buffer1 = {116, 101, 115, 116};
        byte[] buffer2 = {116, 101, 115, 116};

        // Compare buffer1 and buffer2, should return true
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        // Test the isArrayZero method with an array [0, 0, 0, 0]
        byte[] array = {0, 0, 0, 0};

        // Check if the array is zero, should return true
        assertTrue(ArchiveUtils.isArrayZero(array, array.length));
    }

    @Test
    public void testSanitize() {
        // Test the sanitize method with input string "te\0st"
        String input = "te\0st";

        // Expected result
        String expectedResult = "te?st";

        // Compare the actual result with the expected result
        assertEquals(expectedResult, ArchiveUtils.sanitize(input));
    }
}