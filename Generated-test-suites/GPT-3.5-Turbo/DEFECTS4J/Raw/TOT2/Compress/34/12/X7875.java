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
        extraField.setUID(123456789L);
        assertEquals(123456789L, extraField.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(987654321L);
        assertEquals(987654321L, extraField.getGID());
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
    public void testSetUIDAndGetUID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456789L);
        assertEquals(123456789L, extraField.getUID());
    }

    @Test
    public void testSetGIDAndGetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(987654321L);
        assertEquals(987654321L, extraField.getGID());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] expectedData = {1, 1, 123, 78, 5, -59, 93};
        assertArrayEquals(expectedData, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] expectedData = {};
        assertArrayEquals(expectedData, extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] data = {1, 1, 123, 78, 1, -59, 93};
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(123, extraField.getUID());
        assertEquals(93, extraField.getGID());
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
        int expectedHashCode = -1590103264;
        assertEquals(expectedHashCode, extraField.hashCode());
    }

    @Test
    public void testClone() {
        X7875_NewUnix extraField1 = new X7875_NewUnix();
        try {
            X7875_NewUnix extraField2 = (X7875_NewUnix) extraField1.clone();
            assertTrue(extraField1.equals(extraField2));
        } catch (CloneNotSupportedException e) {
            fail("Clone operation failed.");
        }
    }
}