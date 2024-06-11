package org.apache.commons.compress.archivers.tar;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    private byte[] testBuffer;

    @Before
    public void setUp(){
        testBuffer = new byte[10];
    }

    @Test
    public void testParseOctal() {
        testBuffer[0] = 0;
        testBuffer[1] = 3;
        testBuffer[2] = 5;

        long result = TarUtils.parseOctal(testBuffer, 0, 3);

        assertEquals(35, result);
    }

    @Test
    public void testParseOctal_invalidInput() {
        try {
            TarUtils.parseOctal(testBuffer, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testParseOctalOrBinary_octal() {
        testBuffer[0] = 0;
        testBuffer[1] = 3;
        testBuffer[2] = 5;

        long result = TarUtils.parseOctalOrBinary(testBuffer, 0, 3);

        assertEquals(35, result);
    }

    @Test
    public void testParseOctalOrBinary_binary() {
        testBuffer[0] = -128;

        long result = TarUtils.parseOctalOrBinary(testBuffer, 0, 1);

        assertEquals(-128, result);
    }

    @Test
    public void testParseBoolean_true() {
        testBuffer[0] = 1;

        assertTrue(TarUtils.parseBoolean(testBuffer, 0));
    }

    @Test
    public void testParseName() {
        String name = "testName\0";
        byte[] nameBytes = name.getBytes();

        String result = TarUtils.parseName(nameBytes, 0, nameBytes.length);

        assertEquals("testName", result);
    }

    // Add more test cases for other public methods

}