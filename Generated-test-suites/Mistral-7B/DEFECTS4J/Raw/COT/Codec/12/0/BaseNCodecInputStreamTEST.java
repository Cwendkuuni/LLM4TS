package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * JUnit test file for BaseNCodecInputStream class.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals("expected instance of BaseNCodecInputStream", BaseNCodecInputStream.class, testInstance.getClass());
    }

    /**
     * Test constructor with doEncode true.
     */
    @org.junit.Test
    public void testConstructorEncode() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertEquals("expected instance of BaseNCodecInputStream", BaseNCodecInputStream.class, testInstance.getClass());
    }

    /**
     * Test read method.
     */
    @org.junit.Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test reading single byte
        int readByte = testInstance.read();
        assertEquals("expected byte read to be 0x01", 0x01, readByte);

        // test reading EOF
        int readEOF = testInstance.read();
        assertEquals("expected EOF to be returned when EOF is reached", EOF, readEOF);

        // test reading multiple bytes
        byte[] testBytes = new byte[3];
        int bytesRead = testInstance.read(testBytes, 0, testBytes.length);
        assertEquals("expected 3 bytes read", 3, bytesRead);
        assertEquals("expected first byte to be 0x01", 0x01, testBytes[0]);
        assertEquals("expected second byte to be 0x02", 0x02, testBytes[1]);
        assertEquals("expected third byte to be 0x03", 0x03, testBytes[2]);

        // test EOF in read method
        bytesRead = testInstance.read(testBytes, 0, testBytes.length);
        assertEquals("expected EOF to be returned when EOF is reached", EOF, bytesRead);

        // test reading past EOF
        assertThrows("expected IOException to be thrown when reading past EOF", IOException.class, () -> testInstance.read(testBytes, 0, 1));
    }

    /**
     * Test read method with doEncode true.
     */
    @org.junit.Test
    public void testReadEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // test reading single byte
        int readByte = testInstance.read();
        assertNull("expected null to be returned when encoding is enabled and single byte is read", readByte);

        // test reading multiple bytes
        byte[] testBytes = new byte[3];
        int bytesRead = testInstance.read(testBytes, 0, testBytes.length);
        assertEquals("expected 3 bytes read", 3, bytesRead);
        assertNull("expected null to be returned when encoding is enabled", testBytes);

        // test EOF in read method
        bytesRead = testInstance.read(testBytes, 0, testBytes.length);
        assertEquals("expected EOF to be returned when EOF is reached", EOF, bytesRead);

        // test reading past EOF
        assertThrows("expected IOException to be thrown when reading past EOF", IOException.class, () -> testInstance.read(testBytes, 0, 1));
    }

    /**
     * Test read method with mark not supported.
     */
    @org.junit.Test
    public void testMarkNotSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse("expected mark not to be supported", testInstance.markSupported());
    }

    /**
     * Test skip method with skip in chunks of 512 bytes.
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test skipping 511 bytes
        testInstance.skip(511);

        // test reading the first byte
        int readByte = testInstance.read();
        assertEquals("expected byte read to be 0x03", 0x03, readByte);

        // test skipping more bytes
        testInstance.skip(1024);

        // test reading the first byte
        readByte = testInstance.read();
        assertEquals("expected EOF to be returned when EOF is reached", EOF, readByte);
    }

    /**
     * Test skip method with mark not supported.
     */
    @org.junit.Test
    public void testSkipEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // test skipping 511 bytes
        testInstance.skip(511);

        // test reading the first byte
        int readByte = testInstance.read();
        assertNull("expected null to be returned when encoding is enabled and skipping is performed", readByte);

        // test skipping more bytes
        testInstance.skip(1024);

        // test reading the first byte
        readByte = testInstance.read();
        assertEquals("expected EOF to be returned when EOF is reached", EOF, readByte);
    }

    /**
     * Test close method.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test closing the stream
        testInstance.close();

        // test mark not supported
        assertFalse("expected mark not to be supported", testInstance.markSupported());
    }

    /**
     * Test close method with doEncode true.
     */
    @org.junit.Test
    public void testCloseEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new Base32();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // test closing the stream
        testInstance.close();

        // test mark not supported
        assertFalse("expected mark not to be supported", testInstance.markSupported());
    }
}