import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(H, result);
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] result = new byte[5];
        base64InputStream.read(result);
        assertArrayEquals("Hello".getBytes(), result);
    }

    @Test
    public void testReadArrayOffset() throws IOException {
        byte[] result = new byte[10];
        base64InputStream.read(result, 2, 5);
        assertArrayEquals("\u0000\u0000Hello\u0000".getBytes(), result);
    }

    @Test
    public void testReadEmptyArray() throws IOException {
        byte[] result = new byte[0];
        int readLen = base64InputStream.read(result);
        assertEquals(0, readLen);
    }

    @Test
    public void testReadArrayOffsetLen() throws IOException {
        byte[] result = new byte[10];
        base64InputStream.read(result, 0, 0);
        assertArrayEquals(new byte[10], result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullArray() throws IOException {
        byte[] result = null;
        base64InputStream.read(result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayNegativeOffset() throws IOException {
        byte[] result = new byte[10];
        base64InputStream.read(result, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayNegativeLen() throws IOException {
        byte[] result = new byte[10];
        base64InputStream.read(result, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidLength() throws IOException {
        byte[] result = new byte[10];
        base64InputStream.read(result, 0, 11);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidOffset() throws IOException {
        byte[] result = new byte[10];
        base64InputStream.read(result, 11, 10);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStreamEncode() {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStreamEncode = new Base64InputStream(inputStream, true);
        assertNotNull(base64InputStreamEncode);
    }

    @Test
    public void testBase64InputStreamEncodeWithLineLength() {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStreamEncodeLineLength = new Base64InputStream(inputStream, true, 10, "\r\n".getBytes());
        assertNotNull(base64InputStreamEncodeLineLength);
    }

    @Test
    public void testBase64InputStreamDecode() {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        assertNotNull(base64InputStreamDecode);
    }

    @Test
    public void testReadBase64EncodedData() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[10];
        base64InputStreamDecode.read(result);
        assertArrayEquals("java.lang.ByteArrayInput".getBytes(), result);
    }

    @Test
    public void testReadBase64EncodedDataOffsetLength() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[30];
        base64InputStreamDecode.read(result, 10, 10);
        assertArrayEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000java.lang.".getBytes(), result);
    }

    @Test
    public void testReadBase64EncodedDataEmptyArray() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[0];
        int readLen = base64InputStreamDecode.read(result);
        assertEquals(0, readLen);
    }

    @Test(expected = NullPointerException.class)
    public void testReadBase64EncodedDataNullArray() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = null;
        base64InputStreamDecode.read(result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBase64EncodedDataNegativeOffset() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[30];
        base64InputStreamDecode.read(result, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBase64EncodedDataNegativeLen() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[30];
        base64InputStreamDecode.read(result, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBase64EncodedDataInvalidLength() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[30];
        base64InputStreamDecode.read(result, 0, 31);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBase64EncodedDataInvalidOffset() throws IOException {
        byte[] base64EncodedData = "amF2YS5sYW5nLnBhcnNlLkludGVnZXI=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData);
        Base64InputStream base64InputStreamDecode = new Base64InputStream(inputStream, false);
        byte[] result = new byte[30];
        base64InputStreamDecode.read(result, 31, 10);
    }

}