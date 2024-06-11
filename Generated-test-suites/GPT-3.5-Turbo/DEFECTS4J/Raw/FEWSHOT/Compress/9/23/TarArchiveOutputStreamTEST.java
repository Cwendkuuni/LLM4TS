
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertEquals(outputStream.toByteArray().length, entry.getSize());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello World".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        assertEquals(outputStream.toByteArray(), data);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishUnclosedEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        tarOutputStream.finish();
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(tarOutputStream.longFileMode, TarArchiveOutputStream.LONGFILE_ERROR);
    }
    
    @Test(expected = IOException.class)
    public void testCreateArchiveEntryAfterFinish() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
    }

    /**
     * Test cases for other methods in the TarArchiveOutputStream class...
     */
}

