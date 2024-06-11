package org.apache.commons.compress.archivers.zip;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.zip.ZipException;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0x7875, x7875.getHeaderId().getValue());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        assertEquals(12345, x7875.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(54321);
        assertEquals(54321, x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {0x01, 0x04, 0x00, 0x00, 0x30, 0x39};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(4, x7875.getUID());
        assertEquals(12345, x7875.getGID());
    }

    @Test
    public void testEquals() throws CloneNotSupportedException {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = (X7875_NewUnix) x7875_1.clone();
        assertTrue(x7875_1.equals(x7875_2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        x7875.setGID(54321);
        assertEquals(2134020259, x7875.hashCode());
    }

}