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

import org.junit.Test;

/**
 * Test class for CpioArchiveOutputStream.
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCtorWithFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertTrue(archiver instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCtorWithoutFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(outputStream);
        assertEquals(CpioConstants.FORMAT_NEW, archiver.getFormat());
    }

    @Test
    public void testPutNextEntryWithNullEntry() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class, () -> archiver.putNextEntry(null));
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test.txt");
        archiver.putNextEntry(entry1);
        assertThrows(IllegalArgumentException.class, () -> archiver.putNextEntry(entry1));
    }

    @Test
    public void testPutNextEntryWithEmptyName() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(IllegalArgumentException.class, () -> archiver.putNextEntry(entry));
        entry.setName("");
        archiver.putNextEntry(entry);
        assertNotNull(archiver.getNames().get(""));
    }

    @Test
    public void testWriteAndCloseArchiveEntry() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        archiver.write(data, 0, data.length);
        archiver.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(data.length, nextEntry.getSize());
        testInput.close();
    }

    @Test
    public void testWriteAndFinish() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        byte[] data = "Hello, World!".getBytes();
        archiver.write(data, 0, data.length);
        archiver.finish();
        assertTrue(archiver.isFinished());

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(data.length, nextEntry.getSize());
        testInput.close();
    }

    @Test
    public void testWriteAndCloseArchiveEntryWithZeroSize() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        archiver.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(0, nextEntry.getSize());
        testInput.close();
    }

    @Test
    public void testWriteWithNegativeOffsetOrLength() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = "Hello, World!".getBytes();

        assertThrows(IndexOutOfBoundsException.class, () -> archiver.write(data, -1, data.length));
        assertThrows(IndexOutOfBoundsException.class, () -> archiver.write(data, 0, -1));
    }

    @Test
    public void testWriteWithZeroData() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        archiver.write(new byte[0], 0, 0);
        archiver.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(0, nextEntry.getSize());
        testInput.close();
    }

    @Test
    public void testWriteWithDataLongerThanEntrySize() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        archiver.putNextEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        archiver.write(data, 0, data.length);
        archiver.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(11 + data.length, nextEntry.getSize());
        testInput.close();
    }

    @Test
    public void testWriteWithSwappedHalfWords() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        archiver.setSwapHalfWords(true);

        byte[] data = "Hello, World!".getBytes();
        archiver.write(data, 0, data.length);
        archiver.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(data.length, nextEntry.getSize());
        testInput.close();
    }

    @Test
    public void testCloseTwice() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        archiver.write(new byte[0], 0, 0);
        archiver.closeArchiveEntry();
        archiver.closeArchiveEntry();
        archiver.close();

        assertFalse(archiver.isFinished());
        assertNull(archiver.getCpioEntry());
        assertNull(archiver.getNames().get("test.txt"));
    }

    @Test
    public void testFinishTwice() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        archiver.write(new byte[0], 0, 0);
        archiver.finish();
        archiver.finish();
        archiver.close();

        assertTrue(archiver.isFinished());
        assertNull(archiver.getCpioEntry());
        assertNull(archiver.getNames().get("test.txt"));
    }

    @Test
    public void testPutNextEntryWithNullFormat() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(IllegalArgumentException.class, () -> archiver.putNextEntry(entry, null));
    }

    @Test
    public void testWriteToOutputStreamWithNullOutputStream() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(null);
        assertThrows(NullPointerException.class, archiver::putNextEntry);
    }

    @Test
    public void testWriteToOutputStreamWithNullData() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);

        assertThrows(NullPointerException.class, () -> archiver.write(null, 0, 0));
    }

    @Test
    public void testWriteToOutputStreamWithNegativeDataLength() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);

        assertThrows(IndexOutOfBoundsException.class, () -> archiver.write(new byte[0], -1, 0));
    }

    @Test
    public void testWriteToOutputStreamWithDataLongerThanAvailableSpace() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream(3));
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        assertThrows(IOException.class, () -> archiver.write(data, 0, data.length));
    }

    @Test
    public void testWriteToOutputStreamWithSwappedHalfWords() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        archiver.putNextEntry(entry);
        archiver.setSwapHalfWords(true);

        byte[] data = "Hello, World!".getBytes();
        archiver.write(data, 0, data.length);
        archiver.closeArchiveEntry();

        byte[] output = ((ByteArrayOutputStream) archiver.getOutputStream()).toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(output);
        CpioArchiveInputStream testInput = new CpioArchiveInputStream(input);
        ArchiveEntry nextEntry = testInput.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(data.length, nextEntry.getSize());
        testInput.close();

        byte[] swappedData = new byte[data.length];
        for (int i = 0; i < data.length; i += 2) {
            int index1 = (i / 2) * 2;
            int index2 = i + (i / 2);
            swappedData[index1] = data[index2];
            swappedData[index2] = data[index1];
        }
        assertEquals(CpioUtil.long2byteArray(0L, swappedData), output, 0);
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveOutputStream archiver = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();

        assertThrows(IllegalArgumentException.class, () -> archiver.putNextEntry(entry, (short) 0xFFFF));
        assertThrows(IllegalArgumentException.class, () -> archiver.putNextEntry(entry, (short) 0x0000));

        archiver.setFormat(CpioConstants.FORMAT_NEW);
        assertThrows(IOException.class, () -> archiver.putNextEntry(entry, (short) 0xFFFF));
    }
}