package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.SecretKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.junit.Test;
import org.tukaani.xz.LZMAInputStream;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        byte[] password = new byte[8];
        byte[] inputData = new byte[10];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (byte) i;
        }
        
        try (InputStream is = new ByteArrayInputStream(inputData)) {
            Coder coder = new DeflateDecoder();
            InputStream decoder = Coders.addDecoder(is, coder, password);
            assertSame(is, decoder);
            
            byte[] resultData = new byte[inputData.length];
            int read = 0;
            while (read < resultData.length) {
                read += decoder.read(resultData, read, resultData.length - read);
            }
            assertArrayEquals(inputData, resultData);
        }
        
        try {
            Coder coder = new AES256SHA256Decoder();
            Coders.addDecoder(null, coder, password);
            assertThrows(IOException.class, () -> Coders.addDecoder(new ByteArrayInputStream(password), coder, password));
            assertThrows(IOException.class, () -> Coders.addDecoder(new ByteArrayInputStream(password), coder, null));
            assertThrows(IOException.class, () -> Coders.addDecoder(new ByteArrayInputStream(inputData), coder, password),
                    "Unsupported compression method null");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAddEncoder() {
        try (OutputStream out = new ByteArrayOutputStream()) {
            Coder coder = new DeflateEncoder();
            OutputStream encoder = Coders.addEncoder(out, SevenZMethod.DEFLATE, new byte[0]);
            assertSame(out, encoder);
            
            byte[] testData = new byte[] { 1, 2, 3 };
            encoder.write(testData);
            encoder.flush();
            byte[] resultData = out.toByteArray();
            assertArrayEquals(testData, resultData);
        }

        try {
            Coders.addEncoder(null, SevenZMethod.COPY, new byte[0]);
            Coders.addEncoder(new ByteArrayOutputStream(), SevenZMethod.AES256SHA256, new byte[0]);
            assertThrows(IOException.class, () -> Coders.addEncoder(new ByteArrayInputStream(new byte[0]), SevenZMethod.BZIP2, new byte[0]));
            assertThrows(IOException.class, () -> Coders.addEncoder(new ByteArrayOutputStream(), SevenZMethod.LZMA, new byte[0]),
                    "Unsupported compression method LZMA");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLZMADecoder() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 })) {
            Coder coder = new LZMADecoder();
            InputStream decoder = Coders.addDecoder(is, coder, new byte[0]);
            assertSame(is, decoder);

            byte[] resultData = new byte[8];
            int read = 0;
            while (read < resultData.length) {
                read += decoder.read(resultData, read, resultData.length - read);
            }
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, resultData);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            byte[] inputData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
            Coder coder = new LZMADecoder();
            OutputStream encoder = Coders.addEncoder(out, SevenZMethod.LZMA, new byte[0]);
            encoder.write(inputData);
            encoder.flush();
            byte[] resultData = out.toByteArray();
            assertArrayEquals(new byte[0], resultData);

            InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(resultData), coder, new byte[0]);
            byte[] testData = new byte[8];
            int read = 0;
            while (read < testData.length) {
                read += decoder.read(testData, read, testData.length - read);
            }
            assertArrayEquals(inputData, testData);
        }
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[] { 0x3f, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a,
                0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a,
                0x5b, 0x5c, 0x5d, 0x5e, 0x5f })) {
            Coder coder = new DeflateDecoder();
            InputStream decoder = Coders.addDecoder(is, coder, new byte[0]);
            assertSame(is, decoder);

            byte[] resultData = new byte[61];
            int read = 0;
            while (read < resultData.length) {
                read += decoder.read(resultData, read, resultData.length - read);
            }
            assertArrayEquals(new byte[] { 0x3f, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x03, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c,
                    0x4d, 0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x5b, 0x5c,
                    0x5d, 0x5e, 0x5f }, resultData);
        }

        try (OutputStream out = new ByteArrayOutputStream()) {
            byte[] inputData = new byte[] { 0x3f, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x03, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c,
                    0x4d, 0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x5b, 0x5c,
                    0x5d, 0x5e, 0x5f };
            Coder coder = new DeflateDecoder();
            OutputStream encoder = Coders.addEncoder(out, SevenZMethod.DEFLATE, new byte[0]);
            encoder.write(inputData);
            encoder.flush();
            byte[] resultData = out.toByteArray();
            assertArrayEquals(new byte[] { 0x3f, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x3f }, resultData);

            InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(resultData), coder, new byte[0]);
            byte[] testData = new byte[61];
            int read = 0;
            while (read < testData.length) {
                read += decoder.read(testData, read, testData.length - read);
            }
            assertArrayEquals(inputData, testData);
        }
    }

    @Test
    public void testAES256SHA256Decoder() throws NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "password".getBytes();
        byte[] salt = "salt".getBytes();
        byte[] iv = new byte[16];

        try (OutputStream out = new ByteArrayOutputStream()) {
            byte[] plainText = "The quick brown fox jumps over the lazy dog".getBytes();
            Coder coder = new AES256SHA256Decoder();
            OutputStream encoder = Coders.addEncoder(out, SevenZMethod.AES256SHA256, password);

            encoder.write(salt);
            encoder.write(iv);
            encoder.write(plainText);
            encoder.flush();
            byte[] encryptedData = out.toByteArray();

            InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(encryptedData), coder, password);
            byte[] decryptedData = new byte[plainText.length];
            int read = 0;
            while (read < decryptedData.length) {
                read += decoder.read(decryptedData, read, decryptedData.length - read);
            }
            assertArrayEquals(plainText, decryptedData);
        }
    }
}