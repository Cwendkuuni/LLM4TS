package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() {
        // Test creation of CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        assertTrue(cao.isOpen());
        assertFalse(cao.isClosed());
        assertEquals(FORMAT_NEW, cao.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCpioArchiveOutputStreamWithInvalidFormat() {
        OutputStream out = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(out, (short) 0xFF);
    }

    @Test(expected = IOException.class)
    public void testCpioArchiveOutputStreamWithClosedOutputStream() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        cao.close();
        cao.putNextEntry(new CpioArchiveEntry());
    }

    @Test
    public void testPutNextEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);

        // Test null entry
        assertNull(cao.putNextEntry(null));

        // Test writing a new entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        CpioArchiveEntry actualEntry = cao.putNextEntry(entry);
        assertEquals(entry, actualEntry);
        assertEquals(FORMAT_NEW, actualEntry.getFormat());

        // Test writing an old ASCII entry
        entry.setFormat(FORMAT_OLD_ASCII);
        actualEntry = cao.putNextEntry(entry);
        assertEquals(entry, actualEntry);
        assertEquals(FORMAT_OLD_ASCII, actualEntry.getFormat());

        // Test writing an old binary entry
        entry.setFormat(FORMAT_OLD_BINARY);
        actualEntry = cao.putNextEntry(entry);
        assertEquals(entry, actualEntry);
        assertEquals(FORMAT_OLD_BINARY, actualEntry.getFormat());

        // Test writing a new entry with the same name
        entry.setName("testfile");
        assertNull(cao.putNextEntry(entry));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(-1);
        cao.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        cao.putNextEntry(entry);
        String data = "12345";
        byte[] dataBytes = data.getBytes();
        cao.write(dataBytes, 0, dataBytes.length);
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        byte[] expectedData = new byte[dataBytes.length + 123];
        input.read(expectedData);
        assertArrayEquals(expectedData, dataBytes);
        cao.close();
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        cao.putNextEntry(entry);
        String data = "12345";
        byte[] dataBytes = data.getBytes();
        cao.write(dataBytes, 0, dataBytes.length);
        cao.finish();
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        byte[] expectedData = new byte[dataBytes.length + 123 + 1];
        input.read(expectedData);
        assertArrayEquals(expectedData, dataBytes);
        cao.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        cao.putNextEntry(entry);
        String data = "12345";
        byte[] dataBytes = data.getBytes();
        cao.write(dataBytes, 0, dataBytes.length);
        cao.closeArchiveEntry();
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        byte[] expectedData = new byte[dataBytes.length + 123];
        input.read(expectedData);
        assertArrayEquals(expectedData, dataBytes);
        cao.close();
    }

    @Test
    public void testWriteWithClosedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        cao.putNextEntry(entry);
        String data = "12345";
        byte[] dataBytes = data.getBytes();
        cao.write(dataBytes, 0, dataBytes.length);
        cao.finish();
        cao.close();
        assertThrows(IOException.class, () -> cao.write(dataBytes, 0, dataBytes.length));
    }

    @Test
    public void testWriteWithFinishedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        cao.putNextEntry(entry);
        String data = "12345";
        byte[] dataBytes = data.getBytes();
        cao.write(dataBytes, 0, dataBytes.length);
        cao.finish();
        assertThrows(IOException.class, () -> cao.write(dataBytes, 0, dataBytes.length));
    }

    @Test
    public void testWriteWithClosedAndFinishedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(123);
        cao.putNextEntry(entry);
        String data = "12345";
        byte[] dataBytes = data.getBytes();
        cao.write(dataBytes, 0, dataBytes.length);
        cao.close();
        cao.finish();
        assertThrows(IOException.class, () -> cao.write(dataBytes, 0, dataBytes.length));
    }

    @Test
    public void testClose() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        assertFalse(cao.isClosed());
        cao.close();
        assertTrue(cao.isClosed());
    }

    @Test
    public void testPad() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        cao.pad(123, 4);
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        byte[] expectedData = new byte[123 % 4 + 123];
        input.read(expectedData);
        assertArrayEquals(expectedData, new byte[expectedData.length]);
        cao.close();
    }

    @Test
    public void testWriteBinaryLong() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        long number = 12345;
        int length = 4;
        boolean swapHalfWord = false;
        cao.writeBinaryLong(number, length, swapHalfWord);
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        byte[] expectedData = CpioUtil.long2byteArray(number, length, swapHalfWord);
        assertArrayEquals(expectedData, input.readAllBytes());
        cao.close();
    }

    @Test
    public void testWriteAsciiLong() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        long number = 12345;
        int length = 8;
        int radix = 10;
        cao.writeAsciiLong(number, length, radix);
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        String expectedData = String.format("%" + length + "d", number);
        byte[] expectedDataBytes = expectedData.getBytes();
        assertArrayEquals(expectedDataBytes, input.readAllBytes());
        cao.close();
    }

    @Test
    public void testWriteCString() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(out, FORMAT_NEW);
        String str = "test";
        cao.writeCString(str);
        InputStream input = new ByteArrayInputStream(cao.getBuffer().toByteArray());
        byte[] expectedData = str.getBytes();
        byte[] actualData = new byte[expectedData.length + 1];
        input.read(actualData);
        assertArrayEquals(expectedData, actualData, 0, expectedData.length);
        cao.close();
    }
}