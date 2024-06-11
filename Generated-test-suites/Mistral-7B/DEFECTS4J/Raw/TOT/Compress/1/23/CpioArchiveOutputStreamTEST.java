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

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioConstants;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Tests creating a new CpioArchiveOutputStream with a specified format and
     * that the format can be retrieved.
     */
    @org.junit.Test
    public void testCreateAndGetFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
    }

    /**
     * Tests creating a new CpioArchiveOutputStream without specifying a format
     * and that the default format is used.
     */
    @org.junit.Test
    public void testCreateWithoutFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
    }

    /**
     * Tests that the stream cannot be opened if it is already closed.
     */
    @org.junit.Test
    public void testCannotOpenClosedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioStream.close();
        assertThrows(IOException.class, () -> cpioStream.putNextEntry(new CpioArchiveEntry()));
    }

    /**
     * Tests that the stream can be opened multiple times and that each entry
     * is written correctly.
     */
    @org.junit.Test
    public void testCanOpenMultipleTimes() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(10);
        cpioStream.putNextEntry(entry1);
        byte[] testData1 = "Hello World".getBytes();
        cpioStream.write(testData1, 0, testData1.length);
        cpioStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(20);
        cpioStream.putNextEntry(entry2);
        byte[] testData2 = "Test entry 2".getBytes();
        cpioStream.write(testData2, 0, testData2.length);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        expectedEntries.put("test1.txt", new CpioArchiveEntry());
        expectedEntries.put("test2.txt", new CpioArchiveEntry());
        Map<String, CpioArchiveEntry> actualEntries = new HashMap<>();
        CpioArchiveEntry entry;
        while ((entry = CpioArchiveEntry.read(inputStream)) != null) {
            actualEntries.put(entry.getName(), entry);
        }
        assertEquals(expectedEntries, actualEntries);
    }

    /**
     * Tests writing the same entry multiple times results in an error.
     */
    @org.junit.Test
    public void testCannotWriteSameEntryMultipleTimes() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        cpioStream.putNextEntry(entry);
        cpioStream.write("Hello World".getBytes());
        cpioStream.closeArchiveEntry();
        assertThrows(IOException.class, () -> cpioStream.putNextEntry(entry));
    }

    /**
     * Tests that the stream can be closed after writing entries and that the
     * file size is correct.
     */
    @org.junit.Test
    public void testCanCloseStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(10);
        cpioStream.putNextEntry(entry1);
        byte[] testData1 = "Hello World".getBytes();
        cpioStream.write(testData1, 0, testData1.length);
        cpioStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(20);
        cpioStream.putNextEntry(entry2);
        byte[] testData2 = "Test entry 2".getBytes();
        cpioStream.write(testData2, 0, testData2.length);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();
        cpioStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        CpioArchiveEntry entry;
        while ((entry = CpioArchiveEntry.read(inputStream)) != null) {
            entries.put(entry.getName(), entry);
        }

        assertEquals(2, entries.size());
        long expectedSize = 10 + 20 + 1024; // 1024 bytes for the trailer
        assertEquals(expectedSize, out.size());
    }

    /**
     * Tests that the correct error is thrown when an entry with an unknown
     * format is attempted to be written.
     */
    @org.junit.Test
    public void testCannotWriteUnknownFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioStream.putNextEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> cpioStream.finish());
        cpioStream.close();
    }

    /**
     * Tests that the correct error is thrown when a non-existing entry is
     * attempted to be closed.
     */
    @org.junit.Test
    public void testCannotCloseNonExistentEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioStream.finish();
        cpioStream.close();

        assertThrows(IllegalStateException.class, () -> cpioStream.closeArchiveEntry());
    }

    /**
     * Tests that the correct error is thrown when an attempt is made to write
     * after the stream has been finished.
     */
    @org.junit.Test
    public void testCannotWriteAfterFinished() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioStream.putNextEntry(entry);
        cpioStream.write("Hello World".getBytes());
        cpioStream.finish();

        assertThrows(IOException.class, () -> cpioStream.write(new byte[1]));
        cpioStream.close();
    }

    /**
     * Tests that an error is thrown when a CpioArchiveEntry with a duplicate
     * name is attempted to be written.
     */
    @org.junit.Test
    public void testCannotWriteDuplicateEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test.txt");
        entry1.setSize(10);
        cpioStream.putNextEntry(entry1);
        cpioStream.write("Hello World".getBytes());
        cpioStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioStream.putNextEntry(entry1));

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(10);
        cpioStream.putNextEntry(entry2);
        cpioStream.write("Test entry 2".getBytes());
        cpioStream.closeArchiveEntry();

        cpioStream.finish();
        cpioStream.close();
    }

    /**
     * Tests that the stream correctly handles writing a trail entry.
     */
    @org.junit.Test
    public void testCanWriteTrailEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(10);
        cpioStream.putNextEntry(entry1);
        byte[] testData1 = "Hello World".getBytes();
        cpioStream.write(testData1, 0, testData1.length);
        cpioStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(20);
        cpioStream.putNextEntry(entry2);
        byte[] testData2 = "Test entry 2".getBytes();
        cpioStream.write(testData2, 0, testData2.length);
        cpioStream.closeArchiveEntry();

        CpioArchiveEntry trailEntry = new CpioArchiveEntry();
        trailEntry.setName("TRAILER!!!");
        trailEntry.setMode(0);
        trailEntry.setNumberOfLinks(1);
        cpioStream.putNextEntry(trailEntry);
        cpioStream.finish();

        cpioStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        CpioArchiveEntry entry;
        while ((entry = CpioArchiveEntry.read(inputStream)) != null) {
            entries.put(entry.getName(), entry);
        }

        assertEquals(3, entries.size());
        assertNull(entries.get("TRAILER!!!"));
    }
}