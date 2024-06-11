package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyOutputStream() throws IOException {
        OutputStream os = new ByteArrayOutputStream(0);
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);
        tarStream.finish();
        tarStream.close();

        byte[] expectedEof = new byte[TarConstants.TAR_RECORD_LENGTH];
        Arrays.fill(expectedEof, (byte) 0);

        byte[] actualEof = ((ByteArrayOutputStream) os).toByteArray();
        assertArrayEquals("Unexpected EOF record", expectedEof, actualEof);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        String fileName = "testFile.txt";
        byte[] fileContent = "Hello, World!".getBytes();

        ArchiveEntry entry = new TarArchiveEntry(fileName);
        tarStream.putArchiveEntry(entry);

        writeBytesToOutputStream(tarStream, fileContent);
        tarStream.closeArchiveEntry();

        tarStream.finish();
        tarStream.close();

        byte[] expectedHeader = createExpectedHeader(fileName, fileContent.length);
        byte[] expectedContent = fileContent;

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();
        byte[] header = new byte[0];
        byte[] content = new byte[0];

        System.arraycopy(actualBytes, 0, header, 0, TarConstants.TAR_HEADER_LENGTH);
        System.arraycopy(actualBytes, TarConstants.TAR_HEADER_LENGTH, content, 0, fileContent.length);

        assertArrayEquals("Unexpected header", expectedHeader, header);
        assertArrayEquals("Unexpected content", expectedContent, content);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String longFileName = "very/long/filename.txt";
        byte[] fileContent = "Long filename test".getBytes();

        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarStream.putArchiveEntry(entry);

        writeBytesToOutputStream(tarStream, fileContent);
        tarStream.closeArchiveEntry();

        tarStream.finish();
        tarStream.close();

        byte[] expectedLongFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        int headerLength = TarConstants.TAR_HEADER_LENGTH + expectedLongFileNameBytes.length + 1;
        byte[] expectedHeader = createExpectedHeaderWithLongFileName(longFileName, fileContent.length);
        byte[] expectedContent = fileContent;

        byte[] header = new byte[headerLength];
        System.arraycopy(actualBytes, 0, header, 0, headerLength);

        assertArrayEquals("Unexpected header", expectedHeader, header);
        assertArrayEquals("Unexpected content", expectedContent, Arrays.copyOfRange(actualBytes, headerLength, actualBytes.length));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameTruncated() throws IOException {
        OutputStream os = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        String longFileName = "very/long/filename.txt";
        byte[] fileContent = "Long filename test".getBytes();

        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarStream.putArchiveEntry(entry);

        tarStream.finish(); // finish() must be called before closeArchiveEntry()
        tarStream.close();

        tarStream = new TarArchiveOutputStream(os, 512, 512);

        writeBytesToOutputStream(tarStream, fileContent);
        tarStream.closeArchiveEntry();

        tarStream.finish();
        tarStream.close();

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        // The long file name will be truncated in the archive
        byte[] expectedContent = fileContent;
        byte[] header = new byte[TarConstants.TAR_HEADER_LENGTH];

        System.arraycopy(actualBytes, 0, header, 0, TarConstants.TAR_HEADER_LENGTH);
        assertArrayEquals("Unexpected header", createExpectedHeader(longFileName.substring(0, TarConstants.NAMELEN), fileContent.length), header);
        assertArrayEquals("Unexpected content", expectedContent, Arrays.copyOfRange(actualBytes, TarConstants.TAR_HEADER_LENGTH, actualBytes.length));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameError() throws IOException {
        OutputStream os = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os, 512, 512);

        String longFileName = "very/long/filename.txt";
        byte[] fileContent = "Long filename test".getBytes();

        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarStream.putArchiveEntry(entry);

        tarStream.finish(); // finish() must be called before closeArchiveEntry()
        tarStream.close();

        tarStream = new TarArchiveOutputStream(os, 512, 512);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        writeBytesToOutputStream(tarStream, fileContent);
        tarStream.closeArchiveEntry();

        tarStream.finish();
        tarStream.close();

        byte[] actualBytes = ((ByteArrayOutputStream) os).toByteArray();

        // File name is too long for the archive
        assertArrayEquals("Unexpected content", new byte[0], Arrays.copyOfRange(actualBytes, TarConstants.TAR_HEADER_LENGTH, actualBytes.length));
    }

    private void writeBytesToOutputStream(OutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    private byte[] createExpectedHeader(String fileName, long fileSize) {
        TarHeader header = new TarHeader();
        header.setName(fileName);
        header.setSize(fileSize);
        header.setMode("rw-r--r--");
        header.setMtime(System.currentTimeMillis() / 1000L);
        header.setChecksum();
        header.writeTo(new ByteArrayOutputStream());

        return ((ByteArrayOutputStream) header.getOutputStream()).toByteArray();
    }

    private byte[] createExpectedHeaderWithLongFileName(String fileName, long fileSize) {
        TarHeader header = new TarHeader();
        header.setName(fileName);
        header.setSize(fileSize);
        header.setMode("rw-r--r--");
        header.setMtime(System.currentTimeMillis() / 1000L);
        header.setChecksum();

        byte[] longFileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        int headerLength = TarConstants.TAR_HEADER_LENGTH + longFileNameBytes.length + 1;

        header.writeTo(new ByteArrayOutputStream(headerLength));

        return ((ByteArrayOutputStream) header.getOutputStream()).toByteArray();
    }
}