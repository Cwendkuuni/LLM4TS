import org.junit.Test;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);

        cpioOut.close();
        byte[] data = out.toByteArray();
        assertEquals("0707070000000000000000000000000000000000032435643043040000000000000050000000015465737466696c6500000000", byteArrayToHexString(data));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());
        cpioOut.closeArchiveEntry();

        cpioOut.close();
        byte[] data = out.toByteArray();
        assertEquals("0707070000000000000000000000000000000000032435643043040000000000000050000000015465737466696c6500000000313233343500000000000000000000000000000000", byteArrayToHexString(data));
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());
        cpioOut.closeArchiveEntry();

        cpioOut.close();
        byte[] data = out.toByteArray();
        assertEquals("0707070000000000000000000000000000000000032435643043040000000000000050000000015465737466696c6500000000313233343500000000000000000000000000000000", byteArrayToHexString(data));
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());
        cpioOut.closeArchiveEntry();
        cpioOut.finish();

        cpioOut.close();
        byte[] data = out.toByteArray();
        assertEquals("0707070000000000000000000000000000000000032435643043040000000000000050000000015465737466696c650000000031323334350000000000000000000000000000000000000000070705050500000000000000000000000000000000000b5441494c455221212100000000", byteArrayToHexString(data));
    }

    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}