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
        extraField.setUID(123456789);
        assertEquals(123456789, extraField.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(987654321);
        assertEquals(987654321, extraField.getGID());
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
    public void testGetLocalFileDataData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123);
        extraField.setGID(456);
        byte[] expectedResult = {1, 1, 123, 1, 0, 1, 1, 0, 456};
        assertArrayEquals(expectedResult, extraField.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] rawData = {1, 1, 123, 1, 0, 1, 1, 0, 456};
        extraField.parseFromLocalFileData(rawData, 0, rawData.length);
        assertEquals(123, extraField.getUID());
        assertEquals(456, extraField.getGID());
    }

    @Test
    public void testEquals() throws Exception {
        X7875_NewUnix extraField1 = new X7875_NewUnix();
        extraField1.setUID(123);
        extraField1.setGID(456);

        X7875_NewUnix extraField2 = new X7875_NewUnix();
        extraField2.setUID(123);
        extraField2.setGID(456);

        assertTrue(extraField1.equals(extraField2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123);
        extraField.setGID(456);

        assertEquals(-1204920968, extraField.hashCode());
    }
}