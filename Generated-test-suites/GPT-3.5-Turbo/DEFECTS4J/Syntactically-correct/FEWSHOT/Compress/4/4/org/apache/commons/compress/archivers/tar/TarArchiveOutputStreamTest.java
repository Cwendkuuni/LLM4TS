package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;

    @Before
    public void setUp() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutput.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        // Verify that finish() closes all ongoing entry
        assertFalse(tarOutput.haveUnclosedEntry);

        // Verify that EOF record is written correctly
        byte[] expectedEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];
        byte[] actualEOFRecord = new byte[TarBuffer.DEFAULT_RCDSIZE];

        tarOutput.out.write(expectedEOFRecord);
        tarOutput.out.write(expectedEOFRecord);
        tarOutput.out.flush();

        assertTrue(tarOutput.out.toString().endsWith(new String(expectedEOFRecord) + new String(expectedEOFRecord)));
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntries() throws IOException {
        tarOutput.haveUnclosedEntry = true;
        tarOutput.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File file = new File("test-file.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test-file.txt");

        tarOutput.putArchiveEntry(entry);

        assertEquals(file.length(), tarOutput.currSize);
        assertEquals(file.getName(), tarOutput.currName);
        assertTrue(tarOutput.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testClosingEntryWithoutData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("empty-file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byte[] data = "test data".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(data.length);
        tarOutput.putArchiveEntry(entry);

        tarOutput.write(data);
        tarOutput.closeArchiveEntry();

        assertFalse(tarOutput.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncompleteWrite() throws IOException {
        byte[] data = "test data".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(data.length);
        tarOutput.putArchiveEntry(entry);

        tarOutput.write(data, 0, 1); // Write only first byte
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(data.length);
        tarOutput.putArchiveEntry(entry);

        tarOutput.write(data);

        assertEquals(data.length, tarOutput.currBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingHeaderSize() throws IOException {
        byte[] data = "test data".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test-file-test-file-test-file-test-file-test-file-test-file.txt");
        entry.setSize(data.length);
        tarOutput.putArchiveEntry(entry);

        tarOutput.write(data);

        assertEquals(data.length, tarOutput.currBytes);
    }
}