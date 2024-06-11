
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutput;

    @Before
    public void setUp() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        cpioOutput = new CpioArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutput.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutput.putArchiveEntry(entry);
        cpioOutput.closeArchiveEntry();
        assertTrue(cpioOutput.isEntryOpen());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutput.putArchiveEntry(entry);
        cpioOutput.closeArchiveEntry();
        assertFalse(cpioOutput.isEntryOpen());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutput.putArchiveEntry(entry);
        byte[] data = "test".getBytes();
        cpioOutput.write(data);
        cpioOutput.closeArchiveEntry();
        assertEquals(data.length, entry.getSize());
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutput.finish();
        assertTrue(cpioOutput.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        cpioOutput.close();
        assertTrue(cpioOutput.isClosed());
    }
}
