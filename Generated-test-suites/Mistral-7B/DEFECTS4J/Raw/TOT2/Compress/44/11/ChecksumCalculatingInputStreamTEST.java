import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadMethod() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);

        int expectedByte = inputData[0];
        int readByte = checksumCalculatingInputStream.read();

        Assert.assertEquals("Read byte doesn't match the expected value", expectedByte, readByte);
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);

        byte[] outputData = new byte[3];
        int expectedLength = inputData.length;
        int readLength = checksumCalculatingInputStream.read(outputData, 0, outputData.length);

        Assert.assertEquals("Read length doesn't match the expected value", expectedLength, readLength);
        Assert.assertArrayEquals("Read byte array doesn't match the expected value", inputData, outputData);
    }

    @Test(expected = IOException.class)
    public void testReadBeyondEndOfStream() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);

        int readLength = checksumCalculatingInputStream.read(new byte[1], 0, 1);

        // ChecksumCalculatingInputStream should throw an IOException when read beyond the end of the stream
        checksumCalculatingInputStream.read();
    }
}