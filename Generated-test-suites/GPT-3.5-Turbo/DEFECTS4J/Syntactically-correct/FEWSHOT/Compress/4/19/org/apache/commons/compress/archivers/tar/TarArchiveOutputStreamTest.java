
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals(entry.getName(), tarArchiveOutputStream.getCurrentEntry().getName());
        assertEquals(entry.getSize(), tarArchiveOutputStream.getCurrentEntry().getSize());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertNull(tarArchiveOutputStream.getCurrentEntry());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(10, tarArchiveOutputStream.getBytesWritten());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        // Two EOF records should be written
        assertEquals(1024, tarArchiveOutputStream.getBytesWritten());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.finish();
    }

    @Test
    public void testSetLongFileMode() {
        int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
        tarArchiveOutputStream.setLongFileMode(longFileMode);

        assertEquals(longFileMode, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();

        assertTrue(tarArchiveOutputStream.isClosed());
    }
}
