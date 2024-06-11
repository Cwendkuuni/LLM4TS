import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class X7875_NewUnixTest {

    private X7875_NewUnix extraField;

    @Before
    public void setup() {
        extraField = new X7875_NewUnix();
    }

    @Test
    public void testGetUID() {
        extraField.setUID(1000);
        assertEquals(1000, extraField.getUID());
    }

    @Test
    public void testGetGID() {
        extraField.setGID(1000);
        assertEquals(1000, extraField.getGID());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {(byte) 0x01, (byte) 0x04, (byte) 0xE8, (byte) 0x03, (byte) 0xE8, (byte) 0x03};
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, extraField.getUID());
        assertEquals(1000, extraField.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(1000);
        other.setGID(1000);
        assertTrue(extraField.equals(other));
    }

    @Test
    public void testToString() {
        extraField.setUID(1000);
        extraField.setGID(1000);
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", extraField.toString());
    }

}