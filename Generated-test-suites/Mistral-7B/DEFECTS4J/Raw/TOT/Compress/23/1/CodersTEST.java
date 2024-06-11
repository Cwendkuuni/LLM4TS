import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Test data for CopyDecoder".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.COPY, new byte[0]);
        outputStream.close();
        InputStream decoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new CopyDecoder(), new byte[0]);

        byte[] result = new byte[testData.length];
        int read = decoderStream.read(result);
        Assert.assertEquals(testData.length, read);
        Assert.assertArrayEquals(testData, result);

        decoderStream.close();
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Test data for LZMADecoder".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod sevenZMethod = SevenZMethod.LZMA;
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);

        Coders.addEncoder(outputStream, sevenZMethod, password);
        outputStream.write(testData);
        outputStream.close();

        InputStream decoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new LZMADecoder(), password);

        byte[] result = new byte[testData.length];
        int read = decoderStream.read(result);
        Assert.assertEquals(testData.length, read);
        Assert.assertArrayEquals(testData, result);

        decoderStream.close();
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Test data for DeflateDecoder".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod sevenZMethod = SevenZMethod.DEFLATE;
        byte[] password = new byte[0];

        Coders.addEncoder(outputStream, sevenZMethod, password);
        outputStream.write(testData);
        outputStream.close();

        InputStream decoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new DeflateDecoder(), password);

        byte[] result = new byte[testData.length];
        int read = decoderStream.read(result);
        Assert.assertEquals(testData.length, read);
        Assert.assertArrayEquals(testData, result);

        decoderStream.close();
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Test data for UnsupportedDecoder".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod sevenZMethod = new SevenZMethod("UNSUPPORTED_METHOD_ID");
        byte[] password = new byte[0];

        try {
            Coders.addEncoder(outputStream, sevenZMethod, password);
            outputStream.write(testData);
            outputStream.close();

            InputStream decoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new DeflateDecoder(), password);
            decoderStream.close();
        } catch (IOException e) {
            throw e;
        }

        InputStream unsupportedDecoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new UnsupportedDecoder(), password);
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256DecoderWithInvalidPassword() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Test data for AES256SHA256DecoderWithInvalidPassword".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod sevenZMethod = SevenZMethod.AES256SHA256;
        byte[] password = new byte[]{1, 2, 3};

        Coders.addEncoder(outputStream, sevenZMethod, password);
        outputStream.write(testData);
        outputStream.close();

        InputStream decoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new AES256SHA256Decoder(), new byte[]{4, 5, 6});
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException {
        byte[] testData = "Test data for AES256SHA256Decoder".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod sevenZMethod = SevenZMethod.AES256SHA256;
        byte[] password = "password".getBytes(StandardCharsets.UTF_8);

        Coders.addEncoder(outputStream, sevenZMethod, password);
        outputStream.write(testData);
        outputStream.close();

        InputStream decoderStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), new AES256SHA256Decoder(), password);

        byte[] result = new byte[testData.length];
        int read = decoderStream.read(result);
        Assert.assertEquals(testData.length, read);
        Assert.assertArrayEquals(testData, result);

        decoderStream.close();
    }

    private static class UnsupportedDecoder extends Coders.CoderBase {
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        OutputStream encode(final OutputStream out, final byte[] password) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}