package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int result = base64InputStream.read();
            assertEquals(72, result);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadWithByteArray() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            byte[] outputBytes = new byte[5];
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int result = base64InputStream.read(outputBytes, 0, 5);
            assertArrayEquals(new byte[]{ 72, 101, 108, 108, 111 }, outputBytes);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadWithNullByteArray() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int result = base64InputStream.read(null, 1, 5);
            fail("Expected NullPointerException but no exception was thrown");
        } catch (NullPointerException e) {
            assertEquals("byte array parameter is null", e.getMessage());
        } catch (Exception e) {
            fail("Expected NullPointerException but an exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidOffset() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            byte[] outputBytes = new byte[5];
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int result = base64InputStream.read(outputBytes, -1, 5);
            fail("Expected IndexOutOfBoundsException but no exception was thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("offset, len or buffer size are invalid", e.getMessage());
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException but an exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidLength() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            byte[] outputBytes = new byte[5];
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int result = base64InputStream.read(outputBytes, 0, -1);
            fail("Expected IndexOutOfBoundsException but no exception was thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("offset, len or buffer size are invalid", e.getMessage());
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException but an exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidLengthAndOffset() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            byte[] outputBytes = new byte[5];
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int result = base64InputStream.read(outputBytes, -1, -1);
            fail("Expected IndexOutOfBoundsException but no exception was thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("offset, len or buffer size are invalid", e.getMessage());
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException but an exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        try {
            byte[] inputBytes = "Hello World".getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            
            assertFalse(base64InputStream.markSupported());
            
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}