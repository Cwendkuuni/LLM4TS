import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.IV;
import java.security.spec.GCMParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.SecretKeySpec;

import static org.apache.commons.compress.archivers.sevenz.Coders.*;

public class CodersTest {

    private static InputStream testInputStream;
    private static OutputStream testOutputStream;
    private static byte[] testPassword = "testPassword".getBytes(StandardCharsets.UTF_8);

    @BeforeClass
    public static void setup() throws Exception {
        testInputStream = new ByteArrayInputStream(new byte[0]);
        testOutputStream = new ByteArrayOutputStream();
    }

    

    @Test
    public void testAddDecoder_UnsupportedMethod_ThrowsIOException() throws Exception {
        final SevenZMethod unsupportedMethod = new SevenZMethod("UNSUPPORTED_METHOD_ID");
        try {
            addDecoder(testInputStream, unsupportedMethod, testPassword);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAddDecoder_SupportedMethod_ReturnsDecoderStream() throws Exception {
        final SevenZMethod copyMethod = COPY;
        final InputStream decoderStream = addDecoder(testInputStream, copyMethod, testPassword);
        Assert.assertNotNull(decoderStream);
        Assert.assertEquals(testInputStream, decoderStream);
    }

    @Test
    public void testAddEncoder_UnsupportedMethod_ThrowsIOException() throws Exception {
        final SevenZMethod unsupportedMethod = LZMA;
        try {
            addEncoder(testOutputStream, unsupportedMethod, testPassword);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAddEncoder_SupportedMethod_ReturnsEncoderStream() throws Exception {
        final SevenZMethod deflateMethod = DEFLATE;
        final OutputStream encoderStream = addEncoder(testOutputStream, deflateMethod, testPassword);
        Assert.assertNotNull(encoderStream);
        Assert.assertEquals(testOutputStream, encoderStream);
    }

    @Test
    public void testCopyDecoder_decode_ReturnsOriginalInputStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream("Test String".getBytes(StandardCharsets.UTF_8));
        final Coder copyCoder = new CopyDecoder();
        final InputStream decoderStream = addDecoder(inputStream, copyMethod, testPassword);
        final byte[] buffer = new byte[10];
        int length;
        while ((length = decoderStream.read(buffer)) > 0) {
            Assert.assertEquals("Test String".getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(buffer, 0, length));
        }
        Assert.assertEquals(inputStream, decoderStream);
    }

    @Test
    public void testLZMADecoder_decode_ReturnsLZMAInputStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Coder lzmaCoder = new LZMADecoder();
        final InputStream decoderStream = addDecoder(inputStream, LZMA, testPassword);
        Assert.assertTrue(decoderStream instanceof LZMAInputStream);
    }

    @Test
    public void testDeflateDecoder_decode_ReturnsInflaterInputStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Coder deflateCoder = new DeflateDecoder();
        final InputStream decoderStream = addDecoder(inputStream, DEFLATE, testPassword);
        Assert.assertTrue(decoderStream instanceof InflaterInputStream);
    }

    @Test
    public void testBZIP2Decoder_decode_ReturnsBZip2CompressorInputStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Coder bzip2Coder = new BZIP2Decoder();
        final InputStream decoderStream = addDecoder(inputStream, BZIP2, testPassword);
        Assert.assertTrue(decoderStream instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testAES256SHA256Decoder_decode_ReturnsCipherInputStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final byte[] iv = new byte[16];
        final SecretKey key = KeyFactory.getInstance("AES").generateSecret(new SecretKeySpec("testPassword".getBytes(StandardCharsets.UTF_8), "AES"));
        final GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv);
        final Iv ivParameterSpec = new IvParameterSpec(iv);
        final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        final InputStream decoderStream = new CipherInputStream(inputStream, cipher);
        final Coder aes256Coder = new AES256SHA256Decoder();
        final InputStream testDecodedStream = addDecoder(inputStream, AES256SHA256, testPassword);
        Assert.assertTrue(testDecodedStream instanceof CipherInputStream);
        Assert.assertEquals(decoderStream, testDecodedStream);
    }

    
}