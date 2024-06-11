import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "longFileName".repeat(TarConstants.NAMELEN + 1);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile.txt"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String entryName = "testFile.txt";
        File inputFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedHeader = TarArchiveEntry.getHeaderBytes(entryName, TarConstants.FILETYPE_REGULAR, (int) inputFile.length(), 0);
        Assert.assertEquals(expectedHeader.length, tarArchiveOutputStream.getBuffer().position());
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String entryName = "testFile.txt";
        File inputFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "testData".getBytes(StandardCharsets.UTF_8);
        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        tarArchiveOutputStream.write(expectedData);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedDataWithHeader = new byte[expectedData.length + archiveEntry.getHeader().length];
        System.arraycopy(archiveEntry.getHeader(), 0, expectedDataWithHeader, 0, archiveEntry.getHeader().length);
        System.arraycopy(expectedData, 0, expectedDataWithHeader, archiveEntry.getHeader().length, expectedData.length);
        byte[] actualData = tarArchiveOutputStream.getBuffer().array();
        Assert.assertArrayEquals(expectedDataWithHeader, actualData);
    }

    @Test
    public void testFinishAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String entryName = "testFile.txt";
        File inputFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "testData".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getBuffer().array());
        ArchiveEntry actualEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals(archiveEntry.getName(), actualEntry.getName());
        Assert.assertEquals(archiveEntry.getSize(), actualEntry.getSize());
        byte[] expectedDataWithHeader = new byte[testData.length + archiveEntry.getHeader().length];
        System.arraycopy(archiveEntry.getHeader(), 0, expectedDataWithHeader, 0, archiveEntry.getHeader().length);
        System.arraycopy(testData, 0, expectedDataWithHeader, archiveEntry.getHeader().length, testData.length);
        byte[] actualData = new byte[1024];
        int bytesRead = inputStream.read(actualData, 0, actualData.length);
        Assert.assertEquals(expectedDataWithHeader.length, bytesRead);
        Assert.assertArrayEquals(expectedDataWithHeader, actualData, 0, expectedDataWithHeader.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String entryName = "testFile.txt";
        File inputFile = new File("testFile.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }
}