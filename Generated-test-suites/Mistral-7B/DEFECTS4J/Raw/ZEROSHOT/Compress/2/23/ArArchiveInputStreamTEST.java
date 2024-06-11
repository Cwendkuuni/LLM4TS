package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream testInputStream;

    @Before
    public void setUp() {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] data = "Hello World!".getBytes();

        outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream metadataStream = new ByteArrayOutputStream();
        metadataStream.write(header);
        metadataStream.write(name);
        metadataStream.write(Long.toString(data.length).getBytes());
        metadataStream.write(trailer);
        outputStream.write(metadataStream.toByteArray());
        outputStream.write(data);

        ByteArrayInputStream metaDataInputStream = new ByteArrayInputStream(metadataStream.toByteArray());
        testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        inputStream = new ArArchiveInputStream(metaDataInputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = inputStream.getNextArEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals(14, entry.getSize());

        ArArchiveEntry nextEntry = inputStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        byte[] invalidHeader = new byte[]{0};
        ByteArrayInputStream invalidMetaDataInputStream = new ByteArrayInputStream(invalidHeader);
        ArArchiveInputStream invalidArArchiveInputStream = new ArArchiveInputStream(invalidMetaDataInputStream);
        invalidArArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryNoEntry() throws IOException {
        inputStream.getNextArEntry();
        inputStream.getNextArEntry();

        ArArchiveEntry nextEntry = inputStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryUnexpectedEOF() throws IOException {
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());
        ByteArrayInputStream unexpectedEofMetaDataInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream unexpectedEofArArchiveInputStream = new ArArchiveInputStream(unexpectedEofMetaDataInputStream);
        unexpectedEofArArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry nextEntry = inputStream.getNextEntry();
        assertEquals("test.txt", nextEntry.getName());
        assertEquals(14, nextEntry.getSize());

        ArchiveEntry nextNextEntry = inputStream.getNextEntry();
        assertNull(nextNextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryInvalidHeader() throws IOException {
        byte[] invalidHeader = new byte[]{0};
        ByteArrayInputStream invalidMetaDataInputStream = new ByteArrayInputStream(invalidHeader);
        ArchiveInputStream archiveInputStream = new ArArchiveInputStream(invalidMetaDataInputStream);
        archiveInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryNoEntry() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();

        ArchiveEntry nextEntry = inputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryUnexpectedEOF() throws IOException {
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());
        ByteArrayInputStream unexpectedEofMetaDataInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new ArArchiveInputStream(unexpectedEofMetaDataInputStream);
        archiveInputStream.getNextEntry();
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();

        ArchiveEntry nextEntry = inputStream.getNextArEntry();
        assertNull(nextEntry);

        ArchiveEntry nextNextEntry = inputStream.getNextArEntry();
        assertNull(nextNextEntry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        int read = inputStream.read(buffer);
        assertEquals(10, read);

        int read2 = inputStream.read(buffer);
        assertEquals(4, read2);

        int read3 = inputStream.read(buffer);
        assertEquals(-1, read3);
    }
}