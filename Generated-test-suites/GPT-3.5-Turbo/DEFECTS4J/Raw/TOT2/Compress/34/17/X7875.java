import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(0x7875, extraField.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(1000);
        assertEquals(1000, extraField.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(1000);
        assertEquals(1000, extraField.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(3, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(3, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix extraField1 = new X7875_NewUnix();
        X7875_NewUnix extraField2 = new X7875_NewUnix();
        assertTrue(extraField1.equals(extraField2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertNotNull(extraField.hashCode());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] data = {0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x01, 0x04, 0x00, 0x00, 0x00, 0x00};
        assertDoesNotThrow(() -> extraField.parseFromLocalFileData(data, 0, data.length));
    }

    @Test
    public void testClone() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertDoesNotThrow(() -> extraField.clone());
    }

    @Test
    public void testToString() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertNotNull(extraField.toString());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        byte[] result = X7875_NewUnix.trimLeadingZeroesForceMinLength(data);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result);
    }
}