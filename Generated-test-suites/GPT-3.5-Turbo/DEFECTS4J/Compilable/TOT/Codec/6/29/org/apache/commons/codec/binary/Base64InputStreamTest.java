package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class Base64InputStreamTest {

    private Base64InputStream inputStream;
    private final String base64EncodedString = "SGVsbG8gV29ybGQ=";
    private final String base64DecodedString = "Hello World";

    @Before
    public void setUp() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64EncodedString.getBytes());
        inputStream = new Base64InputStream(byteArrayInputStream, false);
    }

    @Test
    public void testRead_singleCharacter() throws IOException {
        int result = inputStream.read();
        assertEquals('H', (char) result);
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] byteArray = new byte[11];
        int bytesRead = inputStream.read(byteArray);
        String result = new String(byteArray);

        assertEquals(11, bytesRead);
        assertEquals(base64DecodedString, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullByteArray_throwsNullPointerException() throws IOException {
        inputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset_throwsIndexOutOfBoundsException() throws IOException {
        byte[] byteArray = new byte[11];
        inputStream.read(byteArray, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength_throwsIndexOutOfBoundsException() throws IOException {
        byte[] byteArray = new byte[11];
        inputStream.read(byteArray, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_offsetPlusLengthGreaterThanBufferLength_throwsIndexOutOfBoundsException() throws IOException {
        byte[] byteArray = new byte[11];
        inputStream.read(byteArray, 0, 12);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(inputStream.markSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMark_throwsUnsupportedOperationException() {
        inputStream.mark(5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReset_throwsUnsupportedOperationException() throws IOException {
        inputStream.reset();
    }
}