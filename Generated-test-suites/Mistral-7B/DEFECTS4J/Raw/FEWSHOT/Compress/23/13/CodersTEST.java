package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
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
import java.util.Arrays;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testCoderIdEquals() {
        SevenZMethod method1 = SevenZMethod.BZIP2;
        SevenZMethod method2 = SevenZMethod.BZIP2;
        CoderId coderId1 = new CoderId(method1, new BZIP2Decoder());
        CoderId coderId2 = new CoderId(method2, new BZIP2Decoder());
        assertTrue(coderId1.equals(coderId2));
    }

    @Test
    public void testCoderIdHashCode() {
        SevenZMethod method1 = SevenZMethod.BZIP2;
        SevenZMethod method2 = SevenZMethod.DEFLATE;
        CoderId coderId1 = new CoderId(method1, new BZIP2Decoder());
        CoderId coderId2 = new CoderId(method2, new DeflateDecoder());
        assertFalse(coderId1.hashCode() == coderId2.hashCode());
    }

    @Test
    public void testCoderIdToString() {
        SevenZMethod method = SevenZMethod.BZIP2;
        CoderId coderId = new CoderId(method, new BZIP2Decoder());
        assertEquals("BZIP2Decoder", coderId.toString());
    }

    @Test
    public void testAddDecoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        Coder coder = new DeflateDecoder();
        SevenZMethod method = SevenZMethod.DEFLATE;
        OutputStream out = Coders.addEncoder(outputStream, method, password);
        InputStream is = new ByteArrayInputStream(new byte[] {});
        InputStream decoded = Coders.addDecoder(is, coder, password);
        byte[] actual = new byte[1024];
        int read = decoded.read(actual, 0, actual.length);
        assertFalse(read > 0);
        assertSame(is, decoded);
        assertEquals(outputStream.size(), Coders.addEncoder(outputStream, method, password).size());
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        Coder coder = new AES256SHA256Decoder();
        SevenZMethod method = SevenZMethod.DEFLATE;
        Coders.addDecoder(new ByteArrayInputStream(new byte[]{}), coder, password);
    }

    @Test
    public void testDecodeLZMA() throws IOException {
        byte[] sourceData = {0x04, 0x22, 0x00, 0x00, 0x00, 0x00, 0x28, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x00, 0x00, 0x02, 0x00, 0x01, 0x00, 0x01};
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        byte[] expected = {0x00, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.LZMA, password);
        outputStream.close();
        inputStream.close();
        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new LZMADecoder(), password);
        byte[] actual = new byte[expected.length];
        int read = decoder.read(actual, 0, actual.length);
        assertFalse(read < expected.length);
        assertArrayEquals(expected, actual);
        decoder.close();
    }

    @Test
    public void testDecodeAES256SHA256() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = new byte[0];
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        byte[] expected = new byte[16];
        Arrays.fill(expected, (byte) 0x67);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputData);
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputData);
        byte[] digest = md.digest();
        SecretKey aesKey = new SecretKeySpec(digest, "AES");
        IvParameterSpec iv = IvParameterSpec.zeroIv(16);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
        byte[] encrypted = cipher.doFinal(expected);
        encoder.write(encrypted);
        encoder.close();
        inputStream.close();
        outputStream.close();
        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new AES256SHA256Decoder(), password);
        byte[] actual = new byte[expected.length];
        int read = decoder.read(actual, 0, actual.length);
        assertFalse(read < expected.length);
        assertArrayEquals(expected, actual);
        decoder.close();
    }

    @Test
    public void testDecodeBZip2() throws IOException {
        byte[] sourceData = {0x42, 0x5A, 0x68, 0x64, 0x6D, 0x6F, 0x00, 0x3B, 0x50, 0x32, 0x00, 0x00, 0x14, 0x14, 0x00, 0x05, 0x42, 0x5A, 0x43, 0x42, 0x50, 0x52, 0x4E, 0x51, 0x56, 0x45, 0x44, 0x49, 0x54, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x28, 0x00, 0x00, 0x00, 0x02, 0x1C};
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        byte[] expected = {0x00, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.BZIP2, password);
        outputStream.close();
        inputStream.close();
        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new BZIP2Decoder(), password);
        byte[] actual = new byte[expected.length];
        int read = decoder.read(actual, 0, actual.length);
        assertFalse(read < expected.length);
        assertArrayEquals(expected, actual);
        decoder.close();
    }

    @Test
    public void testDecodeDeflate() throws IOException {
        byte[] sourceData = new byte[1024];
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, password);
        byte[] dataToCompress = "This is a test".getBytes(StandardCharsets.UTF_8);
        encoder.write(dataToCompress);
        encoder.close();
        inputStream.close();
        outputStream.close();
        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new DeflateDecoder(), password);
        byte[] actual = new byte[dataToCompress.length];
        int read = decoder.read(actual, 0, actual.length);
        assertFalse(read < dataToCompress.length);
        assertArrayEquals(dataToCompress, actual);
        decoder.close();
    }

    @Test
    public void testDecodeCopy() throws IOException {
        byte[] sourceData = new byte[1024];
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.COPY, password);
        encoder.write(sourceData);
        encoder.close();
        inputStream.close();
        outputStream.close();
        InputStream decoder = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new CopyDecoder(), password);
        byte[] actual = new byte[sourceData.length];
        int read = decoder.read(actual, 0, actual.length);
        assertFalse(read < sourceData.length);
        assertArrayEquals(sourceData, actual);
        decoder.close();
    }
}