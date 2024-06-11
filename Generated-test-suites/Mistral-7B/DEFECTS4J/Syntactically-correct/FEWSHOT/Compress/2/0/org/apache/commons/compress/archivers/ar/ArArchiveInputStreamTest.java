package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {
    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        assertArrayEquals("signature should match expected header", new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a}, ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testGetNextArEntry_emptyInputStream() throws IOException {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0][]));
        assertNull("getNextArEntry should return null for empty input stream", inputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntry_invalidHeader() throws IOException {
        byte[] invalidHeader = new byte[]{-1, 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        InputStream inputStream = new ByteArrayInputStream(new byte[][]{invalidHeader});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        try {
            arArchiveInputStream.getNextArEntry();
            fail("getNextArEntry should throw IOException for invalid header");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    public void testGetNextArEntry_validHeader() throws IOException {
        String testData = "name\nlastmodified\nuserid\ngroupid\nfilemode\nlength\nname\nlastmodified\nuserid\ngroupid\nfilemode\nlength\n";
        byte[] testDataBytes = testData.getBytes();
        String testName = "name";
        long testSize = 123L;
        byte[] headerBytes = ArArchiveEntry.HEADER.getBytes();
        byte[] nameBytes = testName.getBytes();
        byte[] trailerBytes = ArArchiveEntry.TRAILER.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerBytes);
        outputStream.write(nameBytes);
        outputStream.write(testData.getBytes());
        outputStream.write(headerBytes);
        byte[] testDataWithHeadersAndTrailer = testDataBytes.concat(new byte[]{0x0a}).concat(headerBytes).concat(nameBytes).concat(trailerBytes);
        InputStream inputStream = new ByteArrayInputStream(new byte[][]{testDataWithHeadersAndTrailer});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertEquals("currentEntry name should match testName", testName, arArchiveEntry.getName());
        assertEquals("currentEntry size should match testSize", testSize, arArchiveEntry.getSize());
    }
}