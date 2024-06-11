import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix instance = new X7875_NewUnix();
        ZipShort result = instance.getHeaderId();
        assertEquals(new ZipShort(0x7875), result);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(123456789);
        long result = instance.getUID();
        assertEquals(123456789, result);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setGID(987654321);
        long result = instance.getGID();
        assertEquals(987654321, result);        
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(123456789);
        long result = instance.getUID();
        assertEquals(123456789, result);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setGID(987654321);
        long result = instance.getGID();
        assertEquals(987654321, result);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix instance = new X7875_NewUnix();
        ZipShort result = instance.getLocalFileDataLength();
        assertEquals(new ZipShort(3), result);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix instance = new X7875_NewUnix();
        ZipShort result = instance.getCentralDirectoryLength();
        assertEquals(new ZipShort(3), result);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix instance = new X7875_NewUnix();
        byte[] result = instance.getLocalFileDataData();
        byte[] expected = {1, 0, 0};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix instance = new X7875_NewUnix();
        byte[] result = instance.getCentralDirectoryData();
        byte[] expected = {};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix instance = new X7875_NewUnix();
        byte[] data = {1, 0, 0};
        instance.parseFromLocalFileData(data, 0, 3);
        assertEquals(1, instance.getUID());
        assertEquals(0, instance.getGID());
    }

    @Test
    public void testReset() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(123456789);
        instance.setGID(987654321);
        instance.reset();
        assertEquals(1000, instance.getUID());
        assertEquals(1000, instance.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix instance1 = new X7875_NewUnix();
        instance1.setUID(123456789);
        instance1.setGID(987654321);

        X7875_NewUnix instance2 = new X7875_NewUnix();
        instance2.setUID(123456789);
        instance2.setGID(987654321);

        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix instance1 = new X7875_NewUnix();
        instance1.setUID(123456789);
        instance1.setGID(987654321);

        X7875_NewUnix instance2 = new X7875_NewUnix();
        instance2.setUID(123456789);
        instance2.setGID(987654321);

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

}