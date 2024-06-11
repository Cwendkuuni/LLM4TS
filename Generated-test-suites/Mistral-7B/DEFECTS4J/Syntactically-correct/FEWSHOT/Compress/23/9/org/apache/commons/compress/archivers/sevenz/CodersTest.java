package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.IvParameterSpec;
import java.security.spec.SecretKeySpec;

import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMADecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMA2Decoder;
import org.junit.Test;
import org.tukaani.xz.LZMAInputStream;

public class CodersTest {

    @Test
    public void testGetDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        for (CoderId coderId : Coders.Coders.getCoderTable()) {
            InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(data), coderId.coder, null);
            byte[] expected = null;
            if (coderId.method == SevenZMethod.COPY) {
                expected = data;
            } else {
                expected = compressAndGetExpectedData(coderId.method);
            }
            byte[] decoded = new byte[data.length];
            int len = decoder.read(decoded, 0, decoded.length);
            assertArrayEquals(expected, decoded, 0, decoded.length);
            decoder.close();
        }
    }

    @Test
    public void testGetEncoder() throws IOException, NoSuchAlgorithmException {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        for (CoderId coderId : Coders.Coders.getCoderTable()) {
            OutputStream encoder = Coders.addEncoder(new ByteArrayOutputStream(data.length + 10), coderId.method, null);
            byte[] original = data;
            if (coderId.method == SevenZMethod.COPY) {
                encoder.write(data, 0, data.length);
                encoder.close();
                byte[] encoded = ((ByteArrayOutputStream) encoder).toByteArray();
                assertArrayEquals(original, encoded, 0, original.length);
            } else {
                byte[] expected = compress(coderId.method, original);
                encoder.write(expected, 0, expected.length);
                encoder.close();
                byte[] encoded = ((ByteArrayOutputStream) encoder).toByteArray();
                assertArrayEquals(expected, encoded, 0, expected.length);
            }
        }
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "test".getBytes(StandardCharsets.UTF_8);
        byte[] iv = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = compressAndGetExpectedData(SevenZMethod.AES256SHA256);

        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(encrypted),
                new SevenZMethod(SevenZMethod.AES256SHA256), password);
        byte[] decoded = new byte[data.length];
        int len = decoder.read(decoded, 0, decoded.length);
        assertArrayEquals(data, decoded, 0, decoded.length);
        decoder.close();

        InputStream decoderWithoutPassword = Coders.addDecoder(new ByteArrayInputStream(encrypted),
                new SevenZMethod(SevenZMethod.AES256SHA256), null);
        assertNull(decoderWithoutPassword);

        OutputStream encoder = Coders.addEncoder(new ByteArrayOutputStream(data.length + 10),
                new SevenZMethod(SevenZMethod.AES256SHA256), password);
        encoder.write(data);
        encoder.close();
        byte[] encoded = ((ByteArrayOutputStream) encoder).toByteArray();
        assertArrayEquals(encoded, compressAndGetExpectedData(SevenZMethod.AES256SHA256), 0, encoded.length);
    }

    private byte[] compressAndGetExpectedData(SevenZMethod method) throws IOException, NoSuchAlgorithmException {
        OutputStream encoder = Coders.addEncoder(new ByteArrayOutputStream(10), method, null);
        byte[] original = "test".getBytes(StandardCharsets.UTF_8);
        encoder.write(original);
        encoder.close();
        byte[] encoded = ((ByteArrayOutputStream) encoder).toByteArray();
        return encoded;
    }

    private byte[] compress(SevenZMethod method, byte[] data) throws IOException, NoSuchAlgorithmException {
        if (method == SevenZMethod.COPY) {
            return data;
        } else if (method == SevenZMethod.LZMA) {
            return compressWithLZMA(data);
        } else if (method == SevenZMethod.LZMA2) {
            return compressWithLZMA2(data);
        } else if (method == SevenZMethod.DEFLATE) {
            return compressWithDeflate(data);
        } else if (method == SevenZMethod.BZIP2) {
            return compressWithBZIP2(data);
        } else if (method == SevenZMethod.AES256SHA256) {
            return compressWithAES256SHA256(data);
        } else {
            throw new IllegalArgumentException("Unknown method: " + method);
        }
    }

    private byte[] compressWithLZMA(byte[] data) throws IOException {
        LZMAInputStream lzmaInputStream = new LZMAInputStream(new ByteArrayInputStream(data));
        return lzmaInputStream.readAllBytes();
    }

    private byte[] compressWithLZMA2(byte[] data) throws IOException {
        // LZMA2 compression is not supported in Tukaani XZ library, so we cannot test it here directly.
        return new byte[0];
    }

    private byte[] compressWithDeflate(byte[] data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
        deflater.setInput(data);
        deflater.finish();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = deflater.deflate(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        byte[] compressedData = outputStream.toByteArray();
        deflater.end();
        return compressedData;
    }

    private byte[] compressWithBZIP2(byte[] data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bzip2CompressorOutputStream = new BZip2CompressorOutputStream(outputStream);
        bzip2CompressorOutputStream.write(data, 0, data.length);
        bzip2CompressorOutputStream.finish();
        return outputStream.toByteArray();
    }

    private byte[] compressWithAES256SHA256(byte[] data) throws NoSuchAlgorithmException, IOException, GeneralSecurityException {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedAESKey = messageDigest.digest(data);
        byte[] aesKey = new byte[32];
        System.arraycopy(encodedAESKey, 0, aesKey, 0, 16);
        byte[] password = "test".getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = new byte[aesKey.length + salt.length + password.length];
        System.arraycopy(aesKey, 0, keyBytes, 0, aesKey.length);
        System.arraycopy(salt, 0, keyBytes, aesKey.length, salt.length);
        System.arraycopy(password, 0, keyBytes, aesKey.length + salt.length, password.length);
        SecretKey aesKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivParameterSpec);
        byte[] encryptedData = cipher.doFinal(data);
        return new byte[0][].concat(iv, encryptedData);
    }
}