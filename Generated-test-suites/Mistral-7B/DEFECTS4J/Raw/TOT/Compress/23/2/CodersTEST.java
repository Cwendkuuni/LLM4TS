import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyCodec() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        Coders.encode(outputStream, SevenZMethod.COPY, null);
        outputStream.writeTo(inputStream);

        InputStream decodedInputStream = Coders.addDecoder(inputStream, new Coders.CoderId(SevenZMethod.COPY, new Coders.CopyDecoder()), null);
        byte[] decodedData = IOUtils.toByteArray(decodedInputStream);

        Assert.assertArrayEquals("Test data".getBytes(StandardCharsets.UTF_8), decodedData);
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = new byte[1024];
        byte[] expectedOutput = new byte[1024]; // Fill with expected data

        InputStream inputStream = new ByteArrayInputStream(inputData);
        OutputStream outputStream = new ByteArrayOutputStream();

        Coders.encode(outputStream, SevenZMethod.LZMA, null);
        outputStream.writeTo(inputStream);

        InputStream decodedInputStream = Coders.addDecoder(inputStream, new Coders.CoderId(SevenZMethod.LZMA, new Coders.LZMADecoder()), null);
        byte[] decodedData = IOUtils.toByteArray(decodedInputStream);

        Assert.assertArrayEquals(expectedOutput, decodedData);
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = new byte[1024]; // Fill with compressed data
        byte[] expectedOutput = new byte[1024]; // Fill with expected data

        InputStream inputStream = new ByteArrayInputStream(inputData);
        OutputStream outputStream = new ByteArrayOutputStream();

        Coders.encode(outputStream, SevenZMethod.DEFLATE, null);
        outputStream.writeTo(inputStream);

        InputStream decodedInputStream = Coders.addDecoder(inputStream, new Coders.CoderId(SevenZMethod.DEFLATE, new Coders.DeflateDecoder()), null);
        byte[] decodedData = IOUtils.toByteArray(decodedInputStream);

        Assert.assertArrayEquals(expectedOutput, decodedData);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));

        Coders.addDecoder(inputStream, new Coders.CoderId(SevenZMethod.COPY, new Object() {
            @Override
            public InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
                throw new UnsupportedOperationException();
            }
        }), null);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = new byte[1024]; // Fill with encrypted data
        byte[] password = "TestPassword".getBytes(StandardCharsets.UTF_8);
        byte[] expectedOutput = new byte[1024]; // Fill with expected data

        InputStream inputStream = new ByteArrayInputStream(inputData);
        OutputStream outputStream = new ByteArrayOutputStream();

        Coders.encode(outputStream, SevenZMethod.AES256SHA256, password);
        outputStream.writeTo(inputStream);

        InputStream decodedInputStream = Coders.addDecoder(inputStream, new Coders.CoderId(SevenZMethod.AES256SHA256, new Coders.AES256SHA256Decoder()), password);
        byte[] decodedData = IOUtils.toByteArray(decodedInputStream);

        Assert.assertArrayEquals(expectedOutput, decodedData);
    }
}