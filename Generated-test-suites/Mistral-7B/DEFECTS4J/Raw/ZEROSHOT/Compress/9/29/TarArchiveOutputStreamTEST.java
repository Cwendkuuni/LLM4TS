import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        IOUtils.closeQuietly(tarArchiveOutputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(inputStream);

        ArchiveEntry archiveEntry = tarArchiveOutputStream2.getNextEntry();
        Assert.assertEquals("test.txt", archiveEntry.getName());

        byte[] readData = new byte[testData.length];
        int read = 0;
        while (read < readData.length) {
            read += tarArchiveOutputStream2.read(readData, read, readData.length - read);
        }
        Assert.assertArrayEquals(testData, readData);

        IOUtils.closeQuietly(tarArchiveOutputStream2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        IOUtils.closeQuietly(tarArchiveOutputStream);

        // This should throw an IOException
        tarArchiveOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // Set longFileMode to an invalid value
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testSetLongFileModeAndWriteLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // Set longFileMode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry("test/long/filename.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        IOUtils.closeQuietly(tarArchiveOutputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(inputStream);

        ArchiveEntry archiveEntry = tarArchiveOutputStream2.getNextEntry();
        Assert.assertEquals("test/long/filename.txt", archiveEntry.getName());

        IOUtils.closeQuietly(tarArchiveOutputStream2);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // Write an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Finish the archive
        tarArchiveOutputStream.finish();

        IOUtils.closeQuietly(tarArchiveOutputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(inputStream);

        // Get the next entry (there should be one)
        ArchiveEntry archiveEntry = tarArchiveOutputStream2.getNextEntry();

        // Verify that the entry is the one we wrote
        Assert.assertEquals("test.txt", archiveEntry.getName());

        // Read the data
        byte[] readData = new byte[testData.length];
        int read = 0;
        while (read < readData.length) {
            read += tarArchiveOutputStream2.read(readData, read, readData.length - read);
        }
        Assert.assertArrayEquals(testData, readData);

        // Close the archive (this should do nothing since we already finished it)
        tarArchiveOutputStream2.close();

        // Try to write to the archive again (this should throw an IOException)
        tarArchiveOutputStream2.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // Write an entry and close it
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Close the archive
        tarArchiveOutputStream.close();

        // Try to write to the archive again (this should throw an IOException)
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }
}