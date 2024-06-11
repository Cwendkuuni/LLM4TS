package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Map<String, CpioArchiveEntry> entryMap;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new FilterOutputStream(byteArrayOutputStream);
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        entryMap = new HashMap<>();
    }

    @Test
    public void testConstructor_ValidFormat() {
        // Test case for valid format constructor
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertNull(cpioArchiveOutputStream.getCpioEntry());
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
    }

    @Test
    public void testConstructor_InvalidFormat() {
        // Test case for invalid format constructor
        assertThrows(IllegalArgumentException.class, () -> new CpioArchiveOutputStream(outputStream, (short) 99));
    }

    @Test
    public void testPutNextEntry_ValidEntry() throws IOException {
        // Test case for putting a valid entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);
        assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPutNextEntry_DuplicateEntry() throws IOException {
        // Test case for putting a duplicate entry
        String name = "test.txt";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(name);
        entryMap.put(name, entry1);
        cpioArchiveOutputStream.putNextEntry(entry1);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(entry1));
    }

    @Test
    public void testPutNextEntry_NullEntry() {
        // Test case for putting a null entry
        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.putNextEntry(null));
    }

    @Test
    public void testPutNextEntry_ValidEntryWithSize() throws IOException {
        // Test case for putting a valid entry with size
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setSize(10);
        cpioArchiveOutputStream.putNextEntry(entry);
        assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
        assertEquals(10, cpioArchiveOutputStream.getNextEntry().getSize());
    }

    @Test
    public void testPutNextEntry_ValidEntryWithoutSize() throws IOException {
        // Test case for putting a valid entry without size
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testWrite_ValidData() throws IOException {
        // Test case for writing valid data
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        assertArrayEquals(data, byteArrayOutputStream.toByteArray());
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite_InvalidOffset() throws IOException {
        // Test case for writing with invalid offset
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(data, -1, data.length));
    }

    @Test
    public void testWrite_InvalidLength() throws IOException {
        // Test case for writing with invalid length
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(data, 0, data.length + 1));
    }

    @Test
    public void testFinish() throws IOException {
        // Test case for finishing the stream
        String name1 = "test1.txt";
        String name2 = "test2.txt";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(name1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(name2);
        cpioArchiveOutputStream.putNextEntry(entry1);
        byte[] data1 = "Hello World 1".getBytes();
        cpioArchiveOutputStream.write(data1, 0, data1.length);
        cpioArchiveOutputStream.putNextEntry(entry2);
        byte[] data2 = "Hello World 2".getBytes();
        cpioArchiveOutputStream.write(data2, 0, data2.length);
        cpioArchiveOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        FilterInputStream filterInputStream = new FilterInputStream(inputStream);
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.getNextEntry();
        assertEquals(entry1, archiveEntry);
        assertEquals(name1, archiveEntry.getName());
        assertEquals(entry1.getSize(), cpioArchiveOutputStream.getNextEntry().getSize());
        assertEquals(data1, CpioUtil.bytes(filterInputStream, data1.length));
        archiveEntry = cpioArchiveOutputStream.getNextEntry();
        assertEquals(entry2, archiveEntry);
        assertEquals(name2, archiveEntry.getName());
        assertEquals(entry2.getSize(), cpioArchiveOutputStream.getNextEntry().getSize());
        assertEquals(data2, CpioUtil.bytes(filterInputStream, data2.length));
        assertNull(cpioArchiveOutputStream.getNextEntry());
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        // Test case for closing the stream
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.close();
        assertEquals(data, byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testCloseArchiveEntry_ClosedEntry() throws IOException {
        // Test case for closing a closed entry
        String name = "test.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] data = "Hello World".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.closeArchiveEntry();
        assertEquals(data, byteArrayOutputStream.toByteArray());
    }
}