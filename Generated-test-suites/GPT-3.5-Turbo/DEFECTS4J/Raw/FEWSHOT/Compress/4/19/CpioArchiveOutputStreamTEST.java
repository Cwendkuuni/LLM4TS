package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {
    private CpioArchiveOutputStream archive;

    @Before
    public void setUp() {
        archive = new CpioArchiveOutputStream(new ByteArrayOutputStream());
    }

    @After
    public void tearDown() throws IOException {
        archive.close();
    }

    @Test
    public void testConstructor_WithOutputStream_NoFormatException() {
        OutputStream output = new ByteArrayOutputStream();
        archive = new CpioArchiveOutputStream(output);
        assertNotNull(archive);
    }

    @Test
    public void testConstructor_WithOutputStreamAndFormat_NoFormatException() {
        OutputStream output = new ByteArrayOutputStream();
        short format = 1;
        archive = new CpioArchiveOutputStream(output, format);
        assertNotNull(archive);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithOutputStreamAndInvalidFormat_IllegalArgumentException()
            throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        short format = 3;
        archive = new CpioArchiveOutputStream(output, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithOutputStreamAndEmptyFormat_IllegalArgumentException()
            throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        short format = 0;
        archive = new CpioArchiveOutputStream(output, format);
    }

    @Test
    public void testCreateArchiveEntry() {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = archive.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertTrue(entry instanceof CpioArchiveEntry);
        assertEquals(inputFile, entry.getFile());
        assertEquals(entryName, entry.getName());
    }
}