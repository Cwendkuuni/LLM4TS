
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream byteArrayOutputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals(entry, tarArchiveOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithExceededSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(100);
        byte[] fileContent = new byte[200]; // Exceed the specified size
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertNull(tarArchiveOutputStream.getCurrentEntry());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        byte[] fileContent = "Hello, World!".getBytes();

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                byteArrayOutputStream.toByteArray());
        TarArchiveEntry archivedEntry = tarArchiveInputStream.getNextTarEntry();
        assertNotNull(archivedEntry);
        assertEquals(entry.getName(), archivedEntry.getName());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveInputStream.read(outputStream);
        byte[] archivedContent = outputStream.toByteArray();

        assertEquals(fileContent.length, archivedContent.length);
        for (int i = 0; i < fileContent.length; i++) {
            assertEquals(fileContent[i], archivedContent[i]);
        }

        tarArchiveInputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                byteArrayOutputStream.toByteArray());
        TarArchiveEntry entry1 = tarArchiveInputStream.getNextTarEntry();
        assertNotNull(entry1);
        TarArchiveEntry entry2 = tarArchiveInputStream.getNextTarEntry();
        assertNotNull(entry2);

        tarArchiveInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                byteArrayOutputStream.toByteArray());
        TarArchiveEntry entry1 = tarArchiveInputStream.getNextTarEntry();
        assertNotNull(entry1);
        TarArchiveEntry entry2 = tarArchiveInputStream.getNextTarEntry();
        assertNotNull(entry2);

        tarArchiveInputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        String entryName = "file.txt";
        File inputFile = new File(entryName);
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        assertNotNull(archiveEntry);
        assertEquals(TarArchiveEntry.class, archiveEntry.getClass());
        assertEquals(entryName, archiveEntry.getName());
    }

    @Test
    public void testSetLongFileMode() {
        int mode = TarArchiveOutputStream.LONGFILE_GNU;
        tarArchiveOutputStream.setLongFileMode(mode);

        assertEquals(mode, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream mockOutputStream = new OutputStream() {
            private boolean flushed = false;
            @Override
            public void write(int b) throws IOException {
            }
            @Override
            public void flush() throws IOException {
                flushed = true;
            }
            boolean isFlushed() {
                return flushed;
            }
        };

        tarArchiveOutputStream = new TarArchiveOutputStream(mockOutputStream);
        tarArchiveOutputStream.flush();

        assertTrue(mockOutputStream.isFlushed());
    }
}
