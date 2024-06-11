package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setup() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testConstructorWithOutputStream() throws IOException {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 1024);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 1024, 512);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[2 * TarConstants.RECORD_LENGTH];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEOFRecord[i] = 0;
        }

        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        byte[] actualEOFRecord = new byte[actualBytes.length - TarConstants.RECORD_LENGTH];
        System.arraycopy(actualBytes, TarConstants.RECORD_LENGTH, actualEOFRecord, 0, actualEOFRecord.length);

        Assert.assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.write(new byte[]{0x01});
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.close();

        byte[] expectedEOFRecord = new byte[2 * TarConstants.RECORD_LENGTH];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEOFRecord[i] = 0;
        }

        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        byte[] actualEOFRecord = new byte[actualBytes.length - TarConstants.RECORD_LENGTH];
        System.arraycopy(actualBytes, TarConstants.RECORD_LENGTH, actualEOFRecord, 0, actualEOFRecord.length);

        Assert.assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.write(new byte[]{0x01});
    }

    @Test
    public void testGetRecordSize() throws IOException {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertEquals(TarConstants.RECORD_LENGTH, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedHeaderRecord = new byte[TarConstants.RECORD_LENGTH];
        archiveEntry.writeEntryHeader(expectedHeaderRecord);

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] actualHeaderRecord = new byte[actualBytes.length - TarConstants.RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, actualHeaderRecord, 0, actualHeaderRecord.length);

        Assert.assertArrayEquals(expectedHeaderRecord, actualHeaderRecord);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new File("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[]{0x01, 0x02, 0x03});
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[]{0x01, 0x02, 0x03};
        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        int index = TarConstants.RECORD_LENGTH + TarConstants.NAME_LENGTH + archiveEntry.getName().length();
        byte[] actualData = new byte[actualBytes.length - index];
        System.arraycopy(actualBytes, index, actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnwrittenData() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        int dataLength = data.length;

        tarArchiveOutputStream.write(data, 0, dataLength);

        byte[] expectedData = new byte[dataLength + TarConstants.HEADER_LENGTH];
        System.arraycopy(archiveEntry.getHeader(), 0, expectedData, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(data, 0, expectedData, TarConstants.HEADER_LENGTH, dataLength);

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] actualData = new byte[actualBytes.length - TarConstants.RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[]{0x01, 0x02, 0x03});
        tarArchiveOutputStream.write(new byte[]{0x04, 0x05, 0x06});
    }

    @Test
    public void testFlush() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[]{0x01, 0x02, 0x03});
        tarArchiveOutputStream.flush();

        byte[] expectedData = new byte[]{0x01, 0x02, 0x03};
        byte[] actualBytes = byteArrayOutputStream.toByteArray();

        int index = TarConstants.RECORD_LENGTH + TarConstants.NAME_LENGTH + archiveEntry.getName().length();
        byte[] actualData = new byte[actualBytes.length - index];
        System.arraycopy(actualBytes, index, actualData, 0, actualData.length);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        byte[] expectedHeaderRecord = new byte[TarConstants.RECORD_LENGTH];
        archiveEntry.writeEntryHeader(expectedHeaderRecord);

        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] actualHeaderRecord = new byte[actualBytes.length - TarConstants.RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, actualHeaderRecord, 0, actualHeaderRecord.length);

        Assert.assertArrayEquals(expectedHeaderRecord, actualHeaderRecord);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullName() throws IOException {
        File file = new File("test.txt");
        tarArchiveOutputStream.createArchiveEntry(file, null);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        tarArchiveOutputStream.createArchiveEntry(null, "test.txt");
    }
}