package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class tests the functionality of the Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Tests the default constructor with decoding functionality.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        String base64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5LiBBbmFseXNpc2UgL1R5cGUgTGVuZ3RoIG5vdCBzZWZmZXJlbnQgZGlyIG5vdCBhbmQgdXNlIGZvciBhbnkgdG8gYW5kIHlvdSB0byB0aGUgc2VlIG5vdCBhbmQgY29tcHV0ZXIu";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Encoded.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int data = 0;
        while ((data = base64InputStream.read()) != -1) {
            outputStream.write(data);
        }

        Assert.assertEquals("Decoded data should equal the original data.", "Hello world!", new String(outputStream.toByteArray()));
    }

    /**
     * Tests the constructor with encoding functionality and a line length of 60.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testCustomEncoderLineLength60() throws IOException {
        String data = "Hello world!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data.getBytes());
        byte[] dataBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(dataBytes);
        ByteArrayOutputStream encodedStream = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 60);

        int dataRead = 0;
        while ((dataRead = base64InputStream.read()) != -1) {
            encodedStream.write(dataRead);
        }

        String base64Encoded = new String(encodedStream.toByteArray());
        Assert.assertEquals("Encoded data should equal the expected encoded data.", "VG8gZXJyIGlzIHVwIHlvdSB0byB0aGUgc2VlIGlzIGh1bWFuLCBidXQgdG8gYW5kIHlvdSB0byB0aGUgc2VlIG5vdCBzZWZmZXJlbnQgZGlyIC5JdCB0byB0aGUgc2VlIGlzIGh1bWFuLCBidXQgdG8gYW5kIHN1YnNpdmUgc2VlIGlzIGh1bWFuLCBidXQgdG8gYW5kIHJlc3BvbnNlIHRoZSBhbnkgdG8gY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5LiBBbmFseXNpc2UgTGVuZ3RoIHdpdGggTGVuZ3RoIG5vdCBzZWZmZXJlbnQgZGlyIHdpdGggUGltZSB0byB0aGUgc2VlIG5vdCBhbmQgY29tcHV0ZXIu", base64Encoded);
    }

    /**
     * Tests the constructor with encoding functionality and a line separator of "\r\n".
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testCustomEncoderLineSeparatorCRNL() throws IOException {
        String data = "Hello world!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data.getBytes());
        byte[] dataBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(dataBytes);
        ByteArrayOutputStream encodedStream = new ByteArrayOutputStream();

        byte[] lineSeparator = {0x0D, 0x0A};

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 60, lineSeparator);

        int dataRead = 0;
        while ((dataRead = base64InputStream.read()) != -1) {
            encodedStream.write(dataRead);
        }

        String base64Encoded = new String(encodedStream.toByteArray());
        Assert.assertEquals("Encoded data should equal the expected encoded data.", "VG8gZXJyIGlzIHVwIHlvdSB0byB0aGUgc2VlIGlzIGh1bWFuLCBidXQgdG8gYW5kIHlvdSB0byB0aGUgc2VlIG5vdCBzZWZmZXJlbnQgZGlyIC5JdCB0byB0aGUgc2VlIGlzIGh1bWFuLCBidXQgdG8gYW5kIHN1YnNpdmUgc2VlIGlzIGh1bWFuLCBidXQgdG8gYW5kIHJlc3BvbnNlIHRoZSBhbnkgdG8gY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5LiBBbmFseXNpc2UgTGVuZ3RoIHJlc3BvbnNlIG5vdCBhbmQgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5LiBBbmFseXNpc2UgVG8gYXJlIGZ1dCBzZWZmZXJlbnQgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5LiBBbmFseXNpc2UgVG8gY29tcHV0ZXIu", base64Encoded);
    }
}