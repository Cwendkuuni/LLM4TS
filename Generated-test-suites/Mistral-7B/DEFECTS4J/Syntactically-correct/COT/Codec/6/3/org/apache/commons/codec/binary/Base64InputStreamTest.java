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

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test cases for {@link Base64InputStream} class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default doEncode=false.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testBase64InputStreamDefaultDecoding() throws IOException {
        final String base64Text = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbGFpcyBpcyBhIGh1bWFuLCBidXQgdG8gYWxsIGNvbnRlbnQgPSAxLDE=";
        InputStream in = new ByteArrayInputStream(base64Text.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = base64InputStream.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        byte[] expectedBytes = new byte[] { (byte) 0x41, (byte) 0x67, (byte) 0x65, (byte) 0x65, (byte) 0x53, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x52, (byte) 0x65, (byte) 0x61, (byte) 0x75, (byte) 0x74, (byte) 0x49, (byte) 0x64, (byte) 0x65, (byte) 0x73, (byte) 0x68, (byte) 0x41, (byte) 0x64, (byte) 0x64, (byte) 0x72, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x31 };
        assertArrayEquals(expectedBytes, out.toByteArray());
    }

    /**
     * Test constructor with doEncode=true.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testBase64InputStreamEncoding() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] inputBytes = new byte[] { 0x41, 0x67, 0x65, 0x65, 0x53, 0x68, 0x65, 0x61, 0x74, 0x65, 0x52, 0x65, 0x61, 0x75, 0x74, 0x49, 0x64, 0x65, 0x73, 0x68, 0x41, 0x64, 0x64, 0x72, 0x65, 0x73, 0x74 };
        InputStream in = new ByteArrayInputStream(inputBytes);
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(in, true, base64.getDefaultLineLength(), base64.getDefaultLineSeparator());

        ByteArrayOutputStream expectedOut = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = base64InputStream.read(buffer)) > 0) {
            expectedOut.write(buffer, 0, len);
        }

        assertArrayEquals(base64.encode(inputBytes), expectedOut.toByteArray());
    }

    /**
     * Test constructor with custom doEncode and lineLength.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testBase64InputStreamCustomDecoding() throws IOException {
        final String base64Text = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gYWxsIGNvbnRlbnQgPSAxLDE=";
        InputStream in = new ByteArrayInputStream(base64Text.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, false, 70);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = base64InputStream.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        byte[] expectedBytes = new byte[] { (byte) 0x41, (byte) 0x67, (byte) 0x65, (byte) 0x65, (byte) 0x53, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x52, (byte) 0x65, (byte) 0x61, (byte) 0x75, (byte) 0x74, (byte) 0x49, (byte) 0x64, (byte) 0x65, (byte) 0x73, (byte) 0x68, (byte) 0x41, (byte) 0x64, (byte) 0x64, (byte) 0x72, (byte) 0x65, (byte) 0x73, (byte) 0x74 };
        assertArrayEquals(expectedBytes, out.toByteArray());
    }

    /**
     * Test constructor with custom doEncode, lineLength and lineSeparator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testBase64InputStreamCustomEncoding() throws IOException {
        ByteArrayOutputStream input = new ByteArrayOutputStream();
        input.write(0x41);
        input.write(0x67);
        input.write(0x65);
        input.write(0x65);
        input.write(0x53);
        input.write(0x68);
        input.write(0x65);
        input.write(0x61);
        input.write(0x74);
        input.write(0x65);
        input.write(0x52);
        input.write(0x65);
        input.write(0x61);
        input.write(0x75);
        input.write(0x74);
        input.write(0x49);
        input.write(0x64);
        input.write(0x65);
        input.write(0x73);
        input.write(0x68);
        input.write(0x41);
        input.write(0x64);
        input.write(0x64);
        input.write(0x72);
        input.write(0x65);
        input.write(0x73);
        input.write(0x74);
        InputStream in = new ByteArrayInputStream(input.toByteArray());
        Base64 base64 = new Base64(70, new byte[] { 0x0D, 0x0A });
        Base64InputStream base64InputStream = new Base64InputStream(in, true, base64);

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = base64InputStream.read(buffer)) > 0) {
            expectedOutput.write(buffer, 0, len);
        }

        byte[] expectedBytes = expectedOutput.toByteArray();
        byte[] decodedBytes = new byte[expectedBytes.length];
        InputStream decoder = new ByteArrayInputStream(expectedBytes);
        BinaryDecoder decoderInstance = new Base64Decoder();

        int bytesDecoded = decoderInstance.decode(decoder, decodedBytes, decodedBytes.length);
        assertArrayEquals(input.toByteArray(), decodedBytes, 0, bytesDecoded);
    }

    /**
     * Test read method when given an empty stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testBase64InputStreamEmptyStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];

        int read = base64InputStream.read(buffer);
        assertFalse(read > 0);
    }

    /**
     * Test read method when given an invalid base64 encoded stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     * @throws DecoderException
     *             if the stream contains invalid base64 encoded data.
     */
    @org.junit.Test
    public void testBase64InputStreamInvalidStream() throws IOException, DecoderException {
        String invalidBase64Text = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gYWxsIGNvbnRlbnQgPSAxLDE=== ";
        InputStream in = new ByteArrayInputStream(invalidBase64Text.getBytes());
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(in, false, base64);

        try {
            base64InputStream.read(new byte[1]);
            assertFalse(true);
        } catch (IOException e) {
            // expected
        }

        Base64Decoder base64Decoder = new Base64Decoder();
        assertThrows(DecoderException.class, () -> base64Decoder.decode(in, new byte[1], 0));
    }

    /**
     * Test read method when given a stream with missing terminator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     * @throws DecoderException
     *             if the stream contains invalid base64 encoded data.
     */
    @org.junit.Test
    public void testBase64InputStreamMissingTerminator() throws IOException, DecoderException {
        String missingTerminatorBase64Text = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gYWxsIGNvbnRlbnQgPSAx";
        InputStream in = new ByteArrayInputStream(missingTerminatorBase64Text.getBytes());
        Base64 base64 = new Base64();
        Base64InputStream base64InputStream = new Base64InputStream(in, false, base64);

        try {
            base64InputStream.read(new byte[1]);
            assertFalse(true);
        } catch (IOException e) {
            // expected
        }

        Base64Decoder base64Decoder = new Base64Decoder();
        assertThrows(DecoderException.class, () -> base64Decoder.decode(in, new byte[1], 0));
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test
    public void testBase64InputStreamMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }
}