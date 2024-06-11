package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class TarUtilsTest {

    // Testing parseOctal method with typical use case
    @Test
    public void testParseOctalTypical() {
        byte[] buffer = "     1234 ".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        assertEquals(1234L, result);
    }

    // Testing parseOctal method with edge case of all NULs
    @Test
    public void testParseOctalAllNULs() {
        byte[] buffer = {0, 0, 0, 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        assertEquals(0L, result);
    }

    // Testing parseOctal method with an invalid byte
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = "12A4 ".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // Continue with additional test cases for other methods...

}