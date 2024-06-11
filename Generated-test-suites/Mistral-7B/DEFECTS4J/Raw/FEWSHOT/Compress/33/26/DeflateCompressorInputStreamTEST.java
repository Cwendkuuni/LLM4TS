package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testCreation() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
        assertSame(inputStream, deflateCompressorInputStream.getInputStream());
    }

    @Test
    public void testCreationWithoutZlibHeader() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        assertNotNull(deflateCompressorInputStream);
        assertSame(inputStream, deflateCompressorInputStream.getInputStream());
    }

    @Test
    public void testReadByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        for (int i = 0; i < input.length - 2; i++) {
            int read = deflateCompressorInputStream.read(buffer, 0, 1);
            assertEquals(input[i + 2], buffer[0]);
            assertTrue(read > 0);
        }

        int read = deflateCompressorInputStream.read(buffer, 0, 1);
        assertEquals(-1, read);
        assertTrue(deflateCompressorInputStream.available() == 0);
        assertTrue(deflateCompressorInputStream.close() > 0);

        byte[] expectedOutput = outputStream.toByteArray();
        assertEquals(expectedOutput, input, "Expected same output as the original input");
    }

    @Test
    public void testReadByte_WithZlibHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = new byte[]{0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        for (int i = 0; i < input.length - 2; i++) {
            int read = deflateCompressorInputStream.read(buffer, 0, 1);
            assertEquals(input[i + 3], buffer[0]);
            assertTrue(read > 0);
        }

        int read = deflateCompressorInputStream.read(buffer, 0, 1);
        assertEquals(-1, read);
        assertTrue(deflateCompressorInputStream.available() == 0);
        assertTrue(deflateCompressorInputStream.close() > 0);

        byte[] expectedOutput = outputStream.toByteArray();
        assertEquals(expectedOutput, new byte[]{input[0], input[1], input[2]}, "Expected same output as the original input without the zlib header");
    }

    @Test
    public void testReadBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[4];
        int offset = 0;
        int read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(4, read);
        for (int i = 0; i < 4; i++) {
            assertEquals(input[i + 3], buffer[i]);
        }

        read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(-1, read);

        // Test reading last data
        buffer = new byte[3];
        read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(3, read);
        for (int i = 0; i < 3; i++) {
            assertEquals(input[input.length - 2 + i], buffer[i]);
        }

        // Test reading empty data
        buffer = new byte[1];
        read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(-1, read);

        assertTrue(deflateCompressorInputStream.close() > 0);

        byte[] expectedOutput = outputStream.toByteArray();
        assertEquals(expectedOutput, input, "Expected same output as the original input");
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skipped = deflateCompressorInputStream.skip(4L);
        assertEquals(4L, skipped);
        byte[] buffer = new byte[1];
        int offset = 0;
        int read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(input[3], buffer[0]);
        assertTrue(read > 0);

        skipped = deflateCompressorInputStream.skip(input.length - 7);
        assertEquals(input.length - 7, skipped);
        read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(-1, read);

        assertTrue(deflateCompressorInputStream.close() > 0);

        byte[] expectedOutput = outputStream.toByteArray();
        assertEquals(expectedOutput, new byte[]{input[0], input[1], input[2]}, "Expected same output as the original input without the zlib header");
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(input.length - 2, deflateCompressorInputStream.available());
        deflateCompressorInputStream.read(new byte[1][], 0, 1);
        assertEquals(input.length - 3, deflateCompressorInputStream.available());

        assertTrue(deflateCompressorInputStream.close() > 0);

        byte[] expectedOutput = outputStream.toByteArray();
        assertEquals(expectedOutput, input, "Expected same output as the original input");
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        int offset = 0;
        int read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(input[0], buffer[0]);
        read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(input[1], buffer[0]);
        read = deflateCompressorInputStream.read(buffer, offset, buffer.length);
        assertEquals(input[2], buffer[0]);

        assertTrue(deflateCompressorInputStream.close() > 0);

        byte[] expectedOutput = outputStream.toByteArray();
        assertEquals(expectedOutput, new byte[]{input[0], input[1], input[2]}, "Expected same output as the original input without the zlib header");
    }

    @Test
    public void testSignatureIsZlib() {
        byte[] zlibInput = new byte[]{0x78, 0xDA, 0x10, 0x14, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        DeflateCompressorInputStream zlibInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(zlibInput));
        assertTrue(zlibInputStream.isZlibHeaderPresent());

        byte[] noZlibInput = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        DeflateCompressorInputStream noZlibInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(noZlibInput), new DeflateParameters());
        assertFalse(noZlibInputStream.isZlibHeaderPresent());
    }
}