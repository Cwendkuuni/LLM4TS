
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals(0, tarArchiveOutputStream.getCurrBytes());
        assertEquals("testFile", tarArchiveOutputStream.getCurrName());
        assertTrue(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(0, tarArchiveOutputStream.getAssemLen());
        assertEquals(0, tarArchiveOutputStream.getCurrBytes());
        assertNull(tarArchiveOutputStream.getCurrName());
        assertFalse(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[5]);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[5]);
        assertEquals(5, tarArchiveOutputStream.getCurrBytes());

        tarArchiveOutputStream.write(new byte[10]);
        assertEquals(15, tarArchiveOutputStream.getCurrBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[15]);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        assertEquals(1024, output.length);
        assertEquals(0, output[1023]);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();

        byte[] output = outputStream.toByteArray();

        assertEquals(1024, output.length);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveEntry entry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(null, "testFile");
        assertEquals("testFile", entry.getName());
    }

}
