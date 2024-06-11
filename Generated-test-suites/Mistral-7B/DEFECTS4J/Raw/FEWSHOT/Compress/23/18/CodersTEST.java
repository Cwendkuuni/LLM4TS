package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.SecretKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, password);
             InputStream is = new ByteArrayInputStream(testData)) {
            os.write(testData, 0, testData.length);
        }
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Coder coder = new DeflateDecoder();
        assertNull(Coders.addDecoder(null, coder, password));
        assertNull(Coders.addDecoder(testInputStream, null, password));
        InputStream resultInputStream = Coders.addDecoder(testInputStream, coder, password);
        assertEquals(testData, resultInputStream.readAllBytes());
    }

    @Test
    public void testAddEncoder() {
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream is = inputStream;
             OutputStream os = Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, password)) {
            os.write(testData, 0, testData.length);
        }
        byte[] encodedData = outputStream.toByteArray();
        byte[] decodedData = new byte[encodedData.length + 1];
        try (InputStream is2 = new ByteArrayInputStream(encodedData);
             OutputStream os2 = new ByteArrayOutputStream()) {
            Coders.addDecoder(is2, new DeflateDecoder(), password).transferTo(os2);
            decodedData = os2.toByteArray();
        }
        assertArrayEquals(testData, decodedData);
    }

    @Test
    public void testLZMADecoder() throws NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);
             InputStream is = new ByteArrayInputStream(testData)) {
            os.write(testData, 0, testData.length);
        }
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Coder coder = new LZMADecoder();
        assertNull(Coders.addDecoder(null, coder, password));
        assertNull(Coders.addDecoder(testInputStream, null, password));
        InputStream resultInputStream = Coders.addDecoder(testInputStream, coder, password);
        assertEquals(testData, resultInputStream.readAllBytes());
    }

    @Test
    public void testAES256SHA256Decoder()
            throws NoSuchAlgorithmException, GeneralSecurityException, IOException {
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt12345".getBytes(StandardCharsets.UTF_8);
        byte[] iv = new byte[16];
        for (int i = 0; i < iv.length; i++) {
            iv[i] = (byte) (i & 0xff);
        }
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
             InputStream is = new ByteArrayInputStream(testData)) {
            byte[] aesKeyBytes = new byte[32];
            System.arraycopy(salt, 0, aesKeyBytes, 0, salt.length);
            System.arraycopy(password, 0, aesKeyBytes, salt.length, password.length);
            SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));
            is = new CipherInputStream(is, cipher);
            os.write(testData, 0, testData.length);
        }
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Coder coder = new AES256SHA256Decoder();
        assertNull(Coders.addDecoder(null, coder, password));
        assertNull(Coders.addDecoder(testInputStream, null, password));
        InputStream resultInputStream = Coders.addDecoder(testInputStream, coder, password);
        assertEquals(testData, resultInputStream.readAllBytes());
    }

    @Test
    public void testUnsupportedDecoder() {
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);
             InputStream is = new ByteArrayInputStream(testData)) {
            os.write(testData, 0, testData.length);
        }
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Coder coder = new DeflateDecoder();
        assertThrows(IOException.class, () -> Coders.addDecoder(testInputStream, coder, password));
    }

    @Test
    public void testUnsupportedEncoder() {
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
             InputStream is = new ByteArrayInputStream(testData)) {
            os.write(testData, 0, testData.length);
        }
        Coder coder = new DeflateDecoder();
        assertThrows(IOException.class, () -> Coders.addEncoder(new ByteArrayOutputStream(), coder, password));
    }
}