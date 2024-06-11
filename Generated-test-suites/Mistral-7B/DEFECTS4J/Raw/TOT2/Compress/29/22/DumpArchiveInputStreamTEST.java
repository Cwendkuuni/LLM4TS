import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class DumpArchiveInputStreamTest {

    @Test
    public void testDumpArchiveInputStream_ValidArchive() throws IOException, ArchiveException {
        // Given
        byte[] archiveData = createTestArchive();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        // When
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();

        // Then
        Assert.assertNotNull(entry);
        Assert.assertEquals("test", entry.getName());

        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test2", entry.getName());

        // Cleanup
        dumpArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testDumpArchiveInputStream_InvalidArchive() throws IOException {
        // Given
        byte[] archiveData = createInvalidArchive();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData);

        // When & Then
        DumpArchiveInputStream dumpArchiveInputStream = null;
        try {
            dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);
        } finally {
            inputStream.close();
        }

        Assert.assertNull(dumpArchiveInputStream);
    }

    @Test(expected = EOFException.class)
    public void testDumpArchiveInputStream_EndOfStream() throws IOException, ArchiveException {
        // Given
        byte[] archiveData = createTestArchive();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        // When
        DumpArchiveEntry entry = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            // Do nothing, just read all entries
        }

        // Then
        Assert.assertFalse(dumpArchiveInputStream.hasNextEntry());
        dumpArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testDumpArchiveInputStream_InvalidRecord() throws IOException, ArchiveException {
        // Given
        byte[] archiveData = createInvalidRecordArchive();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        // When
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();

        // Then
        Assert.assertNull(entry);

        // Cleanup
        dumpArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testDumpArchiveInputStream_CustomEncoding() throws IOException, ArchiveException {
        // Given
        byte[] archiveData = createTestArchive();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveData);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");

        // When
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();

        // Then
        Assert.assertNotNull(entry);
        Assert.assertEquals("test", entry.getName());

        // Cleanup
        dumpArchiveInputStream.close();
        inputStream.close();
    }

    private byte[] createTestArchive() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 1, 1, ".", 0, 0, 1, false, null, null));
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 2, 2, "test", 10, 10, 1, true, null, null));
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 3, 3, "test2", 20, 20, 1, true, null, null));
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 4, 4, ".", 0, 0, 1, false, null, null));
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 5, 5, ".", 0, 0, 1, false, null, null));

        byte[] magicBytes = new byte[]{0x41, 0x53, 0x43, 0x4D, 0x41, 0x50, 0x50, 0x45, 0x42, 0x53, 0x45, 0x54};
        ByteStreams.write(outputStream, DumpArchiveUtil.addChecksum(magicBytes, outputStream.toByteArray()));

        return outputStream.toByteArray();
    }

    private byte[] createInvalidArchive() {
        return new byte[0];
    }

    private byte[] createInvalidRecordArchive() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 1, 1, ".", 0, 0, 1, false, null, null));
        ByteStreams.write(outputStream, new byte[]{0x41, 0x53, 0x43, 0x4D, 0x41, 0x50, 0x50, 0x45, 0x42, 0x53, 0x45, 0x54});
        ByteStreams.write(outputStream, new DumpArchiveEntry(new byte[0], 2, 2, "test", 10, 10, 1, true, null, null));

        return outputStream.toByteArray();
    }
}