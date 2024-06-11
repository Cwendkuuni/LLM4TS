package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.tukaani.xz.LZMAInputStream;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CodersTest {

    @Test
    public void testDecodeCopyDecoder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test Data"[0]});
        Coder copyDecoder = new CopyDecoder();
        InputStream decoded = Coders.addDecoder(input, copyDecoder, null);
        byte[] decodedData = new byte[1024];
        int bytesRead = decoded.read(decodedData);
        assertEquals(1, bytesRead);
        assertArrayEquals(new byte[]{"Test Data"[0]}, decodedData, 0, 1);
    }

    @Test
    public void testDecodeLZMADecoder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x4c, 0x4c, 0x4d, 0x4c, 0x4d, 0x4c, 0x4d, 0x4c, 0x4d});
        Coder lzmaDecoder = new LZMADecoder();
        InputStream decoded = Coders.addDecoder(input, lzmaDecoder, null);
        byte[] decodedData = new byte[10];
        int bytesRead = decoded.read(decodedData);
        assertEquals(10, bytesRead);
        assertArrayEquals(new byte[]{"TestData"[0]}, decodedData, 0, 10);
    }

    @Test
    public void testDecodeDeflateDecoder() throws IOException, NoSuchAlgorithmException {
        String testString = "Test Data";
        byte[] compressedData = compressStringToDeflate(testString);
        InputStream input = new ByteArrayInputStream(compressedData);
        Coder deflateDecoder = new DeflateDecoder();
        InputStream decoded = Coders.addDecoder(input, deflateDecoder, null);
        byte[] decodedData = new byte[testString.length() * 2];
        int bytesRead = decoded.read(decodedData);
        assertEquals(testString.length() * 2, bytesRead);
        assertArrayEquals(testString.getBytes(StandardCharsets.UTF_8), decodedData, 0, testString.length());
    }

    @Test
    public void testDecodeBZIP2Decoder() throws IOException {
        String testString = "Test Data";
        byte[] compressedData = compressStringToBZIP2(testString);
        InputStream input = new ByteArrayInputStream(compressedData);
        Coder bzip2Decoder = new BZIP2Decoder();
        InputStream decoded = Coders.addDecoder(input, bzip2Decoder, null);
        byte[] decodedData = new byte[testString.length() * 2];
        int bytesRead = decoded.read(decodedData);
        assertEquals(testString.length() * 2, bytesRead);
        assertArrayEquals(testString.getBytes(StandardCharsets.UTF_8), decodedData, 0, testString.length());
    }

    @Test
    public void testDecodeAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        String testString = "Test Data";
        byte[] encryptedData = encryptStringToAES256SHA256(testString);
        InputStream input = new ByteArrayInputStream(encryptedData);
        Coder aes256SHA256Decoder = new AES256SHA256Decoder();
        InputStream decoded = Coders.addDecoder(input, aes256SHA256Decoder, null);
        byte[] decodedData = new byte[testString.length() * 2];
        int bytesRead = decoded.read(decodedData);
        assertEquals(testString.length() * 2, bytesRead);
        assertArrayEquals(testString.getBytes(StandardCharsets.UTF_8), decodedData, 0, testString.length());
    }

    @Test
    public void testEncodeCopyEncoder() throws IOException {
        String testString = "Test Data";
        byte[] originalData = testString.getBytes(StandardCharsets.UTF_8);
        OutputStream output = new ByteArrayOutputStream();
        Coder copyEncoder = new CopyEncoder();
        OutputStream encoded = Coders.addEncoder(output, null, null);
        encoded.write(originalData);
        encoded.flush();
        byte[] encodedData = output.toByteArray();
        byte[] decodedData = Coders.addDecoder(new ByteArrayInputStream(encodedData), copyEncoder, null).readAllBytes();
        assertArrayEquals(originalData, decodedData);
    }

    @Test
    public void testEncodeLZMAEncoder() throws IOException {
        String testString = "Test Data";
        byte[] originalData = testString.getBytes(StandardCharsets.UTF_8);
        OutputStream output = new ByteArrayOutputStream();
        Coder lzmaEncoder = new LZMADecoder();
        OutputStream encoded = Coders.addEncoder(output, new SevenZMethod(LZMAMethod.LZMA), null);
        encoded.write(originalData);
        encoded.flush();
        byte[] encodedData = output.toByteArray();
        byte[] decodedData = Coders.addDecoder(new ByteArrayInputStream(encodedData), lzmaEncoder, null).readAllBytes();
        assertArrayEquals(originalData, decodedData);
    }

    @Test
    public void testEncodeDeflateEncoder() throws IOException, NoSuchAlgorithmException {
        String testString = "Test Data";
        byte[] originalData = testString.getBytes(StandardCharsets.UTF_8);
        OutputStream output = new ByteArrayOutputStream();
        Coder deflateEncoder = new DeflateEncoder();
        OutputStream encoded = Coders.addEncoder(output, new SevenZMethod(DeflateMethod.DEFLATE), null);
        encoded.write(originalData);
        encoded.flush();
        byte[] encodedData = output.toByteArray();
        byte[] decodedData = Coders.addDecoder(new ByteArrayInputStream(encodedData), deflateEncoder, null).readAllBytes();
        assertArrayEquals(originalData, decodedData);
    }

    @Test
    public void testEncodeBZIP2Encoder() throws IOException {
        String testString = "Test Data";
        byte[] originalData = testString.getBytes(StandardCharsets.UTF_8);
        OutputStream output = new ByteArrayOutputStream();
        Coder bzip2Encoder = new BZIP2Decoder();
        OutputStream encoded = Coders.addEncoder(output, new SevenZMethod(BZIP2Method.BZIP2), null);
        encoded.write(originalData);
        encoded.flush();
        byte[] encodedData = output.toByteArray();
        byte[] decodedData = Coders.addDecoder(new ByteArrayInputStream(encodedData), bzip2Encoder, null).readAllBytes();
        assertArrayEquals(originalData, decodedData);
    }

    @Test
    public void testEncodeAES256SHA256Encoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        String testString = "Test Data";
        byte[] originalData = testString.getBytes(StandardCharsets.UTF_8);
        byte[] key = "SecretKey".getBytes(StandardCharsets.UTF_8);
        OutputStream output = new ByteArrayOutputStream();
        Coder aes256SHA256Encoder = new AES256SHA256Decoder();
        OutputStream encoded = Coders.addEncoder(output, new SevenZMethod(AES256SHA256Method.AES256SHA256), key);
        encoded.write(originalData);
        encoded.flush();
        byte[] encodedData = output.toByteArray();
        byte[] decodedData = Coders.addDecoder(new ByteArrayInputStream(encodedData), aes256SHA256Encoder, key).readAllBytes();
        assertArrayEquals(originalData, decodedData);
    }

    @Test
    public void testNullDecoder() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Coder nullDecoder = null;
        try {
            Coders.addDecoder(input, nullDecoder, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testNullEncoder() {
        OutputStream output = new ByteArrayOutputStream();
        Coder nullEncoder = null;
        try {
            Coders.addEncoder(output, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testUnknownDecoder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Coder unknownDecoder = new Object() {};
        try {
            Coders.addDecoder(input, unknownDecoder, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testUnknownEncoder() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        Coder unknownEncoder = new Object() {};
        try {
            Coders.addEncoder(output, new SevenZMethod(UnknownMethod.UNKNOWN), null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    private byte[] compressStringToDeflate(String string) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Deflater compressor = new Deflater();
        compressor.setInput(string.getBytes(StandardCharsets.UTF_8));
        compressor.finish();
        byte[] buffer = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buffer);
            output.write(buffer, 0, count);
        }
        compressor.end();
        return output.toByteArray();
    }

    private byte[] compressStringToBZIP2(String string) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BZip2CompressorOutputStream compressor = new BZip2CompressorOutputStream(output);
        compressor.write(string.getBytes(StandardCharsets.UTF_8));
        compressor.finish();
        return output.toByteArray();
    }

    private byte[] encryptStringToAES256SHA256(String string) throws NoSuchAlgorithmException, GeneralSecurityException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        sha256.update(stringBytes);
        byte[] hash = sha256.digest();
        SecretKeySpec aesKey = new SecretKeySpec(hash, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
        byte[] encryptedData = cipher.doFinal(stringBytes);
        return encryptedData;
    }
}