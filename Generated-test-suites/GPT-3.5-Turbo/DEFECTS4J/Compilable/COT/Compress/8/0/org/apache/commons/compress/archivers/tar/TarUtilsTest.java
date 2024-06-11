package org.apache.commons.compress.archivers.tar;

// JUnit test cases for TarUtils class

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test normal case
        byte[] buffer = {'1', '2', '3', '4', '5', '6', '7', '0'};
        long result = TarUtils.parseOctal(buffer, 0, 8);
        assertEquals(12345670, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        // Test case with invalid byte
        byte[] buffer = {'1', '2', '3', '4', '5', '6', '8', '0'};
        TarUtils.parseOctal(buffer, 0, 8);
    }

    // Add more test cases for other public methods

}