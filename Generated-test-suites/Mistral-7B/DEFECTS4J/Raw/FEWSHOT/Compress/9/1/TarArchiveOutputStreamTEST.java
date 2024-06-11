package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;

public class TarArchiveOutputStreamTest {
    private static final int RECORD_SIZE = 512;
    private static final int BUFFER_SIZE = 1024;
    private static final int BLOCK_SIZE = 128;

    @Test
    public void testFinishWithoutArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        tarArchiveOutputStream.finish();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] output = byteArrayOutputStream.toByteArray();

        TarBuffer tarBuffer = new TarBuffer(new ByteArrayInputStream(output), BLOCK_SIZE, RECORD_SIZE);

        byte[] eofRecord = new byte[RECORD_SIZE];
        for (int i = 0; i < eofRecord.length; ++i) {
            eofRecord[i] = 0;
        }

        byte[] expectedEOF = new byte[RECORD_SIZE * 2];
        System.arraycopy(eofRecord, 0, expectedEOF, 0, RECORD_SIZE);
        System.arraycopy(eofRecord, 0, expectedEOF, RECORD_SIZE, RECORD_SIZE);

        byte[] readEOF = tarBuffer.readRecord(eofRecord);
        assertArrayEquals("Unexpected EOF record", eofRecord, readEOF);

        assertEquals("Unexpected number of bytes written", expectedEOF.length, byteArrayOutputStream.size());
    }

    @Test
    public void testFinishWithArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(archiveEntry.getInputStream(), 0, archiveEntry.getInputStream().available());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] output = byteArrayOutputStream.toByteArray();

        TarBuffer tarBuffer = new TarBuffer(new ByteArrayInputStream(output), BLOCK_SIZE, RECORD_SIZE);

        byte[] eofRecord = new byte[RECORD_SIZE];
        for (int i = 0; i < eofRecord.length; ++i) {
            eofRecord[i] = 0;
        }

        byte[] longFileNameRecord = new byte[RECORD_SIZE];
        System.arraycopy(ArchiveUtils.toAsciiBytes("long_file_name.txt"), 0, longFileNameRecord, 0, "long_file_name.txt".length());
        longFileNameRecord[ArchiveConstants.LONG_NAME_LENGTH] = (byte) TarConstants.LF_GNUTYPE_LONGNAME;
        System.arraycopy(ArchiveUtils.toAsciiBytes("long_file_name.txt"), 0, longFileNameRecord, ArchiveConstants.FILE_NAME_LENGTH, "long_file_name.txt".length());

        byte[] headerRecord = new byte[RECORD_SIZE];
        ArchiveUtils.writeTarHeader(headerRecord, "test.txt", (short) 0, (short) 0, (short) 11, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, null);

        byte[] expected = new byte[output.length + 2 * RECORD_SIZE];
        System.arraycopy(longFileNameRecord, 0, expected, 0, longFileNameRecord.length);
        System.arraycopy(headerRecord, 0, expected, longFileNameRecord.length, headerRecord.length);
        System.arraycopy(output, 0, expected, longFileNameRecord.length + headerRecord.length, output.length);
        System.arraycopy(eofRecord, 0, expected, output.length + longFileNameRecord.length + headerRecord.length, eofRecord.length);
        System.arraycopy(eofRecord, 0, expected, output.length + longFileNameRecord.length + headerRecord.length + eofRecord.length, eofRecord.length);

        byte[] read = new byte[expected.length];
        int readLength = 0;

        while (readLength < expected.length) {
            int bytesRead = tarBuffer.read(read, readLength, expected.length - readLength);
            readLength += bytesRead;
        }

        assertArrayEquals("Unexpected archive content", expected, read);
    }

    @Test
    public void testCloseWithoutFinish() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);

        tarArchiveOutputStream.close();

        assertTrue("Unexpected closed flag", tarArchiveOutputStream.isClosed());
        assertFalse("Unexpected finished flag", tarArchiveOutputStream.isFinished());
    }

    @Test
    public void testCloseWithFinish() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        assertTrue("Unexpected closed flag", tarArchiveOutputStream.isClosed());
        assertTrue("Unexpected finished flag", tarArchiveOutputStream.isFinished());
    }
}