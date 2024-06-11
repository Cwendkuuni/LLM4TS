import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CpioArchiveOutputStreamTest {

    @Mock
    private OutputStream mockOutputStream;

    private CpioArchiveOutputStream archiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private CpioArchiveEntry cpioEntry;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        byteArrayOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        archiveOutputStream.putNextEntry(cpioEntry);
        assertEquals(cpioEntry, archiveOutputStream.getCpioEntry());
        assertEquals(0, archiveOutputStream.getWritten());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithClosedStream() throws IOException {
        archiveOutputStream.close();
        archiveOutputStream.putNextEntry(cpioEntry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidEntrySize() throws IOException {
        cpioEntry.setFileSize(10);
        archiveOutputStream.putNextEntry(cpioEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        archiveOutputStream.putNextEntry(cpioEntry);
        archiveOutputStream.write("12345".getBytes(), 0, 5);
        archiveOutputStream.closeArchiveEntry();
        assertNull(archiveOutputStream.getCpioEntry());
        assertEquals(0, archiveOutputStream.getWritten());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidEntrySize() throws IOException {
        archiveOutputStream.putNextEntry(cpioEntry);
        archiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidCRC() throws IOException {
        cpioEntry.setFormat(CpioArchiveOutputStream.FORMAT_NEW_CRC);
        archiveOutputStream.putNextEntry(cpioEntry);
        archiveOutputStream.write("12345".getBytes(), 0, 5);
        archiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        archiveOutputStream.putNextEntry(cpioEntry);
        archiveOutputStream.write("12345".getBytes(), 0, 5);
        archiveOutputStream.finish();
        CpioArchiveEntry trailerEntry = archiveOutputStream.getCpioEntry();
        assertEquals(0, trailerEntry.getMode());
        assertEquals("TRAILER!!!", trailerEntry.getName());
        archiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testClose() throws IOException {
        archiveOutputStream.close();
        assertTrue(archiveOutputStream.isClosed());
    }

    @Test
    public void testWrite() throws IOException {
        archiveOutputStream.putNextEntry(cpioEntry);
        archiveOutputStream.write("12345".getBytes(), 0, 5);
        assertEquals(5, archiveOutputStream.getWritten());
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        archiveOutputStream.close();
        archiveOutputStream.write("12345".getBytes(), 0, 5);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = cpioEntry;
        archiveOutputStream.putArchiveEntry(entry);

        assertEquals(entry, archiveOutputStream.getCpioEntry());
    }

    @Test
    public void testWriteByte() throws IOException {
        archiveOutputStream.write(97);
        byte[] outputBytes = byteArrayOutputStream.toByteArray();
        assertEquals(1, outputBytes.length);
        assertEquals(97, outputBytes[0]);
    }
}