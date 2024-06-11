package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    private InputStream input;
    private OutputStream output;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        output = byteArrayOutputStream;
        input = new ByteArrayInputStream(ArArchiveInputStreamTest.createArHeader());
    }

    @After
    public void tearDown() throws Exception {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
    }

    @Test
    public void testGetNextEntryWithValidArchive() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertEquals("testEntry", entry.getName());
        assertEquals(10, entry.getSize());
    }

    @Test
    public void testGetNextEntryWithInvalidArchive() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(invalidInputStream);
        assertNull(arInputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithEmptyArchive() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(emptyInputStream);
        assertNull(arInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithInvalidEntry() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream(ArArchiveInputStreamTest.createArInvalidEntry());
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(invalidInputStream);
        arInputStream.getNextEntry();
    }

    @Test
    public void testGetNextEntryWithEOF() throws IOException {
        // consume the header
        input.read(new byte[ArArchiveEntry.HEADER.length]);

        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertEquals("testEntry", entry.getName());
        assertEquals(10, entry.getSize());

        // consume the entry content
        byte[] bytes = new byte[10];
        arInputStream.read(bytes);

        // consume the trailer
        arInputStream.read(new byte[ArArchiveEntry.TRAILER.length]);

        assertNull(arInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithUnreadContent() throws IOException {
        // consume the header
        input.read(new byte[ArArchiveEntry.HEADER.length]);

        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        arInputStream.getNextEntry();

        // consume the trailer
        arInputStream.read(new byte[ArArchiveEntry.TRAILER.length]);

        // try to read the entry content
        arInputStream.read(new byte[1]);
    }

    @Test
    public void testRead() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];

        int length = arInputStream.read(buffer);
        assertEquals(10, length);
        assertEquals(ArArchiveEntry.TRAILER.length + ArArchiveEntry.HEADER.length + 16 + 12 + 6 + 6 + 8 + 10, input.getFilePointer());

        IOUtils.closeQuietly(arInputStream);
    }

    @Test
    public void testClose() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        arInputStream.close();
        assertNull(arInputStream.getNextEntry());
        assertEquals(ArArchiveEntry.TRAILER.length + ArArchiveEntry.HEADER.length + 16 + 12 + 6 + 6 + 8 + 10, input.getFilePointer());
    }

    private static byte[] createArHeader() {
        byte[] header = ArArchiveEntry.HEADER;
        byte[] name = "testEntry".getBytes();
        byte[] length = Long.toString(10).getBytes();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = baos;

        os.write(header);
        os.write(name);
        os.write(ArArchiveEntry.TRAILER);

        return baos.toByteArray();
    }

    private static byte[] createArInvalidEntry() {
        byte[] header = ArArchiveEntry.HEADER;
        byte[] name = "testEntry".getBytes();
        byte[] length = Long.toString(10).getBytes();
        byte[] invalidTrailer = new byte[]{'Z', 'A', 'R'}; // invalid trailer

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = baos;

        os.write(header);
        os.write(name);
        os.write(length);
        os.write(invalidTrailer);

        return baos.toByteArray();
    }
}