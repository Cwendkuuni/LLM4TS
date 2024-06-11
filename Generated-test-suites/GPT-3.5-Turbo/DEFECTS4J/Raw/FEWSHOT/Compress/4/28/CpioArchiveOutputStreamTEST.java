import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class CpioArchiveOutputStreamTest {
    private ByteArrayOutputStream output;
    private CpioArchiveOutputStream cpioOutput;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        cpioOutput = new CpioArchiveOutputStream(output);
    }

    @After
    public void tearDown() throws Exception {
        cpioOutput.close();
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("file1");
        entry.setSize(10);
        entry.setMode(0644);
        cpioOutput.putArchiveEntry(entry);

        assertEquals(0, cpioOutput.getBytesWritten());

        byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        cpioOutput.write(data);
        cpioOutput.closeArchiveEntry();

        assertEquals(24, cpioOutput.getBytesWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("file1");
        entry.setSize(10);
        entry.setMode(0644);
        cpioOutput.putArchiveEntry(entry);

        assertEquals(0, cpioOutput.getBytesWritten());

        byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        cpioOutput.write(data);

        cpioOutput.closeArchiveEntry();

        assertEquals(24, cpioOutput.getBytesWritten());
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("file1");
        entry.setSize(10);
        entry.setMode(0644);
        cpioOutput.putArchiveEntry(entry);
        cpioOutput.closeArchiveEntry();

        cpioOutput.finish();

        assertEquals(68, cpioOutput.getBytesWritten());
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("file1");
        entry.setSize(10);
        entry.setMode(0644);
        cpioOutput.putArchiveEntry(entry);
        cpioOutput.closeArchiveEntry();
        cpioOutput.close();

        assertTrue(output.toByteArray().length > 0);
    }
}