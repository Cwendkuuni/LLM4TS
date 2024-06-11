package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {
    @Test
    public void testArArchiveInputStream() {
        byte[] header = ArArchiveEntry.HEADER.getBytes(StandardCharsets.US_ASCII);
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes(StandardCharsets.US_ASCII);
        byte[] testData = "testData".getBytes(StandardCharsets.US_ASCII);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(header);
        ArArchiveEntry expectedEntry = new ArArchiveEntry("testEntry.txt", testData.length);
        os.write(expectedEntry.toString().getBytes(StandardCharsets.US_ASCII));
        os.write(trailer);

        InputStream input = new ByteArrayInputStream(os.toByteArray());
        ArArchiveInputStream testStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = testStream.getNextArEntry();

        assertNotNull(entry);
        assertEquals(expectedEntry.getName(), entry.getName());
        assertEquals(expectedEntry.getSize(), entry.getSize());
        assertEquals(expectedEntry.getFileType(), entry.getFileType());
    }

    @Test
    public void testGetNextEntryWithNoEntries() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream testStream = new ArArchiveInputStream(input);
        ArchiveEntry nextEntry = testStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntryWithInvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[16]);
        ArArchiveInputStream testStream = new ArArchiveInputStream(input);
        ArArchiveEntry nextEntry = testStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntryWithInvalidTrailer() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes(StandardCharsets.US_ASCII);
        byte[] testData = "testData".getBytes(StandardCharsets.US_ASCII);
        byte[] trailer = "invalidTrailer".getBytes(StandardCharsets.US_ASCII);
        byte[] testDataWithTrailer = new byte[header.length + testData.length + trailer.length];
        System.arraycopy(header, 0, testDataWithTrailer, 0, header.length);
        System.arraycopy(testData, 0, testDataWithTrailer, header.length, testData.length);
        System.arraycopy(trailer, 0, testDataWithTrailer, header.length + testData.length, trailer.length);

        InputStream input = new ByteArrayInputStream(testDataWithTrailer);
        ArArchiveInputStream testStream = new ArArchiveInputStream(input);
        ArArchiveEntry nextEntry = testStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntryWithPartialEntry() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes(StandardCharsets.US_ASCII);
        byte[] name = "testName.txt".getBytes(StandardCharsets.US_ASCII);
        byte[] length = "1234567890".getBytes(StandardCharsets.US_ASCII);

        byte[] testData = "testData".getBytes(StandardCharsets.US_ASCII);
        byte[] testDataWithNameAndLength = new byte[header.length + name.length + length.length + testData.length];
        System.arraycopy(header, 0, testDataWithNameAndLength, 0, header.length);
        System.arraycopy(name, 0, testDataWithNameAndLength, header.length, name.length);
        System.arraycopy(length, 0, testDataWithNameAndLength, header.length + name.length, length.length);

        InputStream input = new ByteArrayInputStream(testDataWithNameAndLength);
        ArArchiveInputStream testStream = new ArArchiveInputStream(input);
        ArArchiveEntry nextEntry = testStream.getNextArEntry();
        assertNotNull(nextEntry);
        assertEquals("testName.txt", nextEntry.getName());
        assertEquals(1234567890L, nextEntry.getSize());
        assertNull(nextStream(testStream));
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        byte[] archiveData = new byte[1024];

        for (int i = 0; i < archiveData.length; i++) {
            signature[0] = archiveData[i];
            if (ArArchiveInputStream.matches(signature, i + 1)) {
                break;
            }
        }

        assertNotNull(signature);
    }
}