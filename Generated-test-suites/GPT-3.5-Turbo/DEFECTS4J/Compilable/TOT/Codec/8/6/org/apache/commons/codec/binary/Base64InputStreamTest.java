package org.apache.commons.codec.binary;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;

public class Base64InputStreamTest {

    // Test read() method

    @Test
    public void testReadSingleByte() throws Exception {
        byte[] input = {65, 66, 67}; // ASCII codes for A, B and C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int result = base64InputStream.read();
        Assert.assertEquals(65, result); // A
    }

    @Test
    public void testReadMultipleBytes() throws Exception {
        byte[] input = {65, 66, 67}; // ASCII codes for A, B and C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[2];
        int bytesRead = base64InputStream.read(output, 0, 2);
        Assert.assertEquals(2, bytesRead);
        Assert.assertArrayEquals(new byte[]{65, 66}, output);
    }

    @Test
    public void testReadEndOfFile() throws Exception {
        byte[] input = {65}; // ASCII code for A
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int result = base64InputStream.read();
        Assert.assertEquals(65, result); // A
        result = base64InputStream.read();
        Assert.assertEquals(-1, result); // End of file
    }

    // Test read(byte[], int, int) method

    @Test
    public void testReadBytes() throws Exception {
        byte[] input = {65, 66, 67}; // ASCII codes for A, B and C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[3];
        int bytesRead = base64InputStream.read(output, 0, 3);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(new byte[]{65, 66, 67}, output); // ABC
    }

    @Test
    public void testReadBytesWithOffset() throws Exception {
        byte[] input = {65, 66, 67}; // ASCII codes for A, B and C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[3];
        int bytesRead = base64InputStream.read(output, 1, 2);
        Assert.assertEquals(2, bytesRead);
        Assert.assertArrayEquals(new byte[]{0, 65, 66}, output); // 00AB
    }

    @Test(expected = NullPointerException.class)
    public void testReadBytesWithNullArray() throws Exception {
        byte[] input = {65}; // ASCII code for A
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithInvalidOffset() throws Exception {
        byte[] input = {65}; // ASCII code for A
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[1];
        base64InputStream.read(output, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithInvalidLength() throws Exception {
        byte[] input = {65}; // ASCII code for A
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[1];
        base64InputStream.read(output, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesWithInvalidBufferSize() throws Exception {
        byte[] input = {65}; // ASCII code for A
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[1];
        base64InputStream.read(output, 0, 2);
    }

    @Test
    public void testReadBytesAtEndOfFile() throws Exception {
        byte[] input = {65}; // ASCII code for A
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] output = new byte[2];
        int bytesRead = base64InputStream.read(output, 0, 2);
        Assert.assertEquals(1, bytesRead);
        Assert.assertArrayEquals(new byte[]{65, 0}, output); // A0
    }

    // Test markSupported() method

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        Assert.assertFalse(base64InputStream.markSupported());
    }

}