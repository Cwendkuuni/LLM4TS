package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'0', '7', '5', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        long expected = 075L;
        long result = TarUtils.parseOctal(buffer, 0, 10);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParseOctal() {
        byte[] buffer = {'a', 'b', 'c'};
        TarUtils.parseOctal(buffer, 0, 3);
    }

    // Add more basic test cases for other public methods in TarUtils

    @Test
    public void testEdgeCases() {
        // Test edge cases and exceptional scenarios
    }

    @Test
    public void testExceptionHandling() {
        // Test exception handling scenarios
    }

    // Merge all the individual test cases into a complete test file
}



// Add all the individual test cases here

