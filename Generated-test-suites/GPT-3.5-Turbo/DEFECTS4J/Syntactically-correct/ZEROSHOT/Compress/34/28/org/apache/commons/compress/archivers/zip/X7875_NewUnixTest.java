import org.junit.Test;
import java.math.BigInteger;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix field = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), field.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(12345);
        assertEquals(12345, field.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setGID(54321);
        assertEquals(54321, field.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(98765);
        assertEquals(98765, field.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setGID(56789);
        assertEquals(56789, field.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(12345);
        field.setGID(54321);
        assertEquals(new ZipShort(7), field.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(12345);
        field.setGID(54321);
        assertEquals(new ZipShort(7), field.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix field = new X7875_NewUnix();
        byte[] expected = {1, 4, 57, 0, 0, 0, 112, -91, 0, 0, 0};
        field.setUID(12345);
        field.setGID(54321);
        assertArrayEquals(expected, field.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix field = new X7875_NewUnix();
        byte[] expected = new byte[0];
        assertArrayEquals(expected, field.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix field = new X7875_NewUnix();
        byte[] data = {1, 4, 57, 0, 0, 0, 112, -91, 0, 0, 0};
        field.parseFromLocalFileData(data, 0, data.length);
        assertEquals(12345, field.getUID());
        assertEquals(54321, field.getGID());
    }

    @Test
    public void testReset() {
        X7875_NewUnix field = new X7875_NewUnix();
        field.setUID(12345);
        field.setGID(54321);
        field.reset();
        assertEquals(1000, field.getUID());
        assertEquals(1000, field.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix field = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", field.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix field1 = new X7875_NewUnix();
        field1.setUID(12345);
        field1.setGID(54321);
        X7875_NewUnix field2 = new X7875_NewUnix();
        field2.setUID(12345);
        field2.setGID(54321);
        X7875_NewUnix field3 = new X7875_NewUnix();
        field3.setUID(54321);
        field3.setGID(12345);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix field1 = new X7875_NewUnix();
        field1.setUID(12345);
        field1.setGID(54321);
        X7875_NewUnix field2 = new X7875_NewUnix();
        field2.setUID(12345);
        field2.setGID(54321);
        X7875_NewUnix field3 = new X7875_NewUnix();
        field3.setUID(54321);
        field3.setGID(12345);
        assertEquals(field1.hashCode(), field2.hashCode());
        assertNotEquals(field1.hashCode(), field3.hashCode());
    }
}