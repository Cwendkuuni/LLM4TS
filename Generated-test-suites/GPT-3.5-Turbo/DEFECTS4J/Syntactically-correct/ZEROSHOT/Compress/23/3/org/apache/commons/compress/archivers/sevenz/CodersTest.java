import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.compress.archivers.sevenz.Coders;
import org.junit.Before;
import org.junit.Test;

public class CodersTest {

    private static final String PASSWORD = "password";
    private Coders.Coder coder;

    @Before
    public void setUp() {
        coder = new Coders.Coder();
        coder.decompressionMethodId = SevenZMethod.DEFLATE.getId();
    }

    @Test
    public void testAddDecoder() {
        try {
            InputStream is = CodersTest.class.getResourceAsStream("/test.7z");
            ByteArrayInputStream passwordBytes = new ByteArrayInputStream(PASSWORD.getBytes());
            byte[] password = new byte[passwordBytes.available()];
            passwordBytes.read(password, 0, password.length);
            InputStream result = Coders.addDecoder(is, coder, password);
            assertTrue(result instanceof InflaterInputStream);
        } catch (IOException e) {
            fail("Failed to add decoder");
        }
    }

    @Test
    public void testAddEncoder() {
        try {
            OutputStream os = new ByteArrayOutputStream();
            OutputStream result = Coders.addEncoder(os, SevenZMethod.DEFLATE, PASSWORD.getBytes());
            assertTrue(result instanceof DeflaterOutputStream);
        } catch (IOException e) {
            fail("Failed to add encoder");
        }
    }

    @Test
    public void testCoderTable() {
        Coders.CoderId[] coderTable = Coders.coderTable;
        assertEquals(6, coderTable.length);
        assertEquals(SevenZMethod.COPY, coderTable[0].method);
        assertTrue(coderTable[0].coder instanceof Coders.CopyDecoder);
        assertEquals(SevenZMethod.LZMA, coderTable[1].method);
        assertTrue(coderTable[1].coder instanceof Coders.LZMADecoder);
        assertEquals(SevenZMethod.LZMA2, coderTable[2].method);
        assertTrue(coderTable[2].coder instanceof Coders.LZMA2Decoder);
        assertEquals(SevenZMethod.DEFLATE, coderTable[3].method);
        assertTrue(coderTable[3].coder instanceof Coders.DeflateDecoder);
        assertEquals(SevenZMethod.BZIP2, coderTable[4].method);
        assertTrue(coderTable[4].coder instanceof Coders.BZIP2Decoder);
        assertEquals(SevenZMethod.AES256SHA256, coderTable[5].method);
        assertTrue(coderTable[5].coder instanceof Coders.AES256SHA256Decoder);
    }

    @Test
    public void testCopyDecoder() {
        Coders.CopyDecoder decoder = new Coders.CopyDecoder();
        try {
            InputStream is = CodersTest.class.getResourceAsStream("/test.7z");
            InputStream result = decoder.decode(is, coder, null);
            assertEquals(is, result);
        } catch (IOException e) {
            fail("Failed to use copy decoder");
        }
    }

    @Test
    public void testLZMADecoder() {
        Coders.LZMADecoder decoder = new Coders.LZMADecoder();
        try {
            InputStream is = CodersTest.class.getResourceAsStream("/test.7z");
            coder.properties = new byte[] {32, 0, 0, 0, 0};
            InputStream result = decoder.decode(is, coder, null);
            assertTrue(result instanceof LZMAInputStream);
        } catch (IOException e) {
            fail("Failed to use LZMA decoder");
        }
    }

    @Test
    public void testDeflateDecoder() {
        Coders.DeflateDecoder decoder = new Coders.DeflateDecoder();
        try {
            InputStream is = CodersTest.class.getResourceAsStream("/test.7z");
            coder.properties = new byte[] {32};
            InputStream result = decoder.decode(is, coder, null);
            assertTrue(result instanceof InflaterInputStream);
        } catch (IOException e) {
            fail("Failed to use deflate decoder");
        }
    }

    @Test
    public void testBZIP2Decoder() {
        Coders.BZIP2Decoder decoder = new Coders.BZIP2Decoder();
        try {
            InputStream is = CodersTest.class.getResourceAsStream("/test.7z");
            InputStream result = decoder.decode(is, coder, null);
            assertTrue(result instanceof BZip2CompressorInputStream);
        } catch (IOException e) {
            fail("Failed to use BZIP2 decoder");
        }
    }

    @Test
    public void testAES256SHA256Decoder() {
        Coders.AES256SHA256Decoder decoder = new Coders.AES256SHA256Decoder();
        try {
            InputStream is = CodersTest.class.getResourceAsStream("/test.7z");
            coder.properties = new byte[] {0, 1, 2, 3};
            InputStream result = decoder.decode(is, coder, PASSWORD.getBytes());
            assertTrue(result instanceof CipherInputStream);
        } catch (IOException e) {
            fail("Failed to use AES256SHA256 decoder");
        }
    }
}