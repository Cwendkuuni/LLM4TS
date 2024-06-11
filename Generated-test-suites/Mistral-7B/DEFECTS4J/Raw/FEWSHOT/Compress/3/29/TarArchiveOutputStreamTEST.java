package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        // Default is LONGFILE_ERROR
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarStream.getLongFileMode());

        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.getLongFileMode());

        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        tarStream.writeEOFRecord();

        byte[] eofRecord = tarStream.buffer.getRecord();
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], eofRecord);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        tarStream.finish();

        byte[] eofRecord1 = tarStream.buffer.getRecord();
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], eofRecord1);

        tarStream.finish();

        byte[] eofRecord2 = tarStream.buffer.getRecord();
        assertArrayEquals(new byte[TarConstants.RECORD_SIZE], eofRecord2);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        tarStream.close();

        assertTrue(tarStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutLongLink() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry entry = new TarArchiveEntry(new ByteArrayInputStream(new byte[0]), longFileName);

        tarStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameUsingLongLink() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry entry = new TarArchiveEntry(new ByteArrayInputStream(new byte[0]), longFileName);

        byte[] nameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                TarConstants.LF_GNUTYPE_LONGNAME);
        longLinkEntry.setSize(nameBytes.length + 1); // +1 for NUL

        tarStream.putArchiveEntry(longLinkEntry);
        tarStream.write(nameBytes);
        tarStream.write(0); // NUL terminator
        tarStream.closeArchiveEntry();

        TarArchiveEntry actualEntry = tarStream.getNextEntry();
        assertEquals(entry.getName(), actualEntry.getName());
    }

    @Test
    public void testPutArchiveEntryWithSmallFile() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        String fileName = "test.txt";
        String fileContent = "Hello, world!";

        TarArchiveEntry entry = new TarArchiveEntry(new ByteArrayInputStream(fileContent.getBytes()), fileName);

        tarStream.putArchiveEntry(entry);
        tarStream.write(fileContent.getBytes());
        tarStream.closeArchiveEntry();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputTarStream = new TarArchiveOutputStream(outputStream);
        outputTarStream.putArchiveEntry(entry);
        outputTarStream.write(fileContent.getBytes());
        outputTarStream.closeArchiveEntry();
        outputTarStream.finish();
        outputTarStream.close();

        String actualContent = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(fileContent, actualContent);
    }

    @Test
    public void testWriteWithBufferedData() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        byte[] data = "Hello, world!".getBytes();
        int offset = 0;
        int numToWrite = data.length;

        tarStream.write(data, offset, numToWrite);

        byte[] actualData = tarStream.buffer.getRecord();

        assertArrayEquals(data, actualData);
    }

    @Test
    public void testWriteWithSmallBufferedData() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        byte[] data = "Hello".getBytes();
        int offset = 0;
        int numToWrite = data.length;

        tarStream.write(data, offset, numToWrite);

        byte[] assemBuf = tarStream.assemBuf;
        byte[] recordBuf = tarStream.recordBuf;

        assertTrue(numToWrite < recordBuf.length);

        System.arraycopy(assemBuf, 0, recordBuf, 0, numToWrite);

        byte[] actualData = Arrays.copyOfRange(recordBuf, 0, numToWrite);

        assertArrayEquals(data, actualData);
    }

    @Test
    public void testWriteWithBufferedDataAssemblingNewRecord() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        byte[] data1 = "Hello,".getBytes();
        byte[] data2 = "world!".getBytes();
        int numToWrite1 = data1.length;
        int numToWrite2 = data2.length;

        tarStream.write(data1, 0, numToWrite1);
        tarStream.write(data2, 0, numToWrite2);

        byte[] assemBuf = tarStream.assemBuf;
        byte[] recordBuf = tarStream.recordBuf;

        assertFalse(numToWrite1 + numToWrite2 <= recordBuf.length);

        int assemLen = tarStream.assemLen;

        System.arraycopy(data1, 0, assemBuf, 0, numToWrite1);
        System.arraycopy(data2, 0, assemBuf, numToWrite1, numToWrite2);

        buffer.writeRecord(assemBuf);

        byte[] actualData = Arrays.copyOfRange(recordBuf, 0, numToWrite1 + numToWrite2);

        assertArrayEquals(Arrays.concat(data1, data2), actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithTooMuchData() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        byte[] data = new byte[TarConstants.RECORD_SIZE * 2 + 1];

        tarStream.write(data, 0, data.length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new MemoryOutputStream());

        File testFile = new File("test.txt");
        String fileName = "test.txt";

        ArchiveEntry entry = tarStream.createArchiveEntry(testFile, fileName);

        assertEquals(fileName, entry.getName());
    }
}

class MemoryOutputStream extends OutputStream {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
        byteArrayOutputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byteArrayOutputStream.write(b, off, len);
    }

    public byte[] toByteArray() {
        return byteArrayOutputStream.toByteArray();
    }
}