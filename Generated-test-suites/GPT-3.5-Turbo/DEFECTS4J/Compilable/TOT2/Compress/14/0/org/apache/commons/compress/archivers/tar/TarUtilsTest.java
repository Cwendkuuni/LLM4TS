package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = { '1', '2', '3', '4', ' ' };
        assertEquals(1234, TarUtils.parseOctal(buffer, 0, 4));
    }

    @Test
    public void testParseOctalWithLeadingNUL() {
        byte[] buffer = { 0, '1', '2', '3', '4', ' ' };
        assertEquals(0, TarUtils.parseOctal(buffer, 0, 5));
    }

    @Test
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = { '1', '2', 'A', '4', ' ' };
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer, 0, 4);
        });
    }

    // Similar test cases for other public methods in TarUtils class
    
}