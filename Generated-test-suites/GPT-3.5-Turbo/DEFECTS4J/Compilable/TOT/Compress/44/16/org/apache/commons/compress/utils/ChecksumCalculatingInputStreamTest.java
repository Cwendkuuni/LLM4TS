package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    private ByteArrayInputStream inputStream;
    private ChecksumCalculatingInputStream checksumInputStream;

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = "Hello World".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new Adler32();
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumInputStream.read();
        while (readByte != -1) {
            readByte = checksumInputStream.read();
        }

        assertEquals(checksum.getValue(), 0x7016D258L);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = "Hello World".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new Adler32();
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] readBytes = new byte[5];
        checksumInputStream.read(readBytes);

        assertEquals(checksum.getValue(), 0x7A1DB8CEL);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] testData = "Hello World".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new Adler32();
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] readBytes = new byte[5];
        checksumInputStream.read(readBytes, 1, 3);

        assertEquals(checksum.getValue(), 0x20940D8DL);
    }

    @Test
    public void testSkipBytes() throws IOException {
        byte[] testData = "Hello World".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new Adler32();
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skip = checksumInputStream.skip(5);

        assertEquals(skip, 5);
        assertEquals(checksum.getValue(), 0x2144D59DL);
    }

    @Test
    public void testExhaustInputStream() throws IOException {
        byte[] testData = "Hello World".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new Adler32();
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumInputStream.read();
        while (readByte != -1) {
            readByte = checksumInputStream.read();
        }

        // Modify the checksum value to simulate mismatch
        long incorrectValue = checksum.getValue() + 1;

        // Validate that checksum matches the expected value
        assertEquals(checksumInputStream.getValue(), incorrectValue);

        // Validate that an IOException is thrown when reading beyond the end of stream
        try {
            checksumInputStream.read();
            fail("An IOException is expected here");
        } catch (IOException e) {
            // expected IOException
        }
    }
}