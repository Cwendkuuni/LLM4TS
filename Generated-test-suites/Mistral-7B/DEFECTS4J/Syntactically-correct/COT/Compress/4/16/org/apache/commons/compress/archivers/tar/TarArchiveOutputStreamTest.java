import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * TarArchiveOutputStreamTest class to test all methods in TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        byte[] eofRecord = tarArchiveOutputStream.recordBuf;
        for (int i = 0; i < eofRecord.length; i++) {
            Assert.assertEquals(0, eofRecord[i]);
        }
        tarArchiveOutputStream.writeEOFRecord();
        byte[] secondEofRecord = tarArchiveOutputStream.recordBuf;
        for (int i = 0; i < secondEofRecord.length; i++) {
            Assert.assertEquals(0, secondEofRecord[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String testFileName = "testFileName.txt";
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File(testFileName), "testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] headerRecord = tarArchiveOutputStream.recordBuf;
        Assert.assertArrayEquals(tarArchiveEntry.getHeaderBytes(), headerRecord);
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String testFileName = "testFileNameWithLengthGreaterThanNameLen.txt";
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File(testFileName), "testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testPutArchiveEntryWithGNULongFileNameExtension() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String testFileName = "testFileNameWithLengthGreaterThanNameLen/longLinkName";
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File(testFileName), "testEntry.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] longLinkNameRecord = tarArchiveOutputStream.recordBuf;
        byte[] testFileNameBytes = testFileName.getBytes(StandardCharsets.UTF_8);
        byte[] nameBytes = new byte[testFileNameBytes.length + 1]; // +1 for NUL terminator
        System.arraycopy(testFileNameBytes, 0, nameBytes, 0, testFileNameBytes.length);
        Assert.assertArrayEquals(new byte[]{TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME}, TarBuffer.getRecordTypeAndFlags(longLinkNameRecord));
        Assert.assertArrayEquals(nameBytes, TarBuffer.subrecord(longLinkNameRecord, 512, 512));
        tarArchiveOutputStream.write(nameBytes);
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("testFileName.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        byte[] fileContentRecord = tarArchiveOutputStream.recordBuf;
        Assert.assertEquals(testFileContent.length, TarBuffer.getDataLength(fileContentRecord));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testWriteWithBufferedBytes() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[testFileContent.length];
        System.arraycopy(testFileContent, 0, buffer, 0, testFileContent.length);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("testFileName.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testWriteWithLessBytesThanRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("testFileName.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length - 2);
        tarArchiveOutputStream.closeArchiveEntry();
    }
}