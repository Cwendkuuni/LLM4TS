package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarArchiveOutputStreamTest {
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        output.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, output.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        output.putArchiveEntry(entry);
        assertEquals(entry, output.getCurrentEntry());
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        output.putArchiveEntry(entry);
        output.closeArchiveEntry();
        assertNull(output.getCurrentEntry());
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        byte[] buffer = new byte[10];
        try {
            output.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, output.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream output = new TarArchiveOutputStream(null);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = null;
        try {
            entry = output.createArchiveEntry(inputFile, entryName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(inputFile, entry.getFile());
        assertEquals(entryName, entry.getName());
    }
}