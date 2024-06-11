package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;
import org.apache.commons.compress.compressors.lzma.LZMADictionary;
import org.junit.Test;

import org.tukaani.xz.LZMAOutputStream;

public class CodersTest {

    private static final byte[] PASSWORD = "password".getBytes();

    @Test
    public void testAddDecoder() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Coder deflateCoder = new Coder(new SevenZMethod(SevenZMethod.DEFLATE), new DeflateDecoder());

        final InputStream expectedOutputStream = new ByteArrayInputStream(new byte[0]);
        final InputStream actualOutputStream = Coders.addDecoder(inputStream, deflateCoder, PASSWORD);
        assertEquals(expectedOutputStream, actualOutputStream);

        final Coder bzip2Coder = new Coder(new SevenZMethod(SevenZMethod.BZIP2), new BZIP2Decoder());
        final InputStream bzip2InputStream = new BZip2CompressorInputStream(new ByteArrayInputStream(new byte[0]));
        final InputStream actualBzip2OutputStream = Coders.addDecoder(bzip2InputStream, bzip2Coder, PASSWORD);
        assertEquals(bzip2InputStream, actualBzip2OutputStream);

        final Coder lzmaCoder = new Coder(new SevenZMethod(SevenZMethod.LZMA), new LZMADecoder());
        final InputStream lzmaInputStream = new LZMAInputStream(new ByteArrayInputStream(new byte[0]));
        final InputStream actualLzmaOutputStream = Coders.addDecoder(lzmaInputStream, lzmaCoder, PASSWORD);
        assertEquals(lzmaInputStream, actualLzmaOutputStream);
    }

    @Test
    public void testAddEncoder() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final SevenZMethod lzmaMethod = new SevenZMethod(SevenZMethod.LZMA);
        final Coder lzmaCoder = new Coder(lzmaMethod, new LZMADecoder());
        final OutputStream expectedOutputStream = new LZMAOutputStream(outputStream, LZMAUtils.LZMA_PROPS_DEFAULT);
        final OutputStream actualOutputStream = Coders.addEncoder(outputStream, lzmaMethod, PASSWORD);
        assertEquals(expectedOutputStream, actualOutputStream);

        final SevenZMethod deflateMethod = new SevenZMethod(SevenZMethod.DEFLATE);
        final Coder deflateCoder = new Coder(deflateMethod, new DeflateDecoder());
        final OutputStream actualDeflateOutputStream = Coders.addEncoder(outputStream, deflateMethod, PASSWORD);
        assertEquals(expectedOutputStream, actualDeflateOutputStream);

        final SevenZMethod bzip2Method = new SevenZMethod(SevenZMethod.BZIP2);
        final Coder bzip2Coder = new Coder(bzip2Method, new BZIP2Decoder());
        final OutputStream actualBzip2OutputStream = Coders.addEncoder(outputStream, bzip2Method, PASSWORD);
        assertEquals(expectedOutputStream, actualBzip2OutputStream);
    }

    @Test
    public void testCopyDecoder() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Coder copyCoder = new Coder(new SevenZMethod(SevenZMethod.COPY), new CopyDecoder());
        final InputStream expectedOutputStream = new ByteArrayInputStream(new byte[0]);
        final InputStream actualOutputStream = Coders.addDecoder(inputStream, copyCoder, PASSWORD);
        assertEquals(expectedOutputStream, actualOutputStream);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Coder unsupportedCoder = new Coder(new SevenZMethod(SevenZMethod.AES256SHA256), new AES256SHA256Decoder());
        Coders.addDecoder(inputStream, unsupportedCoder, PASSWORD);
    }

    @Test
    public void testAes256Sha256Decoder() throws NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream inputStream = mock(InputStream.class);
        final Coder aes256Sha256Coder = new Coder(new SevenZMethod(SevenZMethod.AES256SHA256), new AES256SHA256Decoder());

        when(inputStream.readAllBytes()).thenReturn("passwordSalt".getBytes());
        final OutputStream outputStream = new ByteArrayOutputStream();
        final InputStream actualOutputStream = Coders.addDecoder(inputStream, aes256Sha256Coder, PASSWORD);
        assertEquals(outputStream, actualOutputStream);

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final byte[] key = "key".getBytes();
        final byte[] iv = "initializationVector".getBytes();
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        final byte[] expectedEncryptedData = new byte[128];
        cipher.doFinal(expectedEncryptedData);
        final InputStream encryptedInputStream = new ByteArrayInputStream(expectedEncryptedData);

        when(inputStream.readAllBytes()).thenReturn(Arrays.copyOfRange(expectedEncryptedData, 0, expectedEncryptedData.length));
        final OutputStream actualDecryptedOutputStream = Coders.addDecoder(encryptedInputStream, aes256Sha256Coder, PASSWORD).as(OutputStream.class);
        final byte[] actualDecryptedData = actualDecryptedOutputStream.toByteArray();
        assertArrayEquals(new byte[0], actualDecryptedData);
    }
}