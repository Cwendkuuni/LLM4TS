package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.zip.Adler32;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ChecksumCalculatingInputStream}
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream testInput;
    private InputStream checksumInput;
    private ChecksumCalculatingInputStream checksumInputUnderTest;

    @Before
    public void setup() {
        final MessageDigest mockDigest = mock(MessageDigest.class);
        when(mockDigest.getName()).thenReturn("Adler32");
        Adler32 adler32 = new Adler32();
        adler32.update(mockDigest);
        this.checksumInput = new ByteArrayInputStream(new byte[0]);
        this.checksumInputUnderTest = new ChecksumCalculatingInputStream(adler32, this.checksumInput);
        this.testInput = new ByteArrayInputStream(
                "Test data for ChecksumCalculatingInputStream".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testRead() throws IOException {
        final byte[] buffer = new byte[1];
        final int read = checksumInputUnderTest.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
        checksumInputUnderTest.close();

        final byte[] testBuffer = new byte[1];
        final int testRead = testInput.read(testBuffer, 0, testBuffer.length);
        assertEquals(testRead, checksumInputUnderTest.read(testBuffer, 0, testBuffer.length));
        assertEquals(testRead, testInput.read(testBuffer, 0, testBuffer.length));
        assertEquals(testRead, checksumInputUnderTest.getValue() & 0xFF);

        final byte[] testBufferMultiple = new byte[2];
        final int testReadMultiple = testInput.read(testBufferMultiple, 0, testBufferMultiple.length);
        assertEquals(testReadMultiple, checksumInputUnderTest.read(testBufferMultiple, 0, testBufferMultiple.length));
        assertEquals(testReadMultiple, testInput.read(testBufferMultiple, 0, testBufferMultiple.length));
        assertEquals(testReadMultiple, checksumInputUnderTest.getValue() & 0xFFFF);
    }

    @Test
    public void testReadByteArray() throws IOException {
        final byte[] testBuffer = new byte[1];
        final int testRead = testInput.read(testBuffer, 0, testBuffer.length);
        final byte[] testData = new byte[] { testBuffer[0] };

        checksumInputUnderTest.read(testBuffer, 0, testBuffer.length);
        final byte[] checksumData = checksumInputUnderTest.readAllBytes();
        assertArrayEquals(testData, checksumData);

        testInput.close();
        checksumInputUnderTest.close();
    }

    @Test
    public void testReadSkippedBytes() throws IOException {
        final byte[] testBuffer = new byte[1];
        final int testRead = testInput.read(testBuffer, 0, testBuffer.length);
        final int skip = testRead * 2;

        checksumInputUnderTest.read(testBuffer, 0, testBuffer.length);
        checksumInputUnderTest.skip(skip);

        testInput.close();
        checksumInputUnderTest.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        final long n = 1;
        checksumInputUnderTest.skip(n);
    }

    @Test
    public void testGetValue() throws IOException {
        final byte[] testBuffer = new byte[1];
        final int testRead = testInput.read(testBuffer, 0, testBuffer.length);
        checksumInputUnderTest.read(testBuffer, 0, testBuffer.length);

        assertEquals(testRead, checksumInputUnderTest.getValue() & 0xFF);

        final byte[] testBufferMultiple = new byte[2];
        final int testReadMultiple = testInput.read(testBufferMultiple, 0, testBufferMultiple.length);
        checksumInputUnderTest.read(testBufferMultiple, 0, testBufferMultiple.length);

        assertEquals(testReadMultiple, checksumInputUnderTest.getValue() & 0xFFFF);

        testInput.close();
        checksumInputUnderTest.close();
    }
}