package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test cases for ChecksumCalculatingInputStream class.
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumStream;
    private long expectedChecksum;

    @Before
    public void setUp() throws Exception {
        byte[] data = "ChecksumCalculatingInputStream Test Data".getBytes();
        inputStream = new ByteArrayInputStream(data);
        checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), inputStream);
        expectedChecksum = 1125236582L; // Adler32 checksum for the test data
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = checksumStream.read();
        assertThat(readByte, is(notNullValue()));
        checksumStream.read(); // Consume the next byte to update checksum
        long checksum = checksumStream.getValue();
        assertThat(checksum, is(expectedChecksum));
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int readLength = checksumStream.read(buffer);
        byte[] expectedData = new byte[]{'C', 'h', 'e', 'c', 'k', 's', 'u', 'm', 'C', 'a'};
        for (int i = 0; i < readLength; i++) {
            assertThat(buffer[i], is(notNullValue()));
            assertThat(buffer[i], is(expectedData[i]));
        }
        long checksum = checksumStream.getValue();
        assertThat(checksum, is(expectedChecksum));
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        int readLength = checksumStream.read(buffer, offset, length);
        byte[] expectedData = new byte[]{'C', 'h', 'e', 'c', 'k'};
        for (int i = 0; i < readLength; i++) {
            assertThat(buffer[offset + i], is(expectedData[i]));
        }
        long checksum = checksumStream.getValue();
        assertThat(checksum, is(expectedChecksum));
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnRead() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(Checksums.md5(), inputStream);
        checksumStream.read(); // Read a byte
        // Simulate an IOException on read
        inputStream = new ThrowingInputStream(new IOException("IOException on read"));
        checksumStream.read(new byte[10]); // Should throw an IOException
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = checksumStream.skip(7);
        assertThat(skippedBytes, is(1)); // Only one byte can be skipped
        long checksum = checksumStream.getValue();
        assertThat(checksum, is(expectedChecksum));
    }

    @Test
    public void testGetValue() throws IOException {
        long actualChecksum = checksumStream.getValue();
        assertThat(actualChecksum, is(expectedChecksum));
    }

    @After
    public void teardown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
        if (checksumStream != null) {
            checksumStream.close();
        }
    }
}

class ThrowingInputStream extends InputStream {
    private IOException exception;

    public ThrowingInputStream(IOException exception) {
        this.exception = exception;
    }

    @Override
    public int read() throws IOException {
        throw exception;
    }

    @Override
    public int read(byte[] b) throws IOException {
        throw exception;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw exception;
    }

    @Override
    public long skip(long n) throws IOException {
        throw exception;
    }

    @Override
    public void close() throws IOException {
        // Close the underlying InputStream if any
        if (inputStream != null) {
            inputStream.close();
        }
    }
}