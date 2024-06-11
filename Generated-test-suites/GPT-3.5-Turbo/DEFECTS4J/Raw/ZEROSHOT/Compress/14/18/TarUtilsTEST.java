import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 49;
        buffer[1] = 50;
        buffer[2] = 51;
        buffer[3] = 52;
        buffer[4] = 53;
        buffer[5] = 54;
        buffer[6] = 55;
        buffer[7] = 56;
        buffer[8] = 57;
        buffer[9] = ' ';
        
        long result = TarUtils.parseOctal(buffer, 0, 10);
        assertEquals(123456789L, result);
    }

    @Test
    public void testParseOctalWithLeadingZero() {
        byte[] buffer = new byte[8];
        buffer[0] = 48;
        buffer[1] = 49;
        buffer[2] = 50;
        buffer[3] = 51;
        buffer[4] = 52;
        buffer[5] = 53;
        buffer[6] = 54;
        buffer[7] = ' ';
        
        long result = TarUtils.parseOctal(buffer, 0, 8);
        assertEquals(123456L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[10];
        buffer[0] = 49;
        buffer[1] = 50;
        buffer[2] = 51;
        buffer[3] = 52;
        buffer[4] = 53;
        buffer[5] = 54;
        buffer[6] = 55;
        buffer[7] = 56;
        buffer[8] = 58;
        buffer[9] = ' ';
        
        TarUtils.parseOctal(buffer, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidTrailingByte() {
        byte[] buffer = new byte[10];
        buffer[0] = 49;
        buffer[1] = 50;
        buffer[2] = 51;
        buffer[3] = 52;
        buffer[4] = 53;
        buffer[5] = 54;
        buffer[6] = 55;
        buffer[7] = 56;
        buffer[8] = '9';
        buffer[9] = '9';
        
        TarUtils.parseOctal(buffer, 0, 10);
    }
    
    // Other Test methods for TarUtils class go here
    
}