import org.junit.Test;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        cpioOut.putArchiveEntry(entry);

        assertEquals(entry, cpioOut.getCurrentEntry());
    }

    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);

        byte[] data = "12345".getBytes();
        cpioOut.write(data, 0, data.length);

        assertEquals(data.length, cpioOut.getWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();

        assertNotNull(cpioOut.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testInvalidEntrySize() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        cpioOut.putArchiveEntry(entry);
        cpioOut.write("123".getBytes(), 0, 3);
        cpioOut.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);

        cpioOut.finish();

        assertEquals(1, cpioOut.getWritten());
    }

    @Test
    public void testClose() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(baos);

        cpioOut.close();
    }
}