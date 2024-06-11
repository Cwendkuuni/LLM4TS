import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    private static final byte[] TEST_PASSWORD = "testPassword".getBytes(StandardCharsets.UTF_8);

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.COPY, TEST_PASSAGE);
        outputStream.close();
        InputStream copiedStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()),
                new Coder(SevenZMethod.COPY), TEST_PASSAGE);
        byte[] result = new byte[3];
        int read = 0;
        while ((read = copiedStream.read(result)) != -1) {
            Assert.assertArrayEquals(new byte[]{1, 2, 3}, Arrays.copyOfRange(result, 0, read));
        }
        copiedStream.close();
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 5, 5, 6, 6, 6});
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.LZMA, TEST_PASSAGE);
        outputStream.close();
        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()),
                new Coder(SevenZMethod.LZMA), TEST_PASSAGE);
        byte[] result = new byte[11];
        int read = 0;
        while ((read = decodedStream.read(result)) != -1) {
            Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 5, 5, 6, 6, 6}, Arrays.copyOfRange(result, 0, read));
        }
        decodedStream.close();
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x1f, 0x9c, (byte) 0xff, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, TEST_PASSAGE);
        outputStream.close();
        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()),
                new Coder(SevenZMethod.DEFLATE), TEST_PASSAGE);
        byte[] result = new byte[12];
        int read = 0;
        while ((read = decodedStream.read(result)) != -1) {
            Assert.assertArrayEquals(new byte[]{0x78, 0x1f, 0x9c, 0xff, 0, 0, 0, 0, 0, 0, 0, 0}, Arrays.copyOfRange(result, 0, read));
        }
        decodedStream.close();
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x42, 0x5a, 0x68, 0x64, 0x64, 0x72, 0x6b, 0x20, 0x75, 0x6e, 0x61, 0x6d, 0x65, 0x6e, 0x74, 0x77, 0x70, 0x53, 0x63, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x61, 0x74, 0x65, 0x54, 0x79, 0x70, 0x65, 0x31, 0x32, 0x33, 0x34});
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.BZIP2, TEST_PASSAGE);
        outputStream.close();
        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()),
                new Coder(SevenZMethod.BZIP2), TEST_PASSAGE);
        byte[] result = new byte[15];
        int read = 0;
        while ((read = decodedStream.read(result)) != -1) {
            Assert.assertArrayEquals(new byte[]{0x42, 0x5a, 0x68, 0x64, 0x64, 0x72, 0x6b, 0x20, 0x75, 0x6e, 0x61, 0x6d, 0x65, 0x6e, 0x74, 0x77, 0x70}, Arrays.copyOfRange(result, 0, read));
        }
        decodedStream.close();
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] encryptedData = new byte[]{0x12, 0x34, 0x56, 0x78, 0x9a, 0xbc, 0xde, 0xef, 0x12, 0x34, 0x56, 0x78, 0x9a, 0xbc, 0xde, 0xef, 0x12, 0x34, 0x56, 0x78, 0x9a, 0xbc, 0xde, 0xef, 0x12, 0x34, 0x56, 0x78, 0x9a, 0xbc, 0xde, 0xef};
        byte[] expectedDecryptedData = new byte[]{0x67, 0x2b, 0xcd, 0xab, 0x11, 0xe0, 0x4e, 0x6f, 0x67, 0x2b, 0xcd, 0xab, 0x11, 0xe0, 0x4e, 0x6f, 0x67, 0x2b, 0xcd, 0xab, 0x11, 0xe0, 0x4e, 0x6f, 0x67, 0x2b, 0xcd, 0xab, 0x11, 0xe0, 0x4e};
        InputStream inputStream = new ByteArrayInputStream(encryptedData);
        OutputStream outputStream = new ByteArrayOutputStream();
        Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, TEST_PASSAGE);
        outputStream.write(encryptedData);
        outputStream.close();
        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()),
                new Coder(SevenZMethod.AES256SHA256), TEST_PASSAGE);
        byte[] result = new byte[21];
        int read = 0;
        while ((read = decodedStream.read(result)) != -1) {
            Assert.assertArrayEquals(expectedDecryptedData, Arrays.copyOfRange(result, 0, read));
        }
        decodedStream.close();
    }

    private static class Coder extends Coders.CoderBase {
        private final SevenZMethod method;

        private Coder(SevenZMethod method) {
            this.method = method;
        }

        @Override
        public SevenZMethod getMethod() {
            return method;
        }
    }
}