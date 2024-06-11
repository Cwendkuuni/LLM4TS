
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setUp() throws IOException {
        mockOutputStream = mock(OutputStream.class);
        tarOutputStream = new TarArchiveOutputStream(mockOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);

        tarOutputStream.finish();

        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        assertEquals(1024, actualBytes.length);

        for (byte actualByte : actualBytes) {
            assertEquals(0, actualByte);
        }
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();

        assertEquals(true, tarOutputStream.isClosed());

        tarOutputStream.close(); // Verify that no exceptions are thrown when closing twice
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);

        assertNotNull(tarOutputStream.getCurrentEntry());
        assertEquals(entry, tarOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryThrowsIOExceptionForLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFileTestFileTestFileTestFileTestFileTestFile");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutputStream.closeArchiveEntry();

        assertNull(tarOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryThrowsIOExceptionForIncompleteWrite() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutputStream.write(new byte[] {1}); // Write incomplete data
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutputStream.write(new byte[] {1, 2, 3});
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // Verify that the write is successful without any exceptions
    }

    @Test(expected = IOException.class)
    public void testWriteThrowsIOExceptionForExceedingEntrySize() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutputStream.write(new byte[TarConstants.DEFAULT_BLOCKSIZE + 1]);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);

        tarOutputStream.writeEOFRecord();

        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        assertEquals(TarConstants.DEFAULT_BLOCKSIZE, actualBytes.length);

        for (byte actualByte : actualBytes) {
            assertEquals(0, actualByte);
        }
    }

    @Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mockOutputStream = mock(OutputStream.class);
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);

        tarOutputStream.flush();

        // Verify that flush does not throw any exceptions
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, "entryName");

        assertEquals(TarArchiveEntry.class, entry.getClass());
    }
}
