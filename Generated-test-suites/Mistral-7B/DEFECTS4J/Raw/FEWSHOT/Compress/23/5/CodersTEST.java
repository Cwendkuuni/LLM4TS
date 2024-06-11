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
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMA2OutputStream;
import org.tukaani.xz.LZMAUtils;

import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        
        // Test with null input and coder
        assertThrows(IOException.class, () -> Coders.addDecoder(null, null, null));
        
        // Test with null password
        InputStream is = new ByteArrayInputStream(testData);
        Coder copyCoder = new CopyDecoder();
        InputStream expectedResult = is;
        InputStream result = Coders.addDecoder(is, copyCoder, null);
        assertEquals(expectedResult, result);
        
        // Test with valid password
        SecretKey secretKey = generateSecretKey();
        Coder aes256SHA256Coder = new AES256SHA256Decoder();
        InputStream expectedAesResult = new CipherInputStream(new ByteArrayInputStream(testData), aes256SHA256Coder.coder);
        InputStream resultAes = Coders.addDecoder(is, aes256SHA256Coder, secretKey.getEncoded());
        assertEquals(expectedAesResult, resultAes);
        
        // Test with unsupported compression method
        Coder unsupportedCoder = new UnsupportedCoder();
        assertThrows(IOException.class, () -> Coders.addDecoder(is, unsupportedCoder, null));
    }

    @Test
    public void testAddEncoder() {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        
        // Test with null output and coder
        assertThrows(IOException.class, () -> Coders.addEncoder(null, null, null));

        // Test with Copy encoder and no password
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod copyMethod = SevenZMethod.COPY;
        Coder copyCoder = new CopyDecoder();
        OutputStream expectedResult = out;
        OutputStream result = Coders.addEncoder(out, copyMethod, null);
        assertEquals(expectedResult, result);

        // Test with LZMA encoder and password
        byte[] lzmaPassword = new byte[] {0x01, 0x02, 0x03};
        SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        Coder lzmaCoder = new LZMADecoder();
        OutputStream expectedLzmaResult = new LZMAOutputStream(new ByteArrayOutputStream(), 1);
        OutputStream resultLzma = Coders.addEncoder(out, lzmaMethod, lzmaPassword);
        assertEquals(expectedLzmaResult, resultLzma);

        // Test with DEFLATE encoder and no password
        SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
        Coder deflateCoder = new DeflateDecoder();
        OutputStream expectedDeflateResult = new DeflaterOutputStream(new ByteArrayOutputStream(), new Deflater(1));
        OutputStream resultDeflate = Coders.addEncoder(out, deflateMethod, null);
        assertEquals(expectedDeflateResult, resultDeflate);

        // Test with BZIP2 encoder and no password
        SevenZMethod bzip2Method = SevenZMethod.BZIP2;
        Coder bzip2Coder = new BZIP2Decoder();
        OutputStream expectedBzip2Result = new BZip2CompressorOutputStream(new ByteArrayOutputStream(), 9);
        OutputStream resultBzip2 = Coders.addEncoder(out, bzip2Method, null);
        assertEquals(expectedBzip2Result, resultBzip2);

        // Test with AES256SHA256 encoder and password
        KeyGenerator aesKeyGen = null;
        try {
            aesKeyGen = KeyGenerator.getInstance("AES");
            aesKeyGen.init(256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] aesKeyBytes = aesKeyGen.generateKey().getEncoded();
        SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
        Coder aes256SHA256Coder = new AES256SHA256Decoder();
        OutputStream expectedAes256SHA256Result = new CipherOutputStream(
                new ByteArrayOutputStream(),
                Cipher.getInstance("AES/CBC/PKCS5Padding"),
                new IvParameterSpec(new byte[16]),
                aesKeyBytes);
        OutputStream resultAes256SHA256 = Coders.addEncoder(out, aes256SHA256Method, aesKeyBytes);
        assertEquals(expectedAes256SHA256Result, resultAes256SHA256);

        // Test with unsupported compression method
        Coder unsupportedCoder = new UnsupportedCoder();
        assertThrows(IOException.class, () -> Coders.addEncoder(out, unsupportedCoder, null));
    }

    @Test
    public void testCoderTable() {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        
        SevenZMethod[] methods = SevenZMethod.values();
        for (SevenZMethod method : methods) {
            CoderId coderId = null;
            for (CoderId id : Coders.coderTable) {
                if (id.method.equals(method)) {
                    coderId = id;
                    break;
                }
            }
            assertNotNull(coderId);
            assertEquals(method, coderId.method);
            assertEquals(method.getClass().isInstance(coderId.coder), true);
        }
    }

    @Test
    public void testCopyDecoder() {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        
        InputStream in = new ByteArrayInputStream(testData);
        Coder copyCoder = new CopyDecoder();
        Coder expectedCopyCoder = new CopyDecoder();
        InputStream expectedResult = in;
        InputStream result = Coders.addDecoder(in, copyCoder, null);
        assertEquals(expectedResult, result);
        assertEquals(expectedCopyCoder, copyCoder);
    }

    @Test
    public void testLzmaDecoder() throws IOException {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        byte[] props = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long dictSize = 1234567890L;

        byte[] testLzmaData = LZMAUtils.compress(testData, props);

        InputStream in = new ByteArrayInputStream(testLzmaData);
        Coder lzmaCoder = new LZMADecoder();
        Coder expectedLzmaCoder = new LZMADecoder();
        InputStream expectedResult = new LZMAInputStream(new ByteArrayInputStream(testData), -1, 0, (int) dictSize);
        InputStream result = Coders.addDecoder(in, lzmaCoder, null);
        assertEquals(expectedResult, result);
        assertEquals(expectedLzmaCoder, lzmaCoder);
    }

    @Test
    public void testLzma2Decoder() throws IOException {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        byte[] props = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long dictSize = 1234567890L;
        long dictionarySize2 = 9876543210L;

        byte[] testLzma2Data = LZMA2Utils.compress(testData, props, dictionarySize2);

        InputStream in = new ByteArrayInputStream(testLzma2Data);
        Coder lzma2Coder = new LZMA2Decoder();
        Coder expectedLzma2Coder = new LZMA2Decoder();
        InputStream expectedResult = new LZMA2InputStream(new ByteArrayInputStream(testData), props, dictionarySize2);
        InputStream result = Coders.addDecoder(in, lzma2Coder, null);
        assertEquals(expectedResult, result);
        assertEquals(expectedLzma2Coder, lzma2Coder);
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        InputStream in = new ByteArrayInputStream(testData);
        Coder deflateCoder = new DeflateDecoder();
        Coder expectedDeflateCoder = new DeflateDecoder();
        InputStream expectedResult = new InflaterInputStream(new DummyByteAddingInputStream(new ByteArrayInputStream(testData.substring(1))), new Inflater(true));
        InputStream result = Coders.addDecoder(in, deflateCoder, null);
        assertEquals(expectedResult, result);
        assertEquals(expectedDeflateCoder, deflateCoder);
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);

        InputStream in = new ByteArrayInputStream(testData);
        Coder bzip2Coder = new BZIP2Decoder();
        Coder expectedBzip2Coder = new BZIP2Decoder();
        InputStream expectedResult = new BZip2CompressorInputStream(new ByteArrayInputStream(testData));
        InputStream result = Coders.addDecoder(in, bzip2Coder, null);
        assertEquals(expectedResult, result);
        assertEquals(expectedBzip2Coder, bzip2Coder);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        byte[] aesKeyBytes = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        byte[] iv = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

        byte[] encryptedData = encryptData(testData, aesKeyBytes, iv);

        InputStream in = new ByteArrayInputStream(encryptedData);
        Coder aes256SHA256Coder = new AES256SHA256Decoder();
        Coder expectedAes256SHA256Coder = new AES256SHA256Decoder();
        InputStream expectedResult = new CipherInputStream(new ByteArrayInputStream(testData),
                Cipher.getInstance("AES/CBC/PKCS5Padding"),
                new IvParameterSpec(iv), aesKeyBytes);
        InputStream result = Coders.addDecoder(in, aes256SHA256Coder, aesKeyBytes);
        assertEquals(expectedResult, result);
        assertEquals(expectedAes256SHA256Coder, aes256SHA256Coder);
    }

    private byte[] encryptData(byte[] data, byte[] key, byte[] iv) throws NoSuchAlgorithmException, GeneralSecurityException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    private static class UnsupportedCoder extends CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            throw new UnsupportedOperationException("UnsupportedCoder.decode method is not supported.");
        }

        @Override
        OutputStream encode(final OutputStream out, final byte[] password) throws IOException {
            throw new UnsupportedOperationException("UnsupportedCoder.encode method is not supported.");
        }
    }
}