import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = { '0', '1', '2', '3', ' ', ' ' };
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123L, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = { (byte) 0x80, 0, 0, 0 };
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 4);
        assertEquals(128L, result);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = { 1 };
        assertTrue(TarUtils.parseBoolean(buffer, 0));

        byte[] buffer2 = { 0 };
        assertFalse(TarUtils.parseBoolean(buffer2, 0));
    }

    // Additional test cases for other methods can be added here...

}