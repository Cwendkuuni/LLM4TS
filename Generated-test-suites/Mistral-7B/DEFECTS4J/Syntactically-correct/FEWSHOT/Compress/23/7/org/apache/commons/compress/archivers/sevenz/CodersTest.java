package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import javax.crypto.CipherSpi;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpecFactory;
import javax.crypto.spec.SecretKeySpecFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAConstants;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMADict;
import org.apache.commons.compress.compressors.lzma.LZMADictFactory;
import org.apache.commons.compress.compressors.lzma.LZMAError;
import org.apache.commons.compress.compressors.lzma.LZMADictionary;
import org.apache.commons.compress.compressors.lzma.LZMADictionaryWriter;
import org.apache.commons.compress.compressors.lzma.LZMADictionaryReader;
import org.apache.commons.compress.compressors.lzma.LZMADictionaryWriter.LZMAOptions;
import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.LZMADictionary;
import org.tukaani.xz.LZMADictionaryWriter;
import org.tukaani.xz.LZMAStream;
import org.tukaani.xz.LZMAutil;

import org.junit.Test;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final OutputStream encoder = new BZIP2CompressorOutputStream(outputStream);
        final byte[] bzip2Password = "password123".getBytes();
        Coders.addEncoder(encoder, SevenZMethod.BZIP2, bzip2Password);
        encoder.close();
        final InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        final byte[] bzip2Data = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = is.read(bzip2Data, 0, bzip2Data.length)) != -1) {
            // Nothing to test here, as BZip2Decoder is already tested in BZip2DecoderTest
        }
        final Coder coder = mock(Coder.class);
        when(coder.decompressionMethodId).thenReturn(new long[]{SevenZMethod.BZIP2.getId()});
        final InputStream decoder = Coders.addDecoder(is, coder, new byte[0]);
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            // Assertions to be added here
        }
        decoder.close();
        is.close();
    }

    @Test
    public void testAddDecoder_UnsupportedCompressionMethod() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final OutputStream encoder = new LZMAOutputStream(outputStream, new LZMADictionaryWriter(LZMAOptions.DEFAULT));
        final byte[] lzmaPassword = "password123".getBytes();
        Coders.addEncoder(encoder, SevenZMethod.LZMA, lzmaPassword);
        encoder.finish();
        encoder.close();
        final InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        final Coder coder = mock(Coder.class);
        when(coder.decompressionMethodId).thenReturn(new long[]{SevenZMethod.AES256SHA256.getId()});
        try {
            Coders.addDecoder(is, coder, new byte[0]);
            fail();
        } catch (IOException e) {
            assertEquals("Unsupported compression method " + SevenZMethod.LZMA, e.getMessage());
        }
        is.close();
    }

    @Test
    public void testAddEncoder() throws IOException, NoSuchAlgorithmException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        final byte[] deflatePassword = "password123".getBytes();
        Coders.addEncoder(System.out, SevenZMethod.DEFLATE, deflatePassword);
        final OutputStream encoder = Coders.addEncoder(System.out, SevenZMethod.DEFLATE, deflatePassword);
        final byte[] input = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(input)) != -1) {
            encoder.write(input, 0, bytesRead);
        }
        encoder.finish();
        encoder.close();
        inputStream.close();
        final InputStream is = new ByteArrayInputStream(Arrays.copyOfRange(Coders.copyOfBytes(System.out.toString().getBytes()), 0, 1024));
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        final Coder coder = mock(Coder.class);
        when(coder.decompressionMethodId).thenReturn(new long[]{SevenZMethod.DEFLATE.getId()});
        when(coder.properties).thenReturn(new byte[0]);
        final InputStream decoder = Coders.addDecoder(is, coder, new byte[0]);
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            assertArrayEquals("test".getBytes(), Arrays.copyOfRange(decodedData, 0, bytesDecoded));
        }
        decoder.close();
        is.close();
    }

    @Test
    public void testAddEncoder_UnsupportedCompressionMethod() throws IOException, NoSuchAlgorithmException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final OutputStream encoder = new AES256SHA256OutputStream(outputStream, new SecretKeySpec("password123".getBytes(), "AES"));
        final SevenZMethod aes256Sha256Method = SevenZMethod.AES256SHA256;
        Coders.addEncoder(encoder, aes256Sha256Method, null);
        encoder.finish();
        encoder.close();
        final InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        final Coder coder = mock(Coder.class);
        when(coder.decompressionMethodId).thenReturn(new long[]{SevenZMethod.LZMA.getId()});
        try {
            Coders.addDecoder(is, coder, new byte[0]);
            fail();
        } catch (IOException e) {
            assertEquals("Unsupported compression method " + aes256Sha256Method, e.getMessage());
        }
        is.close();
    }

    @Test
    public void testCoderId_CopyDecoder() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        final Coder coder = new CopyDecoder();
        final InputStream decoder = Coders.addDecoder(inputStream, coder, new byte[0]);
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            assertArrayEquals("test".getBytes(), Arrays.copyOfRange(decodedData, 0, bytesDecoded));
        }
        decoder.close();
        inputStream.close();
    }

    @Test
    public void testCoderId_LZMADecoder() throws IOException, GeneralSecurityException {
        final byte[] lzmaPassword = "password123".getBytes();
        final SecretKey lzmaKey = KeyGenerator.getInstance("AES").generateKey();
        final IvParameterSpec lzmaIV = IvParameterSpecFactory.getDefaultInstance("AES", new SecretKeySpec(lzmaKey.getEncoded(), "AES"));
        final OutputStream encoder = new LZMAOutputStream(new ByteArrayOutputStream(), new LZMADictionaryWriter(LZMAOptions.DEFAULT, lzmaPassword, 16384));
        final byte[] input = "test".getBytes();
        encoder.write(input, 0, input.length);
        encoder.finish();
        encoder.close();
        final InputStream is = new ByteArrayInputStream(Arrays.copyOfRange(Coders.copyOfBytes(encoder.toString()), 0, 1024));
        final Coder coder = new LZMADecoder();
        final InputStream decoder = Coders.addDecoder(is, coder, lzmaPassword);
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            assertArrayEquals(input, Arrays.copyOfRange(decodedData, 0, bytesDecoded));
        }
        decoder.close();
        is.close();
    }

    @Test
    public void testCoderId_DeflateDecoder() throws IOException {
        final byte[] deflatePassword = "password123".getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        final OutputStream encoder = new DeflaterOutputStream(new ByteArrayOutputStream(), new Deflater(9, true));
        encoder.write(inputStream.readAllBytes());
        encoder.finish();
        encoder.close();
        final InputStream is = new ByteArrayInputStream(Arrays.copyOfRange(Coders.copyOfBytes(encoder.toString()), 0, 1024));
        final Coder coder = new DeflateDecoder();
        final InputStream decoder = Coders.addDecoder(is, coder, deflatePassword);
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            assertArrayEquals("test".getBytes(), Arrays.copyOfRange(decodedData, 0, bytesDecoded));
        }
        decoder.close();
        is.close();
    }

    @Test
    public void testCoderId_BZIP2Decoder() throws IOException {
        final byte[] bzip2Password = "password123".getBytes();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final OutputStream encoder = new BZip2CompressorOutputStream(outputStream);
        encoder.write("test".getBytes());
        encoder.finish();
        encoder.close();
        final InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        final Coder coder = new BZIP2Decoder();
        final InputStream decoder = Coders.addDecoder(is, coder, bzip2Password);
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            assertArrayEquals("test".getBytes(), Arrays.copyOfRange(decodedData, 0, bytesDecoded));
        }
        decoder.close();
        is.close();
    }

    @Test
    public void testCoderId_AES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final byte[] aes256Sha256Password = "password123".getBytes();
        final byte[] randomSalt = new byte[16];
        new SecureRandom().nextBytes(randomSalt);
        final byte[] randomIV = new byte[16];
        new SecureRandom().nextBytes(randomIV);
        final byte[] input = "test".getBytes();
        final SecretKey aesKey = KeyGenerator.getInstance("AES").generateKey();
        final IvParameterSpec aesIV = IvParameterSpecFactory.getDefaultInstance("AES", new SecretKeySpec(aesKey.getEncoded(), "AES"), randomIV);
        final OutputStream encoder = new CipherOutputStream(new ByteArrayOutputStream(), Cipher.getInstance("AES/CBC/PKCS5Padding"), aesIV);
        encoder.write(input, 0, input.length);
        encoder.finish();
        encoder.close();
        final InputStream is = new ByteArrayInputStream(Arrays.copyOfRange(Coders.copyOfBytes(encoder.toString()), 0, 1024));
        final Coder coder = new AES256SHA256Decoder();
        final InputStream decoder = Coders.addDecoder(is, coder, aes256Sha256Password);
        final byte[] decodedData = new byte[1024];
        int bytesDecoded = 0;
        while ((bytesDecoded = decoder.read(decodedData, 0, decodedData.length)) != -1) {
            assertArrayEquals(input, Arrays.copyOfRange(decodedData, 0, bytesDecoded));
        }
        decoder.close();
        is.close();
    }
}

class AES256SHA256OutputStream extends CipherOutputStream {

    public AES256SHA256OutputStream(OutputStream out, SecretKey key) {
        super(out, Cipher.getInstance("AES/CBC/NoPadding"), new IvParameterSpec(new byte[16]));
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}