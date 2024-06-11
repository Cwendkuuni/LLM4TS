package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Testing for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;
    private ArchiveEntry currentEntry;
    private static final byte[] AR_HEADER = new byte[]{(byte) 0x3c, (byte) 0x21, (byte) 0x72, (byte) 0x61, (byte) 0x68,
            (byte) 0x63, (byte) 0x0a, (byte) 0x3e};
    private static final byte[] AR_ENTRY_HEADER = new byte[]{(byte) 0x41, (byte) 0x50, (byte) 0x4c, (byte) 0x45, (byte) 0x54,
            (byte) 0x43, (byte) 0x41, (byte) 0x4d, (byte) 0x54, (byte) 0x68, (byte) 0x6f, (byte) 0x73, (byte) 0x74, (byte) 0x0a,
            (byte) 0x4b, (byte) 0x53, (byte) 0x53, (byte) 0x4d, (byte) 0x41, (byte) 0x54, (byte) 0x45, (byte) 0x52, (byte) 0x53,
            (byte) 0x45, (byte) 0x54, (byte) 0x0a, (byte) 0x49, (byte) 0x4e, (byte) 0x46, (byte) 0x4f, (byte) 0x53, (byte) 0x53,
            (byte) 0x55, (byte) 0x4d, (byte) 0x41, (byte) 0x54, (byte) 0x45, (byte) 0x52, (byte) 0x4d, (byte) 0x0a, (byte) 0x53,
            (byte) 0x53, (byte) 0x55, (byte) 0x4d, (byte) 0x41, (byte) 0x54, (byte) 0x45, (byte) 0x52, (byte) 0x45, (byte) 0x0a,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x0a, (byte) 0x41, (byte) 0x50, (byte) 0x4c, (byte) 0x45, (byte) 0x54, (byte) 0x43, (byte) 0x41, (byte) 0x52,
            (byte) 0x0a};

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(AR_HEADER);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
        currentEntry = null;
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(arArchiveInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertEquals("test entry name", "name.ext", entry.getName());
        assertEquals("test entry size", 123L, entry.getSize());
        assertNull("current entry should be null after first call", arArchiveInputStream.getNextArEntry());
        inputStream = new ByteArrayInputStream(new byte[0]);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertNull("getNextArEntry should return null at EOF", arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        assertEquals("test entry name", "name.ext", ((ArArchiveEntry) entry).getName());
        assertEquals("test entry size", 123L, ((ArArchiveEntry) entry).getSize());
        assertNull("current entry should be null after first call", arArchiveInputStream.getNextEntry());
        inputStream = new ByteArrayInputStream(new byte[0]);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertNull("getNextEntry should return null at EOF", arArchiveInputStream.getNextEntry());
    }

    @Test
    public void testMatches() throws IOException {
        assertTrue("AR header should match", ArArchiveInputStream.matches(AR_HEADER, AR_HEADER.length));
        assertFalse("Invalid header should not match", ArArchiveInputStream.matches(new byte[10], 10));
    }

    @Test
    public void testClose() throws IOException {
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        assertNull("current entry should be null after close", arArchiveInputStream.getNextArEntry());
        inputStream = new ByteArrayInputStream(AR_ENTRY_HEADER);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
        entry = arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.close();
        assertNull("current entry should be null after close", arArchiveInputStream.getNextArEntry());
    }
}