package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Typical Use Case: Reading one byte from the InputStream
        byte[] inputData1 = {72}; // ASCII value of 'H'
        InputStream inputStream1 = new ByteArrayInputStream(inputData1);
        Base64InputStream base64InputStream1 = new Base64InputStream(inputStream1, true);
        int result1 = base64InputStream1.read();
        assertEquals(72, result1); // expected: ASCII value of 'H'

        // Edge Case: Reading one byte when EOF has been reached
        byte[] inputData2 = {};
        InputStream inputStream2 = new ByteArrayInputStream(inputData2);
        Base64InputStream base64InputStream2 = new Base64InputStream(inputStream2, true);
        int result2 = base64InputStream2.read();
        assertEquals(-1, result2); // expected: -1

        // Error Scenario: Reading one byte from null InputStream
        InputStream inputStream3 = null;
        Base64InputStream base64InputStream3 = new Base64InputStream(inputStream3, true);
        try {
            base64InputStream3.read();
            fail("Exception not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadArray() throws IOException {
        // Typical Use Case: Reading multiple bytes into the byte array
        byte[] inputData1 = {84, 101, 115, 116}; // ASCII values of 'T', 'e', 's', 't'
        byte[] resultData1 = new byte[4];
        InputStream inputStream1 = new ByteArrayInputStream(inputData1);
        Base64InputStream base64InputStream1 = new Base64InputStream(inputStream1, true);
        int bytesRead1 = base64InputStream1.read(resultData1, 0, 4);
        assertEquals(4, bytesRead1); // expected: 4
        assertArrayEquals(inputData1, resultData1); // expected: {84, 101, 115, 116}

        // Edge Case: Reading bytes when offset and len are both 0
        byte[] inputData2 = {72}; // ASCII value of 'H'
        byte[] resultData2 = new byte[1];
        InputStream inputStream2 = new ByteArrayInputStream(inputData2);
        Base64InputStream base64InputStream2 = new Base64InputStream(inputStream2, true);
        int bytesRead2 = base64InputStream2.read(resultData2, 0, 0);
        assertEquals(0, bytesRead2); // expected: 0
        assertArrayEquals(new byte[1], resultData2); // expected: {0}

        // Error Scenario: Reading bytes with negative offset
        byte[] inputData3 = {72}; // ASCII value of 'H'
        byte[] resultData3 = new byte[1];
        InputStream inputStream3 = new ByteArrayInputStream(inputData3);
        Base64InputStream base64InputStream3 = new Base64InputStream(inputStream3, true);
        try {
            base64InputStream3.read(resultData3, -1, 1);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        // Error Scenario: Reading bytes with larger offset than array size
        byte[] inputData4 = {72}; // ASCII value of 'H'
        byte[] resultData4 = new byte[1];
        InputStream inputStream4 = new ByteArrayInputStream(inputData4);
        Base64InputStream base64InputStream4 = new Base64InputStream(inputStream4, true);
        try {
            base64InputStream4.read(resultData4, 2, 1);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        // Error Scenario: Reading bytes with larger offset+length than array size
        byte[] inputData5 = {72}; // ASCII value of 'H'
        byte[] resultData5 = new byte[2];
        InputStream inputStream5 = new ByteArrayInputStream(inputData5);
        Base64InputStream base64InputStream5 = new Base64InputStream(inputStream5, true);
        try {
            base64InputStream5.read(resultData5, 1, 2);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        // Error Scenario: Reading bytes with null byte array
        byte[] inputData6 = {72}; // ASCII value of 'H'
        InputStream inputStream6 = new ByteArrayInputStream(inputData6);
        Base64InputStream base64InputStream6 = new Base64InputStream(inputStream6, true);
        try {
            base64InputStream6.read(null, 0, 1);
            fail("Exception not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testMarkSupported() {
        // Typical Use Case: Base64InputStream does not support marks
        InputStream inputStream1 = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream1 = new Base64InputStream(inputStream1, true);
        boolean result1 = base64InputStream1.markSupported();
        assertFalse(result1); // expected: false
    }
}