package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(entry);
        assertEquals(CpioConstants.MAGIC_NEW.length, outputStream.size());
        assertArrayEquals(CpioConstants.MAGIC_NEW, outputStream.toByteArray(), 0, 0, CpioConstants.MAGIC_NEW.length);

        cpioArchiveOutputStream.write("12345\n".getBytes());
        assertEquals(11, outputStream.size());

        cpioArchiveOutputStream.closeArchiveEntry();

        entry = new CpioArchiveEntry();
        entry.setName("anotherfile");
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioArchiveOutputStream.putNextEntry(entry);
        assertEquals(CpioConstants.MAGIC_OLD_ASCII.length, outputStream.size());
        assertArrayEquals(CpioConstants.MAGIC_OLD_ASCII, outputStream.toByteArray(), 0, 0, CpioConstants.MAGIC_OLD_ASCII.length);

        cpioArchiveOutputStream.write("anotherfile\n".getBytes());
        assertEquals(19, outputStream.size());

        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertEquals(19 + 11, outputStream.size());
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(-1); // invalid format
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(entry2));
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "12345".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        assertEquals(data.length + 11, outputStream.size());

        cpioArchiveOutputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        entries.put("testfile", entry);
        assertEquals(entries, cpioArchiveOutputStream.names);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "12345".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);

        cpioArchiveOutputStream.finish();

        byte[] expectedTrailerData = CpioUtil.long2byteArray(0, 1, false);
        assertEquals(data.length + 11 + expectedTrailerData.length, outputStream.size());

        assertArrayEquals(data, cpioArchiveOutputStream.out.toString().getBytes(), 0, 0, data.length);
        assertArrayEquals(expectedTrailerData, outputStream.toByteArray(), outputStream.size() - expectedTrailerData.length, outputStream.size() - expectedTrailerData.length, expectedTrailerData.length);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        cpioArchiveOutputStream.close();

        assertNull(cpioArchiveOutputStream.cpioEntry);
        assertNull(cpioArchiveOutputStream.names);
    }
}