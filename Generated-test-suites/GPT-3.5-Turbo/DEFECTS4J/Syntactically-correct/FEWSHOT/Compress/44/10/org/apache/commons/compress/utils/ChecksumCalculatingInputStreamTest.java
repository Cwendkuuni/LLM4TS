package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private static final byte[] TEST_DATA = "This is a test".getBytes();

    private ChecksumCalculatingInputStream inputStream;
    private Checksum checksum;

    @Before
    public void setUp() {
        checksum = new Adler32();
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(TEST_DATA));
    }

    @Test
    public void testRead() throws IOException {
        int value;
        while ((value = inputStream.read()) != -1) {
            // Do nothing, just read.
        }
        assertEquals(checksum.getValue(), 262959467l);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer);
        assertEquals(new String(buffer), "This ");
        assertEquals(checksum.getValue(), 2502775583l);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertEquals(new String(buffer), " Is ");
        assertEquals(checksum.getValue(), 95762767l);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = inputStream.skip(4);
        assertTrue(bytesSkipped == 4l);
        assertEquals(checksum.getValue(), 1699759858l);
    }

    @Test
    public void testGetValue() {
        assertEquals(inputStream.getValue(), 0l);
    }
}