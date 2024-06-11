package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * Test class for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(getTestData());
        outputStream.write(getTestData(), 0, getTestData().length);
        outputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (archiveInputStream != null) {
            archiveInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("testentry", entry.getName());
        assertEquals(42L, entry.getSize());

        entry = archiveInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertInstanceOf(ArArchiveEntry.class, entry);
        assertEquals("testentry", ((ArArchiveEntry) entry).getName());
        assertEquals(42L, entry.getSize());

        entry = archiveInputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        byte[] testData = getTestData();
        int length = testData.length;

        // signature of 'ar' archive header
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }

    @Test
    public void testRead() throws IOException {
        final byte[] buffer = new byte[16];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) (i + 1);
        }

        int read = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(buffer.length, read);
        assertArrayEquals(buffer, archiveInputStream.readAllBytes());
    }

    @Test
    public void testReadAllBytes() throws IOException {
        byte[] expected = getTestData();
        byte[] actual = archiveInputStream.readAllBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        assertFalse(archiveInputStream.isOpen());
    }

    private byte[] getTestData() {
        byte[] testData = new byte[1024];
        System.arraycopy(new ArArchiveEntry("testentry", 42L).toString().getBytes(), 0, testData, 0,
                new ArArchiveEntry("testentry", 42L).toString().length());
        System.arraycopy(ArArchiveEntry.TRAILER.getBytes(), 0, testData,
                new ArArchiveEntry("testentry", 42L).toString().length(),
                ArArchiveEntry.TRAILER.length);
        CRC32 crc = new CRC32();
        crc.update(testData);
        testData[testData.length - 4] = (byte) ((int) crc.getValue() & 0xFF);
        testData[testData.length - 3] = (byte) ((int) (crc.getValue() >> 8) & 0xFF);
        testData[testData.length - 2] = (byte) ((int) (crc.getValue() >> 16) & 0xFF);
        testData[testData.length - 1] = (byte) ((int) (crc.getValue() >> 24) & 0xFF);
        return testData;
    }
}