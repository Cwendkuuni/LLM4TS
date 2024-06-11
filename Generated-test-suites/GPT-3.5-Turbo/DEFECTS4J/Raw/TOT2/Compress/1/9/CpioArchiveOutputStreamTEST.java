import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructionWithFormat() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioArchiveOutputStream.FORMAT_NEW);
            assertNotNull(cpioOut);
        } catch (IOException e) {
            fail("Construction with format should not throw an IOException");
        }
    }

    @Test
    public void testPutNextEntry() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile");
            entry.setFileSize(5); // Testing edge case with file size of 5 bytes
            cpioOut.putNextEntry(entry);
            cpioOut.closeArchiveEntry();
            cpioOut.finish();
            assertEquals(110, out.size()); // Total size of the CPIO entry
        } catch (IOException e) {
            fail("Put next entry should not throw an IOException");
        }
    }

    @Test
    public void testWrite() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testfile");
            entry.setFileSize(10);
            cpioOut.putNextEntry(entry);
            cpioOut.write("1234567890".getBytes());
            cpioOut.closeArchiveEntry();
            cpioOut.finish();
            assertEquals(168, out.size()); // Total size of the CPIO entry with data
        } catch (IOException e) {
            fail("Write should not throw an IOException");
        }
    }

    @Test
    public void testClose() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
            cpioOut.close();
            assertTrue(cpioOut.isClosed());
        } catch (IOException e) {
            fail("Close should not throw an IOException");
        }
    }

    @Test
    public void testFinish() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
            cpioOut.finish();
            assertTrue(cpioOut.isFinished());
        } catch (IOException e) {
            fail("Finish should not throw an IOException");
        }
    }

}