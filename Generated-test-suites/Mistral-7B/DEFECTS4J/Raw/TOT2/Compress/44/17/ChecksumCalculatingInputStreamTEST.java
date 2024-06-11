package org.apache.commons.compress.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.apache.commons.compress.utils.Adler32;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadNormalCase() throws IOException {
        // Expert 1: Method 1: read()
    }

    @Test(expected = IOException.class)
    public void testReadIOException() throws IOException {
        // Expert 1: Method 1: read()
    }

    @Test
    public void testReadByteArrayNormalCase() throws IOException {
        // Expert 1: Method 2: read(byte[]) and read(byte[], int, int)
    }

    @Test
    public void testReadByteArrayEdgeCase() throws IOException {
        // Expert 1: Method 2: read(byte[]) and read(byte[], int, int)
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayIOException() throws IOException {
        // Expert 1: Method 2: read(byte[]) and read(byte[], int, int)
    }

    @Test
    public void testSkipNormalCase() throws IOException {
        // Expert 2: Method 3: skip()
    }

    @Test
    public void testSkipEdgeCase() throws IOException {
        // Expert 2: Method 3: skip()
    }

    @Test(expected = IOException.class)
    public void testSkipIOException() throws IOException {
        // Expert 2: Method 3: skip()
    }

    @Test
    public void testGetValueNormalCase() throws IOException {
        // Expert 3: Method 4: getValue()
    }

    @Test
    public void testClose() throws IOException {
        Checksum checksum = new Adler32();
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5, 6 });
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        testStream.close();

        long calculatedChecksum = testStream.getValue();
        assertEquals(4691753478935189545L, calculatedChecksum);
    }

}