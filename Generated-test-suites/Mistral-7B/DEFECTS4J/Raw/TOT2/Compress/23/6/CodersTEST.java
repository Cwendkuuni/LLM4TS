import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test data".getBytes(StandardCharsets.UTF_8)});
        OutputStream output = new ByteArrayOutputStream();
        Coders.addDecoder(input, Coders.SevenZMethod.COPY, null).close();
        byte[] expected = new byte[]{"Test data".getBytes(StandardCharsets.UTF_8)};
        byte[] actual = output.toByteArray();
        assertArrayEquals("Copy decoder test failed", expected, actual);
    }

    @Test
    public void testLZMADecoder_Basic() throws IOException {
        InputStream input = new LZMAInputStream(new ByteArrayInputStream(new byte[]{"Test data".getBytes(StandardCharsets.UTF_8)}));
        OutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, Coders.SevenZMethod.LZMA, null).close();
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(output.toByteArray()), Coders.SevenZMethod.LZMA, null).readAllBytes();
        String decodedString = new String(decoded, StandardCharsets.UTF_8);
        assertEquals("LZMA decoder test failed", "Test data", decodedString);
    }

    @Test(expected = IOException.class)
    public void testLZMADecoder_Unsupported() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test data".getBytes(StandardCharsets.UTF_8)});
        Coders.addDecoder(input, new SevenZMethod("UnsupportedMethodId"), null);
    }

    @Test
    public void testDeflateDecoder_Basic() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0x10, (byte) 0x3c, (byte) 0x00, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x08, (byte) 0x2b, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        OutputStream output = new ByteArrayOutputStream();
        Coders.addDecoder(input, Coders.SevenZMethod.DEFLATE, null).close();
        byte[] expected = new byte[]{(byte) 0x44, (byte) 0x65, (byte) 0x73, (byte) 0x61, (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x20, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x64};
        byte[] actual = output.toByteArray();
        assertArrayEquals("Deflate decoder test failed", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testDeflateDecoder_InvalidInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x78, 0x9c, 0x10, 0x3c, (byte) 0xff, (byte) 0xff});
        Coders.addDecoder(input, Coders.SevenZMethod.DEFLATE, null);
    }

    @Test
    public void testBZIP2Decoder_Basic() throws IOException {
        InputStream input = new BZip2CompressorInputStream(new ByteArrayInputStream(new byte[]{"Test data".getBytes(StandardCharsets.UTF_8)}));
        OutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, Coders.SevenZMethod.BZIP2, null).close();
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(output.toByteArray()), Coders.SevenZMethod.BZIP2, null).readAllBytes();
        String decodedString = new String(decoded, StandardCharsets.UTF_8);
        assertEquals("BZIP2 decoder test failed", "Test data", decodedString);
    }

    @Test
    public void testAES256SHA256Decoder_Basic() throws IOException, NoSuchAlgorithmException {
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        InputStream input = new ByteArrayInputStream(new byte[]{0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});
        OutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, Coders.SevenZMethod.AES256SHA256, password).close();
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(output.toByteArray()), Coders.SevenZMethod.AES256SHA256, password).readAllBytes();
        byte[] expected = new byte[]{0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
        assertArrayEquals("AES256SHA256 decoder test failed", expected, decoded);
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256Decoder_InvalidPassword() throws IOException, NoSuchAlgorithmException {
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);
        InputStream input = new ByteArrayInputStream(new byte[]{0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});
        Coders.addDecoder(input, Coders.SevenZMethod.AES256SHA256, new byte[]{0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});
    }
}