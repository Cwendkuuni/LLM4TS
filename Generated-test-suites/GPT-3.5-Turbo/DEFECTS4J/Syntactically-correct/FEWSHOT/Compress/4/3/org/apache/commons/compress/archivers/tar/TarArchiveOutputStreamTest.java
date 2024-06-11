
package org.apache.commons.compress.archivers.tar;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;

    @Before
    public void setUp() {
        OutputStream outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.longFileMode);

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.longFileMode);

        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.longFileMode);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        assertTrue(tarOutput.haveUnclosedEntry);

        tarOutput.closeArchiveEntry();
        assertFalse(tarOutput.haveUnclosedEntry);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFilenameTruncate() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("verylongfilename", TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutput.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_LongFilenameGnu() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("verylongfilename", TarArchiveOutputStream.LONGFILE_GNU);
        tarOutput.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry_IncompleteWrite() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test Content".getBytes().length);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("Test".getBytes());
        tarOutput.closeArchiveEntry(); // Should throw an exception
    }

    @Test
    public void testFinish() throws Exception {
        tarOutput.finish();
        assertEquals(1024, ((ByteArrayOutputStream) tarOutput.out).size());
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntries() throws Exception {
        tarOutput.haveUnclosedEntry = true;
        tarOutput.finish();
    }

    @Test
    public void testClose() throws Exception {
        tarOutput.close();
        assertTrue(tarOutput.closed);
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutput.getRecordSize();
        assertEquals(512, recordSize);
    }

    @Test
    public void testWrite() throws Exception {
        String content = "Test Content";
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", content.getBytes().length);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(content.getBytes());
        tarOutput.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) tarOutput.out).toByteArray();
        byte[] expectedOutput = new byte[512];
        System.arraycopy(content.getBytes(), 0, expectedOutput, 0, content.getBytes().length);
        assertTrue(Arrays.equals(expectedOutput, output));
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedEntrySize() throws Exception {
        byte[] content = new byte[1024];
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", content.length / 2);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(content);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite_EOFRecord() throws Exception {
        tarOutput.writeEOFRecord();
        byte[] output = ((ByteArrayOutputStream) tarOutput.out).toByteArray();
        byte[] expectedOutput = new byte[512];
        assertTrue(Arrays.equals(expectedOutput, output));
    }

    @Test
    public void testFlush() {
        try {
            tarOutput.flush();
        } catch (Exception e) {
            fail("Flush method threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, entryName);
        assertTrue(entry instanceof TarArchiveEntry);
    }

}
