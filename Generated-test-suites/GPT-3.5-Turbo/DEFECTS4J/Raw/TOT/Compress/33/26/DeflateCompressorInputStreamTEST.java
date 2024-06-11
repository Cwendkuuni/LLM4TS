import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeflateCompressorInputStreamTest {

    private static final int TEST_BUFFER_SIZE = 1024;

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] compressedData = compressData("Test Data");
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int data = deflateInputStream.read();

        deflateInputStream.close();
        inputStream.close();

        Assertions.assertEquals('T', data, "Single byte read mismatch");
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] compressedData = compressData("Test Data");
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[TEST_BUFFER_SIZE];

        int bytesRead = deflateInputStream.read(buffer);

        deflateInputStream.close();
        inputStream.close();

        Assertions.assertEquals(9, bytesRead, "Bytes read mismatch");
        Assertions.assertEquals("Test Data", new String(buffer, StandardCharsets.UTF_8).trim(), "Decompressed data mismatch");
    }

    @Test
    public void testSkip() throws IOException {
        byte[] compressedData = compressData("Test Data");
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long skipBytes = deflateInputStream.skip(4);
        int skippedByte = deflateInputStream.read();

        deflateInputStream.close();
        inputStream.close();

        Assertions.assertEquals(4, skipBytes, "Bytes skipped mismatch");
        Assertions.assertEquals('D', skippedByte, "Skipped byte read mismatch");
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] compressedData = compressData("Test Data");
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateInputStream.available();

        deflateInputStream.close();
        inputStream.close();

        Assertions.assertTrue(availableBytes > 0, "Available bytes mismatch");
    }

    @Test
    public void testClose() throws IOException {
        byte[] compressedData = compressData("Test Data");
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();

        // Attempt to read after closing the stream should throw an exception
        Assertions.assertThrows(IOException.class, () -> deflateInputStream.read(), "Stream should be closed");
    }

    @Test
    public void testSignatureCheck() {
        byte[] signature = {(byte) 0x78, (byte) 0x9c};
        int length = 2;

        boolean isZlib = DeflateCompressorInputStream.isZlibCompressed(signature, length);

        Assertions.assertTrue(isZlib, "Zlib signature check failed");
    }

    private byte[] compressData(String data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Deflater deflater = new Deflater();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
        deflaterOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        deflaterOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}