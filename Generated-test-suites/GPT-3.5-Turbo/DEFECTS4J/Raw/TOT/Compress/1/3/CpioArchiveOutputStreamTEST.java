import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream output;
    private CpioArchiveOutputStream cpioOut;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        cpioOut = new CpioArchiveOutputStream(output);
    }

    @After
    public void tearDown() throws Exception {
        cpioOut.close();
        output.close();
    }

    @Test
    public void testPutNextEntry_typical() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOut.putNextEntry(entry);
        cpioOut.write("test data".getBytes());
        cpioOut.closeArchiveEntry();

        assertEquals("ENTRY \"" + entry.getName() + "\" HEADER CORRECT",
                "07070707070707077777777777777700000000000000001100000000000000001000000000000000000000000000000205465737420646174610A", bytesToHex(output.toByteArray()));
    }

    @Test
    public void testPutNextEntry_edge() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("");
        entry.setSize(0);
        entry.setFormat((short) 0);
        entry.setChksum(0);
        entry.setDevice(0);
        entry.setInode(0);
        entry.setMode(0);
        entry.setGID(0);
        entry.setNumberOfLinks(0);
        entry.setRemoteDevice(0);
        entry.setTime(0);
        cpioOut.putNextEntry(entry);

        assertEquals("ENTRY \"" + entry.getName() + "\" HEADER CORRECT",
                "c723bf4e0078c8e000000000000000000100000000000000000000000000000000e000000000000000000000000000001e00000000000000000000000000000000000000000000000000000000000002f0000000000000000000000000000004ce09d150078c8e0000", bytesToHex(output.toByteArray()));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntry_error() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOut.putNextEntry(entry);
        cpioOut.write("test data".getBytes());
        // Incorrectly closing entry
        cpioOut.close();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

}