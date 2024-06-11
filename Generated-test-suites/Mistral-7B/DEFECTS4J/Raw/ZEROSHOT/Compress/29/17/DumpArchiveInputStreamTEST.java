import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DumpArchiveInputStreamTest {

    @Test
    public void testValidDumpArchive() throws IOException {
        byte[] data = generateDumpArchiveData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry = testStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test", entry.getName());
        Assert.assertEquals("test", entry.getSimpleName());
        Assert.assertEquals(5L, entry.getSize());
        Assert.assertEquals(0L, entry.getStart());

        byte[] buffer = new byte[5];
        int read = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, read);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidDumpArchive() throws IOException {
        byte[] data = generateInvalidDumpArchiveData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    @Test
    public void testGetSummary() throws IOException {
        byte[] data = generateDumpArchiveData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveSummary summary = testStream.getSummary();
        Assert.assertNotNull(summary);
        Assert.assertEquals(1, summary.getEntryCount());
        Assert.assertEquals("test.bin", summary.getEntryName(0));
        Assert.assertEquals("test", summary.getEntrySimpleName(0));
        Assert.assertEquals(5L, summary.getEntrySize(0));
        Assert.assertEquals(0L, summary.getStartPosition());
        Assert.assertEquals(1L, summary.getTotalSize());

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterEOF() throws IOException {
        byte[] data = generateDumpArchiveData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry firstEntry = testStream.getNextEntry();
        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testMissingEntryName() throws IOException {
        byte[] data = generateDumpArchiveDataWithMissingName();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testMissingEntrySize() throws IOException {
        byte[] data = generateDumpArchiveDataWithMissingSize();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testMissingEntryStart() throws IOException {
        byte[] data = generateDumpArchiveDataWithMissingStart();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidEntrySize() throws IOException {
        byte[] data = generateDumpArchiveDataWithInvalidSize();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidEntryStart() throws IOException {
        byte[] data = generateDumpArchiveDataWithInvalidStart();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream testStream = new DumpArchiveInputStream(inputStream, null);

        testStream.getNextEntry();

        testStream.close();
        inputStream.close();
    }

    private byte[] generateDumpArchiveData() {
        DumpArchiveEntry entry = new DumpArchiveEntry(
                1,
                1,
                1,
                "test.bin",
                "test",
                5,
                0,
                5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(entry.getHeaderBytes());
        outputStream.write(entry.getDirectoryBytes());

        byte[] headerBytes = outputStream.toByteArray();

        Map<Integer, Dirent> names = new HashMap<>();
        names.put(1, new Dirent(1, 1, 1, "test"));

        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteArrayOutputStream testDataOutputStream = new ByteArrayOutputStream();
        testDataOutputStream.write(testData);

        DumpArchiveSummary summary = new DumpArchiveSummary(headerBytes, null);

        byte[] data = new byte[headerBytes.length + testData.length];
        System.arraycopy(headerBytes, 0, data, 0, headerBytes.length);
        System.arraycopy(testData, 0, data, headerBytes.length, testData.length);

        return data;
    }

    private byte[] generateInvalidDumpArchiveData() {
        byte[] data = new byte[0];

        return data;
    }

    private byte[] generateDumpArchiveDataWithMissingName() {
        DumpArchiveEntry entry = new DumpArchiveEntry(
                1,
                1,
                1,
                0,
                5,
                0,
                5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(entry.getHeaderBytes());
        outputStream.write(entry.getDirectoryBytes());

        return outputStream.toByteArray();
    }

    private byte[] generateDumpArchiveDataWithMissingSize() {
        DumpArchiveEntry entry = new DumpArchiveEntry(
                1,
                1,
                1,
                "test.bin",
                0,
                5,
                0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(entry.getHeaderBytes());
        outputStream.write(entry.getDirectoryBytes());

        return outputStream.toByteArray();
    }

    private byte[] generateDumpArchiveDataWithMissingStart() {
        DumpArchiveEntry entry = new DumpArchiveEntry(
                1,
                1,
                1,
                "test.bin",
                "test",
                5,
                5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(entry.getHeaderBytes());
        outputStream.write(entry.getDirectoryBytes());

        return outputStream.toByteArray();
    }

    private byte[] generateDumpArchiveDataWithInvalidSize() {
        DumpArchiveEntry entry = new DumpArchiveEntry(
                1,
                1,
                1,
                "test.bin",
                "test",
                5,
                0,
                6);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(entry.getHeaderBytes());
        outputStream.write(entry.getDirectoryBytes());

        return outputStream.toByteArray();
    }

    private byte[] generateDumpArchiveDataWithInvalidStart() {
        DumpArchiveEntry entry = new DumpArchiveEntry(
                1,
                1,
                1,
                "test.bin",
                "test",
                5,
                6,
                5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(entry.getHeaderBytes());
        outputStream.write(entry.getDirectoryBytes());

        return outputStream.toByteArray();
    }
}