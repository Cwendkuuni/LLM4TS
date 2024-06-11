import static org.junit.Assert.*;

import org.junit.Test;

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
    public void testGetLocalFileDataLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(3, extraField.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(3, extraField.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] data = {(byte) 1, (byte) 4, (byte) 0, (byte) 0, (byte) 0};
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, extraField.getUID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix extraField1 = new X7875_NewUnix();
        extraField1.setUID(1000);
        X7875_NewUnix extraField2 = new X7875_NewUnix();
        extraField2.setUID(1000);
        assertTrue(extraField1.equals(extraField2));
    }

}