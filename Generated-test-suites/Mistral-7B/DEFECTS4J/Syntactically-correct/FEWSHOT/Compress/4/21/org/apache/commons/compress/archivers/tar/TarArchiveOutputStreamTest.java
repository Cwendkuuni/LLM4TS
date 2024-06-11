package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinishAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        assertEquals(expectedEOFRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_LENGTH);
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.close();

        byte[] expectedEntryRecord = testEntry.getHeaderRecord();
        byte[] expectedData = new byte[1]; // One byte for testing purpose
        byte[] testData = new byte[] {expectedData[0]};

        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedEntryWithData = new byte[TarConstants.RECORD_LENGTH + testData.length];
        System.arraycopy(expectedEntryRecord, 0, expectedEntryWithData, 0, TarConstants.RECORD_LENGTH);
        System.arraycopy(testData, 0, expectedEntryWithData, TarConstants.RECORD_LENGTH, testData.length);

        byte[] output = outputStream.toByteArray();
        assertEquals(expectedEntryWithData, output, 0, expectedEntryWithData.length);
    }

    @Test
    public void testPutArchiveEntryAndFinishWithoutClosing() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] expectedEntryRecord = testEntry.getHeaderRecord();
        byte[] expectedData = new byte[1]; // One byte for testing purpose
        byte[] testData = new byte[] {expectedData[0]};

        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedEntryWithData = new byte[TarConstants.RECORD_LENGTH + testData.length];
        System.arraycopy(expectedEntryRecord, 0, expectedEntryWithData, 0, TarConstants.RECORD_LENGTH);
        System.arraycopy(testData, 0, expectedEntryWithData, TarConstants.RECORD_LENGTH, testData.length);

        byte[] output = outputStream.toByteArray();
        assertNotNull(output);

        byte[] expectedOutput = new byte[expectedEntryWithData.length + TarConstants.RECORD_LENGTH];
        System.arraycopy(expectedEntryWithData, 0, expectedOutput, 0, expectedEntryWithData.length);
        System.arraycopy(expectedEOFRecord, 0, expectedOutput, expectedEntryWithData.length, TarConstants.RECORD_LENGTH);

        assertEquals(expectedOutput, output, 0, expectedOutput.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        File longFileNameFile = new File("test/long/file/name.txt");
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileNameFile);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameWithLongFileTruncateMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        File longFileNameFile = new File("test/long/file/name.txt");
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileNameFile);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] expectedEntryRecord = testEntry.getShortNameRecord();

        byte[] output = outputStream.toByteArray();
        assertEquals(expectedEntryRecord, output, 0, expectedEntryRecord.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameWithLongFileGNUMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File longFileNameFile = new File("test/long/file/name.txt");
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileNameFile);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte longLinkHeaderRecord[] = testEntry.getLongLinkNameRecord();
        byte entryHeaderRecord[] = testEntry.getHeaderRecord();

        byte[] expectedOutput = new byte[expectedEOFRecord.length * 2 + longLinkHeaderRecord.length + entryHeaderRecord.length];

        System.arraycopy(expectedEOFRecord, 0, expectedOutput, 0, expectedEOFRecord.length);
        System.arraycopy(longLinkHeaderRecord, 0, expectedOutput, expectedEOFRecord.length, longLinkHeaderRecord.length);
        System.arraycopy(entryHeaderRecord, 0, expectedOutput, expectedEOFRecord.length + longLinkHeaderRecord.length, entryHeaderRecord.length);

        byte[] output = outputStream.toByteArray();
        assertEquals(expectedOutput, output, 0, expectedOutput.length);
    }
}