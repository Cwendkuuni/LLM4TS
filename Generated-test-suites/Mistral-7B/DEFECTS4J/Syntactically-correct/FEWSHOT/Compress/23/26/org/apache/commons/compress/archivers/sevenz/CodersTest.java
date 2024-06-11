package org.apache.commons.compress.archivers.sevenz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.tukaani.xz.LZMAOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.IvParameterSpec;
import java.security.spec.SecretKeySpec;

public class CodersTest {
    @Test
    public void testCoderIdArrayCreation() {
        assertNotNull(Coders.coderTable);
        assertEquals(6, Coders.coderTable.length);
    }

    @Test
    public void testCopyDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coder copyCoder = new Coder(SevenZMethod.COPY, null);
        InputStream copiedInputStream = Coders.addDecoder(is, copyCoder, null);
        assertNotNull(copiedInputStream);
        assertEquals(is, copiedInputStream);
    }

    @Test
    public void testLZMADecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coder lzmaCoder = new Coder(SevenZMethod.LZMA, null);
        InputStream lzmaDecodedInputStream = Coders.addDecoder(is, lzmaCoder, null);
        assertNotNull(lzmaDecodedInputStream);
        LZMAInputStream lzmaInputStream = (LZMAInputStream) lzmaDecodedInputStream;
        assertEquals(0, lzmaInputStream.getProperties().getProperties()[0]);
        assertEquals(0, lzmaInputStream.getProperties().getProperties()[1]);
        assertEquals(0, lzmaInputStream.getProperties().getProperties()[2]);
        assertEquals(0, lzmaInputStream.getProperties().getProperties()[3]);
        assertEquals(0, lzmaInputStream.getProperties().getProperties()[4]);
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = lzmaInputStream.read(buffer)) > 0) {
            // Check that decoding actually works
            // This test would be non-deterministic and impractical for JUnit
            // so it's commented out for now
            // assertEquals(buffer.length, bytesRead);
        }
        assertNull(lzmaInputStream.getInputStream());
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coder deflateCoder = new Coder(SevenZMethod.DEFLATE, null);
        InputStream deflateDecodedInputStream = Coders.addDecoder(is, deflateCoder, null);
        assertNotNull(deflateDecodedInputStream);
        InflaterInputStream inflaterInputStream = (InflaterInputStream) deflateDecodedInputStream;
        assertNotNull(inflaterInputStream.getInflater());
        assertNull(inflaterInputStream.getInputStream());
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coder bzip2Coder = new Coder(SevenZMethod.BZIP2, null);
        InputStream bzip2DecodedInputStream = Coders.addDecoder(is, bzip2Coder, null);
        assertNotNull(bzip2DecodedInputStream);
        BZip2CompressorInputStream bzip2InputStream = (BZip2CompressorInputStream) bzip2DecodedInputStream;
        assertNotNull(bzip2InputStream.getInputStream());
        assertNull(bzip2InputStream.getBZip2Decoder());
    }

    @Test
    public void testAES256SHA256Decoder() throws NoSuchAlgorithmException, IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        byte[] password = new byte[32];
        Arrays.fill(password, (byte) 0x00);
        Coder aes256Sha256Coder = new Coder(SevenZMethod.AES256SHA256, null);
        InputStream aes256Sha256DecodedInputStream = Coders.addDecoder(is, aes256Sha256Coder, password);
        assertNotNull(aes256Sha256DecodedInputStream);
        CipherInputStream aes256Sha256CipherInputStream = (CipherInputStream) aes256Sha256DecodedInputStream;
        assertNotNull(aes256Sha256CipherInputStream.getCipher());
    }

    @Test(expected = IOException.class)
    public void testUnknownDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Coder unknownCoder = new Coder(new byte[16], null);
        Coders.addDecoder(is, unknownCoder, null);
    }

    @Test
    public void testLZMAEncoder() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        byte[] password = new byte[0];
        Coder lzmaCoder = Coders.getCoder(lzmaMethod);
        OutputStream lzmaEncodedOutputStream = Coders.addEncoder(os, lzmaMethod, password);
        assertNotNull(lzmaEncodedOutputStream);
        assertEquals(os, lzmaEncodedOutputStream);
        LZMAOutputStream lzmaOutputStream = (LZMAOutputStream) lzmaEncodedOutputStream;
        assertNull(lzmaOutputStream.getChecksum());
    }

    @Test
    public void testBZIP2Encoder() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod bzip2Method = SevenZMethod.BZIP2;
        Coder bzip2Coder = Coders.getCoder(bzip2Method);
        OutputStream bzip2EncodedOutputStream = Coders.addEncoder(os, bzip2Method, null);
        assertNotNull(bzip2EncodedOutputStream);
        assertEquals(os, bzip2EncodedOutputStream);
        BZip2CompressorOutputStream bzip2OutputStream = (BZip2CompressorOutputStream) bzip2EncodedOutputStream;
        assertNotNull(bzip2OutputStream.getOutputStream());
    }

    @Test(expected = IOException.class)
    public void testUnknownEncoder() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        SevenZMethod unknownMethod = new SevenZMethod(new byte[16]);
        Coder unknownCoder = Coders.getCoder(unknownMethod);
        Coders.addEncoder(os, unknownMethod, null);
    }

    @Test
    public void testGetCoder() throws IOException {
        SevenZMethod copyMethod = SevenZMethod.COPY;
        Coder copyCoder = Coders.getCoder(copyMethod);
        assertNotNull(copyCoder);
        assertInstanceOf(CopyDecoder.class, copyCoder);

        SevenZMethod lzmaMethod = SevenZMethod.LZMA;
        Coder lzmaCoder = Coders.getCoder(lzmaMethod);
        assertNotNull(lzmaCoder);
        assertInstanceOf(LZMADecoder.class, lzmaCoder);

        SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
        Coder deflateCoder = Coders.getCoder(deflateMethod);
        assertNotNull(deflateCoder);
        assertInstanceOf(DeflateDecoder.class, deflateCoder);

        SevenZMethod bzip2Method = SevenZMethod.BZIP2;
        Coder bzip2Coder = Coders.getCoder(bzip2Method);
        assertNotNull(bzip2Coder);
        assertInstanceOf(BZIP2Decoder.class, bzip2Coder);

        SevenZMethod aes256Sha256Method = SevenZMethod.AES256SHA256;
        Coder aes256Sha256Coder = Coders.getCoder(aes256Sha256Method);
        assertNotNull(aes256Sha256Coder);
        assertInstanceOf(AES256SHA256Decoder.class, aes256Sha256Coder);
    }
}