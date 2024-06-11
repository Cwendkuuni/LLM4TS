package org.apache.commons.compress.archivers.tar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;
    private OutputStream output;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(output);
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        assertTrue(tarOutput.isFinished());

        tarOutput.close();
        assertTrue(tarOutput.isClosed());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarOutput.finish();
        tarOutput.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarOutput.finish();
        tarOutput.putArchiveEntry(new TarArchiveEntry("test"));
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutEntry() throws IOException {
        tarOutput.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testDoubleCloseArchiveEntry() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("test"));
        tarOutput.closeArchiveEntry();
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWriteBytes() throws IOException {
        byte[] bytes = "test".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", bytes.length);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(bytes);
        tarOutput.closeArchiveEntry();
        tarOutput.close();

        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(entry.getSize(), outputBytes.length);

        for (int i = 0; i < bytes.length; i++) {
            assertEquals(bytes[i], outputBytes[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteBytesAfterFinish() throws IOException {
        byte[] bytes = "test".getBytes();
        tarOutput.finish();
        tarOutput.write(bytes);
    }

    @Test
    public void testWriteBytesOffsetLength() throws IOException {
        byte[] bytes = "test".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", bytes.length);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(bytes, 1, 2);
        tarOutput.closeArchiveEntry();
        tarOutput.close();

        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(entry.getSize(), outputBytes.length);

        assertEquals(bytes[1], outputBytes[0]);
        assertEquals(bytes[2], outputBytes[1]);
    }

    @Test
    public void testWriteSingleByte() throws IOException {
        byte b = 'a';
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", 1);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(b);
        tarOutput.closeArchiveEntry();
        tarOutput.close();

        byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
        assertEquals(entry.getSize(), outputBytes.length);

        assertEquals(b, outputBytes[0]);
    }

    @Test(expected = IOException.class)
    public void testWriteSingleByteAfterFinish() throws IOException {
        byte b = 'a';
        tarOutput.finish();
        tarOutput.write(b);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RECORDSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, "test.txt");
        assertEquals(inputFile, entry.getFile());
        assertEquals("test.txt", entry.getName());
    }

}