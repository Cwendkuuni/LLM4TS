package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputstream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test cases for {@link TarArchiveOutputStream} class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);

        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = new byte[TarConstants.STD_MAGIC + TarConstants.STD_NAME_LENGTH + 1];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedHeader, 0, TarConstants.STD_MAGIC.length);
        System.arraycopy(testFileName.getBytes(StandardCharsets.UTF_8), 0, expectedHeader, TarConstants.STD_MAGIC.length, testFileName.length());
        expectedHeader[TarConstants.STD_MAGIC.length + testFileName.length()] = (byte) 0;

        byte[] recordBuf = new byte[TarConstants.STD_RECORD_LENGTH];
        int readBytes = tarArchiveOutputStream.out.read(recordBuf, 0, recordBuf.length);

        Assert.assertEquals(expectedHeader.length, readBytes);
        Assert.assertArrayEquals(expectedHeader, recordBuf, 0, 0, expectedHeader.length);

        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String longFileName = "longFileNameThatIsLongerThanTarConstants.NAMELEN.length";
        File inputFile = new File("src/test/resources/" + longFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, longFileName);

        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[TarConstants.STD_MAGIC.length * 2];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedEOFRecord, 0, TarConstants.STD_MAGIC.length);

        byte[] recordBuf = new byte[TarConstants.STD_RECORD_LENGTH];
        int readBytes = tarArchiveOutputStream.out.read(recordBuf, 0, recordBuf.length);

        Assert.assertEquals(expectedEOFRecord.length, readBytes);
        Assert.assertArrayEquals(expectedEOFRecord, recordBuf, 0, 0, expectedEOFRecord.length);

        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(testData, 0, testData.length);

        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] recordBuf = new byte[TarConstants.STD_RECORD_LENGTH];
        int readBytes = tarArchiveOutputStream.out.read(recordBuf, 0, recordBuf.length);

        Assert.assertEquals(testData.length + expectedData.length, readBytes);

        System.arraycopy(expectedData, 0, recordBuf, TarConstants.STD_MAGIC.length, testData.length);
        Assert.assertArrayEquals(expectedData, recordBuf, TarConstants.STD_MAGIC.length, 0, testData.length);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(testData, 0, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithLongData() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = new byte[Integer.MAX_VALUE + 1];

        tarArchiveOutputStream.write(testData, 0, testData.length);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);

        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarConstants.STD_MAGIC.length * 2];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedEOFRecord, 0, TarConstants.STD_MAGIC.length);

        byte[] recordBuf = new byte[TarConstants.STD_RECORD_LENGTH];
        int readBytes = tarArchiveOutputStream.out.read(recordBuf, 0, recordBuf.length);

        Assert.assertEquals(expectedEOFRecord.length, readBytes);
        Assert.assertArrayEquals(expectedEOFRecord, recordBuf, 0, 0, expectedEOFRecord.length);

        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);

        tarArchiveOutputStream.flush();

        byte[] recordBuf = new byte[TarConstants.STD_RECORD_LENGTH];
        int readBytes = tarArchiveOutputStream.out.read(recordBuf, 0, recordBuf.length);

        Assert.assertEquals(testData.length, readBytes);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFlushWithClosedArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.flush();
    }

    @Test(expected = IOException.class)
    public void testFlushWithUnclosedEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputstream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + testFileName);
        ArchiveEntry entry = new TarArchiveEntry(inputFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);

        tarArchiveOutputStream.flush();
        tarArchiveOutputStream.closeArchiveEntry();
    }
}