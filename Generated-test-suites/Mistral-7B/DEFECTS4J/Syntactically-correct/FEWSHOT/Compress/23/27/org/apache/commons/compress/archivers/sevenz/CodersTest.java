package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAPropertyParameters;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;
import org.apache.commons.compress.compressors.lzma.LZMADictionary;
import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.LZMA2PropertyParameters;
import org.tukaani.xz.LZMA2Utils;
import org.tukaani.xz.LZMA2Dictionary;

import org.junit.Test;

public class CodersTest {

    private static final byte[] PASSWORD = "password".getBytes(StandardCharsets.UTF_8);
    private static final byte[] IV = "iv".getBytes(StandardCharsets.UTF_8);
    private static final byte[] AES_KEY = "AES_KEY".getBytes(StandardCharsets.UTF_8);

    @Test
    public void testAddDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder copyCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.COPY.getId();
        }};
        final Coder lzmaCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.LZMA.getId();
        }};
        final Coder deflateCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.DEFLATE.getId();
        }};

        final InputStream resultIs = Coders.addDecoder(testIs, copyCoder, null);
        assertEquals(testIs, resultIs);

        resultIs = Coders.addDecoder(testIs, lzmaCoder, null);
        verify(lzmaCoder).init(any(InputStream.class), any(Coder.class), null);
        assertSame(new LZMAInputStream(testIs, -1, (byte) 0, 0), resultIs);

        resultIs = Coders.addDecoder(testIs, deflateCoder, PASSWORD);
        verify(deflateCoder).init(any(InputStream.class), any(Coder.class), PASSWORD);
        assertSame(new InflaterInputStream(new DummyByteAddingInputStream(testIs.wrappedInputStream()), new Inflater(true)), resultIs);
    }

    @Test
    public void testAddDecoderWithPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder aesCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        }};

        final byte[] plainText = "plain text".getBytes(StandardCharsets.UTF_8);
        final byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);

        final byte[] encrypted = Coders.encrypt(plainText, salt, AES_KEY);
        final byte[] decrypted = Coders.decrypt(encrypted, salt, PASSWORD);
        final byte[] expected = new byte[] {0x1, 0x2, 0x3, 0x4};

        final InputStream resultIs = Coders.addDecoder(new ByteArrayInputStream(encrypted), aesCoder, PASSWORD);
        final byte[] actual = IOUtils.toByteArray(resultIs);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testAddDecoderWithUnsupportedCompressionMethod() throws IOException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder unsupportedCoder = new Coder() {{
            decompressionMethodId = new byte[] {0x1, 0x2, 0x3, 0x4}; // unsupported method ID
        }};
        Coders.addDecoder(testIs, unsupportedCoder, null);
    }

    @Test
    public void testAddEncoder() throws IOException {
        final OutputStream testOs = new ByteArrayOutputStream();
        final SevenZMethod copyMethod = SevenZMethod.COPY;
        final SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        final SevenZMethod deflateMethod = SevenZMethod.DEFLATE;

        final Coder copyCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.COPY.getId();
        }};
        final Coder lzmaCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.LZMA.getId();
        }};
        final Coder deflateCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.DEFLATE.getId();
        }};

        final OutputStream resultOs = Coders.addEncoder(testOs, copyMethod, null);
        assertEquals(testOs, resultOs);

        resultOs = Coders.addEncoder(testOs, lzmaMethod, null);
        verify(lzmaCoder).init(any(OutputStream.class), any(byte[].class), null);
        assertSame(new LZMAOutputStream(testOs, new LZMADictionary(), null), resultOs);

        resultOs = Coders.addEncoder(testOs, deflateMethod, PASSWORD);
        verify(deflateCoder).init(any(OutputStream.class), any(byte[].class), PASSWORD);
        assertSame(new DeflaterOutputStream(testOs, new Deflater(9, true)), resultOs);
    }

    @Test
    public void testAes256Sha256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder aesCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.AES256SHA256.getId();
        }};

        final byte[] plainText = "plain text".getBytes(StandardCharsets.UTF_8);
        final byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);
        final byte[] iv = "iv".getBytes(StandardCharsets.UTF_8);

        final byte[] encrypted = Coders.encrypt(plainText, salt, AES_KEY);

        final InputStream resultIs = Coders.addDecoder(new ByteArrayInputStream(encrypted), aesCoder, PASSWORD);
        final byte[] actual = IOUtils.toByteArray(resultIs);
        assertArrayEquals(plainText, actual);
    }

    @Test
    public void testBzip2Decoder() throws IOException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder bzip2Coder = new Coder() {{
            decompressionMethodId = SevenZMethod.BZIP2.getId();
        }};

        final OutputStream testOs = new ByteArrayOutputStream();
        Coders.addEncoder(testOs, SevenZMethod.BZIP2, null);
        final byte[] expected = new byte[] {0x1, 0x2, 0x3};
        IOUtils.write(expected, testOs);
        testOs.close();

        final InputStream resultIs = Coders.addDecoder(testIs, bzip2Coder, null);
        final byte[] actual = IOUtils.toByteArray(resultIs);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder deflateCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.DEFLATE.getId();
        }};

        final OutputStream testOs = new ByteArrayOutputStream();
        Coders.addEncoder(testOs, SevenZMethod.DEFLATE, null);
        IOUtils.write(new byte[] {0x1, 0x2, 0x3}, testOs);
        testOs.close();

        final InputStream resultIs = Coders.addDecoder(testIs, deflateCoder, null);
        final byte[] actual = IOUtils.toByteArray(resultIs);
        assertArrayEquals(new byte[] {0x1, 0x2, 0x3}, actual);
    }

    @Test
    public void testLzmaDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream testIs = new ByteArrayInputStream(new byte[] {0x1, 0x2, 0x3});
        final Coder lzmaCoder = new Coder() {{
            decompressionMethodId = SevenZMethod.LZMA.getId();
        }};
        final LZMAPropertyParameters propParams = mock(LZMAPropertyParameters.class);
        final LZMA2PropertyParameters lzma2PropParams = mock(LZMA2PropertyParameters.class);

        final LZMA2Dictionary lzma2Dict = mock(LZMA2Dictionary.class);
        final LZMADictionary lzmaDict = mock(LZMADictionary.class);
        final byte[] testDict = new byte[] {0x1, 0x2, 0x3};

        when(lzmaCoder.properties).thenReturn(new byte[][] {propParams.toByteArray(), testDict});
        when(lzma2PropParams.getDictionarySize()).thenReturn(testDict.length);
        when(propParams.getDictionarySize()).thenReturn(-1);

        when(lzmaCoder.init(any(InputStream.class), any(Coder.class), any(byte[][].class))).thenReturn(new LZMAInputStream(testIs, -1, (byte) 0, testDict.length));
        when(lzmaCoder.init(any(OutputStream.class), any(byte[][].class))).thenReturn(new LZMAOutputStream(any(OutputStream.class), lzma2PropParams, lzmaDict));

        final InputStream resultIs = Coders.addDecoder(testIs, lzmaCoder, null);
        final byte[] actual = IOUtils.toByteArray(resultIs);
        assertArrayEquals(new byte[] {0x1, 0x2, 0x3}, actual);
    }

    private static byte[] encrypt(byte[] plainText, byte[] salt, byte[] key)
            throws NoSuchAlgorithmException, IOException, GeneralSecurityException {
        final SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final byte[] iv = new byte[16];
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));

        final byte[] encrypted = cipher.doFinal(plainText);
        final byte[] saltAndIV = new byte[salt.length + iv.length];
        System.arraycopy(salt, 0, saltAndIV, 0, salt.length);
        System.arraycopy(iv, 0, saltAndIV, salt.length, iv.length);

        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(saltAndIV);
        sha256.update(key);
        final byte[] hash = sha256.digest();
        final byte[] encryptedWithHash = new byte[encrypted.length + hash.length];
        System.arraycopy(encrypted, 0, encryptedWithHash, 0, encrypted.length);
        System.arraycopy(hash, 0, encryptedWithHash, encrypted.length, hash.length);

        return encryptedWithHash;
    }

    private static byte[] decrypt(byte[] encrypted, byte[] salt, byte[] key)
            throws NoSuchAlgorithmException, IOException, GeneralSecurityException {
        final SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final byte[] iv = new byte[16];

        final byte[] encryptedWithHash = new byte[encrypted.length + 32];
        System.arraycopy(encrypted, 0, encryptedWithHash, 0, encrypted.length);
        System.arraycopy(SevenZMethod.AES256SHA256.getId(), 0, encryptedWithHash, encrypted.length, 32);

        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(salt);
        sha256.update(key);
        final byte[] hash = sha256.digest();
        final byte[] saltAndHash = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hash, 0, saltAndHash, salt.length, hash.length);

        final byte[] saltAndIV = new byte[salt.length + iv.length];
        System.arraycopy(saltAndHash, 0, saltAndIV, 0, salt.length + iv.length - hash.length);

        System.arraycopy(saltAndIV, 0, iv, 0, iv.length);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));

        final byte[] decrypted = cipher.doFinal(Arrays.copyOfRange(encryptedWithHash, 0, encrypted.length));
        return decrypted;
    }
}