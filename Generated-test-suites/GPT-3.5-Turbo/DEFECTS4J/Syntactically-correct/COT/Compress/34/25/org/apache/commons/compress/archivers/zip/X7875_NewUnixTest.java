import static org.junit.Assert.*;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Before;
import org.junit.Test;

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
    public void testGetUID() {
        extraField.setUID(1000);
        assertEquals(1000L, extraField.getUID());
    }

    @Test
    public void testGetGID() {
        extraField.setGID(1000);
        assertEquals(1000L, extraField.getGID());
    }

    @Test
    public void testSetUID() {
        extraField.setUID(1000);
        assertEquals(1000L, extraField.getUID());
    }

    @Test
    public void testSetGID() {
        extraField.setGID(1000);
        assertEquals(1000L, extraField.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        extraField.setUID(1000);
        extraField.setGID(1000);
        assertEquals(new ZipShort(6), extraField.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        extraField.setUID(1000);
        extraField.setGID(1000);
        assertEquals(new ZipShort(6), extraField.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        extraField.setUID(1000);
        extraField.setGID(1000);
        byte[] expectedData = { 1, 4, 0x00, 0x00, 0x03, 0xE8 };
        assertArrayEquals(expectedData, extraField.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertArrayEquals(new byte[0], extraField.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = { 1, 4, 0x00, 0x00, 0x03, 0xE8 };
        extraField.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000L, extraField.getUID());
        assertEquals(1000L, extraField.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = { 1, 4, 0x00, 0x00, 0x03, 0xE8 };
        extraField.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(1000L, extraField.getUID());
        assertEquals(1000L, extraField.getGID());
    }

    @Test
    public void testEquals() {
        extraField.setUID(1000);
        extraField.setGID(1000);

        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(1000);
        other.setGID(1000);

        assertTrue(extraField.equals(other));
    }

    @Test
    public void testHashCode() {
        extraField.setUID(1000);
        extraField.setGID(1000);

        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(1000);
        other.setGID(1000);

        assertEquals(extraField.hashCode(), other.hashCode());
    }
}