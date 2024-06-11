package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() throws IOException {
        String plainText = "This is a test for Base64InputStream.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(Base64.encodeBase64(plainText.getBytes())));
        inputStream = byteArrayInputStream;

        base64InputStream = new Base64InputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (base64InputStream != null) {
            base64InputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * Test default constructor
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(base64InputStream);
    }

    /**
     * Test that the Base64InputStream decodes the input stream
     */
    @Test
    public void testDecode() throws IOException {
        byte[] decodedByteArray = new byte[base64InputStream.available()];
        int decodedLength = base64InputStream.read(decodedByteArray, 0, decodedByteArray.length);

        assertEquals(plainText.getBytes().length, decodedLength);
        String decodedText = new String(decodedByteArray, "UTF-8");
        assertEquals(plainText, decodedText);
    }

    /**
     * Test that the Base64InputStream encodes the input stream
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncode() throws IOException {
        new Base64InputStream(inputStream, true);
    }

    /**
     * Test that the Base64InputStream supports marking
     */
    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}