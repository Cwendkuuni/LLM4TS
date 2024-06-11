

// JUnit 4 test cases for TarArchiveOutputStream

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(outputStream.toString().endsWith("\0\0"));
    }

    @Test(expected = IOException.class)
    public void testFinishFinishedArchive() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("TestEntry"));
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("TestEntry1"));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("TestEntry2"));
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testCloseFinishedArchive() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testWrite() throws IOException {
        String content = "Test content";
        byte[] contentBytes = content.getBytes();
        tarArchiveOutputStream.write(contentBytes);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        assertTrue(outputStream.toString().contains(content));
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("TestEntry");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        assertTrue(outputStream.toString().contains("TestEntry"));
    }

    @Test(expected = IOException.class)
    public void testCloseEntryWithoutEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseEntryWithUnfinishedEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("TestEntry"));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.closeArchiveEntry();
    }
}