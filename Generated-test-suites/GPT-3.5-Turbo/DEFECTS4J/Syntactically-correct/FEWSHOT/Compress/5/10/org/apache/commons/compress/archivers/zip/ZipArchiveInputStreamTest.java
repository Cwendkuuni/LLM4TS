package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;
    private byte[] testFile;

    @Before
    public void setup() {
        testFile = new byte[] {80, 75, 3, 4, 20, 0, 8, 0, 8, 0, 77, -74, -40, -48, 15, 0, 0, 0, 15, 0, 0, 0, 15, 0, 0, 0, 3, 0, 0, 0, 116, 101, 115, 116, 46, 116, 120, 116, 84, 101, 115, 116, 116, 101, 115, 116, 46, 106, 97, 118, 97};
        InputStream inputStream = new ByteArrayInputStream(testFile);
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testMatchesWithValidSignature() {
        byte[] validSignature = {80, 75, 3, 4};
        assertTrue(ZipArchiveInputStream.matches(validSignature, validSignature.length));
    }

    @Test
    public void testGetNextZipEntryWithValidZipEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals(15, entry.getSize());
    }

    @Test
    public void testReadWithStoredEntry() throws IOException {
        zipInputStream.getNextZipEntry();

        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, buffer.length);

        assertEquals(10, bytesRead);
        assertArrayEquals(new byte[] {116, 101, 115, 116, 46, 116, 120, 116, 84, 101}, buffer);
    }

    @Test
    public void testReadWithDeflatedEntry() throws IOException {
        byte[] deflatedEntry = {120, -100, -59, 9, 0, 32, 20, -127, -18, -51, -119, -62, -29, 68, 2, 33, -10, 115, 0, 0};
        InputStream inputStream = new ByteArrayInputStream(deflatedEntry);
        zipInputStream = new ZipArchiveInputStream(inputStream);
        zipInputStream.getNextZipEntry();

        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, buffer.length);

        assertEquals(10, bytesRead);
        assertArrayEquals(new byte[] {116, 101, 115, 116, 46, 116, 120, 116, 84, 101}, buffer);
    }

    @Test(expected = EOFException.class)
    public void testReadWithInvalidEntry() throws IOException {
        zipInputStream.getNextZipEntry();

        byte[] buffer = new byte[100];
        zipInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testClose() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.close();
        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, buffer.length);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        zipInputStream.getNextZipEntry();
        long bytesSkipped = zipInputStream.skip(5);

        assertEquals(5, bytesSkipped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.skip(-5);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals(15, entry.getSize());
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[10];
        zipInputStream.readFully(buffer);

        assertArrayEquals(new byte[] {116, 101, 115, 116, 46, 116, 120, 116, 84, 101}, buffer);
    }
}