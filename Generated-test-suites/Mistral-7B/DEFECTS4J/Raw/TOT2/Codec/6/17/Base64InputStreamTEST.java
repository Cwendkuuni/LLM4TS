import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream inputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Base64 base64;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        inputStream = new Base64InputStream(byteArrayInputStream, false);
        base64 = new Base64(false);
    }

    @Test
    public void testReadBasic() throws IOException {
        byte[] data = {0, 1, 2, 3};
        byteArrayOutputStream.write(data);
        byteArrayOutputStream.flush();

        int expected = data.length;
        int actual = inputStream.read(singleByte, 0, singleByte.length);

        Assert.assertEquals("Read incorrect number of bytes", expected, actual);
        for (int i = 0; i < expected; i++) {
            Assert.assertEquals("Read incorrect byte " + i, data[i], singleByte[i]);
        }
    }

    @Test
    public void testReadAll() throws IOException {
        byte[] data = {0, 1, 2, 3};
        byteArrayOutputStream.write(data);
        byteArrayOutputStream.flush();

        int expected = data.length;
        byte[] buffer = new byte[expected];
        int actual = inputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals("Read incorrect number of bytes", expected, actual);
        Assert.assertArrayEquals("Read incorrect bytes", data, buffer);
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        int expected = -1;
        int actual = inputStream.read();

        Assert.assertEquals("Read incorrect byte", expected, actual);
    }

    @Test
    public void testReadWithEncoding() throws IOException {
        byte[] data = {0, 1, 2, 3};
        byteArrayOutputStream.write(data);
        byteArrayOutputStream.flush();

        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), true);

        byte[] decodedData = new byte[base64.decodeSize(Base64.decodeBase64(data))];
        int actual = inputStream.read(decodedData, 0, decodedData.length);

        Assert.assertEquals("Read incorrect number of bytes", data.length, actual);
        Assert.assertArrayEquals("Decoded incorrect data", data, decodedData);
    }

    @Test(expected = IOException.class)
    public void testReadWithNoData() throws IOException {
        inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        int actual = inputStream.read();
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeParameter() throws IOException {
        inputStream = new Base64InputStream(new ByteArrayInputStream(singleByte), false);
        inputStream.read(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        inputStream = new Base64InputStream(new ByteArrayInputStream(singleByte), false);
        byte[] buffer = new byte[2];
        inputStream.read(buffer, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        inputStream = new Base64InputStream(new ByteArrayInputStream(singleByte), false);
        byte[] buffer = new byte[2];
        inputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithBufferTooSmall() throws IOException {
        inputStream = new Base64InputStream(new ByteArrayInputStream(singleByte), false);
        byte[] buffer = new byte[0];
        inputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testReadLine() throws IOException {
        byte[] data = {48, 48, 49, 49, 52, 52, 53, 53, 56, 56, 57, 57};
        byteArrayOutputStream.write(data);
        byteArrayOutputStream.flush();

        byte[] expected = {48, 48, 49, 49};
        byte[] buffer = new byte[expected.length + 1];
        int actual = inputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals("Read incorrect number of bytes", expected.length + 1, actual);
        String decoded = new String(Base64.decodeBase64(base64.encode(expected)));
        Assert.assertEquals("Decoded incorrect line", decoded, new String(new byte[] {48, 48, 49, 49}));
    }

    @Test
    public void testReadLineWithNoData() throws IOException {
        byte[] buffer = new byte[77];
        int actual = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Read incorrect number of bytes", 0, actual);
    }
}