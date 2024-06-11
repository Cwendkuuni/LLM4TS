
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class TarArchiveOutputStreamTest {
    @Test
    public void testConstructor() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOS.getLongFileMode());
        
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOS.getLongFileMode());
        
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOS.getLongFileMode());

        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testGetRecordSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, 512, 512);
        assertEquals(512, tarOS.getRecordSize());

        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.write(new byte[] {65, 66, 67});
        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        tarOS.writeEOFRecord();
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        tarOS.flush();
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        File inputFile = new File("file.txt");
        String entryName = "file.txt";
        ArchiveEntry entry = tarOS.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertEquals(entryName, entry.getName());
        tarOS.finish();
        tarOS.close();
        assertTrue(tarOS.isClosed());
    }
}
