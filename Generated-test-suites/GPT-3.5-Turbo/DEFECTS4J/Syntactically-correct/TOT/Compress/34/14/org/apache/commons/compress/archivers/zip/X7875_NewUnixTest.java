import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    private X7875_NewUnix extraField;

    @Before
    public void setUp() {
        extraField = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), extraField.getHeaderId());
    }

    @Test
    public void testGetUID_DefaultValue() {
        assertEquals(1000, extraField.getUID());
    }

    @Test
    public void testGetGID_DefaultValue() {
        assertEquals(1000, extraField.getGID());
    }

    @Test
    public void testSetUID() {
        extraField.setUID(1234L);
        assertEquals(1234, extraField.getUID());
    }

    @Test
    public void testSetGID() {
        extraField.setGID(5678L);
        assertEquals(5678, extraField.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expectedLength = new ZipShort(3);
        assertEquals(expectedLength, extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expectedLength = new ZipShort(3);
        assertEquals(expectedLength, extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expectedData = {1, 1, 1};
        assertArrayEquals(expectedData, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expectedData = {};
        assertArrayEquals(expectedData, extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {(byte) 0x01, (byte) 0x01, (byte) 0x01};
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, extraField.getUID());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(extraField.equals(extraField));
    }

    @Test
    public void testEquals_SameValues() {
        X7875_NewUnix other = new X7875_NewUnix();
        assertTrue(extraField.equals(other));
    }

    @Test
    public void testEquals_DifferentVersion() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.version = 2;
        assertFalse(extraField.equals(other));
    }

    @Test
    public void testEquals_DifferentUID() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(1234);
        assertFalse(extraField.equals(other));
    }

    @Test
    public void testEquals_DifferentGID() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setGID(5678);
        assertFalse(extraField.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = extraField.hashCode();
        assertEquals(hashCode, extraField.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "0x7875 Zip Extra Field: UID=1000 GID=1000";
        assertEquals(expectedString, extraField.toString());
    }
}