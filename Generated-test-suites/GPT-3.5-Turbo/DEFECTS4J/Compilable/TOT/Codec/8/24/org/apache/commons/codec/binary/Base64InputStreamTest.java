package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] byteArray = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        base64InputStream = new Base64InputStream(inputStream);
    }
  
    @Test
    public void testRead() {
        try {
            int byteValue = base64InputStream.read();
            assertEquals(72, byteValue); // Expected ASCII value of first character 'H'
        } catch (IOException e) {
            fail("Exception occurred while reading the data");
        }
    }
  
    @Test
    public void testReadWithInvalidOffset() {
        byte[] byteArray = new byte[10];
        try {
            base64InputStream.read(byteArray, -1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
  
    @Test
    public void testReadWithInvalidBufferLength() {
        byte[] byteArray = new byte[5];
        try {
            base64InputStream.read(byteArray, 0, 10);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
  
    @Test
    public void testReadWithZeroLength() {
        byte[] byteArray = new byte[10];
        try {
            int bytesRead = base64InputStream.read(byteArray, 0, 0);
            assertEquals(0, bytesRead); // No bytes should be read
        } catch (IOException e) {
            fail("Exception occurred while reading the data");
        }
    }
  
    @Test
    public void testReadWithBufferEmpty() {
        try {
            int bytesRead = base64InputStream.read(new byte[0]);
            assertEquals(0, bytesRead); // No bytes should be read
        } catch (IOException e) {
            fail("Exception occurred while reading the data");
        }
    }
  
    @Test
    public void testReadWithBufferFull() {
        byte[] byteArray = new byte[14];
        try {
            int bytesRead = base64InputStream.read(byteArray);
            assertEquals(14, bytesRead); // All 14 bytes should be read
            byte[] expectedByteArray = "Hello, World!".getBytes();
            assertArrayEquals(expectedByteArray, byteArray);
        } catch (IOException e) {
            fail("Exception occurred while reading the data");
        }
    }
  
    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported()); // Mark is not supported
    }

}