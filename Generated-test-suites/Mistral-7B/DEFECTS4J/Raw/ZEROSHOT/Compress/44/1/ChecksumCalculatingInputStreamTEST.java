package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException {
        // Test constructor
        Checksum calculator = Checksums.getChecksum("MD5");
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'I', 'n'});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(calculator, input);

        // Test read method
        byte[] buffer = new byte[1];
        int read = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{'T'}, buffer);
        Assert.assertEquals(378445874L, calculator.getValue()); // MD5 checksum for "TestInput"

        // Test read method with negative offset
        try {
            checksumCalculatingInputStream.read(buffer, -1, buffer.length);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            checksumCalculatingInputStream.read(buffer, Integer.MIN_VALUE, buffer.length);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test read method with length < 0
        try {
            checksumCalculatingInputStream.read(buffer, 1, -1);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            checksumCalculatingInputStream.read(buffer, 1, Integer.MIN_VALUE);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test read method with length = 0
        int readLength = checksumCalculatingInputStream.read(buffer, 0, 0);
        Assert.assertEquals(-1, readLength);

        // Test read method with readable data
        readLength = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, readLength);
        Assert.assertArrayEquals(new byte[]{'S'}, buffer);

        // Test read method with remaining data
        calculator = Checksums.getChecksum("SHA-256");
        input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'I', 'n'});
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(calculator, input);
        buffer = new byte[5];
        readLength = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, readLength);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', 'I'}, buffer);
        Assert.assertEquals(10099048783156615151L, calculator.getValue()); // SHA-256 checksum for "TestInput"

        // Test read method with remaining data, less data to read
        readLength = checksumCalculatingInputStream.read(buffer, 0, 3);
        Assert.assertEquals(3, readLength);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, buffer);

        readLength = checksumCalculatingInputStream.read(buffer, 2, 2);
        Assert.assertEquals(2, readLength);
        Assert.assertArrayEquals(new byte[]{'n', 'I'}, buffer);

        // Test getValue method
        long checksumValue = checksumCalculatingInputStream.getValue();
        Assert.assertEquals(10099048783156615151L, checksumValue); // SHA-256 checksum for "TestInput"

        // Test skip method
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(calculator, new ByteArrayInputStream(new byte[10]));
        long skipped = checksumCalculatingInputStream.skip(5);
        Assert.assertEquals(0, skipped);

        skipped = checksumCalculatingInputStream.skip(Long.MIN_VALUE);
        Assert.assertEquals(-1, skipped);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteBuffer() throws IOException {
        // Test constructor
        Checksum calculator = Checksums.getChecksum("MD5");
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'I', 'n'});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(calculator, input);

        // Test read method with ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(5);
        int read = checksumCalculatingInputStream.read(buffer);
        buffer.flip();
        Assert.assertEquals(1, read);
        Assert.assertEquals(Byte.TYPE, buffer.getType());
        Assert.assertEquals(0, buffer.get());
        buffer.compact();

        read = checksumCalculatingInputStream.read(buffer);
        buffer.flip();
        Assert.assertEquals(1, read);
        Assert.assertEquals(Byte.TYPE, buffer.getType());
        Assert.assertEquals((byte) 'S', buffer.get());
        buffer.compact();

        // Test read method with ByteBuffer, negative offset
        try {
            checksumCalculatingInputStream.read(buffer.asReadOnlyBuffer());
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        try {
            checksumCalculatingInputStream.read(ByteBuffer.allocate(5).asReadOnlyBuffer());
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        // Test getValue method with ByteBuffer
        long checksumValue = checksumCalculatingInputStream.getValue();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] tempBuffer = new byte[5];
        while (checksumCalculatingInputStream.read(tempBuffer) > 0) {
            outputStream.write(tempBuffer);
        }
        checksumValue = Checksums.getChecksum(Checksums.getChecksum("MD5")).getValue(outputStream.toByteArray());
        Assert.assertEquals(378445874L, checksumValue); // MD5 checksum for "TestInput"
    }

    @Test
    public void testChecksumCalculatingInputStreamWithMessageDigest() throws IOException {
        // Test constructor
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'I', 'n'});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, input);

        // Test read method
        byte[] buffer = new byte[1];
        int read = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{'T'}, buffer);
        byte[] mdChecksum = md.digest();
        Assert.assertArrayEquals(mdChecksum, new byte[]{'3', '7', '8', '4', '4', '5', '8', '7', '4', '5', '8', '7', '4', '4', '5', '8', '7', '4', '5', '8', '7', '4', '5', '8', '7', '4', '5', '8'}); // MD5 checksum for "TestInput"

        // Test read method with remaining data
        byte[] data = new byte[5];
        read = checksumCalculatingInputStream.read(data, 0, data.length);
        Assert.assertEquals(5, read);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', 'I'}, data);

        read = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, read);
    }
}