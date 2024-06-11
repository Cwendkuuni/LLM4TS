package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.IvParameterSpec;
import java.security.spec.SecretKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.compress.archivers.sevenz.Coders.CoderBase;
import org.apache.commons.compress.archivers.sevenz.Coders.CoderId;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMADecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMA2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.BZIP2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;
import org.tukaani.xz.LZMAOutputStream;
import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() {
        final CoderId copyDecoderId = new CoderId(SevenZMethod.COPY, new CopyDecoder());
        final InputStream is = mock(InputStream.class);
        when(is.readAllBytes()).thenReturn(new byte[0]);

        final byte[] password = "password".getBytes();
        final Coder copyCoder = new CopyDecoder();
        final InputStream result = Coders.addDecoder(is, copyCoder, password);
        assertEquals(is, result);

        final CoderId lzmaDecoderId = new CoderId(SevenZMethod.LZMA, new LZMADecoder());
        final byte[] lzmaTestData = LZMAUtils.compressToByteArray(new byte[0]);
        final InputStream lzmaInputStream = new ByteArrayInputStream(lzmaTestData);
        when(lzmaInputStream.readAllBytes()).thenReturn(lzmaTestData);

        final Coder lzmaCoder = new LZMADecoder();
        final InputStream resultLzma = Coders.addDecoder(lzmaInputStream, lzmaCoder, password);
        assertArrayEquals(new byte[0], resultLzma.readAllBytes());
    }

    @Test
    public void testAddDecoderWithUnsupportedCompressionMethod() {
        final InputStream is = mock(InputStream.class);
        when(is.readAllBytes()).thenReturn(new byte[0]);

        final Coder unsupportedCoder = new Object() {}.getClass();
        assertThrows(IOException.class, () -> {
            Coders.addDecoder(is, unsupportedCoder, new byte[0][0]);
        });
    }

    @Test
    public void testAddEncoder() {
        final OutputStream out = mock(OutputStream.class);

        final SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        final Coder lzmaCoder = new LZMADecoder();
        final OutputStream result = Coders.addEncoder(out, lzmaMethod, new byte[0][0]);
        assertEquals(out, result);

        final SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
        final Coder deflateCoder = new DeflateDecoder();
        assertThrows(UnsupportedOperationException.class, () -> {
            Coders.addEncoder(out, deflateMethod, new byte[0][0]);
        });
    }

    @Test
    public void testAddEncoderWithUnsupportedCompressionMethod() {
        final OutputStream out = mock(OutputStream.class);

        final SevenZMethod unsupportedMethod = new Object() {}.getClass();
        assertThrows(IOException.class, () -> {
            Coders.addEncoder(out, unsupportedMethod, new byte[0][0]);
        });
    }

    @Test
    public void testAes256Sha256Decoder() throws NoSuchAlgorithmException {
        final byte[] salt = "salt".getBytes();
        final byte[] iv = "iv".getBytes();
        final byte[] password = "password".getBytes();

        // Generate key using a dummy algorithm
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("DummyAlgorithm");
        keyGenerator.init(128);
        final SecretKey key = keyGenerator.generateKey();

        final byte[] encryptedData = encryptData(salt, iv, password, key);
        final byte[] decryptedData = decryptData(encryptedData, salt, iv, password);

        final CoderId aes256Sha256DecoderId = new CoderId(SevenZMethod.AES256SHA256, new AES256SHA256Decoder());
        final InputStream inputStream = new ByteArrayInputStream(encryptedData);
        final byte[] result = testDecoder(inputStream, aes256Sha256DecoderId, password);
        assertArrayEquals(decryptedData, result);

        testDecoder(new ByteArrayInputStream(new byte[0]), aes256Sha256DecoderId, password);
        testDecoder(new ByteArrayInputStream(new byte[0]), aes256Sha256DecoderId, null);
        testDecoder(null, aes256Sha256DecoderId, password);
        testDecoder(null, aes256Sha256DecoderId, null);
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        final InputStream deflateInputStream = new ByteArrayInputStream(new byte[0]);
        final byte[] result = testDecoder(deflateInputStream, new CoderId(SevenZMethod.DEFLATE, new DeflateDecoder()), null);
        assertArrayEquals(new byte[0], result);

        deflateInputStream.mark(1);
        final byte[] buffer = new byte[1];
        deflateInputStream.read(buffer);
        deflateInputStream.reset();
        deflateInputStream.read(buffer);
        assertEquals(0, buffer[0]);
    }

    @Test
    public void testBzip2Decoder() throws IOException {
        final byte[] inputData = new byte[]{-77, -88, -89, 77, 88, 89};
        final InputStream bzip2InputStream = new BZip2CompressorInputStream(new ByteArrayInputStream(inputData));
        final byte[] result = testDecoder(bzip2InputStream, new CoderId(SevenZMethod.BZIP2, new BZIP2Decoder()), null);
        assertArrayEquals(inputData, result);

        final InputStream bzip2InputStreamWithPassword = new BZip2CompressorInputStream(new ByteArrayInputStream(inputData), "password".getBytes());
        final byte[] resultWithPassword = testDecoder(bzip2InputStreamWithPassword, new CoderId(SevenZMethod.BZIP2, new BZIP2Decoder()), "password".getBytes());
        assertArrayEquals(inputData, resultWithPassword);
    }

    @Test
    public void testLzmaDecoder() throws IOException {
        final byte[] inputData = new byte[]{-1, -1, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        final InputStream lzmaInputStream = new LZMAInputStream(new ByteArrayInputStream(inputData));
        final byte[] result = testDecoder(lzmaInputStream, new CoderId(SevenZMethod.LZMA, new LZMADecoder()), null);
        assertArrayEquals(inputData, result);

        final InputStream lzmaInputStreamWithPassword = new LZMAInputStream(new ByteArrayInputStream(inputData), new byte[0][0]);
        final byte[] resultWithPassword = testDecoder(lzmaInputStreamWithPassword, new CoderId(SevenZMethod.LZMA, new LZMADecoder()), new byte[0][0]);
        assertArrayEquals(inputData, resultWithPassword);
    }

    @Test
    public void testLzma2Decoder() throws IOException {
        final byte[] inputData = new byte[]{-1, -1, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        final InputStream lzma2InputStream = new LZMA2InputStream(new ByteArrayInputStream(inputData));
        final byte[] result = testDecoder(lzma2InputStream, new CoderId(SevenZMethod.LZMA2, new LZMA2Decoder()), null);
        assertArrayEquals(inputData, result);

        final InputStream lzma2InputStreamWithPassword = new LZMA2InputStream(new ByteArrayInputStream(inputData), new byte[0][0]);
        final byte[] resultWithPassword = testDecoder(lzma2InputStreamWithPassword, new CoderId(SevenZMethod.LZMA2, new LZMA2Decoder()), new byte[0][0]);
        assertArrayEquals(inputData, resultWithPassword);
    }

    @Test
    public void testCopyDecoder() throws IOException {
        final InputStream copyInputStream = new ByteArrayInputStream(new byte[0]);
        final byte[] result = testDecoder(copyInputStream, new CoderId(SevenZMethod.COPY, new CopyDecoder()), null);
        assertArrayEquals(new byte[0], result);
    }

    private byte[] testDecoder(final InputStream inputStream, final CoderId coderId, final byte[] password) throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final OutputStream encoderStream = Coders.addEncoder(outputStream, coderId.method, password);
        final OutputStream decoderStream = Coders.addDecoder(encoderStream, coderId.coder, password);
        final byte[] decoderData = new byte[inputStream.available()];
        inputStream.read(decoderData);
        decoderStream.write(decoderData);
        decoderStream.close();
        encoderStream.close();
        outputStream.close();
        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }

    private byte[] encryptData(final byte[] salt, final byte[] iv, final byte[] password, final SecretKey key) throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(salt);
    }

    private byte[] decryptData(final byte[] encryptedData, final byte[] salt, final byte[] iv, final byte[] password) throws GeneralSecurityException {
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final PBEKeySpec pbeKeySpec = new PBEKeySpec("password".toCharArray(), salt, 1000, 256);
        final SecretKey key = keyFactory.generateSecret(pbeKeySpec);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(encryptedData);
    }
}