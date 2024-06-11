package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1.txt");
        entry1.setSize(8 * 1024);
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(entry1);

        byte[] data = "This is a test".getBytes();
        cpioStream.write(data, 0, data.length);
        cpioStream.closeArchiveEntry();

        cpioStream.putNextEntry(entry1);
        cpioStream.write(data, 0, data.length);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();

        byte[] result = out.toByteArray();

        // Check the header of the first entry
        assertEquals(CpioConstants.MAGIC_NEW.length, result.length, 0);
        assertArrayEquals(CpioConstants.MAGIC_NEW.getBytes(), Arrays.copyOfRange(result, 0, 8));
        assertEquals(CpioConstants.FORMAT_NEW, (short) CpioUtil.readInt16BE(result, 8));
        assertEquals(CpioConstants.FORMAT_NEW, (short) CpioUtil.readInt16BE(result, 10));
        assertEquals(CpioConstants.FORMAT_NEW, (short) CpioUtil.readInt16BE(result, 12));
        assertEquals(CpioConstants.FORMAT_NEW, (short) CpioUtil.readInt16BE(result, 14));
        assertEquals(0755, CpioUtil.readInt32BE(result, 16));
        assertEquals(1000, CpioUtil.readInt32BE(result, 20));
        assertEquals(1000, CpioUtil.readInt32BE(result, 24));
        assertEquals(1, CpioUtil.readInt32BE(result, 28));
        assertEquals(System.currentTimeMillis(), CpioUtil.readLongBE(result, 32));
        assertEquals(8 * 1024, CpioUtil.readLongBE(result, 40));
        assertEquals(1, CpioUtil.readInt32BE(result, 48));
        assertEquals(System.currentTimeMillis(), CpioUtil.readLongBE(result, 52));
        assertEquals(8 * 1024, CpioUtil.readLongBE(result, 60));
        assertEquals(1, CpioUtil.readInt32BE(result, 68));
        assertEquals(0, CpioUtil.readInt32BE(result, 72));
        assertEquals(0, CpioUtil.readInt32BE(result, 76));
        assertEquals(0, CpioUtil.readInt32BE(result, 80));
        assertEquals(0, CpioUtil.readInt32BE(result, 84));
        assertEquals(entry1.getName().length() + 1, (short) CpioUtil.readInt16BE(result, 88));
        assertEquals(0, CpioUtil.readInt16BE(result, 90));
        assertEquals("entry1.txt".getBytes().length, (int) CpioUtil.readInt32BE(result, 92));
        assertEquals(entry1.getName().length() + 1, (int) CpioUtil.readInt32BE(result, 96));
        assertEquals("entry1.txt".getBytes().length, (int) CpioUtil.readInt32BE(result, 100));
        assertEquals("entry1.txt".length(), result.length - 8);

        // Check the data of the first entry
        InputStream input = new ByteArrayInputStream(Arrays.copyOfRange(result, 8, result.length));
        byte[] readData = new byte[data.length];
        assertEquals(data.length, input.read(readData));
        assertArrayEquals(data, readData);

        // Check if the second call to putNextEntry with the same entry creates an error
        try {
            cpioStream.putNextEntry(entry1);
            fail();
        } catch (IOException e) {
            assertEquals("duplicate entry: entry1.txt", e.getMessage());
        }

        cpioStream.close();
    }

    @Test
    public void testPutNextEntryWithDifferentFormats() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1.txt");
        entry1.setSize(8 * 1024);
        entry1.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioStream.putNextEntry(entry1);

        byte[] data = "This is a test".getBytes();
        cpioStream.write(data, 0, data.length);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();

        byte[] result = out.toByteArray();

        // Check the header of the first entry
        assertEquals(CpioConstants.MAGIC_OLD_BINARY.length, result.length, 0);
        assertArrayEquals(CpioConstants.MAGIC_OLD_BINARY.getBytes(), Arrays.copyOfRange(result, 0, 8));
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, (short) CpioUtil.readInt16BE(result, 8));
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, (short) CpioUtil.readInt16BE(result, 10));
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, (short) CpioUtil.readInt16BE(result, 12));
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, (short) CpioUtil.readInt16BE(result, 14));
        assertEquals(0755, CpioUtil.readInt32BE(result, 16));
        assertEquals(1000, CpioUtil.readInt32BE(result, 20));
        assertEquals(1000, CpioUtil.readInt32BE(result, 24));
        assertEquals(1, CpioUtil.readInt32BE(result, 28));
        assertEquals(System.currentTimeMillis(), CpioUtil.readLongBE(result, 32));
        assertEquals(8 * 1024, CpioUtil.readLongBE(result, 40));
        assertEquals(1, CpioUtil.readInt32BE(result, 48));
        assertEquals(System.currentTimeMillis(), CpioUtil.readLongBE(result, 52));
        assertEquals(8 * 1024, CpioUtil.readLongBE(result, 60));
        assertEquals(1, CpioUtil.readInt32BE(result, 68));
        assertEquals(0, CpioUtil.readInt32BE(result, 72));
        assertEquals(0, CpioUtil.readInt32BE(result, 76));
        assertEquals(0, CpioUtil.readInt32BE(result, 80));
        assertEquals(0, CpioUtil.readInt32BE(result, 84));
        assertEquals(entry1.getName().length() + 1, (short) CpioUtil.readInt16BE(result, 88));
        assertEquals(entry1.getName().length(), (int) CpioUtil.readInt32BE(result, 90));
        assertEquals(entry1.getName().length(), (int) CpioUtil.readInt32BE(result, 94));
        assertEquals(entry1.getName().length() + 1, (int) CpioUtil.readInt32BE(result, 98));
        assertEquals("entry1.txt".getBytes().length, (int) CpioUtil.readInt32BE(result, 102));
        assertEquals("entry1.txt".getBytes().length, (int) CpioUtil.readInt32BE(result, 106));
        assertEquals("entry1.txt".length(), result.length - 8);

        cpioStream.close();

        // Test with the same entry but new format
        cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(entry1);
        fail("Should throw an exception");
    }

    @Test
    public void testPutNextEntryWithDuplicateName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1.txt");
        entry1.setSize(8 * 1024);
        cpioStream.putNextEntry(entry1);

        byte[] data = "This is a test".getBytes();
        cpioStream.write(data, 0, data.length);
        cpioStream.closeArchiveEntry();

        try {
            cpioStream.putNextEntry(entry1);
            fail();
        } catch (IOException e) {
            assertEquals("duplicate entry: entry1.txt", e.getMessage());
        }

        cpioStream.finish();

        cpioStream.close();
    }

    @Test
    public void testWriteWithFilteredOutputStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream outFiltered = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(
                new FilterOutputStream(out), CpioConstants.FORMAT_NEW);
        FilterOutputStream outFilter = new FilterOutputStream(outFiltered);

        cpioStream.putNextEntry(new CpioArchiveEntry());
        cpioStream.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
        cpioStream.closeArchiveEntry();
        cpioStream.finish();
        cpioStream.close();

        assertEquals("Hello World!", outFiltered.toString());
        assertEquals(CpioConstants.MAGIC_NEW.length, out.size(), 0);
    }

    @Test
    public void testGetNames() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("entry1.txt");
        entry1.setSize(8 * 1024);
        cpioStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("entry2.txt");
        entry2.setSize(8 * 1024);
        cpioStream.putNextEntry(entry2);

        cpioStream.finish();

        Map<String, CpioArchiveEntry> names = cpioStream.getNames();
        assertEquals(2, names.size());
        assertEquals(entry1, names.get("entry1.txt"));
        assertEquals(entry2, names.get("entry2.txt"));

        cpioStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile");
        cpioStream.putNextEntry(entry);
        cpioStream.finish();
        cpioStream.close();

        byte[] result = out.toByteArray();
        assertEquals(CpioConstants.MAGIC_TRAILER.length, result.length, 0);
        assertSame(result, CpioConstants.MAGIC_TRAILER.getBytes());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpioStream.close();

        try {
            cpioStream.write(1);
            fail();
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void testPutNextEntryWithNonThreadsafeMutableFields() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();

        Thread thread1 = new Thread(() -> {
            try {
                cpioStream.putNextEntry(entry);
                cpioStream.write("1".getBytes(), 0, "1".getBytes().length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                cpioStream.putNextEntry(entry);
                cpioStream.write("2".getBytes(), 0, "2".getBytes().length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cpioStream.finish();

        byte[] result = out.toByteArray();

        // Test the first entry
        InputStream input = new ByteArrayInputStream(Arrays.copyOfRange(result, 8, result.length - 8));
        byte[] readData = new byte[5];
        assertEquals(5, input.read(readData));
        assertEquals("1", new String(readData));

        // Test the second entry
        input = new ByteArrayInputStream(Arrays.copyOfRange(result, 8, result.length - 8));
        readData = new byte[5];
        assertEquals(5, input.read(readData));
        assertEquals("2", new String(readData));

        cpioStream.close();
    }
}