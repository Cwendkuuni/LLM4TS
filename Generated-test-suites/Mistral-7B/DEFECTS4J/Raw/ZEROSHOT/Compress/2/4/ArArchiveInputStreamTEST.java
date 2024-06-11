package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private static final String TEST_DATA = "testdata";

    @Test
    public void testArArchiveInputStream() throws IOException {
        final byte[] testData = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream testDataOutputStream = new ByteArrayOutputStream();
        testDataOutputStream.write(ArArchiveEntry.HEADER);
        testDataOutputStream.write(testData);
        testDataOutputStream.write(ArArchiveEntry.TRAILER);

        final InputStream inputStream = new ByteArrayInputStream(testDataOutputStream.toByteArray());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        final ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertNotNull(arArchiveEntry);
        assertEquals("testdata", arArchiveEntry.getName());
        assertEquals(testData.length, arArchiveEntry.getSize());

        final ArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        assertNull(nextEntry);

        arArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        final byte[] testData = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream testDataOutputStream = new ByteArrayOutputStream();
        testDataOutputStream.write(new byte[]{-1, 0x21, 0x3c, 0x72, 0x61, 0x68, 0x3e, 0x0a});
        testDataOutputStream.write(testData);
        testDataOutputStream.write(ArArchiveEntry.TRAILER);

        final InputStream inputStream = new ByteArrayInputStream(testDataOutputStream.toByteArray());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testNoEntryFound() throws IOException {
        final byte[] testData = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream testDataOutputStream = new ByteArrayOutputStream();
        testDataOutputStream.write(ArArchiveEntry.HEADER);
        testDataOutputStream.write(ArArchiveEntry.TRAILER);

        final InputStream inputStream = new ByteArrayInputStream(testDataOutputStream.toByteArray());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry(); // consume first entry

        final ArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        assertNull(nextEntry);

        arArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidTrailer() throws IOException {
        final byte[] testData = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream testDataOutputStream = new ByteArrayOutputStream();
        testDataOutputStream.write(ArArchiveEntry.HEADER);
        testDataOutputStream.write(testData);
        testDataOutputStream.write(new byte[]{0x11, 0x3c, 0x72, 0x61, 0x68, 0x3e, 0x0a, 0x3e});

        final InputStream inputStream = new ByteArrayInputStream(testDataOutputStream.toByteArray());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }
}