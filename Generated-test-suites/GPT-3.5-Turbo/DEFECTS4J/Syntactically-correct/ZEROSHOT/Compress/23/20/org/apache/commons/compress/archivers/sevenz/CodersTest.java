import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import static org.junit.Assert.*;

public class CodersTest {

    private Coders.Coder copyCoder;
    private Coders.Coder lzmCoder;
    private Coders.Coder lzma2Coder;
    private Coders.Coder deflateCoder;
    private Coders.Coder bzip2Coder;
    private Coders.Coder aes256sha256Coder;

    @Before
    public void setUp() {
        coders = new Coders();
        copyCoder = new Coders.Coder();
        copyCoder.decompressionMethodId = new byte[]{0, 0};
        lzmCoder = new Coders.Coder();
        lzmCoder.decompressionMethodId = new byte[]{1, 1, 1};
        lzma2Coder = new Coders.Coder();
        lzma2Coder.decompressionMethodId = new byte[]{2, 2, 2};
        deflateCoder = new Coders.Coder();
        deflateCoder.decompressionMethodId = new byte[]{3, 3, 3};
        bzip2Coder = new Coders.Coder();
        bzip2Coder.decompressionMethodId = new byte[]{4, 4, 4};
        aes256sha256Coder = new Coders.Coder();
        aes256sha256Coder.decompressionMethodId = new byte[]{5, 5, 5};
    }

    @Test
    public void testAddDecoder_CorrectDecoderReturned() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        InputStream decodedIs = coders.addDecoder(is, copyCoder, null);
        assertTrue(decodedIs instanceof ByteArrayInputStream);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedCompressionMethod_IOExceptionThrown() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        coders.addDecoder(is, new Coders.Coder(), null);
    }

    @Test
    public void testAddEncoder_CorrectEncoderReturned() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        OutputStream encodedOs = coders.addEncoder(os, SevenZMethod.COPY, null);
        assertTrue(encodedOs instanceof ByteArrayOutputStream);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedCompressionMethod_IOExceptionThrown() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        coders.addEncoder(os, new SevenZMethod(), null);
    }

    @Test
    public void testCoderTable() {
        Coders.CoderId[] coderTable = coders.coderTable;
        assertEquals(6, coderTable.length);
        assertEquals(SevenZMethod.COPY, coderTable[0].method);
        assertEquals(SevenZMethod.LZMA, coderTable[1].method);
        assertEquals(SevenZMethod.LZMA2, coderTable[2].method);
        assertEquals(SevenZMethod.DEFLATE, coderTable[3].method);
        assertEquals(SevenZMethod.BZIP2, coderTable[4].method);
        assertEquals(SevenZMethod.AES256SHA256, coderTable[5].method);
    }

    @Test
    public void testCopyDecoder_Decode_ReturnsInputStreamWithoutModifyingIt() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        InputStream decoded = coders.new CopyDecoder().decode(in, copyCoder, null);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, decoded.readAllBytes());
    }

    @Test
    public void testCopyDecoder_Encode_ReturnsOutputStreamWithoutModifyingIt() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        OutputStream encoded = coders.new CopyDecoder().encode(out, null);
        encoded.write(new byte[]{1, 2, 3, 4, 5});
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, out.toByteArray());
    }

    @Test
    public void testLZMADecoder_Decode_ReturnsLZMAInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        InputStream decoded = coders.new LZMADecoder().decode(in, lzmCoder, null);
        assertTrue(decoded instanceof LZMAInputStream);
    }

    @Test
    public void testDeflateDecoder_Decode_ReturnsInflaterInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        InputStream decoded = coders.new DeflateDecoder().decode(in, deflateCoder, null);
        assertTrue(decoded instanceof InflaterInputStream);
    }

    @Test
    public void testDeflateDecoder_Encode_ReturnsDeflaterOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        OutputStream encoded = coders.new DeflateDecoder().encode(out, null);
        assertTrue(encoded instanceof DeflaterOutputStream);
    }

    @Test
    public void testBZIP2Decoder_Decode_ReturnsBZip2CompressorInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        InputStream decoded = coders.new BZIP2Decoder().decode(in, bzip2Coder, null);
        assertTrue(decoded instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testBZIP2Decoder_Encode_ReturnsBZip2CompressorOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        OutputStream encoded = coders.new BZIP2Decoder().encode(out, null);
        assertTrue(encoded instanceof BZip2CompressorOutputStream);
    }

    @Test
    public void testAES256SHA256Decoder_Decode_ReturnsCipherInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        InputStream decoded = coders.new AES256SHA256Decoder().decode(in, aes256sha256Coder, null);
        assertTrue(decoded instanceof CipherInputStream);
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256Decoder_Decode_PasswordRequired_IOExceptionThrown() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] password = null;
        coders.new AES256SHA256Decoder().decode(in, aes256sha256Coder, password);
    }
}