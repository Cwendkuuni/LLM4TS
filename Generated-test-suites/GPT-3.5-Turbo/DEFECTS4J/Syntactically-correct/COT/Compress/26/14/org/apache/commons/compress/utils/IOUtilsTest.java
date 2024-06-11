package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy_basicFunctionality() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        // Act
        long count = IOUtils.copy(input, output);

        // Assert
        Assert.assertEquals(inputBytes.length, count);
        Assert.assertArrayEquals(inputBytes, ((ByteArrayOutputStream) output).toByteArray());
    }

    @Test
    public void testCopy_customBufferSize() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();
        int bufferSize = 2;

        // Act
        long count = IOUtils.copy(input, output, bufferSize);

        // Assert
        Assert.assertEquals(inputBytes.length, count);
        Assert.assertArrayEquals(inputBytes, ((ByteArrayOutputStream) output).toByteArray());
    }

    @Test
    public void testSkip_basicFunctionality() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 2;

        // Act
        long skipped = IOUtils.skip(input, numToSkip);

        // Assert
        Assert.assertEquals(numToSkip, skipped);
        Assert.assertEquals(3, input.read());
        Assert.assertEquals(4, input.read());
        Assert.assertEquals(5, input.read());
    }

    @Test
    public void testSkip_skipReturnsZero() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 10;

        // Act
        long skipped = IOUtils.skip(input, numToSkip);

        // Assert
        Assert.assertEquals(inputBytes.length, skipped);
        Assert.assertEquals(-1, input.read());
    }

    @Test
    public void testReadFully_basicFunctionality() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];

        // Act
        int bytesRead = IOUtils.readFully(input, buffer);

        // Assert
        Assert.assertEquals(inputBytes.length, bytesRead);
        Assert.assertArrayEquals(inputBytes, buffer);
    }

    @Test
    public void testReadFully_partialRead() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[6];

        // Act
        int bytesRead = IOUtils.readFully(input, buffer);

        // Assert
        Assert.assertEquals(inputBytes.length, bytesRead);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 0}, buffer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_invalidOffset() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];
        int offset = 1;
        int len = 5;

        // Act
        IOUtils.readFully(input, buffer, offset, len);
    }

    @Test
    public void testReadFully_validOffsetAndLength() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[7];
        int offset = 1;
        int len = 5;

        // Act
        int bytesRead = IOUtils.readFully(input, buffer, offset, len);

        // Assert
        Assert.assertEquals(inputBytes.length, bytesRead);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 0}, buffer);
    }

    @Test
    public void testToByteArray_basicFunctionality() throws IOException {
        // Arrange
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);

        // Act
        byte[] result = IOUtils.toByteArray(input);

        // Assert
        Assert.assertArrayEquals(inputBytes, result);
    }

    @Test
    public void testCloseQuietly_withNonNullObject() {
        // Arrange
        Closeable closeable = new MockCloseable();

        // Act & Assert
        IOUtils.closeQuietly(closeable);
    }

    @Test
    public void testCloseQuietly_withNullObject() {
        // Act & Assert
        IOUtils.closeQuietly(null);
    }

    private static class MockCloseable implements Closeable {
        @Override
        public void close() throws IOException {
            // Empty
        }
    }
}