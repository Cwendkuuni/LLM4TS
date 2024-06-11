package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    /**
     * Test constructor with correct format
     */
    @Test
    public void testCtorCorrectFormat() {
        assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    /**
     * Test constructor with incorrect format
     */
    @Test
    public void testCtorIncorrectFormat() {
        assertThrows(IllegalArgumentException.class, () -> new CpioArchiveOutputStream(outputStream, (short) 1234));
    }

    /**
     * Test method putNextEntry() with null entry
     */
    @Test
    public void testPutNextEntryNullEntry() {
        assertThrows(NullPointerException.class, () -> cpioOutputStream.putNextEntry(null));
    }

    /**
     * Test method putNextEntry() with already closed stream
     */
    @Test
    public void testPutNextEntryClosedStream() {
        cpioOutputStream.close();
        assertThrows(IOException.class, () -> cpioOutputStream.putNextEntry(new CpioArchiveEntry()));
    }

    /**
     * Test method putNextEntry() with duplicate entry
     */
    @Test
    public void testPutNextEntryDuplicateEntry() {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry1);
        assertThrows(IllegalArgumentException.class, () -> cpioOutputStream.putNextEntry(entry2));
    }

    /**
     * Test method writeHeader() with a valid CpioArchiveEntry
     */
    @Test
    public void testWriteHeaderValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.writeHeader(entry);
        byte[] header = outputStream.toByteArray();
        byte[] expectedHeader = new byte[header.length];
        System.arraycopy(MAGIC_NEW.getBytes(), 0, expectedHeader, 0, MAGIC_NEW.length);
        assertEquals(expectedHeader, header.subArray(0, MAGIC_NEW.length));
    }

    /**
     * Test method write() with a valid byte array
     */
    @Test
    public void testWriteValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        byte[] data = new byte[5];
        cpioOutputStream.write(data);
        byte[] expected = new byte[data.length + 1];
        System.arraycopy(data, 0, expected, 0, data.length);
        expected[data.length] = (byte) 0;
        byte[] actual = outputStream.toByteArray();
        assertEquals(expected, actual.subArray(cpioOutputStream.getWritten(),
                cpioOutputStream.getWritten() + data.length + 1));
    }

    /**
     * Test method write() with an empty byte array
     */
    @Test
    public void testWriteEmptyData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        byte[] data = {};
        cpioOutputStream.write(data);
        byte[] expected = new byte[0];
        byte[] actual = outputStream.toByteArray();
        assertEquals(expected, actual);
    }

    /**
     * Test method closeArchiveEntry() with a valid entry
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("data".getBytes());
        cpioOutputStream.closeArchiveEntry();
        byte[] expectedHeader = new byte[entry.getHeaderSize() + entry.getName().length() + 1];
        byte[] actualHeader = outputStream.toByteArray();
        assertEquals(expectedHeader, actualHeader.subArray(0, expectedHeader.length));
        assertNull(cpioOutputStream.cpioEntry);
        assertEquals(0, cpioOutputStream.written);
        assertEquals(0, cpioOutputStream.crc);
    }

    /**
     * Test method finish() with an empty stream
     */
    @Test
    public void testFinishEmptyStream() throws IOException {
        cpioOutputStream.finish();
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        assertEquals(entries, cpioOutputStream.getNames());
        assertFalse(cpioOutputStream.isFinished());
        assertNull(cpioOutputStream.cpioEntry);
        assertTrue(cpioOutputStream.isClosed());
    }

    /**
     * Test method finish() with a non-empty stream
     */
    @Test
    public void testFinishNonEmptyStream() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry1);
        cpioOutputStream.write("data1".getBytes());
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry2);
        cpioOutputStream.write("data2".getBytes());
        cpioOutputStream.finish();
        assertEquals(2, cpioOutputStream.getNames().size());

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        expectedEntries.put("data1", entry1);
        expectedEntries.put("data2", entry2);
        assertEquals(expectedEntries, cpioOutputStream.getNames());
        assertTrue(cpioOutputStream.isFinished());
        assertNull(cpioOutputStream.cpioEntry);
        assertTrue(cpioOutputStream.isClosed());
    }

    /**
     * Test method close() with an empty stream
     */
    @Test
    public void testCloseEmptyStream() throws IOException {
        cpioOutputStream.close();
        assertTrue(cpioOutputStream.isClosed());
    }

    /**
     * Test method close() with a non-empty stream
     */
    @Test
    public void testCloseNonEmptyStream() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("data".getBytes());
        byte[] expectedData = "data".getBytes();
        byte[] actualData = outputStream.toByteArray();
        assertEquals(expectedData, actualData.subArray(cpioOutputStream.getWritten(),
                cpioOutputStream.getWritten() + expectedData.length));
        cpioOutputStream.close();
        assertTrue(cpioOutputStream.isClosed());
        assertNull(cpioOutputStream.cpioEntry);
        assertEquals(0, cpioOutputStream.written);
        assertEquals(0, cpioOutputStream.crc);
    }

    /**
     * Test method write() with incorrect data length
     */
    @Test
    public void testWriteIncorrectDataLength() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        byte[] data = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> cpioOutputStream.write(data, 5, 3));
    }

    /**
     * Test method write() with incorrect offset
     */
    @Test
    public void testWriteIncorrectOffset() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        byte[] data = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> cpioOutputStream.write(data, -2, 3));
    }

    /**
     * Test method write() with incorrect number of bytes
     */
    @Test
    public void testWriteIncorrectNumberOfBytes() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        byte[] data = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> cpioOutputStream.write(data, 0, 5));
    }

    /**
     * Test method write() with incorrect data
     */
    @Test
    public void testWriteIncorrectDataFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> cpioOutputStream.write((Object) new Object(), 0, 0));
    }

    /**
     * Test method write() with incorrect data type
     */
    @Test
    public void testWriteIncorrectDataType() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> cpioOutputStream.write(1, 0, 0));
    }

    /**
     * Test method close() with a closed stream
     */
    @Test
    public void testCloseClosedStream() {
        cpioOutputStream.close();
        assertThrows(IllegalStateException.class, () -> cpioOutputStream.close());
    }

    /**
     * Test method finish() with a closed stream
     */
    @Test
    public void testFinishClosedStream() {
        cpioOutputStream.close();
        assertThrows(IllegalStateException.class, () -> cpioOutputStream.finish());
    }

    /**
     * Test method write() with a closed stream
     */
    @Test
    public void testWriteClosedStream() {
        cpioOutputStream.close();
        assertThrows(IllegalStateException.class, () -> cpioOutputStream.write(new byte[0], 0, 0));
    }

    /**
     * Test method putNextEntry() with a closed stream
     */
    @Test
    public void testPutNextEntryClosedStream() {
        cpioOutputStream.close();
        assertThrows(IllegalStateException.class, () -> cpioOutputStream.putNextEntry(new CpioArchiveEntry()));
    }

    /**
     * Test method closeArchiveEntry() with a null entry
     */
    @Test
    public void testCloseArchiveEntryNullEntry() {
        cpioOutputStream.closeArchiveEntry();
        assertNull(cpioOutputStream.cpioEntry);
    }

    /**
     * Test method closeArchiveEntry() with a closed stream
     */
    @Test
    public void testCloseArchiveEntryClosedStream() {
        cpioOutputStream.close();
        assertThrows(IllegalStateException.class, () -> cpioOutputStream.closeArchiveEntry());
    }
}