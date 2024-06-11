package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private CpioArchiveEntry cpioArchiveEntry;
    private String testFileName = "testFile.cpio";

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioArchiveEntry = new CpioArchiveEntry();
    }

    @After
    public void tearDown() throws IOException {
        cpioArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testConstructor() {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertNotNull(cpioArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 10);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveEntry.setSize(20);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        assertEquals("testFile.txt\0", getCpioHeaderName((byte[]) outputStream.toByteArray()));
        assertEquals(16, getCpioHeaderNameLength((byte[]) outputStream.toByteArray()));

        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        assertEquals("testFile.txt\0", getCpioHeaderName((byte[]) outputStream.toByteArray()));
        assertEquals(12, getCpioHeaderNameLength((byte[]) outputStream.toByteArray()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateEntry() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        String testString = "Test string";
        byte[] testBytes = testString.getBytes();

        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        byte[] outputBytes = (byte[]) outputStream.toByteArray();

        assertEquals(testBytes.length, outputBytes.length - getCpioHeaderNameLength(outputBytes) - 1);
        assertEquals(testString, new String(getCpioEntryData(outputBytes, 0), 0, testBytes.length));
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        String testString = "Test string";
        byte[] testBytes = testString.getBytes();

        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length + 1);
    }

    @Test
    public void testFinish() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        String testString = "Test string";
        byte[] testBytes = testString.getBytes();

        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.finish();

        byte[] outputBytes = (byte[]) outputStream.toByteArray();
        byte[] trailerBytes = getCpioTrailer((byte[]) outputStream.toByteArray());

        assertEquals(getCpioHeaderNameLength(outputBytes) + getCpioEntryDataLength(outputBytes, 0) + getCpioHeaderNameLength(trailerBytes) + getCpioEntryDataLength(trailerBytes, 0), outputBytes.length);
        assertEquals(trailerBytes[0], CpioConstants.MAGIC_TRAILER_NEW[0]);
        assertEquals(trailerBytes[1], CpioConstants.MAGIC_TRAILER_NEW[1]);
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        String testString = "Test string";
        byte[] testBytes = testString.getBytes();

        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.close();

        byte[] outputBytes = (byte[]) outputStream.toByteArray();
        byte[] trailerBytes = getCpioTrailer((byte[]) outputStream.toByteArray());

        assertEquals(getCpioHeaderNameLength(outputBytes) + getCpioEntryDataLength(outputBytes, 0) + getCpioHeaderNameLength(trailerBytes) + getCpioEntryDataLength(trailerBytes, 0), outputBytes.length);
        assertFalse(outputStream.hasCapacityFor(1));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        cpioArchiveEntry.setName("testFile.txt");
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);

        String testString = "Test string";
        byte[] testBytes = testString.getBytes();

        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] outputBytes = (byte[]) outputStream.toByteArray();
        byte[] trailerBytes = getCpioTrailer((byte[]) outputStream.toByteArray());

        assertEquals(getCpioHeaderNameLength(outputBytes) + getCpioEntryDataLength(outputBytes, 0) + getCpioHeaderNameLength(trailerBytes) + getCpioEntryDataLength(trailerBytes, 0), outputBytes.length);
        assertNull(cpioArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        cpioArchiveOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullInputStream() throws IOException {
        InputStream inputStream = null;
        cpioArchiveOutputStream.write(inputStream, 0, 0);
    }

    @Test
    public void testWriteWithEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        cpioArchiveOutputStream.write(inputStream, 0, 0);

        byte[] outputBytes = (byte[]) outputStream.toByteArray();
        byte[] trailerBytes = getCpioTrailer((byte[]) outputStream.toByteArray());

        assertEquals(getCpioHeaderNameLength(outputBytes) + getCpioEntryDataLength(outputBytes, 0) + getCpioHeaderNameLength(trailerBytes) + getCpioEntryDataLength(trailerBytes, 0), outputBytes.length);
    }

    private byte[] getCpioHeaderName(byte[] bytes) {
        int nameLength = getCpioHeaderNameLength(bytes);

        return new byte[nameLength + 1];
    }

    private int getCpioHeaderNameLength(byte[] bytes) {
        int length = 0;

        while (bytes[length] != 0) {
            length++;
        }

        return length;
    }

    private byte[] getCpioEntryData(byte[] bytes, int startIndex) {
        int length = 0;

        while (bytes[startIndex + length] != 0) {
            length++;
        }

        return new byte[length];
    }

    private int getCpioEntryDataLength(byte[] bytes, int startIndex) {
        int length = 0;

        while (bytes[startIndex + length] != 0) {
            length++;
        }

        return length;
    }

    private byte[] getCpioTrailer(byte[] bytes) {
        int nameLength = getCpioHeaderNameLength(bytes);
        int dataLength = getCpioEntryDataLength(bytes, 0);

        return new byte[nameLength + dataLength];
    }
}