package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testNewInstance() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, cpioOut.getFormat());
    }

    @Test
    public void testNewInstanceWithFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out,
                CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, cpioOut.getFormat());
    }

    @Test
    public void testPutNextEntryAndWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        String entryName = "testEntry.txt";
        long entrySize = 10;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entrySize);
        cpioOut.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        cpioOut.write(testData, 0, testData.length);

        byte[] expectedOutput = createExpectedOutput(entryName, testData);
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    @Test
    public void testPutNextEntryAndWriteWithFormat() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out,
                CpioConstants.FORMAT_OLD_BINARY);

        String entryName = "testEntry.bin";
        long entrySize = 10;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entrySize);
        cpioOut.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        cpioOut.write(testData, 0, testData.length);

        byte[] expectedOutput = createExpectedOutputBinary(entryName, testData);
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    @Test
    public void testPutNextEntryAndWriteWithSameName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        String entryName = "testEntry.txt";
        long entrySize = 10;

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName);
        entry1.setSize(entrySize);
        cpioOut.putNextEntry(entry1);

        byte[] testData = "Test data 1".getBytes();
        cpioOut.write(testData, 0, testData.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName);
        cpioOut.putNextEntry(entry2);

        assertThrows(IllegalStateException.class, () -> {
            byte[] testData2 = "Test data 2".getBytes();
            cpioOut.write(testData2, 0, testData2.length);
        });

        byte[] expectedOutput = createExpectedOutput(entryName, testData);
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(IllegalArgumentException.class, () -> {
            cpioOut.putNextEntry(entry);
            cpioOut.setFormat((short) 0xFFFF);
        });

        cpioOut.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        String entryName = "testEntry.txt";
        long entrySize = 10;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entrySize);
        cpioOut.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        cpioOut.write(testData, 0, testData.length);

        cpioOut.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutput(entryName, testData);
        assertArrayEquals(expectedOutput, out.toByteArray());

        assertNull(cpioOut.getCpioEntry());
        assertEquals(0, cpioOut.getWritten());
        assertEquals(0, cpioOut.getCrc());

        cpioOut.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOut.putNextEntry(entry);

        assertFalse(cpioOut.isFinished());

        cpioOut.finish();

        assertTrue(cpioOut.isFinished());

        byte[] expectedOutput = createTrailerBytes();
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    @Test
    public void testCloseWithFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOut.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        cpioOut.write(testData, 0, testData.length);

        cpioOut.finish();

        byte[] expectedOutput = createExpectedOutput("TRAILER!!!", testData);
        assertArrayEquals(expectedOutput, out.toByteArray());

        assertNull(cpioOut.getCpioEntry());
        assertEquals(0, cpioOut.getWritten());
        assertEquals(0, cpioOut.getCrc());

        cpioOut.close();
    }

    @Test
    public void testCloseWithDuplicateEntries() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);

        String entryName = "testEntry.txt";
        long entrySize = 10;

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName);
        entry1.setSize(entrySize);
        cpioOut.putNextEntry(entry1);

        byte[] testData1 = "Test data 1".getBytes();
        cpioOut.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName);
        cpioOut.putNextEntry(entry2);

        byte[] testData2 = "Test data 2".getBytes();
        cpioOut.write(testData2, 0, testData2.length);

        cpioOut.finish();

        byte[] expectedOutput = createExpectedOutput("TRAILER!!!",
                CpioUtil.concat(testData1, testData2));
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    private byte[] createExpectedOutput(String entryName, byte[] testData) {
        byte[] header = createHeaderBytes(entryName, testData.length,
                CpioConstants.FORMAT_NEW);
        byte[] footer = createFooterBytes();
        return CpioUtil.concat(header, testData, footer);
    }

    private byte[] createExpectedOutputBinary(String entryName, byte[] testData) {
        byte[] header = createHeaderBytesBinary(entryName, testData.length,
                CpioConstants.FORMAT_OLD_BINARY);
        byte[] footer = createFooterBytes();
        return CpioUtil.concat(header, testData, footer);
    }

    private byte[] createTrailerBytes() {
        String name = "TRAILER!!!";
        long size = 0;
        long crc = 0xFFFFFFFF;

        byte[] header = createHeaderBytes(name, size, CpioConstants.FORMAT_NEW);
        return CpioUtil.concat(header);
    }

    private byte[] createHeaderBytes(String entryName, long entrySize, short format) {
        String name = entryName;
        long size = entrySize;
        long crc = 0;

        byte[] nameBytes = name.getBytes();
        int nameLength = nameBytes.length;

        byte[] header = new byte[nameLength + 12];
        for (int i = 0; i < 8; i++) {
            writeAsciiLong(header, i * 8, nameLength, entrySize, format, name);
        }
        writeAsciiLong(header, 8 * 8, crc, format);
        System.arraycopy(nameBytes, 0, header, nameLength, nameLength);
        return header;
    }

    private byte[] createHeaderBytesBinary(String entryName, long entrySize,
            short format) {
        String name = entryName;
        long size = entrySize;

        int nameLength = name.length();

        byte[] header = new byte[nameLength + 16];
        writeBinaryLong(header, 0, false, nameLength, format, name);
        writeBinaryLong(header, nameLength, size, format);

        return header;
    }

    private void writeAsciiLong(byte[] dest, int offset, int length, long number,
            short format, String name) {
        for (int i = 0; i < length; i++) {
            int digit = (int) (name.charAt(i) - 48);
            if (digit < 0 || digit > 9) {
                digit = (int) ((number >>> (8 * (7 - i))) & 0xFF);
            }
            for (int j = 0; j < 8; j++) {
                dest[offset + i + j] = (byte) ((digit >> (8 - j)) & 0xFF);
            }
        }
    }

    private void writeBinaryLong(byte[] dest, int offset, boolean swapHalfWord,
            int length, short format, String name) {
        long number = Long.parseLong(name, (swapHalfWord ? 16 : 10));
        for (int i = 0; i < length; i++) {
            int byteIndex = offset + i;
            byte[] byteArray = CpioUtil.long2byteArray(number, i + 1, swapHalfWord);
            System.arraycopy(byteArray, 0, dest, byteIndex, byteArray.length);
        }
    }

    private byte[] footer = { 0x1A, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0 };

    private byte[] createFooterBytes() {
        return footer;
    }
}