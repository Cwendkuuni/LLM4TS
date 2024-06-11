package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.util.BytesConsumer;
import org.apache.commons.compress.util.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arInput;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(new byte[]{"test", 0, 0, 0, 0, 0, (byte) 0x75, (byte) 0x73, (byte) 0x61, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ArArchiveEntry.TRAILER});
        outputStream.write(new byte[1024]);
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        arInput = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInput.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test", entry.getName());
        assertEquals(1L, entry.getSize());
        entry = arInput.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInput.getNextEntry();
        assertSame(entry, arInput.getNextArEntry());
        assertEquals("test", entry.getName());
        assertEquals(1L, entry.getSize());
        entry = arInput.getNextEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWhenNoMoreEntries() throws IOException {
        arInput.getNextArEntry();
        arInput.getNextArEntry();
        arInput.getNextArEntry();
        arInput.getNextArEntry();
        arInput.getNextArEntry();
    }

    @Test
    public void testClose() throws IOException {
        ArArchiveEntry entry = arInput.getNextArEntry();
        assertNotNull(entry);
        arInput.close();
        try {
            arInput.getNextArEntry();
            fail();
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void testRead() throws IOException {
        final byte[] testByte = new byte[10];
        int read = arInput.read(testByte, 0, testByte.length);
        assertEquals(1L, arInput.getNextArEntry().getSize());
        assertEquals(testByte.length, read);
        for (int i = 0; i < testByte.length; i++) {
            assertEquals((byte) 0, testByte[i]);
        }
    }

    @Test
    public void testReadIntoBytesArray() throws IOException {
        final byte[] testByte = new byte[10];
        int read = arInput.read(testByte);
        assertEquals(1L, arInput.getNextArEntry().getSize());
        assertEquals(testByte.length, read);
        for (int i = 0; i < testByte.length; i++) {
            assertEquals((byte) 0, testByte[i]);
        }
    }

    @Test
    public void testReadIntoByteArrayOffsetLength() throws IOException {
        final byte[] testByte = new byte[10];
        int read = arInput.read(testByte, 0, testByte.length);
        assertEquals(1L, arInput.getNextArEntry().getSize());
        assertEquals(testByte.length, read);
        for (int i = 0; i < testByte.length; i++) {
            assertEquals((byte) 0, testByte[i]);
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[16];
        assertTrue(ArArchiveInputStream.matches(signature, 16));
        signature[0] = 0x22;
        assertFalse(ArArchiveInputStream.matches(signature, 16));
    }

    @Test
    public void testCloseInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);
        arInput.close();
        try {
            arInput.getNextArEntry();
            fail();
        } catch (IOException expected) {
            // expected
        }
    }
}