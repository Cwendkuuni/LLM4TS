import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SpecifiedAlgorithmParameterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CodersTest {

    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = "Copy test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);
        final OutputStream os = outputStream;

        // Test encoding and decoding with empty password
        final Coder coder = Coders.addEncoder(os, SevenZMethod.COPY, null);
        final InputStream decoder = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[inputData.length];
        decoder.read(decodedData);
        assertEquals(inputData.length, decoder.available());
        assertEquals(inputData, decodedData);

        // Test encoding and decoding with password
        final byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        final Coder coderWithPassword = Coders.addEncoder(os, SevenZMethod.COPY, password);
        final InputStream decoderWithPassword = Coders.addDecoder(is, coderWithPassword, password);
        decoderWithPassword.read(decodedData);
        assertEquals(inputData.length, decoderWithPassword.available());
        assertEquals(inputData, decodedData);
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, SpecifiedAlgorithmParameterException {
        final byte[] inputData = "LZMA test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);

        // Test decoding with empty password
        final Coder coder = Coders.addDecoder(is, SevenZMethod.LZMA, null);
        final OutputStream os = outputStream;
        final Coder decoder = Coders.addEncoder(os, SevenZMethod.LZMA, null);
        final InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(decoder.encode(new ByteArrayOutputStream().toByteArray(), null)[0]), decoder, null);
        byte[] decodedData = new byte[inputData.length];
        decodedStream.read(decodedData);
        assertEquals(inputData.length, decodedStream.available());
        assertEquals(inputData, decodedData);

        // Test decoding with password
        final byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        final Coder coderWithPassword = Coders.addEncoder(os, SevenZMethod.LZMA, password);
        final InputStream decoderWithPassword = Coders.addDecoder(is, coderWithPassword, password);
        decoderWithPassword.read(decodedData);
        assertEquals(inputData.length, decoderWithPassword.available());
        assertEquals(inputData, decodedData);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        final byte[] inputData = "Unsupported test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);

        final Coder coder = new DeflateDecoder();
        final OutputStream os = outputStream;
        final Coder encoder = Coders.addEncoder(os, SevenZMethod.COPY, null);

        final InputStream decoder = Coders.addDecoder(is, coder, null);
        decoder.read(new byte[0]);
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        final byte[] inputData = "Deflate test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);

        // Test decoding with empty password
        final Coder coder = Coders.addDecoder(is, SevenZMethod.DEFLATE, null);
        final OutputStream os = outputStream;
        final Coder decoder = Coders.addEncoder(os, SevenZMethod.DEFLATE, null);
        final InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(decoder.encode(new ByteArrayOutputStream().toByteArray(), null)[0]), decoder, null);
        byte[] decodedData = new byte[inputData.length];
        decodedStream.read(decodedData);
        assertEquals(inputData.length, decodedStream.available());
        assertEquals(inputData, decodedData);

        // Test decoding with password
        final byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        final Coder coderWithPassword = Coders.addEncoder(os, SevenZMethod.DEFLATE, password);
        final InputStream decoderWithPassword = Coders.addDecoder(is, coderWithPassword, password);
        decoderWithPassword.read(decodedData);
        assertEquals(inputData.length, decoderWithPassword.available());
        assertEquals(inputData, decodedData);
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        final byte[] inputData = "BZIP2 test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);

        // Test decoding with empty password
        final Coder coder = Coders.addDecoder(is, SevenZMethod.BZIP2, null);
        final OutputStream os = outputStream;
        final Coder decoder = Coders.addEncoder(os, SevenZMethod.BZIP2, null);
        final InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(decoder.encode(new ByteArrayOutputStream().toByteArray(), null)[0]), decoder, null);
        byte[] decodedData = new byte[inputData.length];
        decodedStream.read(decodedData);
        assertEquals(inputData.length, decodedStream.available());
        assertEquals(inputData, decodedData);

        // Test decoding with password
        final byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        final Coder coderWithPassword = Coders.addEncoder(os, SevenZMethod.BZIP2, password);
        final InputStream decoderWithPassword = Coders.addDecoder(is, coderWithPassword, password);
        decoderWithPassword.read(decodedData);
        assertEquals(inputData.length, decoderWithPassword.available());
        assertEquals(inputData, decodedData);
    }

    @Test(expected = GeneralSecurityException.class)
    public void testAES256SHA256DecoderWithInvalidPassword() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = "AES256SHA256 test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);

        final Coder coder = Coders.addDecoder(is, SevenZMethod.AES256SHA256, null);
        final OutputStream os = outputStream;
        final Coder decoder = Coders.addEncoder(os, SevenZMethod.AES256SHA256, "wrongPassword".getBytes(StandardCharsets.UTF_8));
        final InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(decoder.encode(new ByteArrayOutputStream().toByteArray(), null)[0]), decoder, null);
        byte[] decodedData = new byte[inputData.length];
        decodedStream.read(decodedData);
        assertEquals(inputData.length, decodedStream.available());
        assertArrayEquals(inputData, decodedData);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = "AES256SHA256 test data".getBytes(StandardCharsets.UTF_8);
        final InputStream is = new ByteArrayInputStream(inputData);

        final Coder coder = Coders.addDecoder(is, SevenZMethod.AES256SHA256, "password".getBytes(StandardCharsets.UTF_8));
        final OutputStream os = outputStream;
        final Coder decoder = Coders.addEncoder(os, SevenZMethod.AES256SHA256, "password".getBytes(StandardCharsets.UTF_8));
        final InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(decoder.encode(new ByteArrayOutputStream().toByteArray(), null)[0]), decoder, null);
        byte[] decodedData = new byte[inputData.length];
        decodedStream.read(decodedData);
        assertEquals(inputData.length, decodedStream.available());
        assertEquals(inputData, decodedData);
    }
}