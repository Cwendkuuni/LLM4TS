
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        Checksum checksum = new Adler32();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        int result = checksumInputStream.read();

        assertEquals(1, result);
        assertEquals(1, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(2, result);
        assertEquals(258, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(3, result);
        assertEquals(66051, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(4, result);
        assertEquals(16909060, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(5, result);
        assertEquals(4328719365L, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(-1, result);
        assertEquals(4328719365L, checksum.getValue());

        inputStream.close();
        checksumInputStream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        Checksum checksum = new Adler32();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        byte[] buffer = new byte[3];
        int result = checksumInputStream.read(buffer);

        assertEquals(3, result);
        assertEquals(1, checksum.getValue());

        assertTrue(buffer[0] == 1 && buffer[1] == 2 && buffer[2] == 3);

        result = checksumInputStream.read(buffer);

        assertEquals(2, result);
        assertEquals(66051, checksum.getValue());

        assertTrue(buffer[0] == 4 && buffer[1] == 5 && buffer[2] == 3);

        result = checksumInputStream.read(buffer);

        assertEquals(-1, result);
        assertEquals(4328719365L, checksum.getValue());

        inputStream.close();
        checksumInputStream.close();
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        Checksum checksum = new Adler32();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        byte[] buffer = new byte[4];
        int result = checksumInputStream.read(buffer, 1, 2);

        assertEquals(2, result);
        assertEquals(256, checksum.getValue());

        assertTrue(buffer[0] == 0 && buffer[1] == 1 && buffer[2] == 2 && buffer[3] == 0);

        result = checksumInputStream.read(buffer, 2, 3);

        assertEquals(1, result);
        assertEquals(66180, checksum.getValue());

        assertTrue(buffer[0] == 0 && buffer[1] == 1 && buffer[2] == 2 && buffer[3] == 0);

        result = checksumInputStream.read(buffer, 0, 4);

        assertEquals(2, result);
        assertEquals(66051, checksum.getValue());

        assertTrue(buffer[0] == 3 && buffer[1] == 4 && buffer[2] == 5 && buffer[3] == 0);

        result = checksumInputStream.read(buffer, 0, 4);

        assertEquals(-1, result);
        assertEquals(4328719365L, checksum.getValue());

        inputStream.close();
        checksumInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        Checksum checksum = new Adler32();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        long result = checksumInputStream.skip(3);

        assertEquals(3, result);
        assertEquals(16909060, checksum.getValue());

        result = checksumInputStream.skip(2);

        assertEquals(0, result);
        assertEquals(4328719365L, checksum.getValue());

        inputStream.close();
        checksumInputStream.close();
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        Adler32 checksum = new Adler32();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum,
                inputStream);

        int result = checksumInputStream.read();

        assertEquals(1, result);
        assertEquals(1, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(2, result);
        assertEquals(258, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(3, result);
        assertEquals(66051, checksum.getValue());

        long value = checksumInputStream.getValue();

        assertEquals(66051, value);

        result = checksumInputStream.read();

        assertEquals(4, result);
        assertEquals(16909060, checksum.getValue());

        result = checksumInputStream.read();

        assertEquals(5, result);
        assertEquals(4328719365L, checksum.getValue());

        value = checksumInputStream.getValue();

        assertEquals(4328719365L, value);

        result = checksumInputStream.read();

        assertEquals(-1, result);
        assertEquals(4328719365L, checksum.getValue());

        inputStream.close();
        checksumInputStream.close();
    }

}
