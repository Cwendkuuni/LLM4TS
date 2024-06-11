import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(out, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "This is a test".getBytes();

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedRecord = getExpectedRecord("test.txt", testData);
        byte[] actualRecord = byteArrayOutputStream.toByteArray();

        Assert.assertArrayEquals("Unexpected record written", expectedRecord, actualRecord);
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        String longFileName = "a/long/filename/with/many/parts.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);

        byte[] longFileNameBytes = longFileName.getBytes();
        byte[] expectedRecord = getExpectedRecordWithLongFileName(longFileNameBytes);
        byte[] actualRecord = byteArrayOutputStream.toByteArray();

        Assert.assertArrayEquals("Unexpected record written", expectedRecord, actualRecord);
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileNameWithErrorMode() throws IOException {
        String longFileName = "a/long/filename/with/many/parts.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarOutputStream.putArchiveEntry(entry);

        byte[] longFileNameBytes = longFileName.getBytes();
        byte[] expectedRecord = getExpectedRecordWithLongFileName(longFileNameBytes);

        tarOutputStream.write(expectedRecord); // should throw an exception
    }

    @Test
    public void testFinishAndClose() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "This is a test".getBytes();

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        byte[] expectedEOFRecord = getExpectedEOFRecord();
        byte[] actualEOFRecord = new ByteArrayInputStream(out.toByteArray()).readAllBytes();

        Assert.assertArrayEquals("Unexpected EOF record written", expectedEOFRecord, actualEOFRecord);
    }

    private byte[] getExpectedRecord(String entryName, byte[] data) {
        byte[] header = new TarArchiveEntry(entryName).getHeaderBytes();
        byte[] recordData = new byte[data.length + TarConstants.RECORD_SIZE - TarConstants.HEADER_SIZE];
        System.arraycopy(data, 0, recordData, 0, data.length);
        return merge(header, recordData);
    }

    private byte[] getExpectedRecordWithLongFileName(byte[] fileNameBytes) {
        byte[] longLinkName = new byte[fileNameBytes.length + 1];
        System.arraycopy(fileNameBytes, 0, longLinkName, 0, fileNameBytes.length);
        longLinkName[fileNameBytes.length] = (byte) 0;

        byte[] header = new TarArchiveEntry("", TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME).getHeaderBytes();
        byte[] recordData = merge(longLinkName, new byte[TarConstants.NAMELEN]);

        return merge(header, recordData);
    }

    private byte[] getExpectedEOFRecord() {
        return new byte[TarConstants.RECORD_SIZE];
    }

    private byte[] merge(byte[]... bytes) {
        int length = 0;
        for (byte[] array : bytes) {
            length += array.length;
        }

        byte[] result = new byte[length];
        int index = 0;
        for (byte[] array : bytes) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }
        return result;
    }
}