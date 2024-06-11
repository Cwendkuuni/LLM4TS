import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.SecretKeySpec;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws Exception {
        InputStream source = new ByteArrayInputStream(new byte[]{(byte) 0x01});
        OutputStream destination = new ByteArrayOutputStream();
        Coders.addEncoder(destination, SevenZMethod.COPY, null);
        Coders.addDecoder(source, Coders.COPY_DECODER, null);

        byte[] expected = new byte[]{(byte) 0x01};
        byte[] result = new byte[expected.length];
        byte[] buffer = new byte[expected.length];

        int read = 0;
        while (read < expected.length) {
            read = Coders.addDecoder(new ByteArrayInputStream(result), Coders.COPY_DECODER, null).read(buffer, 0, buffer.length);
            System.arraycopy(buffer, 0, result, 0, read);
        }

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testLZMADecoder() throws Exception {
        byte[] compressedData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};

        InputStream source = new ByteArrayInputStream(compressedData);
        OutputStream destination = new ByteArrayOutputStream();
        Coders.addEncoder(destination, SevenZMethod.LZMA, null);
        Coders.addDecoder(source, Coders.LZMA_DECODER, null);

        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};
        byte[] result = new byte[expected.length];

        int read = 0;
        while (read < expected.length) {
            read = Coders.addDecoder(new ByteArrayInputStream(result), Coders.LZMA_DECODER, null).read(result, 0, result.length);
        }

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testAES256SHA256Decoder() throws Exception {
        final byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);

        byte[] plainText = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};
        byte[] encryptedData = null;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            OutputStream output = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
            output.write(plainText);
            output.flush();
            encryptedData = outputStream.toByteArray();
        }

        InputStream source = new ByteArrayInputStream(encryptedData);
        OutputStream destination = new ByteArrayOutputStream();
        Coders.addDecoder(source, Coders.AES256SHA256_DECODER, password);
        Coders.addEncoder(destination, SevenZMethod.COPY, null);

        byte[] expected = plainText;
        byte[] result = new byte[expected.length];

        int read = 0;
        while (read < expected.length) {
            read = Coders.addDecoder(new ByteArrayInputStream(result), Coders.AES256SHA256_DECODER, password).read(result, 0, result.length);
        }

        Assert.assertArrayEquals(expected, result);

        // Edge case: incorrect password
        try {
            Coders.addDecoder(source, Coders.AES256SHA256_DECODER, new byte[0]);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Edge case: empty data
        try (ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream()) {
            OutputStream emptyOutput = Coders.addEncoder(emptyOutputStream, SevenZMethod.AES256SHA256, password);
            emptyOutput.flush();
            InputStream emptyInput = Coders.addDecoder(new ByteArrayInputStream(emptyOutputStream.toByteArray()), Coders.AES256SHA256_DECODER, password);
            byte[] emptyResult = new byte[1];
            int emptyRead = emptyInput.read(emptyResult);
            Assert.assertEquals(-1, emptyRead);
        }

        // Error case: unsupported compression method
        try {
            InputStream unsupportedInputStream = new ByteArrayInputStream(encryptedData);
            Coders.addDecoder(unsupportedInputStream, new Coder() {}, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeflateDecoder() throws Exception {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0x9f, (byte) 0xf3, (byte) 0xa9, (byte) 0x50, (byte) 0x59, (byte) 0xdb, (byte) 0xd5, (byte) 0x53, (byte) 0x53, (byte) 0x58, (byte) 0x51, (byte) 0x52, (byte) 0x44, (byte) 0x55, (byte) 0x46, (byte) 0x46, (byte) 0x47, (byte) 0x47, (byte) 0x48, (byte) 0x48, (byte) 0x49, (byte) 0x49};

        InputStream source = new ByteArrayInputStream(compressedData);
        OutputStream destination = new ByteArrayOutputStream();
        Coders.addEncoder(destination, SevenZMethod.DEFLATE, null);
        Coders.addDecoder(source, Coders.DEFLATE_DECODER, null);

        byte[] expected = new byte[]{(byte) 0x41, (byte) 0x4d, (byte) 0x49, (byte) 0x4c, (byte) 0x4e, (byte) 0x41, (byte) 0x5a, (byte) 0x4e, (byte) 0x53, (byte) 0x4d, (byte) 0x51, (byte) 0x54, (byte) 0x4c, (byte) 0x45, (byte) 0x4f, (byte) 0x4b, (byte) 0x45, (byte) 0x42, (byte) 0x53, (byte) 0x54, (byte) 0x4d, (byte) 0x50, (byte) 0x52, (byte) 0x55, (byte) 0x51, (byte) 0x4a, (byte) 0x54, (byte) 0x45, (byte) 0x53, (byte) 0x55, (byte) 0x48, (byte) 0x4c, (byte) 0x52, (byte) 0x4f};
        byte[] result = new byte[expected.length];

        int read = 0;
        while (read < expected.length) {
            read = Coders.addDecoder(new ByteArrayInputStream(result), Coders.DEFLATE_DECODER, null).read(result, 0, result.length);
        }

        Assert.assertArrayEquals(expected, result);

        // Edge case: incorrect password
        try {
            Coders.addDecoder(source, Coders.DEFLATE_DECODER, new byte[0]);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Error case: unsupported compression method
        try {
            InputStream unsupportedInputStream = new ByteArrayInputStream(compressedData);
            Coders.addDecoder(unsupportedInputStream, new Coder() {}, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}