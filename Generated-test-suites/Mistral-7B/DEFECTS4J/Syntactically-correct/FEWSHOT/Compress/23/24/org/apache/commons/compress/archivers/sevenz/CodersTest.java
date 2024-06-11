package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.sevenz.LZMAInputStream;
import org.apache.commons.compress.compressors.sevenz.LZMAOutputStream;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Test;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.LZMA2OutputStream;

import org.apache.commons.compress.archivers.sevenz.Coders.AES256SHA256Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.BZIP2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.CopyDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateDecoder;
import org.apache.commons.compress.archivers.sevenz.Coders.DeflateEncoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMA2Decoder;
import org.apache.commons.compress.archivers.sevenz.Coders.LZMA2Encoder;

public class CodersTest {

    @Test
    public void testAddDecoderCopyDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        Coder copyDecoder = new CopyDecoder();
        byte[] password = new byte[0];
        InputStream decoder = Coders.addDecoder(inputStream, copyDecoder, password);
        assertSame(inputStream, decoder);
    }

    @Test
    public void testAddDecoderLZMA2Decoder() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -78, 76, 75, 1, 2, -3, 4, 5, 6, 7, 8, 9, -1});
        LZMA2Decoder lzma2Decoder = new LZMA2Decoder();
        byte[] password = new byte[0];

        InputStream decoder = Coders.addDecoder(inputStream, lzma2Decoder, password);
        LZMAInputStream expectedDecoder = new LZMAInputStream(inputStream);
        assertSame(expectedDecoder, decoder);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderUnsupportedDecoder() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1});
        byte[] password = new byte[0];
        Coder unsupportedDecoder = mock(DeflateDecoder.class);
        Coders.addDecoder(inputStream, unsupportedDecoder, password);
    }

    @Test
    public void testAddEncoderDeflateEncoder() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
        byte[] password = new byte[0];
        OutputStream encoder = Coders.addEncoder(outputStream, deflateMethod, password);
        DeflaterOutputStream expectedEncoder = new DeflaterOutputStream(outputStream, new Deflater(9, true));
        assertSame(expectedEncoder, encoder);
    }

    @Test
    public void testGetDecoderForGivenMethod() {
        for (SevenZMethod method : SevenZMethod.values()) {
            for (CoderId coderId : Coders.coderTable) {
                if (coderId.method.equals(method)) {
                    assertNotNull(coderId.coder);
                    break;
                }
            }
            assertNull(findDecoderForMethod(method));
        }
    }

    @Test(expected = IOException.class)
    public void testDecoderForUnsupportedMethod() throws IOException {
        SevenZMethod unsupportedMethod = new SevenZMethod("UnknownMethod");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] password = new byte[0];
        Coders.addDecoder(inputStream, unsupportedMethod, password);
    }

    @Test
    public void testDecoderPasswordDependent() throws IOException, NoSuchAlgorithmException {
        SevenZMethod aes256SHA256 = SevenZMethod.AES256SHA256;
        byte[] password = new byte[16];
        SecretKey secretKey = generateSecretKey(password);
        Coder aes256SHA256Decoder = new AES256SHA256Decoder();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputStream decoder = Coders.addDecoder(inputStream, aes256SHA256, password);

        assertEquals(AES256SHA256Decoder.class, decoder.getClass());

        InputStream decoderWithoutPassword = Coders.addDecoder(inputStream, aes256SHA256, null);
        assertNotSame(decoderWithoutPassword, decoder);
    }

    private Coder findDecoderForMethod(SevenZMethod method) {
        for (CoderId coderId : Coders.coderTable) {
            if (coderId.method.equals(method)) {
                return coderId.coder;
            }
        }
        return null;
    }

    @Test
    public void testEncodeBZIP2() throws IOException {
        byte[] input = new byte[]{-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1};
        byte[] expectedOutput = new byte[]{0x42, 0x5a, 0x68, 0x64, 0x67, 0x73, 0x61, 0x20, 0x42, 0x53, 0x41, 0x54, 0x45, 0x53, 0x54, 0x2d, 0x1a};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod bzip2Method = SevenZMethod.BZIP2;
        byte[] password = new byte[0];
        OutputStream encoder = Coders.addEncoder(outputStream, bzip2Method, password);
        InputStream inputStream = new ByteArrayInputStream(input);
        int length;
        while ((length = inputStream.read()) >= 0) {
            encoder.write(length > 0 ? (byte) length : -length);
        }
        encoder.flush();
        assertArrayEquals(expectedOutput, outputStream.toByteArray());

        BZip2CompressorOutputStream bz2OutputStream = new BZip2CompressorOutputStream(outputStream);
        bz2OutputStream.write(input, 0, input.length);
        bz2OutputStream.finish();
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeAES256SHA256() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] input = new byte[]{-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1};
        byte[] password = new byte[16];
        byte[] salt = new byte[]{1, 2, 3};
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(password);
        random.nextBytes(salt);
        random.nextBytes(iv);

        byte[] expectedOutput = new byte[input.length + iv.length + salt.length];
        System.arraycopy(salt, 0, expectedOutput, 0, salt.length);
        System.arraycopy(iv, 0, expectedOutput, salt.length, iv.length);
        System.arraycopy(input, 0, expectedOutput, iv.length, input.length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod aes256SHA256 = SevenZMethod.AES256SHA256;
        OutputStream encoder = Coders.addEncoder(outputStream, aes256SHA256, password);
        InputStream inputStream = new ByteArrayInputStream(input);
        int length;
        while ((length = inputStream.read()) >= 0) {
            encoder.write(length > 0 ? (byte) length : -length);
        }
        encoder.write(salt, 0, salt.length);
        encoder.write(iv, 0, iv.length);
        encoder.flush();
        assertArrayEquals(expectedOutput, outputStream.toByteArray());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        CipherOutputStream encoderOutput = new CipherOutputStream(outputStream, cipher, ivSpec);
        encoderOutput.write(input, 0, input.length);
        encoderOutput.close();
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeLZMA2() throws IOException {
        byte[] input = new byte[]{-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1};
        LZMA2Options options = new LZMA2Options();
        options.setDictionarySize(2097152);
        options.setOutputCompressionLevel(9);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod lzma2Method = SevenZMethod.LZMA2;
        byte[] password = new byte[0];
        OutputStream encoder = Coders.addEncoder(outputStream, lzma2Method, password);
        InputStream inputStream = new ByteArrayInputStream(input);
        int length;
        while ((length = inputStream.read()) >= 0) {
            encoder.write(length > 0 ? (byte) length : -length);
        }
        encoder.flush();
        LZMAOutputStream lzmaOutputStream = new LZMAOutputStream(outputStream, 9, options);
        lzmaOutputStream.write(input, 0, input.length);
        lzmaOutputStream.finish();
        assertArrayEquals(outputStream.toByteArray(), encoder.toByteArray());
    }

    private SecretKey generateSecretKey(byte[] password) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] secretKeyBytes = secretKey.getEncoded();
        KeySpec keySpec = new SecretKeySpec(password, "AES");
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("AES");
        SecretKey expectedKey = secretKeyFactory.generateSecret(keySpec);
        assertArrayEquals(secretKeyBytes, expectedKey.getEncoded());
        return secretKey;
    }
}