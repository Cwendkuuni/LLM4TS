package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testNewInstance() {
        InputStream is = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8});
        try {
            DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);
            assertNotNull(dcis);
        } finally {
            is.close();
        }
    }

    @Test
    public void testNewInstanceWithParameters() {
        InputStream is = new ByteArrayInputStream(new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8});
        DeflateParameters parameters = new DeflateParameters();
        try {
            DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is, parameters);
            assertNotNull(dcis);
            assertEquals(false, dcis.getInputStream().markSupported());
            assertEquals(false, dcis.marksSupported());
        } finally {
            is.close();
        }
    }

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        InputStream is = new ByteArrayInputStream(input);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        byte[] output = new byte[input.length];
        int bytesRead = dcis.read(output, 0, output.length);
        assertEquals(input.length, bytesRead);
        assertArrayEquals(input, output);

        input = new byte[]{-31, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 127};
        is = new ByteArrayInputStream(input);
        dcis = new DeflateCompressorInputStream(is);

        output = new byte[input.length + 1];
        bytesRead = dcis.read(output, 0, output.length);
        assertEquals(input.length + 1, bytesRead);
        assertArrayEquals(input, new byte[] {input[0], input[1], input[2], input[3], input[4], input[5], input[6],
                input[7], input[8], input[9], input[10], input[11], -128});
        assertEquals(-1, dcis.read());
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        InputStream is = new ByteArrayInputStream(input);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        byte output = dcis.read();
        assertEquals(-37, output);

        output = dcis.read();
        assertEquals(78, output);

        output = dcis.read();
        assertEquals(0, output);

        output = dcis.read();
        assertEquals(0, output);

        output = dcis.read();
        assertEquals(1, output);

        output = dcis.read();
        assertEquals(2, output);

        output = dcis.read();
        assertEquals(3, output);

        output = dcis.read();
        assertEquals(4, output);

        output = dcis.read();
        assertEquals(5, output);

        output = dcis.read();
        assertEquals(6, output);

        output = dcis.read();
        assertEquals(7, output);

        output = dcis.read();
        assertEquals(8, output);

        output = dcis.read();
        assertEquals(-1, output);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        InputStream is = new ByteArrayInputStream(input);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        byte[] output = new byte[input.length];
        int bytesRead = dcis.read(output, 0, output.length);
        assertEquals(input.length, bytesRead);
        assertArrayEquals(input, output);

        bytesRead = dcis.read(output, 0, output.length + 1);
        assertEquals(input.length + 1, bytesRead);
        assertArrayEquals(input, new byte[] {input[0], input[1], input[2], input[3], input[4], input[5], input[6],
                input[7], input[8], input[9], input[10], input[11], -128});
        assertEquals(-1, dcis.read());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        InputStream is = new ByteArrayInputStream(input);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        long bytesSkipped = dcis.skip(5);
        assertEquals(5, bytesSkipped);

        bytesSkipped = dcis.skip(input.length + 1);
        assertEquals(input.length + 1, bytesSkipped);

        bytesSkipped = dcis.skip(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, bytesSkipped);

        bytesSkipped = dcis.skip(Long.MAX_VALUE + 1);
        assertEquals(Long.MAX_VALUE + 1, bytesSkipped);

        input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6};
        is = new ByteArrayInputStream(input);
        dcis = new DeflateCompressorInputStream(is);

        bytesSkipped = dcis.skip(input.length + 10);
        assertEquals(input.length + 10, bytesSkipped);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        InputStream is = new ByteArrayInputStream(input);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        int available = dcis.available();
        assertEquals(input.length, available);

        input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7};
        is = new ByteArrayInputStream(input);
        dcis = new DeflateCompressorInputStream(is);

        available = dcis.available();
        assertEquals(input.length, available);
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        InputStream is = new ByteArrayInputStream(input);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        dcis.close();
        assertFalse(is.isOpen());
    }

    @Test
    public void testCheckZlibSignature() {
        byte[] zlibSignature = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] deflateSignature = new byte[]{-37, 78, 0, 0, 1, 2, 3};

        assertTrue(DeflateCompressorInputStream.checkZlibSignature(zlibSignature));
        assertFalse(DeflateCompressorInputStream.checkZlibSignature(deflateSignature));
    }

    @Test
    public void testReadWithZlibHeader() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] header = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 125, 128};
        InputStream is = new ByteArrayInputStream(header.concat(new ByteArrayInputStream(input.clone()).readAllBytes()));
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        byte[] output = new byte[input.length];
        int bytesRead = dcis.read(output, 0, output.length);
        assertEquals(input.length, bytesRead);
        assertArrayEquals(input, output);

        assertEquals(header.length + input.length, dcis.available());
    }

    @Test
    public void testReadWithZlibHeaderAndAdler32Checksum() throws IOException {
        byte[] input = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] header = new byte[]{-37, 78, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 125, 128};
        byte[] adler32Checksum = new byte[]{34, 123, 56, 78, 45, 90, 21, 67};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        outputStream.write(input);
        byte[] bytes = outputStream.toByteArray();

        InputStream is = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(is);

        byte[] output = new byte[input.length];
        int bytesRead = dcis.read(output, 0, output.length);
        assertEquals(input.length, bytesRead);
        assertArrayEquals(input, output);

        Adler32 adler32 = new Adler32();
        adler32.update(header);
        adler32.update(input);

        byte[] calculatedChecksum = new byte[adler32.getByteSize()];
        System.arraycopy(adler32.getValue(), 0, calculatedChecksum, 0, adler32.getByteSize());

        assertArrayEquals(calculatedChecksum, dcis.getAdler32Checksum());
        assertEquals(header.length + input.length, dcis.available());
    }
}