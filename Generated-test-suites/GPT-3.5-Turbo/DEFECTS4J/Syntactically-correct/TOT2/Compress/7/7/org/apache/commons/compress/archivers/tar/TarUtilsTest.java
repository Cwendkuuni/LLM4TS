import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TarUtilsTest {

    private byte[] testBuffer;

    @Before
    public void setUp() {
        testBuffer = new byte[10];
    }

    @Test
    public void testParseOctal_validInput() {
        testBuffer[0] = '1';
        testBuffer[1] = '2';
        testBuffer[2] = '3';
        assertEquals(83, TarUtils.parseOctal(testBuffer, 0, 3));
    }

    @Test
    public void testParseOctal_invalidInput() {
        testBuffer[0] = '1';
        testBuffer[1] = '2';
        testBuffer[2] = '8';
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testBuffer, 0, 3));
    }

    @Test
    public void testParseName_validInput() {
        testBuffer[0] = 'T';
        testBuffer[1] = 'E';
        testBuffer[2] = 'S';
        testBuffer[3] = 'T';
        assertEquals("TEST", TarUtils.parseName(testBuffer, 0, 4));
    }

    @Test
    public void testFormatNameBytes_validInput() {
        String name = "TEST";
        int newOffset = TarUtils.formatNameBytes(name, testBuffer, 0, 4);
        assertEquals('T', testBuffer[0]);
        assertEquals('E', testBuffer[1]);
        assertEquals('S', testBuffer[2]);
        assertEquals('T', testBuffer[3]);
        assertEquals(4, newOffset);
    }

    @Test
    public void testFormatUnsignedOctalString_validInput() {
        TarUtils.formatUnsignedOctalString(100, testBuffer, 0, 3);
        assertEquals('1', testBuffer[0]);
        assertEquals('2', testBuffer[1]);
        assertEquals('4', testBuffer[2]);
    }

    @Test
    public void testComputeCheckSum_validInput() {
        testBuffer[0] = 1;
        testBuffer[1] = 2;
        testBuffer[2] = 3;
        assertEquals(6, TarUtils.computeCheckSum(testBuffer));
    }
}