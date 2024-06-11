package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     */
    @org.junit.Test
    public void testBase64InputStreamConstructor1() {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertNotNull(base64InputStream);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     */
    @org.junit.Test
    public void testBase64InputStreamConstructor2() {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        assertNotNull(base64InputStream);
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     */
    @org.junit.Test
    public void testBase64InputStreamConstructor3() {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 5, new byte[]{0x0d, 0x0a});
        assertNotNull(base64InputStream);
    }

    /**
     * Test method public int read()
     */
    @org.junit.Test
    public void testReadMethod() throws IOException {
        String testData = "TestData";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        int readByte;
        for (int i = 0; i < testData.length(); i++) {
            readByte = base64InputStream.read();
            assertEquals(testData.charAt(i), (char) readByte);
        }
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test method public int read(byte[] b, int offset, int len)
     */
    @org.junit.Test
    public void testReadMethodArray() throws IOException {
        String testData = "TestData";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] result = new byte[testData.length()];
        int readLength = base64InputStream.read(result, 0, testData.length());
        assertArrayEquals(testData.getBytes(), result);
        assertEquals(-1, base64InputStream.read());
    }

    /**
     * Test method public boolean markSupported()
     */
    @org.junit.Test
    public void testMarkSupportedMethod() {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test method public void close()
     */
    @org.junit.Test
    public void testCloseMethod() throws IOException {
        String testData = "TestData";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        base64InputStream.close();
        assertNull(base64InputStream.in);
    }

    /**
     * Test exception handling when reading beyond available data
     */
    @org.junit.Test(expected = IOException.class)
    public void testReadBeyondAvailableData() throws IOException {
        String testData = "TestData";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
        byte[] result = new byte[testData.length() + 1];
        int readLength = base64InputStream.read(result, 0, testData.length() + 1);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode) with doEncode set to true
     */
    @org.junit.Test
    public void testBase64InputStreamConstructor2WithEncodeTrue() {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        Base64OutputStream base64OutputStream = new Base64OutputStream();
        input.writeTo(base64OutputStream);
        InputStream base64Input = new ByteArrayInputStream(base64OutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(base64Input, true);
        assertNotNull(base64InputStream);

        byte[] result = new byte[4];
        int readLength = base64InputStream.read(result, 0, 4);
        assertArrayEquals(new byte[]{(byte) 0x3C, (byte) 0x2E, (byte) 0x3C, (byte) 0x2E}, result);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) with
     * lineLength set to 5 and lineSeparator set to CRLF
     */
    @org.junit.Test
    public void testBase64InputStreamConstructor3WithLineLengthFiveAndCRLF() {
        InputStream input = new ByteArrayInputStream("TestData".getBytes());
        byte[] lineSeparator = new byte[]{0x0d, 0x0a};
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 5, lineSeparator);
        assertNotNull(base64InputStream);

        byte[] result = new byte[5];
        int readLength = base64InputStream.read(result, 0, 5);
        assertArrayEquals(new byte[]{(byte) 0x3C, (byte) 0x2E, (byte) 0x3C, (byte) 0x2E, (byte) 0x3C}, result);

        byte[] result2 = new byte[5];
        readLength = base64InputStream.read(result2, 0, 5);
        assertArrayEquals(new byte[]{(byte) 0x2E, (byte) 0x3C, (byte) 0x2E, (byte) 0x3C, (byte) 0x2E}, result2);

        byte[] result3 = new byte[4];
        readLength = base64InputStream.read(result3, 0, 4);
        assertArrayEquals(new byte[]{(byte) 0x3C, (byte) 0x2E, (byte) 0x3C, (byte) 0x2E}, result3);

        assertEquals(-1, base64InputStream.read());
    }
}