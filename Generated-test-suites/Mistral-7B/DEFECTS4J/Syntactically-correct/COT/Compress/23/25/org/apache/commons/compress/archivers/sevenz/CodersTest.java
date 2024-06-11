package org.apache.commons.compress.archivers.sevenz.test;

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
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CodersTest {
    

    @Test
    public void testAddDecoder_UnsupportedCompressionMethod() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(Coder.class);
        byte[] password = new byte[16];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{1, 2, 3, 4});

        assertThrows(IOException.class, () -> underTest.addDecoder(is, coder, password));
    }

    @Test
    public void testAddDecoder_SupportedCompressionMethod() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(CopyDecoder.class);
        byte[] password = new byte[1];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.COPY.getId()});
        when(coder.coder).thenReturn(mock(CopyDecoder.class));

        InputStream result = underTest.addDecoder(is, coder, password);

        assertEquals(is, result);
    }

    @Test
    public void testAddDecoder_CopyDecoder() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(CopyDecoder.class);
        byte[] password = new byte[1];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.COPY.getId()});
        when(coder.coder).thenReturn(mock(CopyDecoder.class));

        InputStream result = underTest.addDecoder(is, coder, password);

        assertEquals(is, result);
    }

    @Test
    public void testAddDecoder_LZMADecoder() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(LZMADecoder.class);
        byte[] password = new byte[1];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.LZMA.getId()});
        when(coder.coder).thenReturn(mock(LZMADecoder.class));

        InputStream result = underTest.addDecoder(is, coder, password);

        assertEquals(new LZMAInputStream(is, -1, (byte) 0, (int) (Long.MAX_VALUE)), result);
    }

    @Test
    public void testAddDecoder_DeflateDecoder() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(DeflateDecoder.class);
        byte[] password = new byte[1];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.DEFLATE.getId()});
        when(coder.coder).thenReturn(mock(DeflateDecoder.class));

        InputStream result = underTest.addDecoder(is, coder, password);

        assertEquals(new InflaterInputStream(new DummyByteAddingInputStream(is),
                new Inflater(true)), result);
    }

    @Test
    public void testAddDecoder_BZIP2Decoder() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(BZIP2Decoder.class);
        byte[] password = new byte[1];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.BZIP2.getId()});
        when(coder.coder).thenReturn(mock(BZIP2Decoder.class));

        InputStream result = underTest.addDecoder(is, coder, password);

        assertEquals(new BZip2CompressorInputStream(is), result);
    }

    @Test
    public void testAddDecoder_AES256SHA256Decoder() throws IOException, NoSuchAlgorithmException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(AES256SHA256Decoder.class);
        byte[] password = new byte[16];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.AES256SHA256.getId()});
        when(coder.coder).thenReturn(mock(AES256SHA256Decoder.class));

        InputStream result = underTest.addDecoder(is, coder, password);

        assertNotNull(result);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secret = keyGen.generateKey();
        KeySpec secretKeySpec = secret.getEncoded();
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);

        when(coder.decrypt(eq(is), eq(coder), anyArray())).thenAnswer(invocation -> {
            byte[] decryptedBytes = new byte[secret.getEncoded().length];
            System.arraycopy(secret.getEncoded(), 0, decryptedBytes, 0, secret.getEncoded().length);
            return new ByteArrayInputStream(decryptedBytes);
        });
        when(coder.verifyIntegrityCheck(anyArray())).thenReturn(true);

        InputStream decryptedInputStream = result;
        byte[] decryptedBytes = new byte[secret.getEncoded().length];

        int bytesRead = 0;
        while (bytesRead < secret.getEncoded().length) {
            int numBytesRead = decryptedInputStream.read(decryptedBytes, bytesRead, secret.getEncoded().length - bytesRead);
            bytesRead += numBytesRead;
        }

        assertArrayEquals(secretKeySpec, decryptedBytes);
    }

    @Test
    public void testAddDecoder_UnsupportedSaltSizeAndIVSize() throws IOException {
        InputStream is = mock(InputStream.class);
        Coder coder = mock(AES256SHA256Decoder.class);
        byte[] password = new byte[16];

        Coders underTest = new Coders();

        when(coder.decompressionMethodId).thenReturn(new byte[]{SevenZMethod.AES256SHA256.getId()});
        when(coder.coder).thenReturn(mock(AES256SHA256Decoder.class));

        byte[] properties = new byte[10];
        Arrays.fill(properties, (byte) 0);
        properties[0] = (byte) 0xff;
        properties[1] = (byte) 0xff;

        InputStream result = underTest.addDecoder(is, coder, password);

        assertNull(result);
    }

    @Test
    public void testAddEncoder_UnsupportedCompressionMethod() throws IOException {
        OutputStream out = mock(OutputStream.class);
        SevenZMethod method = mock(SevenZMethod.class);

        Coders underTest = new Coders();

        when(method.name()).thenReturn("UnsupportedMethod");

        assertThrows(IOException.class, () -> underTest.addEncoder(out, method, new byte[16]));
    }

    @Test
    public void testAddEncoder_CopyEncoder() throws IOException {
        OutputStream out = mock(OutputStream.class);
        SevenZMethod method = SevenZMethod.COPY;

        Coders underTest = new Coders();

        InputStream result = underTest.addEncoder(out, method, new byte[1]);

        assertEquals(out, result);
    }

    @Test
    public void testAddEncoder_LZMADecoder() throws IOException {
        OutputStream out = mock(OutputStream.class);
        SevenZMethod method = SevenZMethod.LZMA;

        Coders underTest = new Coders();

        InputStream result = underTest.addEncoder(out, method, new byte[1]);

        assertEquals(new LZMAOutputStream(out), result);
    }

    @Test
    public void testAddEncoder_DeflateEncoder() throws IOException {
        OutputStream out = mock(OutputStream.class);
        SevenZMethod method = SevenZMethod.DEFLATE;

        Coders underTest = new Coders();

        InputStream result = underTest.addEncoder(out, method, new byte[1]);

        assertEquals(new DeflaterOutputStream(out, new Deflater(9, true)), result);
    }

    @Test
    public void testAddEncoder_BZIP2Decoder() throws IOException {
        OutputStream out = mock(OutputStream.class);
        SevenZMethod method = SevenZMethod.BZIP2;

        Coders underTest = new Coders();

        InputStream result = underTest.addEncoder(out, method, new byte[1]);

        assertEquals(new BZip2CompressorOutputStream(out), result);
    }

    @Test
    public void testAddEncoder_AES256SHA256Decoder() throws IOException, NoSuchAlgorithmException {
        OutputStream out = mock(OutputStream.class);
        SevenZMethod method = SevenZMethod.AES256SHA256;

        Coders underTest = new Coders();

        InputStream result = underTest.addEncoder(out, method, new byte[16]);

        assertNotNull(result);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secret = keyGen.generateKey();

        when(coderTable[coderTable.length - 1].coder.encode(eq(out), anyArray()))
                .thenAnswer(invocation -> {
                    byte[] encodedBytes = secret.getEncoded();
                    out.write(encodedBytes, 0, encodedBytes.length);
                    return out;
                });

        InputStream encodedInputStream = result;
        byte[] encodedBytes = new byte[secret.getEncoded().length];

        int bytesRead = 0;
        while (bytesRead < encodedBytes.length) {
            int numBytesRead = encodedInputStream.read(encodedBytes, bytesRead, encodedBytes.length - bytesRead);
            bytesRead += numBytesRead;
        }

        assertArrayEquals(secret.getEncoded(), encodedBytes);
    }

    
}